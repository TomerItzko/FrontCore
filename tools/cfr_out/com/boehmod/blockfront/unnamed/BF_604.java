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
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_605;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_604
extends BF_605 {
    public static final MutableComponent field_2537 = Component.literal((String)"US Bootcamp");
    @NotNull
    private final AbstractGame<?, ?, ?> field_2538;
    private float field_2539;
    private float field_2540;
    private float field_2541;
    private float field_2542;
    private int field_2543;

    public BF_604(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        this.field_2538 = abstractGame;
        this.field_2540 = 1.0f;
        this.field_2539 = 1.0f;
        this.field_2542 = 1.0f;
        this.field_2541 = 1.0f;
        this.field_2543 = 40;
    }

    @Override
    public boolean method_2222(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.field_2540 = this.field_2539;
        this.field_2542 = this.field_2541;
        if (this.field_2539 > 0.0f) {
            this.field_2539 = Mth.lerp((float)0.1f, (float)this.field_2539, (float)0.0f);
            if (this.field_2539 <= 0.01f) {
                this.field_2539 = 0.0f;
            }
        } else if (this.field_2543 > 0) {
            --this.field_2543;
        } else if (this.field_2541 > 0.0f) {
            this.field_2541 = MathUtils.moveTowards(this.field_2541, 0.0f, 0.03f);
        }
        return this.field_2541 <= 0.0f;
    }

    @Override
    public void method_2223(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        int n3 = n / 2;
        int n4 = n2 / 2;
        float f2 = MathUtils.lerpf1(this.field_2539, this.field_2540, f);
        float f3 = MathUtils.lerpf1(this.field_2541, this.field_2542, f);
        if (f3 <= 0.0f) {
            return;
        }
        Color color = new Color(1.0f, 1.0f, 1.0f, Math.min(1.0f - f2, Math.min(1.0f, f3 + 0.1f)));
        int n5 = color.getRGB();
        float f4 = 40.0f * f2;
        GameTeam gameTeam = ((AbstractGamePlayerManager)this.field_2538.getPlayerManager()).getPlayerTeam(Objects.requireNonNull(minecraft.getUser().getProfileId()));
        if (gameTeam != null) {
            BFRendering.rectangle(guiGraphics, 0, 0, n, n2, 0, f3);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)field_2537, n3, (float)(n4 - 35) + f4, n5, 3.0f);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)"August 20th, 1941").withStyle(gameTeam.getStyleText()), n3, (float)(n4 - 10) + f4, n5, 1.0f);
            BFRendering.rectangle(guiGraphics, 0, 0, n, n2, 0, f2);
        }
    }
}

