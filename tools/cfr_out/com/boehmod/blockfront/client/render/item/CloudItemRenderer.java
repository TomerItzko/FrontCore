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
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.item;

import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;

public abstract class CloudItemRenderer<T extends CloudItem<?>> {
    public abstract void method_1746(@NotNull T var1, @NotNull CloudItemStack var2, @NotNull Minecraft var3, PoseStack var4, @NotNull GuiGraphics var5, float var6, float var7, float var8, float var9, float var10);

    public abstract void method_1745(@NotNull T var1, @NotNull CloudItemStack var2, PoseStack var3, @NotNull Minecraft var4, @NotNull GuiGraphics var5, float var6, float var7, float var8, float var9, float var10);

    public abstract void method_1747(@NotNull T var1, @NotNull CloudItemStack var2, @NotNull Minecraft var3, PoseStack var4, @NotNull GuiGraphics var5, int var6, int var7, int var8, int var9, float var10);

    public abstract void method_1749(@NotNull T var1, @NotNull CloudItemStack var2, @NotNull Minecraft var3, @NotNull GuiGraphics var4, @NotNull PoseStack var5, @NotNull MultiBufferSource var6, int var7, int var8, int var9, int var10, int var11, int var12, float var13, float var14, float var15);

    public abstract void method_1748(@NotNull T var1, @NotNull CloudItemStack var2, @NotNull Minecraft var3, @NotNull GuiGraphics var4, @NotNull PoseStack var5, @NotNull MultiBufferSource var6, int var7, int var8, int var9, int var10, int var11, int var12, float var13);
}

