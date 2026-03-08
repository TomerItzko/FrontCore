/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class CapturePointGameClient<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends AbstractGameClient<G, P> {
    private static final String field_2907 = "bf.message.gamemode.capturepoint.capturing";
    private static final String field_2908 = "bf.message.gamemode.capturepoint.defending";
    private static final int field_2909 = 28;
    private static final int field_2910 = 120;
    @Nullable
    protected AbstractCapturePoint<?> currentCapturePoint = null;

    public CapturePointGameClient(@NotNull BFClientManager bFClientManager, @NotNull G g, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, g, clientPlayerDataHandler);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        if (!BFUtils.isPlayerUnavailable((Player)player, playerData) && this.game.getStatus() == GameStatus.GAME) {
            Object p = this.game.getPlayerManager();
            this.renderCapturePointStatus((AbstractGamePlayerManager<G>)p, player, poseStack, graphics, font, height, midX, delta);
        }
    }

    private void renderCapturePointStatus(@NotNull AbstractGamePlayerManager<G> playerManager, @NotNull LocalPlayer player, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, int height, int midX, float delta) {
        if (this.currentCapturePoint == null) {
            return;
        }
        int n = BFRendering.translucentBlack();
        GameTeam gameTeam = playerManager.getPlayerTeam(player.getUUID());
        if (gameTeam == null) {
            return;
        }
        String string = gameTeam.getName();
        boolean bl = gameTeam.equals(this.currentCapturePoint.cbTeam);
        int n2 = 0xFFFFFF;
        if (string.equalsIgnoreCase("Axis")) {
            n2 = 8271921;
        } else if (string.equalsIgnoreCase("Allies")) {
            n2 = 8159560;
        }
        float f = MathUtils.lerpf1(this.currentCapturePoint.field_3231, this.currentCapturePoint.field_3232, delta);
        int n3 = 60;
        int n4 = height - 56 - 30;
        int n5 = 128;
        int n6 = 28;
        int n7 = midX - 64;
        int n8 = n4 - 4;
        BFRendering.rectangle(graphics, n7 + 1, n8 - 1, 126, 1, n);
        BFRendering.rectangle(graphics, n7, n8, 128, 28, n);
        BFRendering.rectangle(graphics, n7 + 1, n8 + 28, 126, 1, n);
        int n9 = midX - 60;
        int n10 = n4 + 12;
        int n11 = 120;
        int n12 = 8;
        BFRendering.rectangle(graphics, n9 + 1, n10 - 1, 118, 1, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(graphics, n9, n10, 120, 8, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(graphics, n9 + 1, n10 + 8, 118, 1, ColorReferences.COLOR_BLACK_SOLID);
        boolean bl2 = true;
        int n13 = n9 + 1;
        int n14 = n10 + 1 - 1;
        int n15 = 118;
        int n16 = 8;
        float f2 = bl ? 118.0f : f * 118.0f / 12.0f - 2.0f;
        BFRendering.rectangle(graphics, n13, n14, 118, 8, n);
        BFRendering.rectangle(poseStack, graphics, n13, n14, f2, 8.0f, n2, 0.8f);
        GameTeam gameTeam2 = this.currentCapturePoint.getCbTeam();
        Style style = gameTeam2 != null ? gameTeam2.getStyleText() : Style.EMPTY.withColor(ChatFormatting.GRAY);
        MutableComponent mutableComponent = Component.literal((String)this.currentCapturePoint.name.toUpperCase(Locale.ROOT)).withStyle(style);
        String string2 = bl ? field_2908 : field_2907;
        MutableComponent mutableComponent2 = Component.translatable((String)string2, (Object[])new Object[]{mutableComponent}).withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, (Component)mutableComponent2, midX, n4);
    }
}

