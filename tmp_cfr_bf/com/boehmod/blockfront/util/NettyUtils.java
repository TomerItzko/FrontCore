/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.util.EnvironmentUtils;
import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import java.net.SocketAddress;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.NotNull;

public class NettyUtils {
    @Nullable
    public static ServerPlayer getServerPlayerFromConnection(@NotNull Channel channel) {
        if (!(channel instanceof AbstractChannel)) {
            return null;
        }
        AbstractChannel abstractChannel = (AbstractChannel)channel;
        SocketAddress socketAddress = abstractChannel.remoteAddress();
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer == null) {
            return null;
        }
        return NettyUtils.findPlayerByAddress(minecraftServer.getPlayerList(), socketAddress);
    }

    @Nullable
    private static ServerPlayer findPlayerByAddress(@NotNull PlayerList playerList, @NotNull SocketAddress address) {
        for (ServerPlayer serverPlayer : playerList.getPlayers()) {
            SocketAddress socketAddress = serverPlayer.connection.getConnection().getRemoteAddress();
            if (!address.equals(socketAddress)) continue;
            return serverPlayer;
        }
        return null;
    }
}

