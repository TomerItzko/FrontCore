/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.world.entity.Entity
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.top;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public interface ITopLevelRenderer {
    public void render(@NotNull RenderLevelStageEvent var1, @NotNull Minecraft var2, @NotNull BFClientManager var3, @NotNull ClientLevel var4, @NotNull ClientPlayerDataHandler var5, @NotNull PoseStack var6, @NotNull Frustum var7, @NotNull GuiGraphics var8, MultiBufferSource.BufferSource var9, @NotNull Font var10, @NotNull Camera var11, @NotNull Iterable<Entity> var12, @NotNull Random var13, float var14);
}

