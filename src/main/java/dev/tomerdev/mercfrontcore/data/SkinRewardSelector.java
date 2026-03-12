package dev.tomerdev.mercfrontcore.data;

import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class SkinRewardSelector {
    public static final List<String> RARITY_NAMES = List.of("coal", "iron", "lapis", "gold", "diamond");
    private static final List<RarityWeight> RARITY_WEIGHTS = List.of(
        new RarityWeight("COAL", 80.0),
        new RarityWeight("IRON", 15.0),
        new RarityWeight("LAPIS", 3.0),
        new RarityWeight("GOLD", 0.6),
        new RarityWeight("DIAMOND", 0.2)
    );

    private SkinRewardSelector() {
    }

    public static SelectedSkinReward pickRandomReward(UUID playerUuid) {
        return pickRandomReward(playerUuid, null);
    }

    public static SelectedSkinReward pickRandomReward(UUID playerUuid, @Nullable String forcedRarity) {
        GunSkinIndex.ensureInitialized();
        if (GunExtraOptionsIndex.snapshot().isEmpty()) {
            GunExtraOptionsIndex.rebuild();
        }

        Map<String, PlayerGunSkinStore.OwnedGunSkins> ownedByGun = PlayerGunSkinStore.getInstance().getPlayerSkins(playerUuid);
        List<SkinCandidate> candidates = buildCandidates(ownedByGun);
        if (candidates.isEmpty()) {
            return null;
        }

        boolean hasUnowned = candidates.stream().anyMatch(candidate -> !candidate.owned());
        List<SkinCandidate> source = hasUnowned
            ? candidates.stream().filter(candidate -> !candidate.owned()).toList()
            : candidates;

        Map<String, Map<Identifier, List<String>>> byRarity = groupByRarityAndGun(source);
        if (byRarity.isEmpty()) {
            return null;
        }

        String rarity = normalizeRarity(forcedRarity);
        if (forcedRarity != null) {
            if (!byRarity.containsKey(rarity)) {
                return null;
            }
        } else {
            rarity = rollRarity(byRarity.keySet(), ThreadLocalRandom.current());
        }
        if (rarity == null) {
            return null;
        }

        Map<Identifier, List<String>> guns = byRarity.get(rarity);
        if (guns == null || guns.isEmpty()) {
            return null;
        }

        List<Identifier> gunIds = List.copyOf(guns.keySet());
        Identifier gunId = gunIds.get(ThreadLocalRandom.current().nextInt(gunIds.size()));
        List<String> skins = guns.get(gunId);
        if (skins == null || skins.isEmpty()) {
            return null;
        }

        String skin = skins.get(ThreadLocalRandom.current().nextInt(skins.size()));
        return new SelectedSkinReward(gunId, skin, rarity);
    }

    public static List<String> getSelectableGunSkins(Identifier id) {
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

    private static List<SkinCandidate> buildCandidates(Map<String, PlayerGunSkinStore.OwnedGunSkins> ownedByGun) {
        List<SkinCandidate> candidates = new ArrayList<>();
        for (Identifier gunId : collectRewardGunIds()) {
            Item item = Registries.ITEM.get(gunId);
            if (item == null || item == Items.AIR) {
                continue;
            }

            Set<String> ownedSkins = ownedByGun.containsKey(gunId.toString())
                ? Set.copyOf(ownedByGun.get(gunId.toString()).ownedSkins())
                : Set.of();

            for (String skin : getSelectableGunSkins(gunId)) {
                if (skin == null || skin.isBlank()) {
                    continue;
                }
                if (GunSkinIndex.getSkinId(item, skin).isEmpty()) {
                    continue;
                }

                String rarity = normalizeRarity(GunSkinIndex.getSkinRarity(item, skin).orElse("COAL"));
                candidates.add(new SkinCandidate(gunId, skin, rarity, ownedSkins.contains(skin)));
            }
        }
        return candidates;
    }

    private static Map<String, Map<Identifier, List<String>>> groupByRarityAndGun(List<SkinCandidate> candidates) {
        Map<String, Map<Identifier, List<String>>> grouped = new LinkedHashMap<>();
        for (SkinCandidate candidate : candidates) {
            grouped
                .computeIfAbsent(candidate.rarity(), ignored -> new LinkedHashMap<>())
                .computeIfAbsent(candidate.gunId(), ignored -> new ArrayList<>())
                .add(candidate.skin());
        }
        return grouped;
    }

    private static String rollRarity(Set<String> availableRarities, ThreadLocalRandom random) {
        double totalWeight = 0.0;
        for (RarityWeight weight : RARITY_WEIGHTS) {
            if (availableRarities.contains(weight.name())) {
                totalWeight += weight.weight();
            }
        }
        if (totalWeight <= 0.0) {
            return availableRarities.stream().findFirst().orElse(null);
        }

        double roll = random.nextDouble(totalWeight);
        for (RarityWeight weight : RARITY_WEIGHTS) {
            if (!availableRarities.contains(weight.name())) {
                continue;
            }
            if (roll < weight.weight()) {
                return weight.name();
            }
            roll -= weight.weight();
        }
        return availableRarities.stream().findFirst().orElse(null);
    }

    private static List<Identifier> collectRewardGunIds() {
        LinkedHashSet<Identifier> gunIds = new LinkedHashSet<>();
        gunIds.addAll(GunSkinIndex.SKINS.keySet());
        gunIds.addAll(GunExtraOptionsIndex.snapshot().keySet());
        return List.copyOf(gunIds);
    }

    public static String normalizeRarity(@Nullable String rarity) {
        if (rarity == null || rarity.isBlank()) {
            return "COAL";
        }
        String normalized = rarity.trim().toUpperCase(java.util.Locale.ROOT);
        return switch (normalized) {
            case "COAL", "IRON", "LAPIS", "GOLD", "DIAMOND" -> normalized;
            default -> "COAL";
        };
    }

    public record SelectedSkinReward(Identifier gunId, String skin, String rarity) {
    }

    private record SkinCandidate(Identifier gunId, String skin, String rarity, boolean owned) {
    }

    private record RarityWeight(String name, double weight) {
    }
}
