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
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.unnamed.BF_605;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_552
extends BF_605 {
    public static final float field_2290 = 0.05f;
    public static final float field_2291 = 0.05f;
    public static final float field_2292 = 0.025f;
    public static final float field_2293 = 4.0f;
    public static final float field_2294 = 1.5f;
    private float field_2295 = 1.0f;
    private float field_2296 = 1.0f;
    private int field_2299 = 40;
    private int field_2300 = 160;
    private float field_2297 = 0.0f;
    private float field_2298 = 0.0f;
    private final Component field_2301;
    private final Component field_2302;

    public BF_552(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        String string = StringUtils.makeFancy(abstractGame.getDisplayName().getString());
        MutableComponent mutableComponent = Component.literal((String)string).withColor(16763221);
        this.field_2301 = Component.literal((String)abstractGame.getMap().getName()).copy().withStyle(BFStyles.BOLD);
        this.field_2302 = mutableComponent;
    }

    @Override
    public boolean method_2222(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.field_2296 = this.field_2295;
        this.field_2298 = this.field_2297;
        this.field_2295 = MathUtils.moveTowards(this.field_2295, 0.0f, 0.025f);
        if (this.field_2299-- <= 0) {
            this.field_2297 = this.field_2300-- <= 0 ? MathUtils.moveTowards(this.field_2297, 0.0f, 0.05f) : MathUtils.moveTowards(this.field_2297, 1.0f, 0.05f);
        }
        return this.field_2300 <= 0 && this.field_2297 <= 0.0f;
    }

    @Override
    public void method_2223(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        int n3 = n / 2;
        int n4 = n2 / 2;
        float f2 = MathUtils.lerpf1(this.field_2297, this.field_2298, f);
        if (f2 >= 0.04f) {
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3, n4, 200, 200, 0.0f, 0.25f * f2);
            int n5 = MathUtils.withAlphaf(0xFFFFFF, f2);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, this.field_2301, n3, n4 - 30, n5, 4.0f);
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, this.field_2302, (float)n3 + 0.5f, n4 + 10, n5, 1.5f);
        }
        float f3 = MathUtils.lerpf1(this.field_2295, this.field_2296, f);
        BFRendering.rectangle(guiGraphics, 0, 0, n, n2, 0, f3);
    }
}

