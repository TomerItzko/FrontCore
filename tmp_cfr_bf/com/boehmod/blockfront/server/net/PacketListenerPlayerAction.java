/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.net;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.NettyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketListenerPlayerAction
extends SimpleChannelInboundHandler<ServerboundPlayerActionPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerboundPlayerActionPacket serverboundPlayerActionPacket) {
        if (serverboundPlayerActionPacket == null) {
            return;
        }
        ServerPlayer serverPlayer = NettyUtils.getServerPlayerFromConnection(channelHandlerContext.channel());
        if (serverPlayer == null) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(serverPlayer.getUUID());
        if (abstractGame != null) {
            ServerboundPlayerActionPacket.Action action = serverboundPlayerActionPacket.getAction();
            switch (action) {
                case DROP_ITEM: 
                case DROP_ALL_ITEMS: {
                    Inventory inventory = serverPlayer.getInventory();
                    ItemStack itemStack = inventory.getSelected();
                    if (((AbstractGamePlayerManager)abstractGame.getPlayerManager()).canPlayerDropItem((Player)serverPlayer, itemStack)) break;
                    inventory.setItem(inventory.selected, itemStack);
                    serverPlayer.containerMenu.sendAllDataToRemote();
                    return;
                }
                case SWAP_ITEM_WITH_OFFHAND: {
                    if (abstractGame.canPlayersSwapItems()) break;
                    return;
                }
            }
        }
        channelHandlerContext.fireChannelRead((Object)serverboundPlayerActionPacket);
    }
}

