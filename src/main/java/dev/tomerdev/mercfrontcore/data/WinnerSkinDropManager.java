package dev.tomerdev.mercfrontcore.data;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.util.BFUtils;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import java.util.Locale;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
            MercFrontCore.LOGGER.debug("Skipped duplicate winner skin reward processing for {}", matchKey);
            return;
        }

        ServerPlayerEntity player = BFUtils.getPlayerByUUID(playerUuid);
        if (player == null || player.getServer() == null) {
            MercFrontCore.LOGGER.debug("Skipped winner skin reward because player {} is unavailable", playerUuid);
            return;
        }

        if (player.getRandom().nextFloat() > rewards.winnerSkinDropChance) {
            MercFrontCore.LOGGER.debug(
                "Skipped winner skin reward roll for {} at chance {}",
                player.getNameForScoreboard(),
                rewards.winnerSkinDropChance
            );
            return;
        }

        SkinRewardSelector.SelectedSkinReward reward = SkinRewardSelector.pickRandomReward(playerUuid);
        if (reward == null) {
            MercFrontCore.LOGGER.warn("No eligible winner skin rewards were available for {}", player.getNameForScoreboard());
            return;
        }

        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        store.grantPlayerSkin(playerUuid, reward.gunId().toString(), reward.skin());
        if (!store.save(player.getServer())) {
            MercFrontCore.LOGGER.warn("Failed to save winner skin drop reward for {}", player.getNameForScoreboard());
            return;
        }

        int applied = store.applyToPlayer(player);
        PacketDistributor.sendToPlayer(player, store.toPacket(playerUuid));
        broadcastRewardMessage(game, manager, player, reward, applied);
        MercFrontCore.LOGGER.info(
            "Awarded winner skin drop {} / {} ({}) to {}",
            reward.gunId(),
            reward.skin(),
            reward.rarity(),
            player.getNameForScoreboard()
        );
    }

    private static String resolveMatchKey(Object game) {
        try {
            Object uuid = game.getClass().getMethod("getUUID").invoke(game);
            if (uuid != null) {
                return uuid.toString();
            }
        } catch (Throwable ignored) {
        }
        try {
            Object uuid = game.getClass().getMethod("getUuid").invoke(game);
            return uuid != null ? uuid.toString() : "unknown";
        } catch (Throwable ignored) {
            return "unknown";
        }
    }

    private static void broadcastRewardMessage(
        Object game,
        BFAbstractManager<?, ?, ?> manager,
        ServerPlayerEntity winner,
        SkinRewardSelector.SelectedSkinReward reward,
        int applied
    ) {
        MutableText message = Text.empty()
            .append(Text.literal("[Victory Reward] ").formatted(Formatting.GOLD, Formatting.BOLD))
            .append(Text.literal(winner.getNameForScoreboard()).formatted(Formatting.YELLOW))
            .append(Text.literal(" received ").formatted(Formatting.GRAY))
            .append(Text.literal("'").formatted(Formatting.GRAY))
            .append(Text.literal(reward.skin()).formatted(getRarityFormatting(reward.rarity()), Formatting.BOLD))
            .append(Text.literal("' skin for ").formatted(Formatting.GRAY))
            .append(Text.literal(reward.gunId().toString()).formatted(Formatting.AQUA));

        if (applied > 0) {
            message = message.append(Text.literal(" and it was applied immediately.").formatted(Formatting.GREEN));
        } else {
            message = message.append(Text.literal(".").formatted(Formatting.GRAY));
        }

        for (ServerPlayerEntity target : getMatchPlayers(game, manager, winner)) {
            target.sendMessage(message, false);
        }
    }

    private static List<ServerPlayerEntity> getMatchPlayers(
        Object game,
        BFAbstractManager<?, ?, ?> manager,
        ServerPlayerEntity winner
    ) {
        if (winner.getServer() == null) {
            return List.of();
        }
        List<ServerPlayerEntity> resolved = new java.util.ArrayList<>();
        for (ServerPlayerEntity player : winner.getServer().getPlayerManager().getPlayerList()) {
            try {
                if (manager.getGameWithPlayer(player.getUuid()) == game) {
                    resolved.add(player);
                }
            } catch (Throwable ignored) {
            }
        }
        return resolved;
    }

    private static Formatting getRarityFormatting(String rarity) {
        return switch (rarity.toUpperCase(Locale.ROOT)) {
            case "IRON" -> Formatting.GRAY;
            case "LAPIS" -> Formatting.BLUE;
            case "GOLD" -> Formatting.GOLD;
            case "DIAMOND" -> Formatting.AQUA;
            default -> Formatting.DARK_GRAY;
        };
    }
}
