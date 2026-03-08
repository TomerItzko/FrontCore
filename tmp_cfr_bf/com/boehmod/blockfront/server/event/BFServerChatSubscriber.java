/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public final class BFServerChatSubscriber {
    @SubscribeEvent
    public static void onEvent(@NotNull ServerChatEvent event) {
        ServerPlayer serverPlayer = event.getPlayer();
        ServerLevel serverLevel = serverPlayer.serverLevel();
        UUID uUID = serverPlayer.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return;
        }
        BFUtils.handleGamePlayerChat(bFAbstractManager, bFAbstractManager.getPlayerDataHandler(), abstractGame, serverLevel, serverPlayer, event.getMessage(), uUID);
        event.setCanceled(true);
    }
}

