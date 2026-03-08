/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.mixin;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ConnectScreen.class})
public abstract class ConnectScreenMixin
extends Screen {
    @Unique
    private final ResourceLocation blockFrontAll$backgroundTexture = this.blockFrontAll$getRandomTexture();

    protected ConnectScreenMixin() {
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

