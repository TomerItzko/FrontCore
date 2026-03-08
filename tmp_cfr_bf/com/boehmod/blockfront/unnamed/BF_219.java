/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BF_219
extends BF_218 {
    private static final char field_1362 = '\u00a7';
    private final String field_1360;
    private boolean field_1363 = false;
    @Nullable
    private String field_1361;

    public BF_219(@NotNull String string) {
        this.field_1360 = string;
    }

    @NotNull
    public static String method_995(@NotNull String string) {
        char[] cArray = string.toCharArray();
        int n = cArray.length - 1;
        for (int i = 0; i < n; ++i) {
            if (cArray[i] != '&' || "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(cArray[i + 1]) <= -1) continue;
            cArray[i] = 167;
            cArray[i + 1] = Character.toLowerCase(cArray[i + 1]);
        }
        return new String(cArray);
    }

    @NotNull
    public BF_219 method_994(@Nullable String string) {
        this.field_1361 = string;
        return this;
    }

    public void method_993() {
        this.field_1363 = true;
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
    }

    @Override
    public boolean method_990() {
        return true;
    }

    @Override
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, BF_219.method_995(this.field_1360), this.field_1354.method_558() - 15);
        boolean bl = this.method_991();
        boolean bl2 = this.method_992();
        int n3 = objectList.size();
        for (int i = 0; i < n3; ++i) {
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)Component.literal((String)(String.valueOf(bl || bl2 && this.field_1361 != null ? ChatFormatting.WHITE : "") + (String)objectList.get(i))), this.field_1357 + 2, this.field_1358 + 2 + (this.field_1363 ? 5 : 10) * i, this.field_1363 ? 0.5f : 1.0f);
        }
    }

    @Override
    public int height() {
        Font font = Minecraft.getInstance().font;
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, BF_219.method_995(this.field_1360), this.field_1354.method_558() - 15);
        return objectList.size() * (this.field_1363 ? 6 : 12);
    }

    @Override
    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        super.method_982(minecraft, bFClientManager, clientPlayerDataHandler, n, n2, n3);
        String[] stringArray = this.field_1360.split(" ");
        if (this.field_1361 != null) {
            if (this.field_1361.startsWith("http://") || this.field_1361.startsWith("https://")) {
                BFMenuScreen.openUrl(minecraft, this.field_1361);
            }
        } else {
            for (String string : stringArray) {
                if (string.length() > 2 && string.startsWith("&")) {
                    string = string.substring(2);
                }
                if (!string.startsWith("http://") && !string.startsWith("https://")) continue;
                BFMenuScreen.openUrl(minecraft, string);
                return;
            }
        }
    }

    @Override
    public int method_989() {
        return this.field_1354.method_558();
    }
}

