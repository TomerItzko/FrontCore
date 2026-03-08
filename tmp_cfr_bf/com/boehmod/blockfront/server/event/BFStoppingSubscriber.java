/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.BFLog;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public class BFStoppingSubscriber {
    @SubscribeEvent
    public static void onServerStopping(@NotNull ServerStoppingEvent event) {
        BFLog.log("Shutting down cloud manager for the dedicated server...", new Object[0]);
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager instanceof BFServerManager) {
            BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
            bFServerManager.shutdown();
        }
    }
}

