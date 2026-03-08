/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStatus;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public abstract class IdleGameStage<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends AbstractGameStage<G, P> {
    @Override
    public void stageStarted(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
    }

    @Override
    public void onSecondPassed(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
    }

    @Override
    public boolean isStageOver(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        return !players.isEmpty();
    }

    @Override
    public void stageEnded(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
    }

    @Override
    public void method_3911(@NotNull G g, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.IDLE;
    }
}

