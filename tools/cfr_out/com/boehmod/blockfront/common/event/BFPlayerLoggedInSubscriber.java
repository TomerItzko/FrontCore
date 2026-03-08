/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.RequestType
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class BFPlayerLoggedInSubscriber {
    public static void onPlayerLoggedIn(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        BFServerManager bFServerManager;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        ServerPlayer serverPlayer = (ServerPlayer)event.getEntity();
        ServerLevel serverLevel = serverPlayer.serverLevel();
        UUID uUID = serverPlayer.getUUID();
        Object obj = bFAbstractManager.getPlayerDataHandler();
        MinecraftServer minecraftServer = serverPlayer.getServer();
        if (bFAbstractManager instanceof BFServerManager && (bFServerManager = (BFServerManager)bFAbstractManager).isMatchMakingEnabled() && minecraftServer != null && minecraftServer.isDedicatedServer()) {
            BFUtils.initPlayerForGame(obj, serverLevel, serverPlayer);
            BFUtils.initPlayerForGame(bFAbstractManager, obj, serverLevel, serverPlayer);
            ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).getRequester().push(RequestType.PLAYER_DATA, uUID);
        }
        ((PlayerDataHandler)obj).method_862(bFAbstractManager, (Level)serverLevel);
    }
}

