/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class ClientGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>> {
    protected static final int field_482 = 15;
    private final int field_483;

    public ClientGameElement(int n) {
        this.field_483 = n;
    }

    public abstract void update(@NotNull Minecraft var1, @NotNull G var2, @NotNull P var3, @NotNull AbstractGameClient<G, P> var4, @NotNull LocalPlayer var5);

    @OverridingMethodsMustInvokeSuper
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        int n3 = BFRendering.translucentBlack();
        int n4 = this.method_490(font);
        BFRendering.rectangle(graphics, n + 1, n2, n4 - 2, 1, n3);
        BFRendering.rectangle(graphics, n, n2 + 1, n4, 13, n3);
        BFRendering.rectangle(graphics, n + 1, n2 + 15 - 1, n4 - 2, 1, n3);
    }

    public int method_490(@NotNull Font font) {
        return this.field_483;
    }
}

