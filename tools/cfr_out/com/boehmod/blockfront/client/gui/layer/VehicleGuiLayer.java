/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.floats.FloatFloatPair
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.CubeMap
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.gui.layer;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.gui.BFVehiclePanoramas;
import com.boehmod.blockfront.client.gui.layer.BFAbstractGuiLayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_622;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public final class VehicleGuiLayer
extends BFAbstractGuiLayer {
    private static final ResourceLocation field_1007 = BFRes.loc("textures/gui/overlay/vehicledamage.png");

    public static void method_740(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull AbstractVehicleEntity abstractVehicleEntity, float f, float f2) {
        BF_623 bF_623 = abstractVehicleEntity.method_2324((Entity)localPlayer);
        if (bF_623 == null) {
            return;
        }
        CubeMap cubeMap = BFVehiclePanoramas.getCubeMap(bF_623.method_2378());
        if (cubeMap == null) {
            return;
        }
        float f3 = MathUtils.lerpf1(PlayerTickable.field_147, PlayerTickable.field_148, f2);
        float f4 = Mth.sin((float)(f / 2.8f)) + 1.0f;
        float f5 = Mth.sin((float)(f / 3.2f)) * f4;
        float f6 = Mth.sin((float)(f / 2.4f));
        float f7 = Mth.sin((float)(f / 3.6f)) * f6;
        float f8 = 0.1f * f5;
        float f9 = 0.2f * f7;
        float f10 = f8 * f3;
        float f11 = f9 * f3;
        Entity entity = minecraft.getCameraEntity();
        float f12 = entity != null ? entity.getYRot() : 0.0f;
        float f13 = MathUtils.lerpf1(abstractVehicleEntity.getXRot(), abstractVehicleEntity.xRotO, f2);
        float f14 = MathUtils.lerpf1(abstractVehicleEntity.getYRot(), abstractVehicleEntity.yRotO, f2) - f12;
        BF_622 bF_622 = abstractVehicleEntity.method_2316();
        FloatFloatPair floatFloatPair = bF_622.method_2373(f2);
        f13 += floatFloatPair.firstFloat();
        f14 += -floatFloatPair.secondFloat();
        BF_623 bF_6232 = abstractVehicleEntity.method_2324((Entity)localPlayer);
        if (bF_6232 != null) {
            for (BF_633 bF_633 : bF_6232.method_2388()) {
                f14 += bF_633.method_2471(f2);
            }
        }
        Vector3f vector3f = ShakeManager.getDelta(f2);
        cubeMap.render(minecraft, -f13 + f10 + vector3f.x, f14 + f11 + vector3f.y, 1.0f);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta, @NotNull BFClientManager manager) {
        Entity entity;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null || !minecraft.options.getCameraType().isFirstPerson() || !((entity = localPlayer.getVehicle()) instanceof AbstractVehicleEntity)) {
            return;
        }
        AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
        int n = graphics.guiWidth();
        int n2 = graphics.guiHeight();
        PoseStack poseStack = graphics.pose();
        float f = BFRendering.getRenderTime();
        VehicleGuiLayer.method_740(minecraft, localPlayer, abstractVehicleEntity, f, delta.getGameTimeDeltaPartialTick(false));
        if (abstractVehicleEntity.method_2331()) {
            float f2 = (Mth.sin((float)(f / 6.0f)) + 1.0f) / 2.0f * 0.75f;
            BFRendering.texture(poseStack, graphics, field_1007, 0, 0, n, n2, f2);
        }
    }
}

