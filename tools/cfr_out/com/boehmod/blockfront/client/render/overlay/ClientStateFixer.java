/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.neoforged.neoforge.client.event.RenderFrameEvent$Post
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.overlay;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.match.ClientMatchMaking;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.overlay.GameOverlayRenderer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.jetbrains.annotations.NotNull;

public final class ClientStateFixer
extends GameOverlayRenderer {
    @Override
    public void render(@NotNull RenderFrameEvent.Post event, @NotNull Minecraft minecraft, LocalPlayer player, ClientLevel level, @NotNull PoseStack poseStack, @NotNull Font font, GuiGraphics graphics, BFClientManager manager, @NotNull PlayerCloudData cloudData, BFClientPlayerData gameData, AbstractGame<?, ?, ?> game, int width, int height, boolean bl, float renderTime, float delta) {
        ClientMatchMaking clientMatchMaking = manager.getMatchMaking();
        float f = clientMatchMaking.getFadeAlpha(delta);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.rectangle(graphics, 0, 0, width, height, 0, f);
        poseStack.popPose();
    }
}

