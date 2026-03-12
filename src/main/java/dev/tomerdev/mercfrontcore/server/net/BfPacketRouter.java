package dev.tomerdev.mercfrontcore.server.net;

import dev.tomerdev.mercfrontcore.MercFrontCore;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public final class BfPacketRouter {
    private BfPacketRouter() {
    }

    public static boolean attach(ServerPlayerEntity player) {
        Channel channel = resolveChannel(player);
        if (channel == null) {
            return false;
        }
        channel.eventLoop().execute(() -> {
            if (!channel.isActive()) {
                return;
            }
            try {
                removeHandlerIfPresent(channel.pipeline(), "mod_packet_handler_actions");
                attachHandler(channel.pipeline(), "mod_packet_handler_interaction", "com.boehmod.blockfront.server.net.PacketListenerInteraction");
                attachHandler(channel.pipeline(), "mod_packet_handler_move", "com.boehmod.blockfront.server.net.PacketListenerPlayerMove");
                MercFrontCore.LOGGER.info("BF packet router attached for {}", player.getNameForScoreboard());
            } catch (Throwable t) {
                MercFrontCore.LOGGER.error(
                    "Failed attaching BF packet router for {}: {}",
                    player.getNameForScoreboard(),
                    t.toString()
                );
            }
        });
        return true;
    }

    private static void attachHandler(ChannelPipeline pipeline, String name, String className) throws Exception {
        if (pipeline.get(name) != null) {
            pipeline.remove(name);
        }
        ChannelHandler handler = newHandler(className);
        if (pipeline.get("packet_handler") != null) {
            pipeline.addBefore("packet_handler", name, handler);
        } else {
            pipeline.addLast(name, handler);
        }
    }

    private static void removeHandlerIfPresent(ChannelPipeline pipeline, String name) {
        try {
            if (pipeline.get(name) != null) {
                pipeline.remove(name);
                MercFrontCore.LOGGER.info("Removed BF handler '{}' to allow vanilla action packet flow", name);
            }
        } catch (Throwable ignored) {
        }
    }

    private static ChannelHandler newHandler(String className) throws Exception {
        Class<?> type = Class.forName(className);
        Constructor<?> ctor = type.getDeclaredConstructor();
        ctor.setAccessible(true);
        return (ChannelHandler) ctor.newInstance();
    }

    private static Channel resolveChannel(ServerPlayerEntity player) {
        try {
            ServerPlayNetworkHandler networkHandler = player.networkHandler;
            if (networkHandler == null) {
                Object fallbackHandler = getFieldRecursive(player, "connection");
                if (!(fallbackHandler instanceof ServerPlayNetworkHandler asNetworkHandler)) {
                    return null;
                }
                networkHandler = asNetworkHandler;
            }

            Object connection = invokeNoArg(networkHandler, "getConnection");
            if (connection == null) {
                connection = getFieldRecursive(networkHandler, "connection");
            }
            if (connection == null) {
                return null;
            }

            Object channelCandidate = invokeNoArg(connection, "channel");
            if (channelCandidate == null) {
                channelCandidate = invokeNoArg(connection, "getChannel");
            }
            if (channelCandidate == null) {
                channelCandidate = getFieldRecursive(connection, "channel");
            }
            return channelCandidate instanceof Channel c ? c : null;
        } catch (Throwable ignored) {
            return null;
        }
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

    private static Object invokeNoArg(Object target, String methodName) {
        if (target == null) {
            return null;
        }
        try {
            Method method = target.getClass().getMethod(methodName);
            method.setAccessible(true);
            return method.invoke(target);
        } catch (NoSuchMethodException ignored) {
            Class<?> type = target.getClass();
            while (type != null && type != Object.class) {
                try {
                    Method declaredMethod = type.getDeclaredMethod(methodName);
                    declaredMethod.setAccessible(true);
                    return declaredMethod.invoke(target);
                } catch (NoSuchMethodException ex) {
                    type = type.getSuperclass();
                } catch (Throwable ex) {
                    return null;
                }
            }
            return null;
        } catch (Throwable ignored) {
            return null;
        }
    }
}
