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
import com.boehmod.blockfront.game.AbstractGameClient;
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

public class BF_553
extends BF_605 {
    private static final Component field_2314 = Component.translatable((String)"bf.message.gamemode.victory").withStyle(BFStyles.BOLD);
    private static final Component field_2315 = Component.translatable((String)"bf.message.gamemode.defeat").withStyle(BFStyles.BOLD);
    public static final float field_2311 = 0.05f;
    public static final float field_2303 = 0.05f;
    public static final float field_2304 = 0.025f;
    public static final float field_2305 = 4.0f;
    public static final float field_2306 = 1.5f;
    private int field_2312 = 20;
    private int field_2313 = 140;
    private float field_2309 = 0.0f;
    private float field_2310 = 0.0f;
    private float field_2307 = 0.0f;
    private float field_2308 = 0.0f;
    private final Component field_2316;
    private final Component field_2317;

    public BF_553(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Component component, boolean bl) {
        String string = StringUtils.makeFancy(component.getString());
        MutableComponent mutableComponent = Component.literal((String)string).withColor(16763221);
        this.field_2316 = bl ? field_2314 : field_2315;
        this.field_2317 = mutableComponent;
    }

    @Override
    public boolean method_2222(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @Nullable LocalPlayer localPlayer, @Nullable ClientLevel clientLevel) {
        this.field_2310 = this.field_2309;
        this.field_2308 = this.field_2307;
        if (this.field_2312-- <= 0) {
            this.field_2309 = this.field_2313-- <= 0 ? MathUtils.moveTowards(this.field_2309, 0.0f, 0.05f) : MathUtils.moveTowards(this.field_2309, 1.0f, 0.05f);
        }
        if (this.field_2313 <= 0 && this.field_2309 <= 0.0f) {
            this.field_2307 = MathUtils.moveTowards(this.field_2307, 1.0f, 0.025f);
        }
        if (this.field_2307 >= 1.0f && this.field_2308 >= 1.0f) {
            AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
            if (abstractGameClient != null) {
                abstractGameClient.getSummaryManager().updateScreens(false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void method_2223(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2, float f) {
        int n3 = n / 2;
        int n4 = n2 / 2;
        float f2 = MathUtils.lerpf1(this.field_2309, this.field_2310, f);
        if (f2 >= 0.04f) {
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3, n4, 200, 200, 0.0f, 0.25f * f2);
            int n5 = MathUtils.withAlphaf(0xFFFFFF, f2);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, this.field_2316, n3, n4 - 30, n5, 4.0f);
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, this.field_2317, (float)n3 + 0.5f, n4 + 10, n5, 1.5f);
        }
        float f3 = MathUtils.lerpf1(this.field_2307, this.field_2308, f);
        BFRendering.rectangle(guiGraphics, 0, 0, n, n2, 0, f3);
    }
}

