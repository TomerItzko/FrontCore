/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCoin;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CloudItemCoinRenderer
extends DefaultCloudItemRenderer<CloudItemCoin> {
    @Override
    public void method_1746(@NotNull CloudItemCoin cloudItemCoin, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4, float f5) {
        this.method_1745(cloudItemCoin, cloudItemStack, poseStack, minecraft, guiGraphics, f, f2, f3, f4, f5);
    }

    @Override
    public void method_1745(@NotNull CloudItemCoin cloudItemCoin, @NotNull CloudItemStack cloudItemStack, PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4, float f5) {
        ResourceLocation resourceLocation = BFRes.loc("textures/coins/" + cloudItemCoin.getSuffixForDisplay() + ".png");
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, f, f2, f3 * f5, f4 * f5);
    }

    @Override
    public void method_1747(@NotNull CloudItemCoin cloudItemCoin, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        float f2 = 7.0f;
        int n5 = n / 2;
        int n6 = n2 / 2;
        float f3 = -0.5f + (float)n4 / (float)n2;
        float f4 = -0.5f + (float)n3 / (float)n;
        ArmoryInspectScreen.field_949 = 25.0f * f3;
        ArmoryInspectScreen.field_950 = 65.0f * f4;
        float f5 = 112.0f;
        float f6 = 112.0f;
        ResourceLocation resourceLocation = BFRes.loc("textures/coins/" + cloudItemCoin.getSuffixForDisplay() + ".png");
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, (float)n5, (float)(n6 - 8), 112.0f, 112.0f, 0.0f);
    }

    @Override
    public void method_1749(@NotNull CloudItemCoin cloudItemCoin, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2, float f3) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f4 = 1.2f;
        float f5 = 19.2f;
        float f6 = 19.2f;
        ResourceLocation resourceLocation = BFRes.loc("textures/coins/" + cloudItemCoin.getSuffixForDisplay() + ".png");
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, (float)(n + n7), (float)(n2 + n8 - 7), 19.2f, 19.2f, 0.0f);
    }

    @Override
    public void method_1748(@NotNull CloudItemCoin cloudItemCoin, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f2 = 3.0f;
        float f3 = 48.0f;
        float f4 = 48.0f;
        ResourceLocation resourceLocation = BFRes.loc("textures/coins/" + cloudItemCoin.getSuffixForDisplay() + ".png");
        BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, (float)(n + n7), (float)(n2 + n8 - 3), 48.0f, 48.0f, 0.0f);
    }
}

