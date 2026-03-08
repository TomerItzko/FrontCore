/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.renderer.DimensionSpecialEffects$OverworldEffects
 *  net.minecraft.client.renderer.LightTexture
 *  net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.world;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BFOverworldEffects
extends DimensionSpecialEffects.OverworldEffects
implements IDimensionSpecialEffectsExtension {
    public boolean renderClouds(@NotNull ClientLevel clientLevel, int n, float f, @NotNull PoseStack poseStack, double d, double d2, double d3, @NotNull Matrix4f matrix4f, @NotNull Matrix4f matrix4f2) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null) {
            return abstractGame.getMapEnvironment().getDisableClouds();
        }
        return false;
    }

    public boolean renderSky(@NotNull ClientLevel clientLevel, int n, float f, @NotNull Matrix4f matrix4f, @NotNull Camera camera, @NotNull Matrix4f matrix4f2, boolean bl, @NotNull Runnable runnable) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null) {
            return abstractGame.getMapEnvironment().getDisableSky();
        }
        return false;
    }

    public boolean renderSnowAndRain(@NotNull ClientLevel clientLevel, int n, float f, @NotNull LightTexture lightTexture, double d, double d2, double d3) {
        return false;
    }

    public boolean tickRain(@NotNull ClientLevel clientLevel, int n, @NotNull Camera camera) {
        return false;
    }

    public void adjustLightmapColors(@NotNull ClientLevel clientLevel, float f, float f2, float f3, float f4, int n, int n2, @NotNull Vector3f vector3f) {
        MapEnvironment mapEnvironment;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null && (mapEnvironment = abstractGame.getMapEnvironment()).hasCustomLightColor()) {
            int n3 = mapEnvironment.getCustomLightColor();
            vector3f.add((float)(n3 >> 16 & 0xFF) / 255.0f, (float)(n3 >> 8 & 0xFF) / 255.0f, (float)(n3 & 0xFF) / 255.0f);
        }
    }
}

