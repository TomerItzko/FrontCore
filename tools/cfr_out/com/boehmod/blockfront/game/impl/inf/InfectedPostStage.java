/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.PostGameStage;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedIdleStage;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class InfectedPostStage
extends PostGameStage<InfectedGame, InfectedPlayerManager>
implements TimedStage<InfectedGame, InfectedPlayerManager> {
    @Override
    public void stageStarted(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        super.stageStarted(infectedGame, infectedPlayerManager, bFAbstractManager, playerDataHandler, serverLevel, set);
        WinningTeamData winningTeamData = infectedPlayerManager.getWinningTeam(serverLevel, set, null);
        if (winningTeamData != null) {
            infectedGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
        }
    }

    @Override
    public void method_3911(@NotNull InfectedGame infectedGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<InfectedGame, InfectedPlayerManager> createNextStage(@NotNull InfectedGame infectedGame) {
        return new InfectedIdleStage();
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((InfectedGame)game, (InfectedPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((InfectedGame)abstractGame, serverLevel, (Set<UUID>)set);
    }
}

