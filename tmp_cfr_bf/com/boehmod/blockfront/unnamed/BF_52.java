/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_52
extends BFButton {
    public static final int field_272 = 24;
    public static final int field_273 = 100;
    @NotNull
    private static final ResourceLocation field_270 = BFRes.loc("textures/gui/menu/button_gloss.png");
    @NotNull
    private static final ResourceLocation field_271 = BFRes.loc("textures/gui/backshadow_white.png");
    private static final float field_277 = 10.0f;
    private static int field_274 = 0;
    private static float field_278;
    private static float field_279;
    private static boolean field_276;
    private static float field_280;
    private static float field_281;
    private int field_275 = ColorReferences.COLOR_TEAM_ALLIES_SOLID;

    public BF_52(@NotNull Component component, int n, int n2, int n3, int n4, @NotNull Button.OnPress onPress) {
        super(n, n2, n3, n4, component, onPress);
    }

    public void method_365(int n) {
        this.field_275 = n;
    }

    public static void onUpdate() {
        float f = 96.0f;
        field_279 = field_278;
        if (field_274-- <= 0 && (field_278 += 10.0f) >= 292.0f) {
            field_274 = 10;
            field_279 = -96.0f;
            field_278 = -96.0f;
        }
        field_281 = field_280;
        field_280 = Mth.lerp((float)0.6f, (float)field_280, (float)(field_276 ? 1.0f : 0.0f));
    }

    @Override
    public void method_381(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, int n, int n2, float f, float f2) {
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = this.getX();
        int n6 = this.getY();
        float f3 = MathUtils.lerpf1(field_280, field_281, f2);
        float f4 = (float)this.width * 1.5f;
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, field_271, (float)(n5 + n3), (float)(n6 + n4), f4, f4, 0.0f, f3 * 0.5f, this.field_275);
        super.method_381(guiGraphics, poseStack, n, n2, f, f2);
        field_276 = this.isHovered;
        int n7 = 96;
        float f5 = MathUtils.lerpf1(field_278, field_279, f2);
        BFRendering.enableScissor(guiGraphics, n5, n6, this.width, this.height);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_270, (float)(n5 + n4) + f5, (float)(n6 + n4), 96.0f, 96.0f, 45.0f, 0.75f);
        guiGraphics.disableScissor();
    }

    @Override
    public int method_398() {
        return this.isHoveredOrFocused() ? -6250336 : ColorReferences.COLOR_WHITE_SOLID;
    }

    static {
        field_279 = 0.0f;
        field_281 = 0.0f;
    }
}

