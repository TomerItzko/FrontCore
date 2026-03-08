/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.toasts.Toast
 *  net.minecraft.client.gui.components.toasts.Toast$Visibility
 *  net.minecraft.client.gui.components.toasts.ToastComponent
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.util.FormattedCharSequence
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.gui.BFNotificationType;
import com.boehmod.blockfront.client.render.BFRendering;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

public final class BFNotification
implements Toast {
    public static final int field_1367 = 4;
    public static final int field_1368 = 4;
    @NotNull
    private final BFNotificationType type;
    @NotNull
    private Component title;
    @Nullable
    private Component message;
    private long displayTime;
    private boolean updated;

    public BFNotification(@NotNull BFNotificationType type, @NotNull Component title, @Nullable Component message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public static void addToast(@NotNull ToastComponent component, @NotNull BFNotificationType type, @NotNull Component title, @Nullable Component message) {
        BFNotification bFNotification = (BFNotification)component.getToast(BFNotification.class, (Object)type);
        if (bFNotification == null) {
            component.addToast((Toast)new BFNotification(type, title, message));
        } else {
            bFNotification.update(title, message);
        }
    }

    public static void show(@NotNull Minecraft minecraft, @NotNull Component message) {
        BFNotification.show(minecraft, BlockFront.DISPLAY_NAME_COMPONENT, message, BFNotificationType.CLIENT_NOTIFICATION, false);
    }

    public static void show(@NotNull Minecraft minecraft, @NotNull Component message, @NotNull BFNotificationType type) {
        BFNotification.show(minecraft, BlockFront.DISPLAY_NAME_COMPONENT, message, type, false);
    }

    public static void show(@NotNull Minecraft minecraft, @NotNull Component title, @NotNull Component message) {
        BFNotification.show(minecraft, title, message, false);
    }

    public static void show(@NotNull Minecraft minecraft, @NotNull Component title, @NotNull Component message, boolean narrator) {
        BFNotification.show(minecraft, title, message, BFNotificationType.CLIENT_NOTIFICATION, narrator);
    }

    public static void show(@NotNull Minecraft minecraft, @NotNull Component title, @NotNull Component message, @NotNull BFNotificationType type, boolean narrator) {
        if (minecraft.options.hideGui) {
            return;
        }
        BFNotification.addToast(minecraft.getToasts(), type, title, message);
        if (narrator) {
            String string = ChatFormatting.stripFormatting((String)(title.getString() + message.getString()));
            minecraft.getNarrator().sayNow(string);
        }
    }

    public void update(@NotNull Component title, @Nullable Component message) {
        this.title = title;
        this.message = message;
        this.updated = true;
    }

    @NotNull
    public Toast.Visibility render(@NotNull GuiGraphics graphics, @NotNull ToastComponent components, long millis) {
        int n;
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        PoseStack poseStack = graphics.pose();
        if (this.updated) {
            this.displayTime = millis;
            this.updated = false;
        }
        boolean bl = millis - this.displayTime >= 5000L;
        float f = bl ? 1.0f : (float)(millis - this.displayTime) / 5000.0f;
        int n2 = this.height();
        int n3 = this.width();
        this.renderBackground(poseStack, graphics);
        int n4 = n2 - 8;
        int n5 = n3 - n4 - 4;
        BFRendering.textureSquare(poseStack, graphics, this.type.getIcon(), n5, 4, n4);
        BFRendering.drawString(font, graphics, this.title, 4, 4, this.type.getColor());
        if (this.message != null) {
            n = (n3 - n4 - 4) * 2;
            List list = font.split((FormattedText)this.message, n);
            BFRendering.renderTextLines(poseStack, font, (MultiBufferSource)graphics.bufferSource(), (List<FormattedCharSequence>)list, 4.0f, 14.0f, 5, 0.5f);
        }
        n = 2;
        int n6 = n2 - 3;
        int n7 = n3 - 4;
        BFRendering.rectangle(poseStack, graphics, 2.0f, n6, (float)n7 * f, 1.0f, 0xFFFFFF, 0.25f * f);
        return bl ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    public void renderBackground(@NotNull PoseStack pose, @NotNull GuiGraphics graphics) {
        int n = this.width();
        int n2 = this.height();
        int n3 = this.getToastType().getColor();
        int n4 = n3 - -1442840576;
        BFRendering.rectangle(graphics, 1, 0, n - 2, 1, -14606047);
        BFRendering.rectangle(graphics, 0, 1, n, n2 - 2, -14606047);
        BFRendering.rectangle(graphics, 1, n2 - 1, n - 2, 1, -14606047);
        BFRendering.rectangle(graphics, 1, 1, n - 2, 1, n3);
        BFRendering.rectangle(graphics, 1, n2 - 2, n - 2, 1, n4);
        BFRendering.rectangleGradient(graphics, 1, 2, 1, n2 - 4, n3, n4);
        BFRendering.rectangleGradient(graphics, n - 2, 2, 1, n2 - 4, n3, n4);
    }

    @NotNull
    public BFNotificationType getToastType() {
        return this.type;
    }

    public int height() {
        return 32;
    }
}

