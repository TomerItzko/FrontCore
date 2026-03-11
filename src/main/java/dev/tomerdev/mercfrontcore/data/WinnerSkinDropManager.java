package dev.tomerdev.mercfrontcore.data;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.util.BFUtils;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;

public final class WinnerSkinDropManager {
    private static final Set<String> PROCESSED_MATCH_RESULTS = ConcurrentHashMap.newKeySet();

    private WinnerSkinDropManager() {
    }

    public static void resetSession() {
        PROCESSED_MATCH_RESULTS.clear();
    }

    public static void maybeAwardWinnerSkin(Object game, BFAbstractManager<?, ?, ?> manager, UUID playerUuid, boolean victory) {
        if (!victory) {
            return;
        }

        var rewards = MercFrontCoreConfigManager.get().rewards;
        if (!rewards.enableWinnerSkinDrops || rewards.winnerSkinDropChance <= 0.0f) {
            return;
        }

        String matchKey = resolveMatchKey(game) + ":" + playerUuid;
        if (!PROCESSED_MATCH_RESULTS.add(matchKey)) {
            return;
        }

        ServerPlayerEntity player = BFUtils.getPlayerByUUID(playerUuid);
        if (player == null || player.getServer() == null) {
            return;
        }

        if (player.getRandom().nextFloat() > rewards.winnerSkinDropChance) {
            return;
        }

        GunSkinIndex.ensureInitialized();
        List<SkinReward> rewardsPool = buildEligibleRewards(playerUuid);
        if (rewardsPool.isEmpty()) {
            return;
        }

        SkinReward reward = rewardsPool.get(player.getRandom().nextInt(rewardsPool.size()));
        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        store.grantPlayerSkin(playerUuid, reward.gunId().toString(), reward.skinName());
        if (!store.save(player.getServer())) {
            MercFrontCore.LOGGER.warn("Failed to save winner skin drop reward for {}", player.getNameForScoreboard());
            return;
        }

        int applied = store.applyToPlayer(player);
        PacketDistributor.sendToPlayer(player, store.toPacket(playerUuid));
        player.sendMessage(
            Text.literal(
                "Victory reward: you received gun skin '" + reward.skinName() + "' for " + reward.gunId()
                    + (applied > 0 ? " and it was applied to your inventory." : ".")
            ),
            false
        );
        MercFrontCore.LOGGER.info(
            "Awarded winner skin drop {} / {} to {}",
            reward.gunId(),
            reward.skinName(),
            player.getNameForScoreboard()
        );
    }

    private static List<SkinReward> buildEligibleRewards(UUID playerUuid) {
        Map<String, PlayerGunSkinStore.OwnedGunSkins> ownedByGun = PlayerGunSkinStore.getInstance().getPlayerSkins(playerUuid);
        List<GunRewardPool> gunPools = new ArrayList<>();
        for (Identifier gunId : GunSkinIndex.SKINS.keySet()) {
            Item item = Registries.ITEM.get(gunId);
            if (item == null || item == Items.AIR) {
                continue;
            }
            List<String> selectableSkins = getSelectableGunSkins(gunId);
            if (selectableSkins.isEmpty()) {
                continue;
            }
            Set<String> owned = ownedByGun.containsKey(gunId.toString())
                ? Set.copyOf(ownedByGun.get(gunId.toString()).ownedSkins())
                : Set.of();
            List<String> validUnownedSkins = new ArrayList<>();
            List<String> validAllSkins = new ArrayList<>();
            for (String skinName : selectableSkins) {
                if (skinName == null || skinName.isBlank()) {
                    continue;
                }
                if (GunSkinIndex.getSkinId(item, skinName).isEmpty()) {
                    continue;
                }
                validAllSkins.add(skinName);
                if (!owned.contains(skinName)) {
                    validUnownedSkins.add(skinName);
                }
            }
            if (!validAllSkins.isEmpty()) {
                gunPools.add(new GunRewardPool(gunId, validUnownedSkins, validAllSkins));
            }
        }
        if (gunPools.isEmpty()) {
            return List.of();
        }

        List<GunRewardPool> preferredGunPools = gunPools.stream()
            .filter(pool -> !pool.unownedSkins().isEmpty())
            .toList();
        List<GunRewardPool> selectedGunPoolSource = !preferredGunPools.isEmpty() ? preferredGunPools : gunPools;

        List<SkinReward> rewards = new ArrayList<>();
        for (GunRewardPool pool : selectedGunPoolSource) {
            List<String> skinPool = !pool.unownedSkins().isEmpty() ? pool.unownedSkins() : pool.allSkins();
            for (String skin : skinPool) {
                rewards.add(new SkinReward(pool.gunId(), skin));
            }
        }
        return rewards;
    }

    private static List<String> getSelectableGunSkins(Identifier id) {
        var declaredFallbackSkins = GunExtraOptionsIndex.getDeclaredFallbackSkins(id);
        if (!declaredFallbackSkins.isEmpty()) {
            return new ArrayList<>(declaredFallbackSkins);
        }
        Item item = Registries.ITEM.get(id);
        if (item != null && item != Items.AIR) {
            var strictSkins = GunSkinIndex.getStrictSkinNames(item);
            if (!strictSkins.isEmpty()) {
                return new ArrayList<>(strictSkins);
            }
        }
        var gunOptions = GunExtraOptionsIndex.snapshot().get(id);
        if (gunOptions != null && !gunOptions.skins().isEmpty()) {
            return new ArrayList<>(gunOptions.skins());
        }
        return List.of();
    }

    private static String resolveMatchKey(Object game) {
        try {
            Object uuid = game.getClass().getMethod("getUuid").invoke(game);
            return uuid != null ? uuid.toString() : "unknown";
        } catch (Throwable ignored) {
            return "unknown";
        }
    }

    private record SkinReward(Identifier gunId, String skinName) {
    }

    private record GunRewardPool(Identifier gunId, List<String> unownedSkins, List<String> allSkins) {
    }
}
