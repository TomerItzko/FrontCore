/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFViewportSubscriber {
    @SubscribeEvent
    public static void onRenderFog(@NotNull ViewportEvent.RenderFog event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gameRenderer.getMainCamera().getFluidInCamera() != FogType.NONE) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
        if (!mapEnvironment.hasCustomFogDensity()) {
            return;
        }
        event.setCanceled(true);
        event.setNearPlaneDistance(mapEnvironment.getNearFogDensity());
        event.setFarPlaneDistance(mapEnvironment.getFarFogDensity());
        event.setFogShape(FogShape.CYLINDER);
    }

    @SubscribeEvent
    public static void onComputeFogColor(@NotNull ViewportEvent.ComputeFogColor event) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
        if (!mapEnvironment.hasCustomFogColor()) {
            return;
        }
        int n = mapEnvironment.getCustomFogColor();
        event.setRed((float)(n >> 16 & 0xFF) / 255.0f);
        event.setGreen((float)(n >> 8 & 0xFF) / 255.0f);
        event.setBlue((float)(n & 0xFF) / 255.0f);
    }
}

