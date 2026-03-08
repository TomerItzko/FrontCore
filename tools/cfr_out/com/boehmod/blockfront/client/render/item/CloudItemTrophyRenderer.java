/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemTrophy
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemTrophy;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CloudItemTrophyRenderer
extends DefaultCloudItemRenderer<CloudItemTrophy> {
    @Override
    public void method_1746(@NotNull CloudItemTrophy cloudItemTrophy, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4, float f5) {
        this.method_1745(cloudItemTrophy, cloudItemStack, poseStack, minecraft, guiGraphics, f, f2, f3, f4, 0.5f);
    }

    @Override
    public void method_1745(@NotNull CloudItemTrophy cloudItemTrophy, @NotNull CloudItemStack cloudItemStack, PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, float f, float f2, float f3, float f4, float f5) {
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItemTrophy, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, f, f2, f5);
    }

    @Override
    public void method_1747(@NotNull CloudItemTrophy cloudItemTrophy, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        float f2 = (float)(3.0 * minecraft.getWindow().getGuiScale());
        float f3 = -0.5f + (float)n4 / (float)n2;
        float f4 = -0.5f + (float)n3 / (float)n;
        int n5 = n / 2;
        int n6 = n2 / 2;
        poseStack.pushPose();
        poseStack.translate(0.0f, -15.0f, 150.0f);
        ArmoryInspectScreen.field_949 = 25.0f * f3;
        ArmoryInspectScreen.field_950 = 65.0f * f4;
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItemTrophy, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, n5, n6, f2);
        poseStack.popPose();
    }

    @Override
    public void method_1749(@NotNull CloudItemTrophy cloudItemTrophy, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2, float f3) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f4 = 1.2f;
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItemTrophy, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, n + n7, n2 + n8 - 8, 1.2f);
    }

    @Override
    public void method_1748(@NotNull CloudItemTrophy cloudItemTrophy, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        int n7 = n3 / 2;
        int n8 = n4 / 2;
        float f2 = 3.5f;
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItemTrophy, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, n + n7, n2 + n8 - 3, 3.5f);
    }
}

