/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.PreGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGameStage;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class TeamDeathmatchPreStage
extends PreGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> {
    public static final int field_3458 = 20;

    public TeamDeathmatchPreStage() {
        super(20);
    }

    @Override
    public void stageStarted(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1541(BFMusicType.WARMUP));
    }

    @Override
    public void stageEnded(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.method_2977(playerDataHandler, set);
        GameTeam gameTeam = teamDeathmatchPlayerManager.getTeamByName("Axis");
        GameTeam gameTeam2 = teamDeathmatchPlayerManager.getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        gameTeam.teleportPlayersToSpawns(playerDataHandler, teamDeathmatchGame);
        gameTeam2.teleportPlayersToSpawns(playerDataHandler, teamDeathmatchGame);
        BFUtils.method_2944(serverLevel, teamDeathmatchGame, set);
        BFUtils.initPlayersForGame(serverLevel, set, playerDataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> createNextStage(@NotNull TeamDeathmatchGame teamDeathmatchGame) {
        return new TeamDeathmatchGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.PRE_GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull UUID uUID) {
        BFUtils.queueMusic(uUID, GameMusic.create().method_1541(BFMusicType.WARMUP));
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

