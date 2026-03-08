/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BF_340 {
    private static final float field_1711 = 45.0f;
    public static final float field_1712 = 0.7f;
    private static final float field_1713 = 0.7f;
    private static final float field_1714 = 0.4f;
    private static final int field_1717 = 6;
    @NotNull
    private final ResourceLocation field_1710;
    private float field_1715;
    private float field_1716 = 0.0f;
    private float field_1707 = 0.7f;
    private float field_1708 = 0.7f;
    private float field_1705 = 0.4f;
    private float field_1706 = 0.4f;
    private int field_1718 = 2;
    private final boolean field_1709;

    public BF_340(@NotNull Random random, boolean bl) {
        this.field_1710 = BFRes.loc("textures/misc/muzzleflash/eject" + random.nextInt(6) + ".png");
        this.field_1709 = bl;
    }

    public boolean method_1256() {
        if (this.field_1718-- > 0) {
            return false;
        }
        this.field_1708 = this.field_1707;
        this.field_1716 = this.field_1715;
        this.field_1706 = this.field_1705;
        this.field_1715 = MathUtils.moveTowards(this.field_1715, 1.0f, 0.3f);
        this.field_1705 = MathUtils.moveTowards(this.field_1705, 1.0f, 0.3f);
        this.field_1707 = MathUtils.moveTowards(this.field_1707, 0.0f, 0.3f);
        return this.field_1707 + this.field_1708 <= 0.0f;
    }

    public void method_1257(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f, float f2) {
        float f3 = 0.5f * MathUtils.lerpf1(this.field_1715, this.field_1716, f2);
        if (f3 <= 0.0f) {
            return;
        }
        float f4 = MathUtils.lerpf1(this.field_1707, this.field_1708, f2);
        float f5 = MathUtils.lerpf1(this.field_1705, this.field_1706, f2);
        BFRendering.centeredTexture(poseStack, guiGraphics, this.field_1710, this.field_1709 ? -f3 : (f3 += 0.1f), -f3, f5, f5, this.field_1709 ? -45.0f : 45.0f, 0.7f * f4 * f);
    }
}

