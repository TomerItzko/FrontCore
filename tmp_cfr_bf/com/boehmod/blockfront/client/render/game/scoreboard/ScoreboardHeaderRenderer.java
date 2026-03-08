/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.scoreboard;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public abstract class ScoreboardHeaderRenderer {
    private static final int field_3768 = 200;
    private float field_3765 = 0.0f;
    private float field_3766 = 0.0f;
    private int field_3767 = 0;

    public boolean shouldSwitch() {
        ++this.field_3767;
        this.field_3766 = this.field_3765;
        this.field_3765 = this.field_3767 < 200 ? MathUtils.moveTowards(this.field_3765, 1.0f, 0.1f) : MathUtils.moveTowards(this.field_3765, 0.0f, 0.1f);
        return this.field_3767 > 200 && this.field_3765 + this.field_3766 == 0.0f;
    }

    public void reset() {
        this.field_3767 = 0;
        this.field_3765 = 0.0f;
        this.field_3766 = 0.0f;
    }

    public abstract void render(PoseStack var1, Font var2, GuiGraphics var3, AbstractGame<?, ?, ?> var4, int var5, int var6, int var7, int var8, int var9, int var10, float var11);

    public final float method_3902(float f) {
        return MathUtils.lerpf1(this.field_3765, this.field_3766, f);
    }
}

