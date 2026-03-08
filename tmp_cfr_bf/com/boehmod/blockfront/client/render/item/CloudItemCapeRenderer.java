/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCape;
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

public class CloudItemCapeRenderer
extends DefaultCloudItemRenderer<CloudItemCape> {
    @Override
    public void method_1749(@NotNull CloudItemCape cloudItemCape, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2, float f3) {
        ResourceLocation resourceLocation = BFRes.loc("textures/models/capes/" + cloudItemCape.getSuffixForDisplay() + ".png");
        int n7 = 10;
        int n8 = 17;
        float f4 = -0.5f + (float)n6 / (float)n4;
        float f5 = -0.5f + (float)n5 / (float)n3;
        ArmoryInspectScreen.field_949 = 25.0f * f4;
        ArmoryInspectScreen.field_950 = 65.0f * f5;
        BFRendering.resetShaderColor();
        guiGraphics.blit(resourceLocation, n + n3 / 2 - 5, n2 + 4, 1.0f, 0.0f, 10, 17, 64, 32);
    }

    @Override
    public void method_1748(@NotNull CloudItemCape cloudItemCape, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        poseStack.pushPose();
        poseStack.translate((float)(n + n7 - 4), (float)(n2 + n8 + 15), 0.0f);
        poseStack.pushPose();
        BFRendering.resetShaderColor();
        ResourceLocation resourceLocation = BFRes.loc("textures/models/capes/" + cloudItemCape.getSuffixForDisplay() + ".png");
        int n9 = 3;
        int n10 = 10;
        int n11 = 17;
        guiGraphics.blit(resourceLocation, -10, -55, 3.0f, 0.0f, 30, 51, 192, 96);
        poseStack.popPose();
        poseStack.popPose();
    }
}

