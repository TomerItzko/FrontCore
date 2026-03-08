/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.SimpleChannelInboundHandler
 *  net.minecraft.network.protocol.game.ServerboundInteractPacket
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 */
package com.boehmod.blockfront.server.net;

import com.boehmod.blockfront.util.NettyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class PacketListenerInteraction
extends SimpleChannelInboundHandler<ServerboundInteractPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerboundInteractPacket serverboundInteractPacket) {
        Entity entity;
        ServerPlayer serverPlayer = NettyUtils.getServerPlayerFromConnection(channelHandlerContext.channel());
        if (serverPlayer != null && (entity = serverboundInteractPacket.getTarget(serverPlayer.serverLevel())) != null && entity.getId() == serverPlayer.getId()) {
            return;
        }
        channelHandlerContext.fireChannelRead((Object)serverboundInteractPacket);
    }
}
