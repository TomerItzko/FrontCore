/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.overlay;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.overlay.GameOverlayRenderer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.jetbrains.annotations.NotNull;

public final class PartyOverlayRenderer
extends GameOverlayRenderer {
    @NotNull
    private static final ResourceLocation field_1871 = BFRes.loc("textures/gui/menu/icons/friends.png");

    @Override
    public void render(@NotNull RenderFrameEvent.Post event, @NotNull Minecraft minecraft, LocalPlayer player, ClientLevel level, @NotNull PoseStack poseStack, @NotNull Font font, GuiGraphics graphics, BFClientManager manager, @NotNull PlayerCloudData cloudData, BFClientPlayerData gameData, AbstractGame<?, ?, ?> game, int width, int height, boolean bl, float renderTime, float delta) {
        Optional optional = cloudData.getParty();
        optional.ifPresent(matchParty -> {
            float f2 = 1.0f + Mth.sin((float)(renderTime / 30.0f));
            int n2 = 6;
            BFRendering.component2d(poseStack, font, graphics, (Component)Component.translatable((String)"bf.message.party.in.withcount", (Object[])new Object[]{matchParty.getPlayers().size()}).withStyle(ChatFormatting.BOLD), 8.0f, height - 5, 0x77FFFFFF, 0.5f);
            BFRendering.texture(poseStack, graphics, field_1871, 1, height - 6, 6, 6, f2 / 2.0f);
        });
    }
}

