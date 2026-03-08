package dev.tomerdev.mercfrontcore.server;

import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;

public final class ProxyCompatibility {
    private ProxyCompatibility() {
    }

    public static boolean isCompatibilityEnabled() {
        MercFrontCoreConfig cfg = MercFrontCoreConfigManager.get();
        return cfg.proxy.enableProxyCompatibility;
    }

    public static boolean shouldBlockForwardedPlayers() {
        MercFrontCoreConfig cfg = MercFrontCoreConfigManager.get();
        return cfg.proxy.enforceDirectConnection;
    }
}
