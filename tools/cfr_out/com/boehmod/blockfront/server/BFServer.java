/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.common.BFCommonCloudPacketHandlers;
import com.boehmod.blockfront.cloud.server.BFServerCloudPacketHandlers;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.setup.BFServerPortSetup;
import com.boehmod.blockfront.util.BFLog;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import org.jetbrains.annotations.NotNull;

public class BFServer {
    public static void init(@NotNull BlockFront bf, @NotNull FMLDedicatedServerSetupEvent event) {
        BFServerManager bFServerManager = new BFServerManager();
        bf.setManager(bFServerManager);
        event.enqueueWork(() -> {
            BFLog.log("Setting up dedicated server...", new Object[0]);
            BFServerPortSetup.patchServerPort(bFServerManager);
            BFCommonCloudPacketHandlers.register();
            BFServerCloudPacketHandlers.register(bFServerManager);
            BFLog.log("Finished setting up dedicated server...", new Object[0]);
        });
    }
}

