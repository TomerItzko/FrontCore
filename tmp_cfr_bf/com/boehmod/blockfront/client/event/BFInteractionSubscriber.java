/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFInteractionSubscriber {
    @SubscribeEvent
    public static void onEntityInteract(@NotNull PlayerInteractEvent.EntityInteract event) {
        BFInteractionSubscriber.onPlayerInteract((PlayerInteractEvent)event);
    }

    @SubscribeEvent
    public static void onRightClickBlock(@NotNull PlayerInteractEvent.RightClickBlock event) {
        BFInteractionSubscriber.onPlayerInteract((PlayerInteractEvent)event);
    }

    @SubscribeEvent
    public static void onRightClickItem(@NotNull PlayerInteractEvent.RightClickItem event) {
        BFInteractionSubscriber.onPlayerInteract((PlayerInteractEvent)event);
    }

    @SubscribeEvent
    public static void onRightClickEmpty(@NotNull PlayerInteractEvent.RightClickEmpty event) {
        BFInteractionSubscriber.onPlayerInteract((PlayerInteractEvent)event);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(@NotNull PlayerInteractEvent.LeftClickBlock event) {
        BFInteractionSubscriber.onPlayerInteract((PlayerInteractEvent)event);
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(@NotNull PlayerInteractEvent.LeftClickEmpty event) {
        BFInteractionSubscriber.onPlayerInteract((PlayerInteractEvent)event);
    }

    private static void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        BFClientPlayerData bFClientPlayerData;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Mod manager is not null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        Player player = event.getEntity();
        if (BFUtils.isPlayerUnavailable(player, bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player)) && event instanceof ICancellableEvent) {
            ICancellableEvent iCancellableEvent = (ICancellableEvent)event;
            iCancellableEvent.setCanceled(true);
        }
    }
}

