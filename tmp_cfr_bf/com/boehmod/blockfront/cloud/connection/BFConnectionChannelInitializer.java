/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud.connection;

import com.boehmod.bflib.cloud.packet.PacketCodec;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.cloud.connection.BFConnectionInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class BFConnectionChannelInitializer
extends ChannelInitializer<SocketChannel> {
    @NotNull
    private final BFConnection connectionHandler;

    public BFConnectionChannelInitializer(@NotNull BFConnection modConnection) {
        this.connectionHandler = modConnection;
    }

    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast("idleStateHandler", (ChannelHandler)new IdleStateHandler(30L, 0L, 0L, TimeUnit.SECONDS)).addLast("frameDecoder", (ChannelHandler)PacketCodec.createFrameDecoder()).addLast("frameEncoder", (ChannelHandler)PacketCodec.createFrameEncoder()).addLast("packetDecoder", (ChannelHandler)new PacketCodec.PacketDecoder()).addLast("packetEncoder", (ChannelHandler)new PacketCodec.PacketEncoder()).addLast("handler", (ChannelHandler)new BFConnectionInboundHandler(this.connectionHandler));
    }
}

