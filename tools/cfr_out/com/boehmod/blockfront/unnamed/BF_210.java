/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_210
extends BF_218 {
    @NotNull
    private static final ResourceLocation field_1323 = BFRes.loc("textures/gui/menu/icons/check_white.png");
    public static final float field_1324 = 15.0f;
    @NotNull
    private final CloudAchievement field_1320;
    private final boolean field_1321;
    public boolean field_1322 = false;
    private float field_1318;
    private float field_1319 = 0.0f;

    public BF_210(@NotNull CloudAchievement cloudAchievement, boolean bl) {
        this.field_1320 = cloudAchievement;
        this.field_1321 = bl;
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
        this.field_1319 = this.field_1318;
        this.field_1318 = Mth.lerp((float)0.75f, (float)this.field_1318, (float)(this.method_992() ? 1.0f : 0.0f));
    }

    @Override
    public boolean method_990() {
        return false;
    }

    @Override
    public int height() {
        return 40;
    }

    @Override
    public int method_989() {
        return this.field_1354.method_558() - 15;
    }

    @Override
    public int method_974() {
        return 2;
    }

    @Override
    public int method_975() {
        return 2;
    }

    @Override
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        super.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
        boolean bl = this.method_992();
        if (bl) {
            BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1357, this.field_1358, this.method_989(), this.height(), ColorReferences.COLOR_WHITE_SOLID, 0.25f);
            if (!this.field_1322) {
                this.field_1322 = true;
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)1.0f));
            }
        } else {
            this.field_1322 = false;
        }
        int n3 = this.field_1321 ? ColorReferences.COLOR_TEAM_ALLIES_SOLID : (bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ChatFormatting.GRAY.getColor() + ColorReferences.COLOR_BLACK_SOLID);
        MutableComponent mutableComponent = Component.literal((String)this.field_1320.getName()).withColor(n3).withStyle(ChatFormatting.BOLD);
        float f3 = MathUtils.lerpf1(this.field_1318, this.field_1319, f2);
        float f4 = 15.0f * f3;
        BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358 + (this.height() - 1), this.method_989(), 1, n3);
        BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, (float)(this.field_1357 + 5) + f4, this.field_1358 + 6);
        int n4 = 16;
        int n5 = this.method_989() - (31 + (int)(f4 * 2.0f));
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, this.field_1320.getDescription(), n5);
        ChatFormatting chatFormatting = bl ? ChatFormatting.WHITE : ChatFormatting.DARK_GRAY;
        int n6 = objectList.size();
        for (int i = 0; i < n6; ++i) {
            MutableComponent mutableComponent2 = Component.literal((String)((String)objectList.get(i))).withStyle(chatFormatting);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent2, (float)(this.field_1357 + 5) + f4, this.field_1358 + 18 + 10 * i);
        }
        float f5 = (float)(this.field_1357 + this.method_989()) - (float)this.height() / 2.0f;
        float f6 = (float)this.field_1358 + (float)this.height() / 2.0f;
        if (this.field_1321) {
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, field_1323, f5, f6, 16.0f, 16.0f, 0.0f, 1.0f, n3);
        }
    }
}

