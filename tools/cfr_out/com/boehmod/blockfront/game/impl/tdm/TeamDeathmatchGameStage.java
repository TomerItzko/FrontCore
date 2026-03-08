/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPostStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeamDeathmatchGameStage
extends AbstractGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager>
implements TimedStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> {
    @NotNull
    private final GameStageTimer field_3455 = new GameStageTimer(10, 0).warningTime(20);
    private static final int field_3456 = 10;

    @Override
    public void stageStarted(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
    }

    @Override
    public void onSecondPassed(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.field_3455.update(set);
    }

    @Override
    public boolean isStageOver(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        WinningTeamData winningTeamData = teamDeathmatchPlayerManager.getWinningTeam(serverLevel, set, this.field_3455);
        return winningTeamData != null;
    }

    @Override
    public void stageEnded(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void method_3911(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> createNextStage(@NotNull TeamDeathmatchGame teamDeathmatchGame) {
        return new TeamDeathmatchPostStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull UUID uUID) {
        BFUtils.queueMusic(uUID, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
    }

    @Override
    @Nullable
    public GameStageTimer getStageTimer(@NotNull TeamDeathmatchGame teamDeathmatchGame) {
        return this.field_3455;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((TeamDeathmatchGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((TeamDeathmatchGame)game, (TeamDeathmatchPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((TeamDeathmatchGame)game, (TeamDeathmatchPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

