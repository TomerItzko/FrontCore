/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.event.tick.PlayerTickEvent$Post
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFPlayerTickSubscriber {
    @SubscribeEvent
    public static void onPlayerTickPost(@NotNull PlayerTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        Player player = event.getEntity();
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player);
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(player);
        bFClientPlayerData.update(bFClientManager, player.level(), player, abstractGame);
        bFClientPlayerData.method_1140(minecraft, bFClientManager, clientPlayerDataHandler, ThreadLocalRandom.current(), clientLevel, localPlayer, abstractGame, abstractGame != null ? abstractGameClient : null, player);
        playerCloudData.method_1716(player);
    }
}

