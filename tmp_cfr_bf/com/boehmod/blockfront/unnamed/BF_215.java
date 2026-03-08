/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.net.HttpTextureManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.unnamed.BF_218;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public final class BF_215
extends BF_218 {
    @NotNull
    private final String field_1341;
    public int field_1342;
    public int field_1343;

    public BF_215(@NotNull String string, int n, int n2) {
        this.field_1341 = string;
        this.field_1342 = n;
        this.field_1343 = n2;
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
    }

    @Override
    protected void method_979() {
        super.method_979();
    }

    @Override
    public boolean method_990() {
        return false;
    }

    @Override
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        BFRendering.texture(poseStack, guiGraphics, HttpTextureManager.getOrLoad(minecraft, this.field_1341), this.field_1357 + 5, this.field_1358, this.field_1342, this.field_1343);
    }

    @Override
    public int height() {
        return this.field_1343;
    }

    @Override
    public int method_989() {
        return this.field_1354.method_558();
    }
}

