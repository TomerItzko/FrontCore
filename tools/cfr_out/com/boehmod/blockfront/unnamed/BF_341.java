/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector2f
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

public class BF_341 {
    private static final float field_1726 = 0.075f;
    @NotNull
    private final ResourceLocation field_1725;
    private float field_1723;
    private float field_1727 = 0.0f;
    private float field_1724;
    private float field_1728 = 0.0f;
    @NotNull
    private final Vector2f field_1721;
    private float field_1719;
    private float field_1720;
    private int field_1729;
    private int field_1730 = 0;
    private final boolean field_1722;

    public BF_341(@NotNull Vector2f vector2f, @NotNull String string, boolean bl) {
        this.field_1721 = vector2f;
        this.field_1725 = BFRes.loc("textures/misc/muzzleflash/bullet_" + string + ".png");
        this.field_1719 = 1.0f;
        this.field_1720 = 1.0f;
        this.field_1722 = bl;
    }

    public boolean method_1258() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.field_1727 = this.field_1723;
        this.field_1728 = this.field_1724;
        this.field_1730 = this.field_1729;
        this.field_1720 = this.field_1719;
        float f = 0.2f;
        float f2 = 0.99f;
        float f3 = 0.01f;
        float f4 = 0.1f;
        this.field_1721.add(0.0f, 0.2f).mul(1.0f - 0.01f * this.field_1721.length()).add(threadLocalRandom.nextFloat() * 0.1f, threadLocalRandom.nextFloat() * 0.1f).mul(0.99f);
        this.field_1719 = Mth.lerp((float)0.2f, (float)this.field_1719, (float)0.0f);
        this.method_1259();
        return this.field_1729++ > 20;
    }

    private void method_1259() {
        this.field_1723 = this.field_1722 ? (this.field_1723 -= this.field_1721.x * 0.15f) : (this.field_1723 += this.field_1721.x * 0.15f);
        this.field_1724 += this.field_1721.y * 0.2f;
    }

    public void method_1260(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, float f) {
        float f2 = MathUtils.lerpf1(this.field_1723, this.field_1727, f);
        float f3 = MathUtils.lerpf1(this.field_1724, this.field_1728, f);
        float f4 = MathUtils.lerpf1(this.field_1729, this.field_1730, f);
        float f5 = MathUtils.lerpf1(this.field_1719, this.field_1720, f);
        BFRendering.centeredTexture(poseStack, guiGraphics, this.field_1725, f2, f3, 0.075f, 0.075f, -100.0f * f4, f5);
    }
}

