/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CloudItemCallingCardRenderer
extends DefaultCloudItemRenderer<CloudItemCallingCard> {
    @Override
    public void method_1747(@NotNull CloudItemCallingCard cloudItemCallingCard, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        int n5 = n / 2;
        float f2 = 0.5f;
        float f3 = 220.0f;
        float f4 = 50.0f;
        ResourceLocation resourceLocation = BFRes.loc("textures/gui/callingcard/" + cloudItemCallingCard.getSuffixForDisplay() + ".png");
        BFRendering.rectangleWithDarkShadow(poseStack, guiGraphics, (float)(n5 + 3) - 110.0f, 85.0f, 220.0f, 50.0f, 0, 0.5f);
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, (float)(n5 + 3), 110.0f, 220.0f, 50.0f);
    }

    @Override
    public void method_1749(@NotNull CloudItemCallingCard cloudItemCallingCard, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2, float f3) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        int n9 = 40;
        int n10 = 10;
        ResourceLocation resourceLocation = BFRes.loc("textures/gui/callingcard/" + cloudItemCallingCard.getSuffixForDisplay() + ".png");
        BFRendering.centeredRectangleWithShadow(poseStack, guiGraphics, f + (float)n7, n2 + n8 - 8, 40.0f, 10.0f, 0, 0.0f);
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, f + (float)n7, (float)(n2 + n8 - 8), 40.0f, 10.0f, 0.0f);
    }

    @Override
    public void method_1748(@NotNull CloudItemCallingCard cloudItemCallingCard, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f2 = 1.5f;
        float f3 = 90.0f;
        float f4 = 15.0f;
        ResourceLocation resourceLocation = BFRes.loc("textures/gui/callingcard/" + cloudItemCallingCard.getSuffixForDisplay() + ".png");
        float f5 = n + n7;
        float f6 = n2 + n8 - 2;
        float f7 = 180.0f;
        float f8 = 45.0f;
        BFRendering.rectangleWithDarkShadow(poseStack, guiGraphics, f5 - 90.0f, f6 - 22.5f, 180.0f, 45.0f, BFRendering.translucentBlack());
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, f5, f6, 180.0f, 45.0f);
    }
}

