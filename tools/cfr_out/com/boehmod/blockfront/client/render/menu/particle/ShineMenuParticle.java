/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.menu.particle;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class ShineMenuParticle
implements MenuParticle {
    private static final ResourceLocation field_425 = BFRes.loc("textures/gui/emitter/shine.png");
    private final float field_428;
    private final float field_429;
    private boolean field_424 = false;
    private float field_426;
    private float field_427 = 0.0f;

    public ShineMenuParticle(float f, float f2) {
        this.field_428 = f;
        this.field_429 = f2;
    }

    @Override
    public void init(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean shouldRemove(float renderTime) {
        this.field_427 = this.field_426;
        if (!this.field_424) {
            if (this.field_426 < 1.0f) {
                this.field_426 += 0.25f;
            } else {
                this.field_424 = true;
            }
        } else if (this.field_426 > 0.0f) {
            this.field_426 -= 0.05f;
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, float renderTime, float delta) {
    }

    @Override
    public void renderAbove(@NotNull Minecraft minecraft, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, float renderTime, float delta) {
        float f = this.field_427 + (this.field_426 - this.field_427) * delta;
        int n = 15;
        float f2 = Mth.sin((float)((renderTime + f * 100.0f / 30.0f) / 15.0f));
        float f3 = 20.0f * f2;
        BFRendering.centeredTexture(poseStack, graphics, field_425, this.field_428, this.field_429, 15.0f * f, 15.0f * f, f3, f);
    }

    @Override
    @NotNull
    public MenuParticle.Type getType() {
        return MenuParticle.Type.BACKGROUND;
    }
}

