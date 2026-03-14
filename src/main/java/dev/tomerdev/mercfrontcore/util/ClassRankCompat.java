package dev.tomerdev.mercfrontcore.util;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;

public final class ClassRankCompat {
    private static final Field MIN_RANK_REQUIRED_FIELD = findMinRankRequiredField();
    private static final EnumMap<MatchClass, PlayerRank> DEFAULT_RANKS = captureDefaultRanks();

    private ClassRankCompat() {
    }

    public static void applyConfiguredRanks(MercFrontCoreConfig config) {
        applyRankNames(toRankNameMap(config));
    }

    public static PlayerRank getRequiredRank(MatchClass matchClass) {
        if (matchClass == null) {
            return PlayerRank.RECRUIT;
        }
        String configuredName = toRankNameMap(dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager.get()).get(matchClass);
        return resolveRank(configuredName, DEFAULT_RANKS.getOrDefault(matchClass, PlayerRank.RECRUIT));
    }

    public static boolean canUseClass(PlayerCloudData profile, MatchClass matchClass) {
        if (profile == null || matchClass == null) {
            return false;
        }
        PlayerRank requiredRank = getRequiredRank(matchClass);
        return profile.getRank().getID() >= requiredRank.getID();
    }

    public static String describeRank(PlayerRank rank) {
        if (rank == null) {
            return "unknown";
        }
        return rank.getTitle() + " [" + rank.getID() + "]";
    }

    public static void applyRankNames(Map<MatchClass, String> rankNames) {
        if (MIN_RANK_REQUIRED_FIELD == null) {
            return;
        }
        for (MatchClass matchClass : MatchClass.values()) {
            PlayerRank fallback = DEFAULT_RANKS.getOrDefault(matchClass, PlayerRank.RECRUIT);
            PlayerRank target = resolveRank(rankNames.get(matchClass), fallback);
            try {
                MIN_RANK_REQUIRED_FIELD.set(matchClass, target);
            } catch (IllegalAccessException e) {
                MercFrontCore.LOGGER.warn("Failed to set class rank requirement for {}", matchClass, e);
            }
        }
    }

    public static Map<MatchClass, String> toRankNameMap(MercFrontCoreConfig config) {
        EnumMap<MatchClass, String> rankNames = new EnumMap<>(MatchClass.class);
        MercFrontCoreConfig.ClassRankSettings settings = config != null && config.classRanks != null
            ? config.classRanks
            : new MercFrontCoreConfig.ClassRankSettings();
        rankNames.put(MatchClass.CLASS_RIFLEMAN, settings.rifleman);
        rankNames.put(MatchClass.CLASS_LIGHT_INFANTRY, settings.lightInfantry);
        rankNames.put(MatchClass.CLASS_ASSAULT, settings.assault);
        rankNames.put(MatchClass.CLASS_SUPPORT, settings.support);
        rankNames.put(MatchClass.CLASS_MEDIC, settings.medic);
        rankNames.put(MatchClass.CLASS_SNIPER, settings.sniper);
        rankNames.put(MatchClass.CLASS_GUNNER, settings.gunner);
        rankNames.put(MatchClass.CLASS_ANTI_TANK, settings.antiTank);
        rankNames.put(MatchClass.CLASS_SPECIALIST, settings.specialist);
        rankNames.put(MatchClass.CLASS_COMMANDER, settings.commander);
        return rankNames;
    }

    private static PlayerRank resolveRank(String configuredName, PlayerRank fallback) {
        if (configuredName == null || configuredName.isBlank()) {
            return fallback;
        }
        try {
            Object rank = PlayerRank.class.getField(configuredName.trim().toUpperCase(java.util.Locale.ROOT)).get(null);
            if (rank instanceof PlayerRank playerRank) {
                return playerRank;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        MercFrontCore.LOGGER.warn(
            "Unknown PlayerRank '{}' in class rank config. Falling back to {}.",
            configuredName,
            fallback
        );
        return fallback;
    }

    private static Field findMinRankRequiredField() {
        try {
            Field field = MatchClass.class.getDeclaredField("minRankRequired");
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            MercFrontCore.LOGGER.warn("Unable to access MatchClass.minRankRequired", e);
            return null;
        }
    }

    private static EnumMap<MatchClass, PlayerRank> captureDefaultRanks() {
        EnumMap<MatchClass, PlayerRank> defaults = new EnumMap<>(MatchClass.class);
        for (MatchClass matchClass : MatchClass.values()) {
            defaults.put(matchClass, matchClass.getMinRankRequired());
        }
        return defaults;
    }
}
