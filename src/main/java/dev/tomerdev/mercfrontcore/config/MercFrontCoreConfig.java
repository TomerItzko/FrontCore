package dev.tomerdev.mercfrontcore.config;

public final class MercFrontCoreConfig {
    public ProxySettings proxy = new ProxySettings();
    public AudioSettings audio = new AudioSettings();
    public RewardSettings rewards = new RewardSettings();

    public static final class ProxySettings {
        public boolean enableProxyCompatibility = true;
        public boolean enforceDirectConnection = false;
        public boolean trustForwardedIdentity = true;
    }

    public static final class AudioSettings {
        public float gunSfxVolume = 1.0f;
        public float grenadeSfxVolume = 1.0f;
    }

    public static final class RewardSettings {
        public boolean enableWinnerSkinDrops = true;
        public float winnerSkinDropChance = 0.25f;
    }
}
