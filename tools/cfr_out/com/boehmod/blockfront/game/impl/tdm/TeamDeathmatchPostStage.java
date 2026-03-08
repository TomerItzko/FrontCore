/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.PostGameStage;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchIdleStage;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class TeamDeathmatchPostStage
extends PostGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager>
implements TimedStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> {
    @Override
    public void stageStarted(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull TeamDeathmatchPlayerManager teamDeathmatchPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        super.stageStarted(teamDeathmatchGame, teamDeathmatchPlayerManager, bFAbstractManager, playerDataHandler, serverLevel, set);
        WinningTeamData winningTeamData = teamDeathmatchPlayerManager.getWinningTeam(serverLevel, set, null);
        if (winningTeamData != null) {
            teamDeathmatchGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
            BFUtils.queueMusic(set, GameMusic.create().method_1540(BFMusicType.CONCLUDE_GENERIC).method_1536(5));
        }
    }

    @Override
    public void method_3911(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> createNextStage(@NotNull TeamDeathmatchGame teamDeathmatchGame) {
        return new TeamDeathmatchIdleStage();
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((TeamDeathmatchGame)game, (TeamDeathmatchPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((TeamDeathmatchGame)abstractGame, serverLevel, (Set<UUID>)set);
    }
}

