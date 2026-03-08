package dev.tomerdev.mercfrontcore.util;

import dev.tomerdev.mercfrontcore.AddonConstants;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public final class AfkCompat {
    private static final long DEBUG_LOG_INTERVAL_MS = 10_000L;
    private static final Map<String, Long> LAST_DEBUG_LOG = new ConcurrentHashMap<>();

    private static volatile boolean initialized;
    private static Method blockFrontGetInstance;
    private static Method blockFrontGetManager;
    private static Method managerGetPlayerDataHandler;
    private static Method dataHandlerGetPlayerData;
    private static Method playerDataGetAfkTracker;
    private static Method afkTrackerPlayerMoved;

    private AfkCompat() {
    }

    public static boolean markPlayerMoved(ServerPlayerEntity player, String source) {
        if (player == null) {
            return false;
        }
        if (!initialize()) {
            return false;
        }
        try {
            Object blockFront = blockFrontGetInstance.invoke(null);
            if (blockFront == null) {
                return false;
            }
            Object manager = blockFrontGetManager.invoke(blockFront);
            if (manager == null) {
                return false;
            }
            Object dataHandler = managerGetPlayerDataHandler.invoke(manager);
            if (dataHandler == null) {
                return false;
            }
            Object playerData = dataHandlerGetPlayerData.invoke(dataHandler, player);
            if (playerData == null) {
                return false;
            }
            Object afkTracker = playerDataGetAfkTracker.invoke(playerData);
            if (afkTracker == null) {
                return false;
            }

            afkTrackerPlayerMoved.invoke(afkTracker);
            debug(player.getUuid(), player.getNameForScoreboard(), source, "AFK mark move packet");
            return true;
        } catch (Throwable t) {
            debug(player.getUuid(), player.getNameForScoreboard(), source, "AFK mark failed: " + t.getClass().getSimpleName());
            return false;
        }
    }

    private static boolean initialize() {
        if (initialized) {
            return true;
        }
        synchronized (AfkCompat.class) {
            if (initialized) {
                return true;
            }
            try {
                Class<?> blockFrontClass = Class.forName("com.boehmod.blockfront.BlockFront");
                blockFrontGetInstance = blockFrontClass.getMethod("getInstance");
                blockFrontGetManager = blockFrontClass.getMethod("getManager");
                managerGetPlayerDataHandler = blockFrontGetManager.getReturnType().getMethod("getPlayerDataHandler");
                dataHandlerGetPlayerData = managerGetPlayerDataHandler.getReturnType()
                    .getMethod("getPlayerData", PlayerEntity.class);
                playerDataGetAfkTracker = dataHandlerGetPlayerData.getReturnType().getMethod("getAfkTracker");
                afkTrackerPlayerMoved = playerDataGetAfkTracker.getReturnType().getMethod("playerMoved");
                initialized = true;
                return true;
            } catch (Throwable t) {
                AddonConstants.LOGGER.warn("[mercfrontcore] AFK compat init failed: {}", t.getClass().getSimpleName());
                return false;
            }
        }
    }

    private static void debug(UUID playerId, String playerName, String source, String message) {
        String key = playerId + ":" + source;
        long now = System.currentTimeMillis();
        Long prev = LAST_DEBUG_LOG.get(key);
        if (prev != null && now - prev < DEBUG_LOG_INTERVAL_MS) {
            return;
        }
        LAST_DEBUG_LOG.put(key, now);
        AddonConstants.LOGGER.warn("[mercfrontcore][afk] {} via {} for {}", message, source, playerName);
    }
}
