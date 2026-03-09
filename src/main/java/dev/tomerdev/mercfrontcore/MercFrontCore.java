package dev.tomerdev.mercfrontcore;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.client.MercFrontCoreClient;
import dev.tomerdev.mercfrontcore.event.MercFrontCoreModEvents;
import dev.tomerdev.mercfrontcore.server.MercFrontCoreServer;

@Mod(AddonConstants.MOD_ID)
public final class MercFrontCore {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final String BUILD_MARKER = "MFC-BUILD-2026-03-09-DROPRESET-01";

    public MercFrontCore(IEventBus modBus) {
        LOGGER.info("Initializing {} ({})", AddonConstants.MOD_NAME, AddonConstants.MOD_VERSION);
        LOGGER.error("MERCFRONTCORE BUILD MARKER {}", BUILD_MARKER);
        MercFrontCoreConfigManager.load();
        modBus.addListener(MercFrontCoreModEvents::onRegisterPayloadHandlers);
        MercFrontCoreClient.bootstrap();
        MercFrontCoreServer.bootstrap();
    }
}
