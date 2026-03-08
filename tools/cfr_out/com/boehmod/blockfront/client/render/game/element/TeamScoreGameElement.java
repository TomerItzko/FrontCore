/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TeamScoreGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends ClientGameElement<G, P> {
    @NotNull
    private Component field_478 = Component.empty();
    @NotNull
    private Component field_479 = Component.empty();

    public TeamScoreGameElement() {
        super(1);
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull G game, @NotNull P playerManager, @NotNull AbstractGameClient<G, P> gameClient, @NotNull LocalPlayer player) {
        GameTeam gameTeam;
        int n = 8159560;
        int n2 = 8271921;
        GameTeam gameTeam2 = ((AbstractGamePlayerManager)playerManager).getTeamByName("Axis");
        if (gameTeam2 != null) {
            int n3 = gameTeam2.getObjectInt(BFStats.SCORE);
            this.field_478 = Component.literal((String)StringUtils.formatLong(n3)).withColor(8271921);
        }
        if ((gameTeam = ((AbstractGamePlayerManager)playerManager).getTeamByName("Allies")) != null) {
            int n4 = gameTeam.getObjectInt(BFStats.SCORE);
            this.field_479 = Component.literal((String)StringUtils.formatLong(n4)).withColor(8159560);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        super.render(graphics, poseStack, font, n, n2, f);
        int n3 = this.method_490(font);
        int n4 = n3 / 2;
        int n5 = n + n4;
        BFRendering.rectangle(poseStack, graphics, (float)n5 - 0.5f, n2 + 2, 1.0f, 11.0f, ColorReferences.COLOR_WHITE_SOLID, 0.25f);
        int n6 = n + 1 + n4 / 2;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, this.field_479, n6, n2 + 4);
        int n7 = n5 + n4 / 2;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, this.field_478, n7, n2 + 4);
    }

    @Override
    public int method_490(@NotNull Font font) {
        return 48;
    }
}

