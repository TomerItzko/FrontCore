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
 *  org.joml.Vector3f
 *  software.bernie.geckolib.cache.object.GeoBone
 */
package com.boehmod.blockfront.client.render.geo.gun;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.client.render.geo.gun.AbstractGunGeoPart;
import com.boehmod.blockfront.common.gun.GunMagType;
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
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;

public class RevolverGunGeoPart<T extends BFWeaponItem<T>>
extends AbstractGunGeoPart<T> {
    public RevolverGunGeoPart(BFWeaponItemRenderer<T> bFWeaponItemRenderer) {
        super(bFWeaponItemRenderer);
    }

    @Override
    public void method_1267(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, float f, float f2, float f3, float f4) {
    }

    @Override
    public boolean shouldSkipRendering(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GeoBone geoBone, @NotNull ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull T t, boolean bl, int n, int n2, int n3, float f, float f2, float f3) {
        if (!(t instanceof GunItem)) {
            return false;
        }
        GunItem gunItem = (GunItem)t;
        if (!gunItem.method_4188() || gunItem.method_4190()) {
            return false;
        }
        GunMagType gunMagType = gunItem.getMagTypeOrDefault(itemStack);
        int n4 = gunMagType.capacity();
        float f4 = GunItem.getAmmoLoaded(itemStack);
        float f5 = 1.0f - f4 / (float)n4;
        String string = geoBone.getName();
        if (string.equalsIgnoreCase("mag")) {
            Vector3f vector3f = gunItem.method_4195();
            poseStack.translate(vector3f.x * f5, vector3f.y * f5, vector3f.z * f5);
        }
        if (string.startsWith("bullet")) {
            try {
                boolean bl2;
                int n5 = Integer.parseInt(string.replace("bullet", ""));
                boolean bl3 = gunItem.method_4189() ? (float)n5 > f4 - 1.0f : (bl2 = (float)n5 < (float)n4 - f4);
                if (bl2) {
                    return true;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
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

