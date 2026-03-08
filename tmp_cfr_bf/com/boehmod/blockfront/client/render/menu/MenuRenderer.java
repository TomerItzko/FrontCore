/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.menu;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public abstract class MenuRenderer {
    public abstract void renderBackground(@NotNull BFClientManager var1, @NotNull ClientPlayerDataHandler var2, @NotNull Minecraft var3, @NotNull BFMenuScreen var4, @NotNull PoseStack var5, @NotNull Font var6, @NotNull GuiGraphics var7, int var8, int var9, int var10, int var11, float var12, float var13);

    public abstract void renderBelow(@NotNull Minecraft var1, @NotNull BFClientManager var2, PlayerCloudData var3, @NotNull BFMenuScreen var4, @NotNull GuiGraphics var5, @NotNull PoseStack var6, @NotNull Font var7, int var8, int var9, int var10, int var11, float var12, float var13);

    public abstract void renderAbove(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull BFMenuScreen var3, @NotNull PoseStack var4, @NotNull GuiGraphics var5, @NotNull Font var6, int var7, int var8, int var9, int var10, float var11, float var12);

    public abstract void tick(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull BFMenuScreen var3, float var4);
}

