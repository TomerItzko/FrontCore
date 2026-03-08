/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.EnvironmentUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public final class BFServerTickSubscriber {
    @SubscribeEvent
    public static void onTickPost(@NotNull ServerTickEvent.Post event) {
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer == null) {
            return;
        }
        ServerLevel serverLevel = minecraftServer.getLevel(Level.OVERWORLD);
        if (serverLevel == null) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager instanceof BFServerManager) {
            BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
            bFServerManager.update(minecraftServer, serverLevel);
        }
    }
}

