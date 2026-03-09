package dev.tomerdev.mercfrontcore.server.net;

import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.util.MatchCompat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public final class MatchDropPacketGuard extends ChannelDuplexHandler {
    public static final String HANDLER_NAME = "mercfrontcore_drop_guard";
    private static final Set<UUID> DROP_BLOCKED_PLAYERS = ConcurrentHashMap.newKeySet();
    private final ServerPlayerEntity player;

    public MatchDropPacketGuard(ServerPlayerEntity player) {
        this.player = player;
    }

    public static boolean attach(ServerPlayerEntity player) {
        Channel channel = resolveChannel(player);
        if (channel == null) {
            MercFrontCore.LOGGER.debug(
                "Drop guard channel resolve pending for {}",
                player == null ? "unknown" : player.getNameForScoreboard()
            );
            return false;
        }
        channel.eventLoop().execute(() -> {
            if (!channel.isActive()) {
                return;
            }
            if (channel.pipeline().get(HANDLER_NAME) == null) {
                ChannelPipeline pipeline = channel.pipeline();
                if (pipeline.get("packet_handler") != null) {
                    pipeline.addBefore("packet_handler", HANDLER_NAME, new MatchDropPacketGuard(player));
                } else {
                    pipeline.addLast(HANDLER_NAME, new MatchDropPacketGuard(player));
                }
                MercFrontCore.LOGGER.info("Drop guard attached for {}", player.getNameForScoreboard());
            }
        });
        return true;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (shouldBlock(msg)) {
            MercFrontCore.LOGGER.info(
                "Blocked drop packet {} for {}",
                msg == null ? "null" : msg.getClass().getName(),
                player.getNameForScoreboard()
            );
            resync();
            return;
        }
        super.channelRead(ctx, msg);
    }

    private boolean shouldBlock(Object msg) {
        if (player == null || !DROP_BLOCKED_PLAYERS.contains(player.getUuid())) {
            return false;
        }

        if (msg instanceof PlayerActionC2SPacket actionPacket) {
            PlayerActionC2SPacket.Action action = actionPacket.getAction();
            return action == PlayerActionC2SPacket.Action.DROP_ITEM
                || action == PlayerActionC2SPacket.Action.DROP_ALL_ITEMS
                || action == PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND;
        }
        if (msg instanceof ClickSlotC2SPacket clickPacket) {
            SlotActionType type = clickPacket.getActionType();
            int slot = clickPacket.getSlot();
            int button = clickPacket.getButton();
            return type == SlotActionType.THROW
                || slot == 45
                || (type == SlotActionType.SWAP && button == 40);
        }
        String className = msg.getClass().getName();
        if (className.endsWith("ServerboundPlayerActionPacket")) {
            Object action = invokeNoArg(msg, "getAction");
            String actionName = action == null ? "" : action.toString().toUpperCase(Locale.ROOT);
            return actionName.contains("DROP_ITEM")
                || actionName.contains("DROP_ALL_ITEMS")
                || actionName.contains("SWAP_ITEM_WITH_OFFHAND");
        }
        if (className.endsWith("ServerboundContainerClickPacket")) {
            Object actionType = invokeNoArg(msg, "getActionType");
            if (actionType == null) {
                actionType = invokeNoArg(msg, "getClickType");
            }
            String typeName = actionType == null ? "" : actionType.toString().toUpperCase(Locale.ROOT);
            int slot = intMethod(msg, "getSlot", "getSlotNum");
            int button = intMethod(msg, "getButton", "getButtonNum");
            return typeName.contains("THROW")
                || slot == 45
                || (typeName.contains("SWAP") && button == 40);
        }
        return false;
    }

    public static void refreshDropBlockState(MinecraftServer server) {
        if (server == null) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID uuid = player.getUuid();
            if (player.isAlive() && MatchCompat.isInGameSession(player)) {
                DROP_BLOCKED_PLAYERS.add(uuid);
            } else {
                DROP_BLOCKED_PLAYERS.remove(uuid);
            }
        }
    }

    public static void clearState(ServerPlayerEntity player) {
        if (player != null) {
            DROP_BLOCKED_PLAYERS.remove(player.getUuid());
        }
    }

    private void resync() {
        player.server.execute(() -> {
            player.getInventory().markDirty();
            player.currentScreenHandler.sendContentUpdates();
            player.playerScreenHandler.sendContentUpdates();
        });
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

    private static int intMethod(Object target, String... methodNames) {
        if (target == null || methodNames == null) {
            return Integer.MIN_VALUE;
        }
        for (String methodName : methodNames) {
            Object value = invokeNoArg(target, methodName);
            if (value instanceof Number number) {
                return number.intValue();
            }
        }
        return Integer.MIN_VALUE;
    }
}
