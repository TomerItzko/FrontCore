/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.prompt.confirm;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.screen.prompt.BFPromptScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.ConfirmCallback;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFConfirmPromptScreen
extends BFPromptScreen {
    private static final Component MESSAGE_NO = Component.translatable((String)"bf.message.no");
    private static final Component MESSAGE_YES = Component.translatable((String)"bf.message.yes");
    @NotNull
    private final ConfirmCallback callback;

    public BFConfirmPromptScreen(@Nullable Screen screen, @NotNull Component component, @NotNull ConfirmCallback callback) {
        super(screen, component);
        this.callback = callback;
    }

    public BFConfirmPromptScreen method_1082() {
        this.field_1522 = false;
        return this;
    }

    protected void method_1101(boolean bl) {
        this.callback.onResult(bl);
    }

    @Override
    public void method_758() {
        int n = this.width / 2 - 95;
        int n2 = 85 + 11 * this.field_1518.size() + 6;
        this.addRenderableWidget((GuiEventListener)new BFButton(n + 2, n2, 80, 20, MESSAGE_NO, button -> {
            if (this.field_1522) {
                this.method_1081();
            }
            this.method_1101(false);
        }).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW));
        this.addRenderableWidget((GuiEventListener)new BFButton(n + 190 - 80 - 2, n2, 80, 20, MESSAGE_YES, button -> {
            if (this.field_1522) {
                this.method_1081();
            }
            this.method_1101(true);
        }).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2 - 95;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        this.method_1087(guiGraphics, n3, 80);
        poseStack.popPose();
    }

    int method_1080() {
        return 0;
    }
}

