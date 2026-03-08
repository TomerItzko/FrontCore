/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
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

public class BF_1179
extends BF_605 {
    private final int field_6705;
    private final float field_6702;
    private final float field_6703;
    private final float field_6704;
    private boolean field_6701 = false;
    private float field_6699 = 0.0f;
    private float field_6700 = 0.0f;

    public BF_1179(int n, float f, float f2, float f3) {
        this.field_6705 = n;
        this.field_6702 = f;
        this.field_6703 = f2;
        this.field_6704 = f3;
    }

    @Override
    public boolean method_2222(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.field_6700 = this.field_6699;
        if (this.field_6701) {
            this.field_6699 = MathUtils.moveTowards(this.field_6699, 0.0f, this.field_6703);
        } else {
            this.field_6699 = MathUtils.moveTowards(this.field_6699, this.field_6704, this.field_6702);
            if (this.field_6699 >= this.field_6704) {
                this.field_6701 = true;
            }
        }
        return this.field_6701 && this.field_6699 <= 0.0f;
    }

    @Override
    public void method_2223(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        float f2 = MathUtils.lerpf1(this.field_6699, this.field_6700, f);
        BFRendering.rectangle(guiGraphics, 0, 0, n, n2, this.field_6705, f2);
    }
}

