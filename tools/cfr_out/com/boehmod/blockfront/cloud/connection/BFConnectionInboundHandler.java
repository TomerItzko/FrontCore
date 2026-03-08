/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.primitives.EncryptionKeyExchangePacket
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInboundHandlerAdapter
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.cloud.connection;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.primitives.EncryptionKeyExchangePacket;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.util.BFLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFConnectionInboundHandler
extends ChannelInboundHandlerAdapter {
    @Nullable
    private ByteBuf cumulativeBuffer = Unpooled.buffer();
    @NotNull
    private final BFConnection connectionHandler;

    public BFConnectionInboundHandler(@NotNull BFConnection modConnection) {
        this.connectionHandler = modConnection;
    }

    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) {
        if (object instanceof IPacket) {
            IPacket iPacket = (IPacket)object;
            this.connectionHandler.bumpIdle();
            if (iPacket instanceof EncryptionKeyExchangePacket) {
                EncryptionKeyExchangePacket encryptionKeyExchangePacket = (EncryptionKeyExchangePacket)iPacket;
                BFLog.log("[Connection Handler] Received encryption key exchange packet", new Object[0]);
                this.connectionHandler.handleEncryptionKeyExchange(encryptionKeyExchangePacket);
            } else {
                this.connectionHandler.addIncomingPacket(iPacket);
            }
        }
    }

    public void channelInactive(ChannelHandlerContext channelHandlerContext) {
        BFLog.log("[Connection Handler] Channel inactive", new Object[0]);
        this.connectionHandler.disconnect("Connection closed by server", false);
        if (this.cumulativeBuffer != null) {
            this.cumulativeBuffer.release();
            this.cumulativeBuffer = null;
        }
    }

    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        BFLog.logThrowable("[Connection Handler] Channel exception", throwable, new Object[0]);
        this.connectionHandler.disconnect("Channel error: " + throwable.getMessage(), false);
        if (this.cumulativeBuffer != null) {
            this.cumulativeBuffer.release();
            this.cumulativeBuffer = null;
        }
    }
}

