/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.jetbrains.annotations.NotNull;

public abstract class TextGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends ClientGameElement<G, P> {
    @Nullable
    private Component text = null;

    public TextGameElement() {
        super(1);
    }

    public void setText(@Nullable Component component) {
        this.text = component;
    }

    protected void method_484(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2) {
        if (this.text != null) {
            int n3 = this.method_490(font) / 2;
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, this.text, n + n3, n2 + 4);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        super.render(graphics, poseStack, font, n, n2, f);
        this.method_484(graphics, poseStack, font, n, n2);
    }

    @Override
    public int method_490(@NotNull Font font) {
        if (this.text != null) {
            return font.width((FormattedText)this.text) + 6;
        }
        return super.method_490(font);
    }
}

