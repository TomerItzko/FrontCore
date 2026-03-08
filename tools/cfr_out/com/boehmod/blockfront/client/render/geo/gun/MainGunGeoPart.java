/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  org.apache.commons.lang3.mutable.MutableInt
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3d
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;

public class MainGunGeoPart<T extends BFWeaponItem<T>>
extends AbstractGunGeoPart<T> {
    public MainGunGeoPart(BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        super(bFWeaponItemRenderer);
    }

    @Override
    public void method_1267(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, float f, float f2, float f3, float f4) {
        if (!itemDisplayContext.firstPerson()) {
            return;
        }
        String string = geoBone.getName();
        if (!string.startsWith("main") || !(itemStack.getItem() instanceof GunItem)) {
            return;
        }
        float f5 = 0.1f;
        Vector3d vector3d = geoBone.getRotationVector();
        Vector3d vector3d2 = geoBone.getPositionVector();
        boolean bl = string.equals("main");
        if (bl) {
            BFRenderHandSubscriber.field_359 = 0.0f;
            BFRenderHandSubscriber.field_360 = 0.0f;
            BFRenderHandSubscriber.field_361 = 0.0f;
        }
        BFRenderHandSubscriber.field_359 += (float)(vector3d.x * (double)0.1f * (double)f2);
        BFRenderHandSubscriber.field_360 += (float)(vector3d.y * (double)0.1f * (double)f2);
        BFRenderHandSubscriber.field_361 += (float)(vector3d.z * (double)0.1f * (double)f2);
        BFRenderHandSubscriber.field_359 += (float)(-vector3d2.y * 12.0 * (double)f2);
        vector3d2.mul((double)f2);
        vector3d.mul((double)f2);
    }

    @Override
    public boolean shouldSkipRendering(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, boolean bl, int n, int n2, int n3, float f, float f2, float f3) {
        return false;
    }

    @Override
    public boolean method_1265(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull GeoBone geoBone, @NotNull T t, @NotNull ItemStack itemStack, @NotNull ResourceLocation resourceLocation, @NotNull ItemDisplayContext itemDisplayContext, @NotNull VertexConsumer vertexConsumer, boolean bl, float f, int n, int n2, int n3) {
        return false;
    }

    @Override
    public boolean method_1266(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull ItemStack itemStack, @NotNull MutableInt mutableInt, @Nullable GunScopeConfig gunScopeConfig, @NotNull ItemDisplayContext itemDisplayContext, @NotNull BFClientPlayerData bFClientPlayerData) {
        return false;
    }
}

