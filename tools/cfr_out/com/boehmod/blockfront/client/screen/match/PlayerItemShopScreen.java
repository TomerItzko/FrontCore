/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.match;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.ShopItemButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.common.net.packet.BFGameBuyItemRequestPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.tag.IHasItemShop;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class PlayerItemShopScreen
extends BFScreen {
    @NotNull
    private static final Component field_820 = Component.translatable((String)"bf.screen.ingame.buy.menu");
    @NotNull
    private final IHasItemShop game;

    public PlayerItemShopScreen(@NotNull IHasItemShop game) {
        super(field_820);
        this.game = game;
    }

    protected void init() {
        super.init();
        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) {
            return;
        }
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = 45;
        boolean bl = true;
        int n4 = n - 67 - 1;
        int n5 = n2 - 67 - 1;
        List<GameShopItem> list = this.game.getShopItems(localPlayer.getUUID());
        int n6 = list.size();
        for (int i = 0; i < n6; ++i) {
            GameShopItem gameShopItem = list.get(i);
            ItemStack itemStack = gameShopItem.getItemStack();
            int n7 = i;
            Vec2 vec2 = gameShopItem.getButtonPos();
            if (vec2 == null) continue;
            int n8 = (int)(1.0f + vec2.x);
            int n9 = (int)(1.0f - vec2.y);
            int n10 = gameShopItem.getPrice();
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(n10)).withStyle(ChatFormatting.GREEN);
            MutableComponent mutableComponent2 = itemStack.getDisplayName().copy();
            int n11 = n4 + 45 * n8 + 1 * n8;
            int n12 = n5 + 45 * n9 + 1 * n9;
            this.addRenderableWidget((GuiEventListener)new ShopItemButton(n11, n12, 45, 45, (Component)mutableComponent2, button -> this.buyItem(n7), gameShopItem).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).tip((Component)Component.literal((String)"Cost: ").append((Component)mutableComponent)));
        }
    }

    public void tick() {
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame == null || abstractGame.getStatus() != GameStatus.GAME) {
            this.onClose();
        }
    }

    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, BFRendering.translucentBlack());
        super.render(guiGraphics, n, n2, f);
    }

    public void buyItem(int index) {
        this.onClose();
        PacketUtils.sendToServer(new BFGameBuyItemRequestPacket(index));
    }
}

