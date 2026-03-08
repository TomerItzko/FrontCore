package dev.tomerdev.mercfrontcore.config;

public final class MercFrontCoreConfig {
    public ProxySettings proxy = new ProxySettings();
    public AudioSettings audio = new AudioSettings();

    public static final class ProxySettings {
        public boolean enableProxyCompatibility = true;
        public boolean enforceDirectConnection = false;
        public boolean trustForwardedIdentity = true;
    }

    public static final class AudioSettings {
        public float gunSfxVolume = 1.0f;
        public float grenadeSfxVolume = 1.0f;
    }
}
