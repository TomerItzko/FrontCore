/*
 * Decompiled with CFR.
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

public final class ChristmasMenuParticle
implements MenuParticle {
    public static final ResourceLocation field_431 = BFRes.loc("textures/gui/emitter/flash.png");
    public static final ResourceLocation field_432 = BFRes.loc("textures/gui/emitter/snowflake.png");
    private final float field_436;
    private final float field_433;
    private boolean field_430 = false;
    private float field_434;
    private float field_435 = 0.0f;

    public ChristmasMenuParticle(float f, float f2) {
        this.field_436 = f;
        this.field_433 = f2;
    }

    @Override
    public void init(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean shouldRemove(float renderTime) {
        this.field_435 = this.field_434;
        if (!this.field_430) {
            if (this.field_434 < 1.0f) {
                this.field_434 += 0.25f;
            } else {
                this.field_430 = true;
            }
        } else if (this.field_434 > 0.0f) {
            this.field_434 -= 0.05f;
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
        float f = this.field_435 + (this.field_434 - this.field_435) * delta;
        int n = 15;
        float f2 = Mth.sin((float)((renderTime + f * 100.0f / 30.0f) / 15.0f));
        float f3 = 20.0f * f2;
        BFRendering.centeredTexture(poseStack, graphics, field_431, this.field_436, this.field_433, 25.0f * f, 25.0f * f, f3, f / 2.0f);
        BFRendering.centeredTexture(poseStack, graphics, field_432, this.field_436, this.field_433, 15.0f * f, 15.0f * f, f3, f);
    }

    @Override
    @NotNull
    public MenuParticle.Type getType() {
        return MenuParticle.Type.BACKGROUND;
    }
}

