/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.PostGameStage;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownIdleStage;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class TroubleTownPostStage
extends PostGameStage<TroubleTownGame, TroubleTownPlayerManager>
implements TimedStage<TroubleTownGame, TroubleTownPlayerManager> {
    @Override
    public void stageStarted(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        super.stageStarted(troubleTownGame, troubleTownPlayerManager, bFAbstractManager, playerDataHandler, serverLevel, set);
        WinningTeamData winningTeamData = troubleTownPlayerManager.getWinningTeam(serverLevel, set, null);
        troubleTownGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
    }

    @Override
    public void method_3911(@NotNull TroubleTownGame troubleTownGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager> createNextStage(@NotNull TroubleTownGame troubleTownGame) {
        return new TroubleTownIdleStage();
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((TroubleTownGame)game, (TroubleTownPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((TroubleTownGame)abstractGame, serverLevel, (Set<UUID>)set);
    }
}

