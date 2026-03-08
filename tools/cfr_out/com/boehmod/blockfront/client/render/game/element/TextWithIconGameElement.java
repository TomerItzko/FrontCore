/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.TextGameElement;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class TextWithIconGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends TextGameElement<G, P> {
    @Nullable
    private ResourceLocation field_465 = null;

    public void setIconTexture(@Nullable ResourceLocation resourceLocation) {
        this.field_465 = resourceLocation;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        super.render(graphics, poseStack, font, n, n2, f);
        if (this.field_465 == null) {
            return;
        }
        int n3 = n + 3;
        int n4 = n2 + 2;
        int n5 = 11;
        BFRendering.texture(poseStack, graphics, this.field_465, n3, n4, 11, 11);
    }

    @Override
    protected void method_484(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2) {
        super.method_484(guiGraphics, poseStack, font, n + (this.field_465 != null ? 7 : 0), n2);
    }

    @Override
    public int method_490(@NotNull Font font) {
        return super.method_490(font) + (this.field_465 != null ? 12 : 0);
    }
}

