/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TimeGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends ClientGameElement<G, P> {
    @NotNull
    private static final ResourceLocation field_480 = BFRes.loc("textures/gui/stopwatch.png");
    @NotNull
    private Component field_481 = Component.empty();

    public TimeGameElement() {
        super(1);
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull G game, @NotNull P playerManager, @NotNull AbstractGameClient<G, P> gameClient, @NotNull LocalPlayer player) {
        this.field_481 = gameClient.method_2678().getComponent();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        super.render(graphics, poseStack, font, n, n2, f);
        int n3 = n + 3;
        int n4 = n2 + 2;
        int n5 = 11;
        BFRendering.texture(poseStack, graphics, field_480, n3, n4, 11, 11);
        int n6 = n3 + 11 + 2;
        BFRendering.drawStringWithShadow(font, graphics, this.field_481, n6, n2 + 4);
    }

    @Override
    public int method_490(@NotNull Font font) {
        return 45;
    }
}

