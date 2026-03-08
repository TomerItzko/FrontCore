/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.overlay;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.overlay.GameOverlayRenderer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.jetbrains.annotations.NotNull;

public final class FlashOverlayRenderer
extends GameOverlayRenderer {
    private static void renderFlash(@NotNull GuiGraphics graphiics, int width, int height, float time) {
        float f = MathUtils.lerpf1(PlayerTickable.FLASH_TIMER, PlayerTickable.FLASH_TIMER_PREV, time);
        if (f >= 0.1f) {
            BFRendering.rectangle(graphiics, 0, 0, width, height, 0xFFFFFF, Math.min(f, 1.0f));
        }
    }

    @Override
    public void render(@NotNull RenderFrameEvent.Post event, @NotNull Minecraft minecraft, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull PoseStack poseStack, @NotNull Font font, GuiGraphics graphics, BFClientManager manager, @NotNull PlayerCloudData cloudData, BFClientPlayerData gameData, AbstractGame<?, ?, ?> game, int width, int height, boolean bl, float renderTime, float delta) {
        if (player == null) {
            return;
        }
        if (minecraft.options.getCameraType().isFirstPerson()) {
            FlashOverlayRenderer.renderFlash(graphics, width, height, delta);
        }
    }
}

