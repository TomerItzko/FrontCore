/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemArmour
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CloudItemArmorRenderer
extends DefaultCloudItemRenderer<CloudItemArmour> {
    @Override
    public void method_1747(@NotNull CloudItemArmour cloudItemArmour, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItemArmour, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, (float)n / 2.0f - 10.0f, (float)n2 / 2.0f - 15.0f, 7.5f);
    }
}

