package dev.tomerdev.mercfrontcore.util;

import java.util.UUID;
import net.minecraft.client.network.ClientPlayerEntity;

public final class ClientMatchCompat {
    private ClientMatchCompat() {
    }

    public static boolean isInGameSession(ClientPlayerEntity player) {
        if (player == null) {
            return false;
        }
        try {
            Class<?> blockFrontClass = Class.forName("com.boehmod.blockfront.BlockFront");
            Object blockFront = blockFrontClass.getMethod("getInstance").invoke(null);
            if (blockFront == null) {
                return false;
            }
            Object manager = blockFrontClass.getMethod("getManager").invoke(blockFront);
            if (manager == null) {
                return false;
            }
            UUID uuid = player.getUuid();
            Object game = manager.getClass().getMethod("getGameWithPlayer", UUID.class).invoke(manager, uuid);
            return game != null;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isInActiveMatch(ClientPlayerEntity player) {
        return isInGameSession(player);
    }
}
