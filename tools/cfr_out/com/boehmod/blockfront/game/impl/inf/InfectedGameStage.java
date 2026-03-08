/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.game.impl.inf.InfectedPostStage;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class InfectedGameStage
extends AbstractGameStage<InfectedGame, InfectedPlayerManager>
implements TimedStage<InfectedGame, InfectedPlayerManager> {
    @NotNull
    private final GameStageTimer field_3447 = new GameStageTimer(0, 25).warningTime(10);

    @Override
    public void stageStarted(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void onSecondPassed(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        infectedGame.getZone("main").ifPresent(zombieZone -> zombieZone.setActive(true));
        BFUtils.method_2977(playerDataHandler, set);
        if (!infectedGame.method_3639(serverLevel) && infectedGame.method_3624(serverLevel).isEmpty()) {
            this.field_3447.update(set);
            if (!this.field_3447.isRunning()) {
                infectedGame.advanceRound(serverLevel, set);
                this.field_3447.restart();
            }
        }
    }

    @Override
    public boolean isStageOver(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        WinningTeamData winningTeamData = infectedPlayerManager.getWinningTeam(serverLevel, set, this.field_3447);
        return winningTeamData != null;
    }

    @Override
    public void stageEnded(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.playSound(set, (SoundEvent)BFSounds.MATCH_GAMEMODE_INF_GAMEOVER.get(), SoundSource.MASTER);
        WinningTeamData winningTeamData = infectedPlayerManager.getWinningTeam(serverLevel, set, this.field_3447);
        if (winningTeamData != null) {
            infectedGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
            if (winningTeamData.topPlayers != null) {
                for (UUID uUID : winningTeamData.topPlayers) {
                    BFUtils.incrementPlayerStat(bFAbstractManager, infectedGame, uUID, BFStats.INFECTED_MATCHES_WON);
                }
            }
        }
    }

    @Override
    public void method_3911(@NotNull InfectedGame infectedGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<InfectedGame, InfectedPlayerManager> createNextStage(@NotNull InfectedGame infectedGame) {
        return new InfectedPostStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull InfectedGame infectedGame, @NotNull UUID uUID) {
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull InfectedGame infectedGame) {
        return this.field_3447;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((InfectedGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((InfectedGame)game, (InfectedPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((InfectedGame)game, (InfectedPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

