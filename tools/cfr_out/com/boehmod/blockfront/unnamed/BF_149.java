/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_149
implements MenuParticle {
    private final int field_769;
    private int field_765;
    private int field_766;
    private int field_767;
    private int field_768 = 0;
    private float field_771;
    private float field_773;
    private float field_772;
    private float field_774;
    private float field_764 = 1.0f;
    private int field_770 = 300;

    public BF_149(float f, float f2) {
        this.field_771 = this.field_773 = f;
        this.field_772 = this.field_774 = f2;
        IntArrayList intArrayList = new IntArrayList();
        intArrayList.add(-3407872);
        intArrayList.add(-8454144);
        intArrayList.add(-5427152);
        this.field_769 = intArrayList.getInt(ThreadLocalRandom.current().nextInt(intArrayList.size()));
    }

    public BF_149 method_617(int n, int n2, int n3, int n4) {
        this.field_765 = n;
        this.field_766 = n2;
        this.field_767 = n3;
        this.field_768 = n4;
        return this;
    }

    @Override
    public void init(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean shouldRemove(float renderTime) {
        float f = Mth.sin((float)((renderTime += (float)this.hashCode() / 15.0f) / 30.0f));
        this.field_773 = this.field_771;
        this.field_774 = this.field_772;
        this.field_772 -= 0.85f;
        this.field_771 -= f;
        this.field_764 = Mth.lerp((float)0.01f, (float)this.field_764, (float)0.0f);
        if (this.field_770-- <= 0) {
            return true;
        }
        return this.field_764 <= 0.0f;
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, float renderTime, float delta) {
        boolean bl;
        renderTime += (float)this.hashCode() / 10.0f;
        boolean bl2 = bl = this.field_767 != 0 && this.field_768 != 0;
        if (bl) {
            BFRendering.enableScissor(graphics, this.field_765, this.field_766, this.field_767, this.field_768);
        }
        float f = Mth.sin((float)(renderTime / 5.0f));
        float f2 = 0.5f * f;
        float f3 = Mth.sin((float)(renderTime / 2.0f));
        float f4 = Mth.sin((float)(renderTime / 8.0f));
        float f5 = f3 * f4;
        float f6 = (1.0f + 0.5f * f5) * this.field_764;
        float f7 = MathUtils.lerpf1(this.field_771, this.field_773, delta);
        float f8 = MathUtils.lerpf1(this.field_772, this.field_774, delta);
        BFRendering.tintedTexture(poseStack, graphics, BFRendering.EMBER_TEXTURE, f7, f8, 16.0f, 16.0f, f2, f6, this.field_769);
        if (bl) {
            graphics.disableScissor();
        }
    }

    @Override
    public void renderAbove(@NotNull Minecraft minecraft, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, float renderTime, float delta) {
    }

    @Override
    @NotNull
    public MenuParticle.Type getType() {
        return MenuParticle.Type.BACKGROUND;
    }
}

