package dev.tomerdev.mercfrontcore.server.net;

import com.boehmod.blockfront.util.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public final class BfNettyCompat {
    private BfNettyCompat() {
    }

    public static ServerPlayerEntity resolvePlayer(Channel channel) {
        if (channel == null) {
            return null;
        }

        try {
            ServerPlayerEntity player = NettyUtils.getServerPlayerFromConnection(channel);
            if (player != null) {
                return player;
            }
        } catch (Throwable ignored) {
        }

        try {
            ChannelPipeline pipeline = channel.pipeline();
            ChannelHandler packetHandler = pipeline.get("packet_handler");
            if (packetHandler instanceof ServerPlayNetworkHandler networkHandler) {
                return networkHandler.player;
            }

            Object player = getFieldRecursive(packetHandler, "player");
            if (player instanceof ServerPlayerEntity serverPlayer) {
                return serverPlayer;
            }

            Object connection = getFieldRecursive(packetHandler, "connection");
            if (connection instanceof ServerPlayNetworkHandler networkHandler) {
                return networkHandler.player;
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static Object getFieldRecursive(Object target, String name) {
        if (target == null) {
            return null;
        }
        Class<?> type = target.getClass();
        while (type != null && type != Object.class) {
            try {
                Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException ignored) {
                type = type.getSuperclass();
            } catch (Throwable ignored) {
                return null;
            }
        }
        return null;
    }
}
