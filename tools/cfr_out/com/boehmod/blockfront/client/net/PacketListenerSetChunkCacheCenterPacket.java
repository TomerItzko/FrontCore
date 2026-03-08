/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.SimpleChannelInboundHandler
 *  net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.util.BFLog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;

public class PacketListenerSetChunkCacheCenterPacket
extends SimpleChannelInboundHandler<ClientboundSetChunkCacheCenterPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientboundSetChunkCacheCenterPacket clientboundSetChunkCacheCenterPacket) throws Exception {
        if (clientboundSetChunkCacheCenterPacket == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (bFClientManager.getCinematics().isSequencePlaying()) {
            BFLog.log("Ignoring received chunk cache center packet because a cinematic sequence is playing.", new Object[0]);
            return;
        }
        channelHandlerContext.fireChannelRead((Object)clientboundSetChunkCacheCenterPacket);
    }
}

