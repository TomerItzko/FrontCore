/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.menu;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.client.render.menu.MenuRenderer;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ParticleMenuRenderer
extends MenuRenderer {
    @Override
    public void renderBackground(@NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, PlayerCloudData cloudData, @NotNull BFMenuScreen screen, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        for (MenuParticle menuParticle : screen.getMenuParticles()) {
            if (menuParticle.getType() != MenuParticle.Type.BACKGROUND) continue;
            menuParticle.renderBelow(minecraft, graphics, poseStack, renderTime, delta);
        }
    }

    @Override
    public void renderAbove(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        for (MenuParticle menuParticle : screen.getMenuParticles()) {
            if (menuParticle.getType() != MenuParticle.Type.BACKGROUND) continue;
            menuParticle.renderAbove(minecraft, graphics, poseStack, renderTime, delta);
        }
    }

    @Override
    public void tick(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, float renderTime) {
        screen.getMenuParticles().removeIf(particle -> particle.shouldRemove(renderTime));
    }
}

