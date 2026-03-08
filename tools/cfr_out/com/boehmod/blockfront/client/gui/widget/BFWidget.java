/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class BFWidget {
    @NotNull
    private Screen screen;
    @NotNull
    private final List<BFWidget> bfWidgets = new ObjectArrayList();
    @NotNull
    private final List<AbstractWidget> mcWidgets = new ObjectArrayList();
    protected int field_564;
    protected int field_565;
    protected int field_566;
    protected int height;
    private boolean field_561 = false;
    private boolean field_562 = true;
    @Nullable
    private Component field_570 = null;
    private boolean field_559 = false;
    private boolean field_560 = false;

    public BFWidget(int n, int n2, int n3, int height, @NotNull Screen screen) {
        this.field_564 = n;
        this.field_565 = n2;
        this.field_566 = n3;
        this.height = height;
        this.screen = screen;
    }

    @NotNull
    public BFWidget method_546(@NotNull Component component) {
        this.field_570 = component;
        return this;
    }

    @NotNull
    public BFWidget method_531() {
        this.field_559 = true;
        return this;
    }

    @NotNull
    public BFWidget method_547() {
        this.field_562 = false;
        return this;
    }

    public void method_535(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler) {
        this.clearAll();
        this.clearBfWidgets();
        this.bfWidgets.forEach(bFWidget -> bFWidget.method_535(minecraft, bFClientManager, clientPlayerDataHandler));
    }

    @OverridingMethodsMustInvokeSuper
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        this.bfWidgets.forEach(bFWidget -> bFWidget.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f));
        if (this.field_561) {
            if (!this.field_560 && this.field_559) {
                this.field_560 = true;
                if (this.field_570 != null) {
                    minecraft.getNarrator().sayNow((Component)Component.literal((String)this.field_570.getString()));
                }
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)((float)((double)0.9f + (double)0.1f * Math.random()))));
            }
        } else {
            this.field_560 = false;
        }
    }

    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        this.field_561 = BFRendering.isPointInRectangle(this.field_564, this.field_565, this.field_566, this.height, n, n2) && minecraft.screen == this.screen;
        poseStack.pushPose();
        if (this.field_562) {
            this.method_534(minecraft, clientPlayerDataHandler, poseStack, guiGraphics, n, n2, f, f2);
        }
        for (BFWidget bFWidget : this.bfWidgets) {
            bFWidget.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        }
        for (BFWidget bFWidget : this.bfWidgets) {
            bFWidget.method_541(minecraft, bFClientManager, clientPlayerDataHandler, font, guiGraphics, n, n2, f2);
        }
        for (AbstractWidget abstractWidget : this.mcWidgets) {
            abstractWidget.render(guiGraphics, n, n2, f2);
        }
        poseStack.popPose();
    }

    public void method_541(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        if (this.field_561) {
            this.method_542(font, guiGraphics, n, n2);
        }
        for (AbstractWidget abstractWidget : this.mcWidgets) {
            if (!(abstractWidget instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)abstractWidget;
            bFButton.method_379(minecraft, guiGraphics.pose(), font, guiGraphics, n, n2, f);
        }
    }

    protected void method_534(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        poseStack.pushPose();
        for (BFWidget bFWidget : this.bfWidgets) {
            bFWidget.method_534(minecraft, clientPlayerDataHandler, poseStack, guiGraphics, n, n2, f, f2);
        }
        poseStack.popPose();
    }

    public void method_542(@NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        if (this.field_570 != null) {
            guiGraphics.renderTooltip(font, this.field_570, n, n2);
        }
        poseStack.popPose();
    }

    public boolean mouseScrolled(double d, double d2, double d3, double d4) {
        for (AbstractWidget object : this.mcWidgets) {
            if (!object.mouseScrolled(d, d2, d3, d4)) continue;
            return true;
        }
        for (BFWidget bFWidget : this.bfWidgets) {
            if (!bFWidget.mouseScrolled(d, d2, d3, d4)) continue;
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double d, double d2, int n) {
        for (AbstractWidget object : this.mcWidgets) {
            if (!object.mouseReleased(d, d2, n)) continue;
            return true;
        }
        for (BFWidget bFWidget : this.bfWidgets) {
            if (!bFWidget.mouseReleased(d, d2, n)) continue;
            return true;
        }
        return false;
    }

    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        for (AbstractWidget object : this.mcWidgets) {
            if (!object.mouseClicked(d, d2, n)) continue;
            return true;
        }
        for (BFWidget bFWidget : this.bfWidgets) {
            if (!bFWidget.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n)) continue;
            return true;
        }
        return false;
    }

    public boolean keyPressed(int n, int n2, int n3) {
        return true;
    }

    public void clearAll() {
        this.mcWidgets.clear();
        this.bfWidgets.forEach(BFWidget::clearAll);
    }

    public void clearBfWidgets() {
        this.bfWidgets.forEach(BFWidget::clearBfWidgets);
    }

    public boolean addMcWidget(@NotNull AbstractWidget widget) {
        return this.mcWidgets.add(widget);
    }

    public boolean addBfWidget(@NotNull BFWidget widget) {
        return this.bfWidgets.add(widget);
    }

    public boolean removeBfWidget(@NotNull BFWidget widget) {
        return this.bfWidgets.remove(widget);
    }

    @NotNull
    public List<AbstractWidget> method_550() {
        return Collections.unmodifiableList(this.mcWidgets);
    }

    public boolean method_549(@NotNull AbstractWidget abstractWidget) {
        return this.mcWidgets.stream().anyMatch(abstractWidget2 -> abstractWidget2 == abstractWidget);
    }

    public int method_558() {
        return this.field_566;
    }

    public int height() {
        return this.height;
    }

    public void method_551(int n) {
        this.field_564 = n;
    }

    public void method_552(int n) {
        this.field_565 = n;
    }

    public void method_553(int n) {
        this.field_566 = n;
    }

    public void method_554(int n) {
        this.height = n;
    }

    protected void method_563() {
        this.bfWidgets.clear();
    }

    @NotNull
    public Screen getScreen() {
        return this.screen;
    }

    protected boolean method_557() {
        return this.field_561;
    }

    protected void method_555(boolean bl) {
        this.field_561 = bl;
    }

    public void method_556(boolean bl) {
        this.field_562 = bl;
    }

    public int method_559() {
        return this.field_564;
    }

    public int method_560() {
        return this.field_565;
    }

    public void setScreen(@NotNull Screen screen) {
        this.screen = screen;
    }
}

