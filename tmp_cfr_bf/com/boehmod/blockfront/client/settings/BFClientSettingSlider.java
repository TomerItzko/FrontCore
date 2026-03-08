/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.BFRendering;
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
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingSlider
extends BFClientSetting {
    private final float field_1933;
    private float value;
    private float field_1935;
    private float field_1936 = 0.0f;

    public BFClientSettingSlider(@NotNull String string, @NotNull String string2, @NotNull BFClientSettingCategory bFClientSettingCategory, float f) {
        super(string, string2, bFClientSettingCategory, false);
        this.field_1933 = f;
        this.value = f;
    }

    @Override
    public void method_1510(@NotNull Minecraft minecraft, @NotNull Screen screen, int n) {
        if (n == 0) {
            this.value = BFClientSetting.method_1512(this.value);
        } else if (n == 1) {
            this.value = this.field_1933;
        }
        BFClientSettings.isUnsaved = true;
        this.field_1943 = 1.0f;
    }

    @Override
    public void readFDS(@NotNull FDSTagCompound root) {
        this.value = root.getFloat(this.getKey(), this.field_1933);
    }

    @Override
    public void writeFDS(@NotNull FDSTagCompound root) {
        root.setFloat(this.getKey(), this.value);
    }

    @Override
    public void method_1509(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2) {
        float f3 = MathUtils.lerpf1(this.field_1935, this.field_1936, f2);
        float f4 = MathUtils.lerpf1(this.field_1943, this.field_1944, f2);
        float f5 = (float)n3 * f3;
        BFRendering.rectangle(guiGraphics, n, n2, n3, n4, -12303292, f4);
        BFRendering.rectangle(guiGraphics, n, n2 + n4 - 1, n3, 1, -12303292);
        BFRendering.rectangle(poseStack, guiGraphics, (float)n, (float)(n2 + n4 - 1), f5, 1.0f, ColorReferences.COLOR_WHITE_SOLID);
        String string = (int)(100.0f * this.value) + "%";
        BFRendering.centeredString(font, guiGraphics, (Component)Component.literal((String)string).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GRAY), n5, n6 - 4);
    }

    @Override
    public int getColor() {
        return ChatFormatting.GRAY.getColor();
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.field_1936 = this.field_1935;
        this.field_1935 = (float)Mth.lerp((double)0.5, (double)this.field_1935, (double)this.value);
    }
}

