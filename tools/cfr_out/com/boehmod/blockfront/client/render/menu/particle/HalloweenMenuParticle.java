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

public final class HalloweenMenuParticle
implements MenuParticle {
    private static final ResourceLocation field_745 = BFRes.loc("textures/gui/emitter/flash.png");
    private static final ResourceLocation field_746 = BFRes.loc("textures/gui/emitter/bat.png");
    private final float field_747;
    private final float field_748;
    private boolean field_744 = false;
    private float field_749;
    private float field_750 = 0.0f;

    public HalloweenMenuParticle(float f, float f2) {
        this.field_747 = f;
        this.field_748 = f2;
    }

    @Override
    public void init(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean shouldRemove(float renderTime) {
        this.field_750 = this.field_749;
        if (!this.field_744) {
            if (this.field_749 < 1.0f) {
                this.field_749 += 0.25f;
            } else {
                this.field_744 = true;
            }
        } else if (this.field_749 > 0.0f) {
            this.field_749 -= 0.05f;
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
        float f = renderTime + (float)this.hashCode() / 200.0f;
        float f2 = Mth.sin((float)(f / 5.0f));
        float f3 = Mth.sin((float)(f / 8.0f));
        float f4 = Mth.sin((float)(f / 10.0f));
        float f5 = f2 * f3;
        float f6 = f2 * f4;
        poseStack.pushPose();
        poseStack.translate(3.0f * f5, 3.0f * f6, 0.0f);
        float f7 = this.field_750 + (this.field_749 - this.field_750) * delta;
        int n = 15;
        float f8 = Mth.sin((float)((renderTime + f7 * 100.0f / 30.0f) / 15.0f));
        float f9 = 20.0f * f8;
        BFRendering.centeredTexture(poseStack, graphics, field_745, this.field_747, this.field_748, 25.0f * f7, 25.0f * f7, f9, f7 / 2.0f);
        BFRendering.centeredTexture(poseStack, graphics, field_746, this.field_747, this.field_748, 15.0f * f7, 15.0f * f7, f9, f7);
        poseStack.popPose();
    }

    @Override
    @NotNull
    public MenuParticle.Type getType() {
        return MenuParticle.Type.BACKGROUND;
    }
}

