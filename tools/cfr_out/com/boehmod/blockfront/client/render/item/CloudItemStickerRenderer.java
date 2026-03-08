/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemSticker
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemSticker;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;

public class CloudItemStickerRenderer
extends DefaultCloudItemRenderer<CloudItemSticker> {
    @Override
    public void method_1747(@NotNull CloudItemSticker cloudItemSticker, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        int n5 = n / 2;
        int n6 = n2 / 2;
        float f2 = 0.75f;
        float f3 = 150.0f * f2;
        float f4 = 150.0f * f2;
        BFRendering.centeredTexture(poseStack, guiGraphics, BFRes.loc("textures/stickers/" + cloudItemSticker.getSuffixForDisplay() + ".png"), (float)n5, (float)n6, f3, f4);
    }

    @Override
    public void method_1749(@NotNull CloudItemSticker cloudItemSticker, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2, float f3) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        poseStack.pushPose();
        BFRendering.centeredTexture(poseStack, guiGraphics, BFRes.loc("textures/stickers/" + cloudItemSticker.getSuffixForDisplay() + ".png"), f + (float)n7, (float)(n2 + n8 - 7), 22.0f, 22.0f, 0.0f);
        poseStack.popPose();
    }

    @Override
    public void method_1748(@NotNull CloudItemSticker cloudItemSticker, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f2 = 1.9f;
        float f3 = 19.0f;
        float f4 = 19.0f;
        poseStack.pushPose();
        BFRendering.centeredTexture(poseStack, guiGraphics, BFRes.loc("textures/stickers/" + cloudItemSticker.getSuffixForDisplay() + ".png"), (float)(n + n7), (float)(n2 + n8 - 4), 57.0f, 57.0f, 0.0f, 6.0f);
        poseStack.popPose();
    }
}

