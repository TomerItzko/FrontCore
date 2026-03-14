package dev.tomerdev.mercfrontcore;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.client.MercFrontCoreClient;
import dev.tomerdev.mercfrontcore.event.MercFrontCoreModEvents;
import dev.tomerdev.mercfrontcore.server.MercFrontCoreServer;
import dev.tomerdev.mercfrontcore.util.ClassRankCompat;

@Mod(AddonConstants.MOD_ID)
public final class MercFrontCore {
    public static final Logger LOGGER = LogUtils.getLogger();

    public MercFrontCore(IEventBus modBus) {
        LOGGER.info("Initializing {} ({})", AddonConstants.MOD_NAME, AddonConstants.MOD_VERSION);
        MercFrontCoreConfigManager.load();
        ClassRankCompat.applyConfiguredRanks(MercFrontCoreConfigManager.get());
        modBus.addListener(MercFrontCoreModEvents::onRegisterPayloadHandlers);
        MercFrontCoreClient.bootstrap();
        MercFrontCoreServer.bootstrap();
    }
}
