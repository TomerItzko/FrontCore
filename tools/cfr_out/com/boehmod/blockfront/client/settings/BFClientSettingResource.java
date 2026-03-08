/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.BFCrosshair;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.client.settings.BFClientSettingCategory;
import com.boehmod.blockfront.client.settings.BFClientSettingPrompt;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingResource
extends BFClientSettingPrompt {
    public BFClientSettingResource(@NotNull String string, @NotNull String string2, @NotNull String string3, @NotNull BFClientSettingCategory bFClientSettingCategory) {
        super(string, string2, string3, bFClientSettingCategory, BFTextPromptScreen.Filter.MINECRAFT_RESOURCE);
    }

    @Override
    public void method_1509(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, int n3, int n4, int n5, int n6, float f, float f2) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        int n7 = ColorReferences.COLOR_WHITE_SOLID;
        float f3 = MathUtils.lerpf1(this.field_1943, this.field_1944, f2);
        BFRendering.rectangle(guiGraphics, n, n2, n3, n4, n7, f3);
        BFRendering.rectangle(guiGraphics, n, n2 + n4 - 1, n3, 1, n7);
        String string = this.method_1525();
        BFCrosshair bFCrosshair = (BFCrosshair)bFClientManager.getCrosshairResource().method_1647(string);
        int n8 = n4 / 2;
        float f4 = Mth.clamp((float)Mth.sin((float)(f / 15.0f)), (float)0.0f, (float)1.0f);
        float f5 = BFClientSettings.CROSSHAIR_SPREADFADE.isEnabled() ? f4 : 0.0f;
        float f6 = Mth.clamp((float)(1.0f - f5), (float)0.0f, (float)1.0f);
        float f7 = BFClientSettings.CROSSHAIR_ALPHA.getValue() * f6;
        float f8 = n3 - n8;
        float f9 = n2 + n8;
        BFRendering.crosshair(poseStack, guiGraphics, bFCrosshair, (float)n + f8, f9, f7, BFClientSettings.CROSSHAIR_DOT.isEnabled(), BFClientSettings.CROSSHAIR_LINES.isEnabled(), f4);
    }
}

