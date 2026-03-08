/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.DisconnectedScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package com.boehmod.blockfront.mixin;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={DisconnectedScreen.class})
public abstract class DisconnectedScreenMixin
extends Screen {
    @Unique
    private final ResourceLocation blockFrontAll$backgroundTexture = this.blockFrontAll$getRandomTexture();

    protected DisconnectedScreenMixin() {
        super((Component)Component.empty());
    }

    @Unique
    @NotNull
    private ResourceLocation blockFrontAll$getRandomTexture() {
        int n = ThreadLocalRandom.current().nextInt(0, 4);
        return BFRes.loc("textures/gui/background/loading/background" + n + ".png");
    }

    public void renderBackground(GuiGraphics guiGraphics, int n, int n2, float f) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();
        BFRendering.method_5483(minecraft, poseStack, guiGraphics, this.blockFrontAll$backgroundTexture, this.width, this.height, f);
    }
}

