/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.screen.GunStoreScreen;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFGunStoreBuyPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.StringUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public final class BF_201
extends GunStoreScreen {
    public BF_201(Screen screen, List<GameShopItem> list, AbstractGame<?, ?, ?> abstractGame) {
        super(screen, list, abstractGame);
        list.forEach(gameShopItem -> {
            Item item = gameShopItem.getItemStack().getItem();
            String string = StringUtils.formatLong(gameShopItem.getPrice());
            this.method_922((GameShopItem)gameShopItem, new String[]{item.getName(item.getDefaultInstance()).getString(), String.valueOf(ChatFormatting.GREEN) + "$" + string});
        });
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void method_924(@NotNull Item item, int n) {
        PacketUtils.sendToServer(new BFGunStoreBuyPacket(item));
        this.minecraft.setScreen(null);
        GunItem.field_4059 = 20;
    }
}

