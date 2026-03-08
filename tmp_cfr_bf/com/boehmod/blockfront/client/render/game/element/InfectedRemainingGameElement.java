/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class InfectedRemainingGameElement
extends ClientGameElement<InfectedGame, InfectedPlayerManager> {
    private static final ResourceLocation field_467 = BFRes.loc("textures/text/infected.png");
    @NotNull
    private Component field_469 = Component.empty();
    private int field_468 = 1;

    public InfectedRemainingGameElement() {
        super(1);
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull AbstractGameClient<InfectedGame, InfectedPlayerManager> abstractGameClient, @NotNull LocalPlayer localPlayer) {
        int n = infectedGame.field_3537 + infectedGame.numInfectedToSpawn;
        this.field_469 = Component.literal((String)StringUtils.formatLong(n)).withStyle(ChatFormatting.RED);
        this.field_468 = minecraft.font.width((FormattedText)this.field_469) + 15 + 3;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        super.render(graphics, poseStack, font, n, n2, f);
        int n3 = n + 3;
        int n4 = n2 + 2;
        int n5 = 11;
        BFRendering.texture(poseStack, graphics, field_467, n3, n4, 11, 11);
        int n6 = n3 + 11 + 2;
        BFRendering.drawStringWithShadow(font, graphics, this.field_469, n6, n2 + 4);
    }

    @Override
    public int method_490(@NotNull Font font) {
        return this.field_468;
    }
}

