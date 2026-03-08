/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BF_57
extends BFButton {
    private boolean field_315 = false;
    private float field_317 = 0.0f;
    private boolean field_316 = false;

    public BF_57(Minecraft minecraft, int n, int n2, int n3, int n4, Component component, BFTitleScreen bFTitleScreen) {
        this(minecraft, n, n2, n3, n4, component, button -> minecraft.setScreen((Screen)bFTitleScreen));
    }

    public BF_57(Minecraft minecraft, int n, int n2, int n3, int n4, Component component, Button.OnPress onPress) {
        super(n, n2, n3, n4, component, onPress);
        Screen screen = minecraft.screen;
        if (screen instanceof BFMenuScreen) {
            BFMenuScreen bFMenuScreen = (BFMenuScreen)screen;
            this.field_315 = component.getString().equalsIgnoreCase(bFMenuScreen.field_1048.getString());
        }
        this.width = (minecraft.font.width((FormattedText)component) + 15) / 2;
    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
        if (this.field_315) {
            return;
        }
        super.playDownSound(soundManager);
    }

    public void onPress() {
        if (this.field_315) {
            return;
        }
        super.onPress();
    }

    public BF_57 method_400() {
        this.field_316 = true;
        return this;
    }

    @Override
    public void method_381(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, int n, int n2, float f, float f2) {
        float f3;
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = this.getX();
        int n6 = this.getY();
        if (this.field_315) {
            f3 = Mth.sin((float)(f / 2.0f));
            float f4 = Mth.sin((float)(f / 4.0f));
            float f5 = f3 * f4;
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.FADE_YELLOW, n5 + n3, n6 + n4, 50, 30, 1.0f, 1.0f - 0.2f * f5);
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.FADE_YELLOW, n5 + n3, n6 + n4, 50, 30, 1.0f, this.field_317);
            super.method_381(guiGraphics, poseStack, n, n2, f, f2);
        }
        this.field_317 = Mth.lerp((float)0.3f, (float)this.field_317, (float)(this.isHovered ? 1.0f : 0.0f));
        if (this.field_315 && this.field_316) {
            f3 = 2.0f * this.field_317;
            BFRendering.orderedRectangle(poseStack, (float)(n5 + n3) + 0.5f, n6 + this.height, n3, f3, this.method_399(), 1);
            BFRendering.orderedRectangle(poseStack, n5, n6 + this.height, (float)n3 + 0.5f, f3, this.method_399(), 2);
        }
    }

    @Override
    public void method_376(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)this.getMessage().withStyle(ChatFormatting.BOLD), (float)(this.getX() + n3) + 0.5f, this.getY() + n4 - 2, this.method_398(), 0.5f);
    }

    @NotNull
    public MutableComponent getMessage() {
        return Component.literal((String)super.getMessage().getString().toUpperCase(Locale.ROOT).replace("_", " "));
    }

    @Override
    public int method_398() {
        return this.field_315 ? ColorReferences.COLOR_BLACK_SOLID : (this.isHoveredOrFocused() ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_WHITE_SOLID);
    }

    @Override
    public int method_399() {
        return this.field_315 ? ColorReferences.COLOR_THEME_YELLOW_SOLID : BFRendering.translucentBlack();
    }

    @NotNull
    public /* synthetic */ Component getMessage() {
        return this.getMessage();
    }
}

