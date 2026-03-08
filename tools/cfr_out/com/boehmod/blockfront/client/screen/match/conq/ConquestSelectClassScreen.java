/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match.conq;

import com.boehmod.blockfront.client.gui.widget.ConquestMapWidget;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.match.MatchSelectClassScreen;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestGameClient;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public final class ConquestSelectClassScreen
extends MatchSelectClassScreen<ConquestGame, ConquestGameClient> {
    private static final int field_1251 = 80;
    private static final int field_1252 = 199;
    private static final int field_1253 = 80;
    private static final int field_1254 = 134;
    private static final int field_1255 = 145;
    private boolean field_1250 = false;

    public ConquestSelectClassScreen(@Nullable Screen screen, @NotNull GameTeam gameTeam, @NotNull ConquestGame conquestGame, @NotNull ConquestGameClient conquestGameClient) {
        super(screen, gameTeam, conquestGame, conquestGameClient);
    }

    public ConquestSelectClassScreen method_911() {
        this.field_1250 = true;
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_1250) {
            LocalPlayer localPlayer = this.minecraft.player;
            BFClientPlayerData bFClientPlayerData = ((ClientPlayerDataHandler)this.manager.getPlayerDataHandler()).getPlayerData(this.minecraft);
            if (localPlayer != null && !BFUtils.isPlayerUnavailable((Player)localPlayer, bFClientPlayerData)) {
                this.minecraft.setScreen(this.field_1238);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) {
            return;
        }
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = n3 - 199;
        int n6 = n4 - 80;
        PoseStack poseStack = guiGraphics.pose();
        if (this.field_1250) {
            BFClientPlayerData bFClientPlayerData = ((ClientPlayerDataHandler)this.manager.getPlayerDataHandler()).getPlayerData(this.minecraft);
            MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(bFClientPlayerData.getRespawnTimer() / 20)).withStyle(ChatFormatting.RED);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.dead.countdown", (Object[])new Object[]{mutableComponent});
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)mutableComponent2, n5 + 67, n6 + 145 + 5, 0xFFFFFF);
        } else {
            MutableComponent mutableComponent;
            int n7 = BFUtils.getPlayerStat(this.field_1217, localPlayer.getUUID(), BFStats.CAPTURE_POINT_SPAWN, -1);
            if (n7 != -1 && n7 < ((ConquestGame)this.field_1217).capturePoints.size()) {
                ConquestCapturePoint conquestCapturePoint = (ConquestCapturePoint)((ConquestGame)this.field_1217).capturePoints.get(n7);
                MutableComponent mutableComponent3 = Component.literal((String)conquestCapturePoint.name).withStyle(ChatFormatting.GREEN);
                mutableComponent = Component.translatable((String)"bf.message.cpoint.selected", (Object[])new Object[]{mutableComponent3});
            } else {
                MutableComponent mutableComponent4 = Component.translatable((String)"bf.message.cpoint.hq").withStyle(ChatFormatting.GREEN);
                mutableComponent = Component.translatable((String)"bf.message.cpoint.selected", (Object[])new Object[]{mutableComponent4});
            }
            BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)mutableComponent, n5 + 67, n6 + 145 + 5, 0xFFFFFF);
        }
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = n - 199;
        int n4 = n2 - 80;
        this.method_764(new ConquestMapWidget(n3, n4, 134, 145, this, (ConquestGame)this.field_1217));
    }

    @Override
    public int method_903() {
        return 80;
    }

    @Override
    public void method_894() {
        if (!this.field_1250) {
            super.method_894();
        }
    }
}

