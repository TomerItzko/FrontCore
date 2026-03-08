package dev.tomerdev.mercfrontcore.client.sound;

import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;

public final class SoundVolumeResolver {
    private SoundVolumeResolver() {
    }

    public static float resolveScale(String soundName) {
        SoundType type = classify(soundName);
        MercFrontCoreConfig.AudioSettings audio = MercFrontCoreConfigManager.get().audio;
        return switch (type) {
            case GUN -> clamp(audio.gunSfxVolume);
            case GRENADE -> clamp(audio.grenadeSfxVolume);
            case OTHER -> 1.0f;
        };
    }

    private static SoundType classify(String soundName) {
        String s = normalize(soundName).toLowerCase(java.util.Locale.ROOT);
        if (!isBlockfrontSound(s)) {
            return SoundType.OTHER;
        }
        if (isGrenadeSound(s)) {
            return SoundType.GRENADE;
        }
        if (isGunSound(s)) {
            return SoundType.GUN;
        }
        return SoundType.OTHER;
    }

    private static boolean isBlockfrontSound(String soundName) {
        return soundName.startsWith("bf:")
            || soundName.startsWith("blockfront:")
            || soundName.startsWith("item.gun.")
            || soundName.startsWith("item.grenade.")
            || soundName.startsWith("item.rocket.")
            || soundName.startsWith("item.rpg.")
            || soundName.startsWith("item.launcher.");
    }

    private static boolean isGunSound(String s) {
        return s.contains(".gun.")
            || s.contains("item.gun.")
            || s.contains(".weapon.")
            || s.contains(".rifle.")
            || s.contains(".pistol.")
            || s.contains(".shotgun.")
            || s.contains(".smg.")
            || s.contains(".lmg.")
            || s.endsWith(".fire")
            || s.endsWith(".shot")
            || s.contains(".reload.")
            || s.contains(".bullet.");
    }

    private static boolean isGrenadeSound(String s) {
        return s.contains(".grenade.")
            || s.contains(".rocket.")
            || s.contains(".rpg.")
            || s.contains(".launcher.")
            || s.contains(".explosion")
            || s.contains(".explode")
            || s.contains(".detonate")
            || s.contains(".blast");
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static float clamp(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }

    private enum SoundType {
        GUN,
        GRENADE,
        OTHER
    }
}
