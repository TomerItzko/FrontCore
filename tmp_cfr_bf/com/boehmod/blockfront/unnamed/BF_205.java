/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.settings.BFClientSetting;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_205
extends BF_218 {
    @NotNull
    public BFClientSetting field_1290;
    private float field_1289;
    private boolean field_1291;

    public BF_205(@NotNull BFClientSetting bFClientSetting) {
        this.field_1290 = bFClientSetting;
    }

    @Override
    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)((float)((double)0.8f + (double)0.2f * Math.random()))));
        this.field_1290.method_1510(minecraft, this.field_1354.getScreen(), n3);
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
        this.field_1290.onUpdate();
    }

    @Override
    public boolean method_990() {
        return true;
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
        int n3;
        super.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
        boolean bl = this.method_992();
        int n4 = this.height();
        int n5 = this.method_989();
        this.field_1289 = Mth.lerp((float)0.2f, (float)this.field_1289, (float)(bl ? 1.0f : 0.0f));
        if (bl) {
            BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1357, this.field_1358, n5, n4, ColorReferences.COLOR_WHITE_SOLID, 0.1f);
            if (!this.field_1291) {
                this.field_1291 = true;
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)1.0f));
            }
        } else {
            this.field_1291 = false;
        }
        MutableComponent mutableComponent = Component.translatable((String)this.field_1290.getName()).withColor(bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : this.field_1290.getColor()).withStyle(ChatFormatting.BOLD);
        BFRendering.rectangleGradient(guiGraphics, this.field_1357, this.field_1358, n5, n4, ColorReferences.COLOR_BLACK_TRANSPARENT, 0x33000000);
        float f3 = 15.0f * this.field_1289;
        BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, (float)(this.field_1357 + 5) + f3, this.field_1358 + 4);
        int n6 = (int)((float)(n5 - 40) - f3);
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, I18n.get((String)this.field_1290.getDescription(), (Object[])new Object[0]), n6);
        int n7 = objectList.size();
        for (n3 = 0; n3 < n7; ++n3) {
            MutableComponent mutableComponent2 = Component.literal((String)((String)objectList.get(n3))).withStyle(bl ? ChatFormatting.WHITE : ChatFormatting.DARK_GRAY);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent2, (float)(this.field_1357 + 5) + f3, this.field_1358 + 16 + 10 * n3);
        }
        n3 = n4 / 2;
        int n8 = this.field_1357 + n5 - n3;
        int n9 = this.field_1358 + n3;
        this.field_1290.method_1509(minecraft, poseStack, guiGraphics, font, this.field_1357, this.field_1358, n5, n4, n8, n9, f, f2);
    }
}

