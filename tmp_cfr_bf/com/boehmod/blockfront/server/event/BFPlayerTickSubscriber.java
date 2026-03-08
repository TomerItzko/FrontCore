/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public class BFPlayerTickSubscriber {
    @SubscribeEvent
    public static void onTickPost(@NotNull PlayerTickEvent.Post event) {
        ServerPlayer serverPlayer = (ServerPlayer)event.getEntity();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (!(bFAbstractManager instanceof BFServerManager)) {
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
        ServerPlayerDataHandler serverPlayerDataHandler = (ServerPlayerDataHandler)bFServerManager.getPlayerDataHandler();
        UUID uUID = serverPlayer.getUUID();
        BFServerPlayerData bFServerPlayerData = (BFServerPlayerData)serverPlayerDataHandler.getPlayerData((Player)serverPlayer);
        PlayerCloudData playerCloudData = serverPlayerDataHandler.getCloudProfile((Player)serverPlayer);
        ServerLevel serverLevel = serverPlayer.serverLevel();
        AbstractGame<?, ?, ?> abstractGame = bFServerManager.getGameWithPlayer(uUID);
        bFServerPlayerData.update(bFServerManager, serverLevel, serverPlayer, abstractGame);
        playerCloudData.method_1716((Player)serverPlayer);
        playerCloudData.method_1713(bFServerManager);
        boolean bl = bFServerManager.isMatchMakingEnabled();
        if (abstractGame != null) {
            ((AbstractGamePlayerManager)((Object)abstractGame.getPlayerManager())).tickPlayer(bFServerManager, serverLevel, serverPlayer, serverPlayer.getScoreboardName(), uUID, bl, playerCloudData, bFServerPlayerData);
        }
    }
}

