package dev.tomerdev.mercfrontcore.server;

import net.neoforged.neoforge.common.NeoForge;
import dev.tomerdev.mercfrontcore.server.event.MercFrontCoreServerEvents;

public final class MercFrontCoreServer {
    private static final MercFrontCoreServerEvents EVENTS = new MercFrontCoreServerEvents();

    private MercFrontCoreServer() {
    }

    public static void bootstrap() {
        NeoForge.EVENT_BUS.register(EVENTS);
    }
}
