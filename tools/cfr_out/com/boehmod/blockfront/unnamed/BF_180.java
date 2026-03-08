/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public abstract class BF_180 {
    public final float field_1010;
    public final float field_1011;
    public float field_1008;
    public float field_1009 = 1.0f;

    public BF_180(float f, float f2) {
        this.field_1010 = f;
        this.field_1011 = f2;
        this.field_1008 = 1.0f;
    }

    public boolean method_742() {
        this.field_1009 = this.field_1008;
        this.field_1008 -= 0.05f;
        return this.field_1008 <= 0.0f;
    }

    public abstract void method_741(@NotNull PoseStack var1, @NotNull GuiGraphics var2, float var3);
}

