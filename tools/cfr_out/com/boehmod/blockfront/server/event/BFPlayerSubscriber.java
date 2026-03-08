/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedOutEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.server.BFServerManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public class BFPlayerSubscriber {
    @SubscribeEvent
    public static void onPlayerLoggedIn(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (!(bFAbstractManager instanceof BFServerManager)) {
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
        ServerPlayer serverPlayer = (ServerPlayer)event.getEntity();
        bFServerManager.handlePlayerJoin(serverPlayer);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(@NotNull PlayerEvent.PlayerLoggedOutEvent event) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (!(bFAbstractManager instanceof BFServerManager)) {
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
        ServerPlayer serverPlayer = (ServerPlayer)event.getEntity();
        bFServerManager.method_4575(serverPlayer);
    }
}

