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
 *  net.minecraft.network.chat.Component
 *  net.neoforged.neoforge.client.event.RenderFrameEvent$Post
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.overlay;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.overlay.GameOverlayRenderer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.jetbrains.annotations.NotNull;

public final class DisclaimerOverlayRenderer
extends GameOverlayRenderer {
    private static final int field_1867 = 50;
    private static final int field_1868 = 8;
    @NotNull
    private static final Component field_1869 = BlockFront.RELEASE_STAGE_COMPONENT.copy().withStyle(BFStyles.BOLD);
    @NotNull
    private static final Component field_1870 = Component.translatable((String)"bf.message.gameplay").withStyle(BFStyles.BOLD);

    @Override
    public void render(@NotNull RenderFrameEvent.Post event, @NotNull Minecraft minecraft, LocalPlayer player, ClientLevel level, @NotNull PoseStack poseStack, @NotNull Font font, GuiGraphics graphics, BFClientManager manager, @NotNull PlayerCloudData cloudData, BFClientPlayerData gameData, AbstractGame<?, ?, ?> game, int width, int height, boolean bl, float renderTime, float delta) {
        if (minecraft.options.hideGui || minecraft.getDebugOverlay().showDebugScreen() || !BlockFront.RELEASE_STAGE.shouldDisplayDisclaimer()) {
            return;
        }
        int n = width - 50 - 8 + 8;
        int n2 = 8;
        int n3 = n + 25;
        int n4 = MathUtils.withAlphaf(0xFFFFFF, 0.105f);
        BFRendering.centeredString(font, graphics, field_1869, n3, 8, n4);
        BFRendering.centeredComponent2d(poseStack, font, graphics, field_1870, n3, 18.0f, n4, 0.5f);
    }
}

