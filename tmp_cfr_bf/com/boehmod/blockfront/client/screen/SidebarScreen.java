/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public abstract class SidebarScreen
extends BFMenuScreen {
    protected static float field_1031 = 0.0f;
    @Nullable
    public final Screen field_1036;
    public boolean field_1030 = true;
    protected float field_1032;
    protected float field_1033 = 0.0f;
    protected float field_1034;
    protected float field_1035 = 0.0f;
    private boolean field_6347 = false;

    public SidebarScreen(@Nullable Screen screen, @NotNull Component component) {
        super(component);
        this.field_1036 = screen;
    }

    public SidebarScreen(@NotNull Screen screen, @NotNull Component component, boolean bl) {
        this(screen, component);
        this.field_1030 = bl;
    }

    @Nonnull
    public SidebarScreen method_5491(boolean bl) {
        this.field_6347 = bl;
        return this;
    }

    protected abstract boolean method_753(double var1, double var3);

    @Override
    @OverridingMethodsMustInvokeSuper
    public void init() {
        super.init();
        field_1031 = (float)this.width / 4.0f;
        if (!this.field_1030) {
            this.field_1032 = this.field_1033 = field_1031;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        float f2 = BFRendering.getRenderTime();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)this.manager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(this.minecraft);
        if (this.field_1036 != null && (this.field_1036.width != this.width || this.field_1036.height != this.height)) {
            this.field_1036.init(this.minecraft, this.width, this.height);
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, -400.0f);
        if (this.field_1036 != null) {
            this.field_1036.render(guiGraphics, n, n2, f);
            if (this.field_6347) {
                BFRendering.backgroundBlur(this.minecraft, f);
            }
        }
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        this.renderBackground(guiGraphics, n, n2, f);
        poseStack.pushPose();
        poseStack.translate(this.method_756(f), 0.0f, 0.0f);
        this.method_754(guiGraphics, f);
        this.renderables.forEach(renderable -> renderable.render(guiGraphics, n, n2, f));
        this.method_768(this.minecraft, clientPlayerDataHandler, poseStack, guiGraphics, playerCloudData, n, n2, f2, f);
        for (Renderable renderable2 : this.renderables) {
            if (!(renderable2 instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)renderable2;
            bFButton.method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f2);
        }
        poseStack.popPose();
        poseStack.translate(0.0f, 0.0f, -400.0f);
        poseStack.popPose();
    }

    public float method_756(float f) {
        return MathUtils.lerpf1(this.field_1032, this.field_1033, f);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        float f2 = MathUtils.lerpf1(this.field_1034, this.field_1035, f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, BFRendering.translucentBlack(), f2 * 0.5f);
        BFRendering.texture(poseStack, guiGraphics, CORNERSHADOW, 0, 0, this.width, this.height, f2 * 2.0f);
    }

    public void method_754(@NotNull GuiGraphics guiGraphics, float f) {
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        super.tick();
        this.field_1035 = this.field_1034;
        this.field_1034 = Mth.lerp((float)0.2f, (float)this.field_1034, (float)1.0f);
        this.field_1033 = this.field_1032;
        this.field_1032 = Mth.lerp((float)0.2f, (float)this.field_1032, (float)0.0f);
        if (this.field_1032 <= 0.0f) {
            this.field_1030 = true;
        }
        if (this.field_1030) {
            this.field_1032 = 0.0f;
        }
        if (this.field_1036 != null) {
            this.field_1036.tick();
        }
    }

    public void method_752() {
        Screen screen = this.field_1036;
        if (screen instanceof SidebarScreen) {
            SidebarScreen sidebarScreen = (SidebarScreen)screen;
            sidebarScreen.field_1030 = true;
        }
        this.minecraft.setScreen(this.field_1036);
    }

    @Override
    public boolean mouseClicked(double d, double d2, int n) {
        SoundManager soundManager = this.minecraft.getSoundManager();
        if (n == 0 && this.method_753(d, d2)) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_CLOSE.get()), (float)((float)((double)0.9f + (double)0.1f * Math.random()))));
            this.method_752();
            return true;
        }
        return super.mouseClicked(d, d2, n);
    }
}

