package dev.tomerdev.mercfrontcore.util;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.server.BFServerManager;
import net.minecraft.server.network.ServerPlayerEntity;

public final class MatchCompat {
    private MatchCompat() {
    }

    public static boolean isInGameSession(ServerPlayerEntity player) {
        if (player == null) {
            return false;
        }
        try {
            BlockFront blockFront = BlockFront.getInstance();
            if (blockFront == null) {
                return false;
            }
            BFAbstractManager<?, ?, ?> abstractManager = blockFront.getManager();
            if (!(abstractManager instanceof BFServerManager manager)) {
                return false;
            }

            AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(player.getUuid());
            return game != null;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isInActiveMatch(ServerPlayerEntity player) {
        return isInGameSession(player);
    }
}
