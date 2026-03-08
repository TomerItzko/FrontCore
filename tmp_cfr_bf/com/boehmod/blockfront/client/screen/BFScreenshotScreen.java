/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BFScreenshotScreen
extends BFScreen {
    private static final Component field_1212 = Component.translatable((String)"bf.screen.screenshot");
    private static final ResourceLocation field_1211 = BFRes.loc("screenshot");
    private final NativeImage field_1210;

    public BFScreenshotScreen(@NotNull NativeImage nativeImage) {
        super(field_1212);
        TextureManager textureManager = this.minecraft.getTextureManager();
        this.field_1210 = nativeImage;
        textureManager.release(field_1211);
        textureManager.register(field_1211, (AbstractTexture)new DynamicTexture(nativeImage));
    }

    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        int n3;
        int n4;
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n5 = this.field_1210.getWidth();
        int n6 = this.field_1210.getHeight();
        float f2 = ((float)this.width - 20.0f) / (float)n5;
        float f3 = ((float)this.height - 20.0f) / (float)n6;
        float f4 = (float)n6 * f2;
        if (f4 > (float)(this.height - 20)) {
            n4 = (int)((float)n6 * f3);
            n3 = (int)((float)n5 * f3);
        } else {
            n4 = (int)((float)n6 * f2);
            n3 = (int)((float)n5 * f2);
        }
        int n7 = this.width / 2;
        int n8 = this.height / 2;
        float f5 = (float)n3 / 1.25f;
        float f6 = (float)n4 / 1.25f;
        float f7 = (float)n7 - f5 / 2.0f;
        float f8 = (float)n8 - f6 / 2.0f;
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, 0, 0.75f);
        BFRendering.rectangle(poseStack, guiGraphics, f7 - 1.0f, f8 - 1.0f, f5 + 2.0f, f6 + 2.0f, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.texture(poseStack, guiGraphics, field_1211, f7, f8, f5, f6);
        BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_1212, n7, (float)n8 - f6 / 2.0f - 15.0f);
    }

    public void onClose() {
        super.onClose();
        this.minecraft.getTextureManager().release(field_1211);
    }
}

