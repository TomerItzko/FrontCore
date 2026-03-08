/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.StringUtils;
import java.awt.Rectangle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BF_199 {
    @NotNull
    private final GameShopItem field_1256;
    @NotNull
    private final String[] field_1257;
    private int field_1258;
    private int field_1259;
    private int field_1260;
    private int field_1261;

    public BF_199(int n, int n2, @NotNull GameShopItem gameShopItem, @NotNull String[] stringArray, int n3, int n4) {
        this.field_1258 = n;
        this.field_1259 = n2;
        this.field_1256 = gameShopItem;
        this.field_1257 = stringArray;
        this.field_1260 = n3;
        this.field_1261 = n4;
    }

    public void method_916(@NotNull GuiGraphics guiGraphics, @NotNull Font font) {
        ItemStack itemStack = this.field_1256.getItemStack();
        Item item = itemStack.getItem();
        int n = this.field_1256.getPrice();
        MutableComponent mutableComponent = Component.literal((String)StringUtils.abbreviate(item.getName(item.getDefaultInstance()).getString(), 12));
        if (n > 0) {
            String string = StringUtils.formatLong(n);
            MutableComponent mutableComponent2 = Component.literal((String)(" $" + string)).withStyle(ChatFormatting.GREEN);
            mutableComponent.append((Component)mutableComponent2);
        }
        BFRendering.drawString(font, guiGraphics, (Component)mutableComponent, this.field_1258 + 5, this.field_1259 + 7);
    }

    public void method_915(@NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, int n, float f) {
        GameTeam gameTeam;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        int n2 = 16759296;
        if (abstractGame != null && (gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(localPlayer.getUUID())) != null) {
            n2 = gameTeam.getColor();
        }
        float f2 = 1.0f + Mth.sin((float)(f / 10.0f));
        BFRendering.rectangle(guiGraphics, this.field_1258 - n, this.field_1259, n, this.field_1261, n2, f2);
        BFRendering.rectangle(guiGraphics, this.field_1258 + this.field_1260, this.field_1259, n, this.field_1261, n2, f2);
        BFRendering.rectangle(guiGraphics, this.field_1258 - n, this.field_1259 - n, this.field_1260 + n * 2, n, n2, f2);
        BFRendering.rectangle(guiGraphics, this.field_1258 - n, this.field_1259 + this.field_1261, this.field_1260 + n * 2, n, n2, f2);
    }

    public boolean method_914(int n, int n2) {
        Rectangle rectangle = new Rectangle(this.field_1258, this.field_1259, this.field_1260, this.field_1261);
        return rectangle.contains(n, n2);
    }

    @NotNull
    public Item method_912() {
        return this.field_1256.getItemStack().getItem();
    }

    @NotNull
    public String[] method_913() {
        return this.field_1257;
    }

    public void method_917(int n) {
        this.field_1258 = n;
    }

    public void method_918(int n) {
        this.field_1259 = n;
    }

    public void method_919(int n) {
        this.field_1260 = n;
    }

    public void method_920(int n) {
        this.field_1261 = n;
    }
}

