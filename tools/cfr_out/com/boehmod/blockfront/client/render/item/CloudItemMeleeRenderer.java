/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemMelee
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemMelee;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.DefaultCloudItemRenderer;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CloudItemMeleeRenderer
extends DefaultCloudItemRenderer<CloudItemMelee> {
    @Override
    public void method_1747(@NotNull CloudItemMelee cloudItemMelee, @NotNull CloudItemStack cloudItemStack, @NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, int n4, float f) {
        float f2 = (float)(7.0 * minecraft.getWindow().getGuiScale());
        int n5 = n / 2;
        int n6 = n2 / 2;
        poseStack.pushPose();
        float f3 = -0.5f + (float)n4 / (float)n2;
        float f4 = -0.5f + (float)n3 / (float)n;
        poseStack.translate(0.0f, -30.0f, 150.0f);
        ArmoryInspectScreen.field_949 = 25.0f * f3;
        ArmoryInspectScreen.field_950 = 65.0f * f4;
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItemMelee, cloudItemStack);
        BFRendering.item(poseStack, guiGraphics, itemStack, n5, n6 + 30, f2);
        poseStack.popPose();
    }

    @Override
    protected float method_1757() {
        return 2.0f;
    }

    @Override
    protected float method_1758() {
        return 8.0f;
    }
}

