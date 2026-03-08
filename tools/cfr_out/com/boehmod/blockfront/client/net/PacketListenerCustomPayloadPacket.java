/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.SimpleChannelInboundHandler
 *  net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.neoforged.neoforge.network.payload.ClientboundCustomSetTimePayload
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.payload.ClientboundCustomSetTimePayload;
import org.jetbrains.annotations.NotNull;

public class PacketListenerCustomPayloadPacket
extends SimpleChannelInboundHandler<ClientboundCustomPayloadPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ClientboundCustomPayloadPacket clientboundCustomPayloadPacket) {
        ClientboundCustomSetTimePayload clientboundCustomSetTimePayload;
        if (clientboundCustomPayloadPacket == null) {
            return;
        }
        CustomPacketPayload customPacketPayload = clientboundCustomPayloadPacket.payload();
        if (customPacketPayload instanceof ClientboundCustomSetTimePayload && this.onReadTimePayload(channelHandlerContext, clientboundCustomSetTimePayload = (ClientboundCustomSetTimePayload)customPacketPayload)) {
            return;
        }
        channelHandlerContext.fireChannelRead((Object)clientboundCustomPayloadPacket);
    }

    private boolean onReadTimePayload(@NotNull ChannelHandlerContext channelHandlerContext, @NotNull ClientboundCustomSetTimePayload clientboundCustomSetTimePayload) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null) {
            MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
            int n = mapEnvironment.getTime();
            ClientboundCustomSetTimePayload clientboundCustomSetTimePayload2 = new ClientboundCustomSetTimePayload(clientboundCustomSetTimePayload.gameTime(), (long)n, clientboundCustomSetTimePayload.gameRule(), clientboundCustomSetTimePayload.dayTimeFraction(), clientboundCustomSetTimePayload.dayTimePerTick());
            ClientboundCustomPayloadPacket clientboundCustomPayloadPacket = new ClientboundCustomPayloadPacket((CustomPacketPayload)clientboundCustomSetTimePayload2);
            channelHandlerContext.fireChannelRead((Object)clientboundCustomPayloadPacket);
            return true;
        }
        return false;
    }
}

