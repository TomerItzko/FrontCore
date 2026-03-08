/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DefaultCloudItemRenderer<T extends CloudItem<?>>
extends CloudItemRenderer<T> {
    private static final float field_7035 = 3.0f;
    private static final float field_7036 = 1.5f;

    @Override
    public void method_1746(@NotNull T t, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4, float f5) {
    }

    @Override
    public void method_1745(@NotNull T t, @NotNull CloudItemStack cloudItemStack, PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4, float f5) {
    }

    @Override
    public void method_1747(@NotNull T t, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        int n5 = n / 2;
        int n6 = n2 / 2;
        ItemStack itemStack = ItemSkinIndex.method_1722(t, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, n5, (float)n6 - 10.0f, 6.0f);
    }

    @Override
    public void method_1749(@NotNull T t, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2, float f3) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f4 = this.method_1757();
        float f5 = f + (float)n7;
        float f6 = (float)(n2 + n8) - 7.5f;
        ItemStack itemStack = ItemSkinIndex.method_1722(t, cloudItemStack);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, this.method_5956());
        BFRendering.item(poseStack, guiGraphics, itemStack, f5, f6, f4);
        poseStack.popPose();
    }

    protected float method_5956() {
        return 0.0f;
    }

    protected float method_1757() {
        return 1.5f;
    }

    @Override
    public void method_1748(@NotNull T t, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f2 = this.method_1758();
        ItemStack itemStack = ItemSkinIndex.method_1722(t, cloudItemStack);
        float f3 = (float)(n + n7) - 4.0f;
        float f4 = n2 + n8;
        BFRendering.item(poseStack, guiGraphics, itemStack, f3, f4, f2);
    }

    protected float method_1758() {
        return 3.0f;
    }
}

