/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.unnamed.BF_180;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class BF_179
extends BF_180 {
    public BF_179(float f, float f2) {
        super(f, f2);
    }

    @Override
    public void method_741(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f) {
        float f2 = MathUtils.lerpf1(this.field_1008, this.field_1009, f);
        BFRendering.rectangle(poseStack, guiGraphics, 125.0f - this.field_1010, 125.0f - this.field_1011 - 5.0f * f2, 10.0f, 10.0f, ColorReferences.COLOR_WHITE_SOLID, 0.3f * f2);
    }
}

