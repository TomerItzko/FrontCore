/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent$DebugText
 *  net.neoforged.neoforge.client.event.RenderGuiLayerEvent$Pre
 *  net.neoforged.neoforge.client.gui.VanillaGuiLayers
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFGuiSubscriber {
    @SubscribeEvent
    public static void onRenderGuiLayerPre(@NotNull RenderGuiLayerEvent.Pre event) {
        Object object;
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ResourceLocation resourceLocation = event.getName();
        ItemStack itemStack = localPlayer.getMainHandItem();
        if (resourceLocation.equals((Object)VanillaGuiLayers.CROSSHAIR)) {
            if (bFClientManager.getCinematics().isSequencePlaying()) {
                event.setCanceled(true);
                return;
            }
            object = ((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getPlayerData(minecraft);
            boolean bl = BFUtils.isPlayerUnavailable((Player)localPlayer, object);
            if (bl) {
                event.setCanceled(true);
                return;
            }
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof IHasCrosshair) {
                event.setCanceled(true);
                return;
            }
            if (localPlayer.getVehicle() instanceof AbstractVehicleEntity) {
                event.setCanceled(true);
                return;
            }
        }
        if ((object = bFClientManager.getGame()) == null) {
            return;
        }
        if (resourceLocation.equals((Object)VanillaGuiLayers.ARMOR_LEVEL) || resourceLocation.equals((Object)VanillaGuiLayers.FOOD_LEVEL) || resourceLocation.equals((Object)VanillaGuiLayers.EXPERIENCE_BAR) || resourceLocation.equals((Object)VanillaGuiLayers.EXPERIENCE_LEVEL) || resourceLocation.equals((Object)VanillaGuiLayers.PLAYER_HEALTH) || resourceLocation.equals((Object)VanillaGuiLayers.VEHICLE_HEALTH) || resourceLocation.equals((Object)VanillaGuiLayers.HOTBAR) || resourceLocation.equals((Object)VanillaGuiLayers.TAB_LIST) || resourceLocation.equals((Object)VanillaGuiLayers.BOSS_OVERLAY)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderDebugText(CustomizeGuiOverlayEvent.DebugText event) {
        if (!Minecraft.getInstance().getDebugOverlay().showDebugScreen()) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        event.getRight().add("");
        event.getRight().add("bf".toUpperCase(Locale.ROOT) + " Cloud Info:");
        event.getRight().add("Connected: " + (clientConnectionManager.getStatus().isConnected() ? String.valueOf(ChatFormatting.GREEN) + "true" : String.valueOf(ChatFormatting.RED) + "false"));
        event.getRight().add("Reconnect Time: " + String.valueOf(ChatFormatting.GRAY) + clientConnectionManager.getConnection().getReconnectTickCount() / 20);
        event.getRight().add("Client UUID: " + String.valueOf(ChatFormatting.GRAY) + String.valueOf(clientConnectionManager.getUUID()));
        event.getRight().add("Client Name: " + String.valueOf(ChatFormatting.GRAY) + clientConnectionManager.getUsername());
        event.getRight().add("");
        event.getRight().add("Cached Player Data: " + String.valueOf(ChatFormatting.GRAY) + clientPlayerDataHandler.dataCacheSize());
        event.getRight().add("Cached Player Cloud Data: " + String.valueOf(ChatFormatting.GRAY) + clientPlayerDataHandler.profileCacheSize());
        String string = StringUtils.formatLong(bFClientManager.getParticleManager().count());
        event.getRight().add("");
        event.getRight().add("Instanced Particle Count: " + String.valueOf(ChatFormatting.GRAY) + string + String.valueOf(ChatFormatting.WHITE) + " / " + String.valueOf(ChatFormatting.GRAY) + "16384");
    }
}

