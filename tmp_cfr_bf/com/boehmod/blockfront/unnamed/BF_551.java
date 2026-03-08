/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.unnamed.BF_605;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_551
extends BF_605 {
    private final float field_2285;
    private final float field_2286;
    private final float field_2287;
    private final float field_2288;
    private int field_2283 = 0;
    private float field_2281;
    private float field_2282 = 0.0f;
    private float field_2289 = 0.0f;
    private boolean field_2284 = false;

    public BF_551(float f, float f2, float f3) {
        this.field_2285 = f;
        this.field_2286 = f2;
        this.field_2287 = f3;
        this.field_2288 = f + f3 + f2;
    }

    @Override
    public boolean method_2222(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.field_2282 = this.field_2281;
        if (this.field_2284) {
            return true;
        }
        this.field_2289 += 0.05f;
        if (this.field_2289 <= this.field_2285) {
            this.field_2281 = this.field_2289 / this.field_2285;
        } else if (this.field_2289 <= this.field_2285 + this.field_2287) {
            this.field_2281 = 1.0f;
        } else if (this.field_2289 <= this.field_2288) {
            float f = this.field_2289 - (this.field_2285 + this.field_2287);
            this.field_2281 = 1.0f - f / this.field_2286;
        } else {
            this.field_2281 = 0.0f;
            this.field_2284 = true;
            return true;
        }
        this.field_2281 = Math.max(0.0f, Math.min(1.0f, this.field_2281));
        return false;
    }

    @Override
    public void method_2223(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        float f2 = MathUtils.lerpf1(this.field_2281, this.field_2282, f);
        BFRendering.rectangle(guiGraphics, 0, 0, n, n2, this.field_2283, f2);
    }

    @NotNull
    public BF_551 method_1915(int n) {
        this.field_2283 = n;
        return this;
    }

    public float method_1916() {
        return this.field_2281;
    }

    public float method_1918(float f) {
        return this.field_2282 + (this.field_2281 - this.field_2282) * f;
    }

    public float method_1917() {
        return Math.min(1.0f, this.field_2289 / this.field_2288);
    }
}

