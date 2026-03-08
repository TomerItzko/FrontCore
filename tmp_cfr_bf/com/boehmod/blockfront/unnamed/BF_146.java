/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_146
implements MenuParticle {
    @NotNull
    private static final IntList field_752 = new IntArrayList();
    private final int field_753 = ThreadLocalRandom.current().nextInt(1000);
    private final int field_754;
    private int field_755;
    private int field_756;
    private int field_757;
    private int field_758 = 0;
    private float field_759;
    private float field_761;
    private float field_760;
    private float field_762;
    private float field_751 = 1.0f;
    private float field_763 = 0.5f;

    public BF_146(float f, float f2) {
        this((Integer)RandomUtils.randomFromList(field_752), f, f2);
    }

    public BF_146(int n, float f, float f2) {
        this.field_754 = n;
        this.field_759 = this.field_761 = f;
        this.field_760 = this.field_762 = f2;
    }

    @NotNull
    public BF_146 method_611(int n, int n2, int n3, int n4) {
        this.field_755 = n;
        this.field_756 = n2;
        this.field_757 = n3;
        this.field_758 = n4;
        return this;
    }

    @NotNull
    public BF_146 method_610(float f) {
        this.field_763 = f;
        return this;
    }

    @Override
    public void init(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean shouldRemove(float renderTime) {
        float f = Mth.sin((float)((renderTime + (float)this.field_753) / 10.0f)) * 0.5f;
        this.field_761 = this.field_759;
        this.field_762 = this.field_760;
        this.field_760 -= this.field_763;
        this.field_759 -= f;
        this.field_751 = MathUtils.moveTowards(this.field_751, 0.0f, 0.01f);
        return this.field_751 <= 0.0f;
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, float renderTime, float delta) {
        boolean bl;
        renderTime += (float)this.hashCode() / 10.0f;
        boolean bl2 = bl = this.field_757 != 0 && this.field_758 != 0;
        if (bl) {
            BFRendering.enableScissor(graphics, this.field_755, this.field_756, this.field_757, this.field_758);
        }
        float f = Mth.sin((float)(renderTime / 5.0f));
        float f2 = 0.5f * f;
        float f3 = Mth.sin((float)(renderTime / 2.0f));
        float f4 = Mth.sin((float)(renderTime / 8.0f));
        float f5 = f3 * f4;
        float f6 = (1.0f + 0.5f * f5) * this.field_751;
        float f7 = MathUtils.lerpf1(this.field_759, this.field_761, delta);
        float f8 = MathUtils.lerpf1(this.field_760, this.field_762, delta);
        int n = 8;
        float f9 = 45.0f;
        float f10 = f7 - 22.5f + 4.0f;
        float f11 = f8 - 22.5f + 4.0f;
        BFRendering.tintedTexture(poseStack, graphics, BFMenuScreen.FADE_YELLOW, f10, f11, 45.0f, 45.0f, 0.0f, f6, this.field_754);
        BFRendering.tintedTexture(poseStack, graphics, BFRendering.EMBER_TEXTURE, f7, f8, 8.0f, 8.0f, f2, f6, this.field_754);
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

    static {
        field_752.add(-74);
        field_752.add(-884443);
        field_752.add(-2389168);
        field_752.add(-1007569);
        field_752.add(-5019);
    }
}

