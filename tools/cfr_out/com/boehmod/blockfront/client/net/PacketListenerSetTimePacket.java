/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.SimpleChannelInboundHandler
 *  net.minecraft.network.protocol.game.ClientboundSetTimePacket
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;

public class PacketListenerSetTimePacket
extends SimpleChannelInboundHandler<ClientboundSetTimePacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientboundSetTimePacket clientboundSetTimePacket) {
        if (clientboundSetTimePacket == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null) {
            MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
            int n = mapEnvironment.getTime();
            channelHandlerContext.fireChannelRead((Object)new ClientboundSetTimePacket(clientboundSetTimePacket.getGameTime(), (long)n, false));
            return;
        }
        channelHandlerContext.fireChannelRead((Object)clientboundSetTimePacket);
    }
}

