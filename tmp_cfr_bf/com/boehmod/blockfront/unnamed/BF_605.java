/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BF_605 {
    public abstract boolean method_2222(@NotNull Minecraft var1, @NotNull BFClientManager var2, @Nullable LocalPlayer var3, @Nullable ClientLevel var4);

    public abstract void method_2223(@NotNull Minecraft var1, @NotNull PoseStack var2, @NotNull GuiGraphics var3, @NotNull Font var4, int var5, int var6, float var7);
}

