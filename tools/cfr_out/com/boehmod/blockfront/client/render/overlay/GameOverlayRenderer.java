/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
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
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.jetbrains.annotations.NotNull;

public abstract class GameOverlayRenderer {
    public abstract void render(@NotNull RenderFrameEvent.Post var1, @NotNull Minecraft var2, @Nullable LocalPlayer var3, @Nullable ClientLevel var4, @NotNull PoseStack var5, @NotNull Font var6, @Nullable GuiGraphics var7, BFClientManager var8, @NotNull PlayerCloudData var9, BFClientPlayerData var10, @Nullable AbstractGame<?, ?, ?> var11, int var12, int var13, boolean var14, float var15, float var16);
}

