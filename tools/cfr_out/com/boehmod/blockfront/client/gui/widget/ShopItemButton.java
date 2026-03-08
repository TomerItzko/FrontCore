/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.GameShopItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class ShopItemButton
extends BFButton {
    @NotNull
    private final GameShopItem shopItem;

    public ShopItemButton(int n, int n2, int n3, int n4, @NotNull Component component, @NotNull Button.OnPress onPress, @NotNull GameShopItem gameShopItem) {
        super(n, n2, n3, n4, component, onPress);
        this.shopItem = gameShopItem;
    }

    @Override
    public void method_381(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, int n, int n2, float f, float f2) {
        super.method_381(guiGraphics, poseStack, n, n2, f, f2);
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = this.getX() + n3;
        int n6 = this.getY() + n4;
        BFRendering.item(poseStack, guiGraphics, this.shopItem.getItemStack(), n5, n6, 1.5f);
    }

    @Override
    public void method_376(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        int n3 = this.width / 2;
        int n4 = this.getX() + n3;
        int n5 = this.getY() + this.height;
        int n6 = this.shopItem.getPrice();
        String string = this.shopItem.getItemStack().getDisplayName().getString().replace("[", "").replace("]", "");
        MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.BOLD);
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, n4, n5 - 14, 0xFFFFFF, 0.4f);
        if (n6 > 0) {
            MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(this.shopItem.getPrice())).withStyle(ChatFormatting.GREEN);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent2, n4, n5 - 8, 0xFFFFFF, 0.8f);
        }
    }
}

