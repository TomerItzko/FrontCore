/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.SimpleChannelInboundHandler
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.protocol.game.ClientboundSystemChatPacket
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Locale;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

public class PacketListenerSystemChatPacket
extends SimpleChannelInboundHandler<ClientboundSystemChatPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientboundSystemChatPacket clientboundSystemChatPacket) {
        if (clientboundSystemChatPacket == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null) {
            String string = I18n.get((String)"multiplayer.player.joined", (Object[])new Object[]{""}).toLowerCase(Locale.ROOT);
            String string2 = I18n.get((String)"multiplayer.player.joined.renamed", (Object[])new Object[]{""}).toLowerCase(Locale.ROOT);
            String string3 = I18n.get((String)"multiplayer.player.left", (Object[])new Object[]{""}).toLowerCase(Locale.ROOT);
            String string4 = clientboundSystemChatPacket.content().getString().toLowerCase(Locale.ROOT);
            if (string4.endsWith(string) || string4.endsWith(string2) || string4.endsWith(string3)) {
                return;
            }
        }
        channelHandlerContext.fireChannelRead((Object)clientboundSystemChatPacket);
    }
}

