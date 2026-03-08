/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class PostGameStage<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends AbstractGameStage<G, P>
implements TimedStage<G, P> {
    @NotNull
    public static final Component field_3773 = Component.translatable((String)"bf.message.disconnect.mm.restarting");
    @NotNull
    public GameStageTimer field_3772 = new GameStageTimer(1, 0).warningTime(15);

    @Override
    public void stageStarted(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        ((AbstractGame)game).getMapVoteManager().startVoteSequence(modManager);
        BFUtils.method_2870(dataHandler, players);
    }

    @Override
    public void onSecondPassed(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.field_3772.update(players);
    }

    @Override
    public boolean isStageOver(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        return players.isEmpty() || !this.field_3772.isRunning();
    }

    @Override
    public void stageEnded(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        BFUtils.method_2977(dataHandler, players);
        if (!((AbstractGame)game).getMapVoteManager().method_3716(modManager, (AbstractGamePlayerManager<?>)playerManager, level)) {
            BFUtils.method_2919(players, field_3773);
        }
        ((AbstractGame)game).reset(level);
        ((AbstractGame)game).stopMatchMaking(modManager);
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> dataHandler, @NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull G game, @NotNull UUID gameId) {
        BFUtils.method_2876(dataHandler, player);
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.POST_GAME;
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull G game) {
        return this.field_3772;
    }
}

