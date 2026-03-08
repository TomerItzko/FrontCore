/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BFOverlayScreen
extends BFMenuScreen {
    @Nullable
    protected final Screen parentScreen;

    public BFOverlayScreen(@Nullable Screen parentScreen, @NotNull Component title) {
        this(parentScreen, title, (Component)Component.empty());
    }

    public BFOverlayScreen(@Nullable Screen screen, @NotNull Component title, @NotNull Component component) {
        super(title, component);
        this.parentScreen = screen;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void init() {
        super.init();
        if (this.parentScreen != null) {
            this.parentScreen.init(this.minecraft, this.width, this.height);
        }
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        super.tick();
        if (this.parentScreen != null) {
            this.parentScreen.tick();
        }
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        PlayerCloudData playerCloudData = ((ClientPlayerDataHandler)this.manager.getPlayerDataHandler()).getCloudData(this.minecraft);
        float f2 = BFRendering.getRenderTime();
        if (this.parentScreen != null) {
            this.parentScreen.render(guiGraphics, n, n2, f);
        }
        BFRendering.backgroundBlur(this.minecraft, f);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        this.renderOverlay(poseStack, guiGraphics, n, n2, f);
        for (Object object : this.renderables) {
            object.render(guiGraphics, n, n2, f);
        }
        for (Object object : this.field_1047) {
            ((BFWidget)object).render(this.minecraft, this.manager, this.playerDataHandler, poseStack, guiGraphics, this.font, playerCloudData, n, n2, f2, f);
        }
        for (Object object : this.renderables) {
            if (!(object instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)((Object)object);
            bFButton.method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f);
        }
        poseStack.popPose();
    }

    public abstract void renderOverlay(@NotNull PoseStack var1, @NotNull GuiGraphics var2, int var3, int var4, float var5);
}

