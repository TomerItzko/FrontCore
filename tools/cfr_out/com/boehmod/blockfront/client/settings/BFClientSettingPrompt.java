/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.client.screen.settings.BFClientSettingPromptScreen;
import com.boehmod.blockfront.client.settings.BFClientSetting;
import com.boehmod.blockfront.client.settings.BFClientSettingCategory;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingPrompt
extends BFClientSetting {
    @NotNull
    private static final Component field_2015 = Component.translatable((String)"bf.menu.button.settings.prompt");
    @NotNull
    private final BFTextPromptScreen.Filter[] field_2014;
    @NotNull
    private final String field_2012;
    @NotNull
    private String field_2013;

    public BFClientSettingPrompt(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull BFClientSettingCategory bFClientSettingCategory) {
        this(string, string2, string3, bFClientSettingCategory, BFTextPromptScreen.Filter.COMMON);
    }

    public BFClientSettingPrompt(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull BFClientSettingCategory bFClientSettingCategory, BFTextPromptScreen.Filter ... filterArray) {
        super(string, string2, bFClientSettingCategory, false);
        this.field_2014 = filterArray;
        this.field_2012 = string3;
        this.field_2013 = string3;
    }

    public void method_1524(@NotNull String string) {
        this.field_2013 = string;
        BFClientSettings.isUnsaved = true;
        this.field_1943 = 1.0f;
    }

    public String method_1525() {
        return this.field_2013;
    }

    @Override
    public void method_1510(@NotNull Minecraft minecraft, @NotNull Screen screen, int n) {
        if (n == 0) {
            minecraft.setScreen(new BFClientSettingPromptScreen(screen, this).method_1095(this.field_2014).method_1084(field_2015));
        } else if (n == 1) {
            this.method_1524(this.field_2012);
        }
    }

    @Override
    public void readFDS(@NotNull FDSTagCompound root) {
        this.field_2013 = root.getString(this.getKey(), this.field_2012);
    }

    @Override
    public void writeFDS(@NotNull FDSTagCompound root) {
        root.setString(this.getKey(), this.field_2013);
    }

    @Override
    public void method_1509(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2) {
        int n7 = ColorReferences.COLOR_WHITE_SOLID;
        float f3 = MathUtils.lerpf1(this.field_1943, this.field_1944, f2);
        BFRendering.rectangle(guiGraphics, n, n2, n3, n4, n7, f3);
        BFRendering.rectangle(guiGraphics, n, n2 + n4 - 1, n3, 1, n7);
        String string = String.format("'%s'", this.field_2013);
        int n8 = font.width(string);
        BFRendering.drawString(font, guiGraphics, (Component)Component.literal((String)string), n5 - n8, n6);
    }

    @Override
    public int getColor() {
        return ChatFormatting.GRAY.getColor();
    }
}

