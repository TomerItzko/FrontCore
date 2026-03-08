/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.menu;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public interface MenuParticle {
    public void init(@NotNull Minecraft var1);

    public boolean shouldRemove(float var1);

    public void renderBelow(@NotNull Minecraft var1, @NotNull GuiGraphics var2, @NotNull PoseStack var3, float var4, float var5);

    public void renderAbove(@NotNull Minecraft var1, @NotNull GuiGraphics var2, @NotNull PoseStack var3, float var4, float var5);

    @NotNull
    public Type getType();

    public static enum Type {
        MAIN,
        BACKGROUND;

    }
}

