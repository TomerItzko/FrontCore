/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.net;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.NettyUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PacketListenerPlayerMove
extends SimpleChannelInboundHandler<ServerboundMovePlayerPacket> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
        if (serverboundMovePlayerPacket == null) {
            return;
        }
        ServerPlayer serverPlayer = NettyUtils.getServerPlayerFromConnection(channelHandlerContext.channel());
        if (serverPlayer == null) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (!(bFAbstractManager instanceof BFServerManager)) {
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
        ServerPlayerDataHandler serverPlayerDataHandler = (ServerPlayerDataHandler)bFServerManager.getPlayerDataHandler();
        BFServerPlayerData bFServerPlayerData = (BFServerPlayerData)serverPlayerDataHandler.getPlayerData((Player)serverPlayer);
        double d = serverPlayer.getX();
        double d2 = serverPlayer.getZ();
        double d3 = serverboundMovePlayerPacket.getX(0.0);
        double d4 = serverboundMovePlayerPacket.getZ(0.0);
        if (serverboundMovePlayerPacket.hasRotation() && d3 != d && d4 != d2 && d3 != 0.0 && d4 != 0.0) {
            bFServerPlayerData.getAfkTracker().playerMoved();
        }
        channelHandlerContext.fireChannelRead((Object)serverboundMovePlayerPacket);
    }
}

