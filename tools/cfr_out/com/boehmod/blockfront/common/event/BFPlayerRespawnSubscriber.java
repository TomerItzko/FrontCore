/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.neoforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class BFPlayerRespawnSubscriber {
    public static void onPlayerRespawn(@NotNull PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer serverPlayer = (ServerPlayer)event.getEntity();
        ServerLevel serverLevel = serverPlayer.serverLevel();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        bFAbstractManager.initPlayerForGame(bFAbstractManager, (PlayerDataHandler<?>)obj, serverLevel, serverPlayer, serverPlayer.getUUID());
    }
}

