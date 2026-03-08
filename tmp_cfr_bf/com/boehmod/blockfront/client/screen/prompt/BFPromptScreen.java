/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFOverlayScreen;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public abstract class BFPromptScreen
extends BFOverlayScreen {
    public static final int field_1524 = 190;
    public static final int field_1525 = 60;
    public static final int field_1526 = 80;
    public static final int field_1527 = 20;
    @NotNull
    public static final ResourceLocation CORNERSHADOW = BFRes.loc("textures/gui/cornershadow.png");
    public static final int field_1528 = BFRendering.translucentBlack();
    public static final int field_1529 = 15;
    @NotNull
    protected final List<FormattedCharSequence> field_1518 = new ObjectArrayList();
    private float field_1519;
    private float field_1521 = 0.0f;
    protected boolean field_1520 = false;
    protected boolean field_1522 = true;
    @NotNull
    private final Component field_1530;

    public BFPromptScreen(@Nullable Screen screen, @NotNull Component component) {
        super(screen, component);
        this.field_1530 = component;
    }

    public boolean method_1083(double d, double d2) {
        int n = 15;
        int n2 = 220;
        int n3 = 60 + 11 * this.field_1518.size() + 30 + this.method_1080();
        int n4 = this.width / 2 - 110;
        int n5 = 35;
        return d < (double)n4 || d > (double)(n4 + 220) || d2 < 35.0 || d2 > (double)(35 + n3);
    }

    public <T extends BFPromptScreen> T method_1082() {
        this.field_1522 = false;
        return (T)((Object)this);
    }

    public <T extends BFPromptScreen> T method_1084(@NotNull Component component) {
        if (component.getString().isEmpty()) {
            this.field_1518.add(component.getVisualOrderText());
        } else {
            this.field_1518.addAll(this.font.split((FormattedText)component, 190));
        }
        return (T)((Object)this);
    }

    public <T extends BFPromptScreen> T method_1085(Component ... componentArray) {
        for (Component component : componentArray) {
            this.method_1084(component);
        }
        return (T)((Object)this);
    }

    public void onClose() {
        if (this.field_1520 && this.field_1522) {
            this.method_1081();
        } else {
            super.onClose();
        }
    }

    @Override
    public void init() {
        super.init();
        if (this.field_1520 && this.field_1522) {
            this.method_1081();
        }
        this.field_1520 = true;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        super.tick();
        this.field_1521 = this.field_1519;
        this.field_1519 = Mth.lerp((float)0.4f, (float)this.field_1519, (float)1.0f);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void renderOverlay(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, int n, int n2, float f) {
        float f2 = MathUtils.lerpf1(this.field_1521, this.field_1519, f);
        BFRendering.rectangle(graphics, 0, 0, this.width, this.height, BFRendering.translucentBlack(), 0.4f * f2);
        int n3 = 15;
        int n4 = 220;
        int n5 = 60 + 11 * this.field_1518.size() + 30 + this.method_1080();
        int n6 = this.width / 2 - 110;
        int n7 = 35;
        int n8 = field_1528;
        BFRendering.rectangle(graphics, n6 + 1, 35, 218, 1, n8);
        BFRendering.rectangle(graphics, n6, 36, 220, n5 - 2, n8);
        BFRendering.rectangle(graphics, n6 + 1, 35 + n5 - 1, 218, 1, n8);
        int n9 = 2;
        BFRendering.promptBackground(poseStack, graphics, n6 + 2, 37, 216, n5 - 4);
        BFRendering.texture(poseStack, graphics, CORNERSHADOW, 0, 0, this.width, this.height);
        this.method_1086(poseStack, graphics);
    }

    @Override
    public boolean mouseClicked(double d, double d2, int n) {
        if (n == 0 && this.method_1083(d, d2)) {
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_CLOSE.get()), (float)1.0f));
            this.method_1081();
            return true;
        }
        return super.mouseClicked(d, d2, n);
    }

    protected void method_1086(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics) {
        int n = 95;
        int n2 = this.width / 2 - 95;
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)this.field_1530.copy().withStyle(BFStyles.BOLD), n2 + 95, 48, 1.5f);
    }

    protected void method_1087(@NotNull GuiGraphics guiGraphics, int n, int n2) {
        int n3 = 95;
        int n4 = this.field_1518.size();
        for (int i = 0; i < n4; ++i) {
            FormattedCharSequence formattedCharSequence = this.field_1518.get(i);
            int n5 = this.font.width(formattedCharSequence);
            guiGraphics.drawString(this.font, formattedCharSequence, n + 95 - n5 / 2, n2 + i * 11, 0xFFFFFF);
        }
    }

    protected void method_1081() {
        if (!this.field_1522) {
            this.minecraft.setScreen(null);
            return;
        }
        Screen screen = this.parentScreen;
        if (screen instanceof SidebarScreen) {
            SidebarScreen sidebarScreen = (SidebarScreen)screen;
            sidebarScreen.field_1030 = true;
        }
        this.minecraft.setScreen(this.parentScreen);
    }

    abstract int method_1080();
}

