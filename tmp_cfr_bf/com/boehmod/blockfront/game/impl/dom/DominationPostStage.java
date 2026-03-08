/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.dom;

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
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationIdleStage;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class DominationPostStage
extends PostGameStage<DominationGame, DominationPlayerManager>
implements TimedStage<DominationGame, DominationPlayerManager> {
    @Override
    public void stageStarted(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        super.stageStarted(dominationGame, dominationPlayerManager, bFAbstractManager, playerDataHandler, serverLevel, set);
        WinningTeamData winningTeamData = dominationPlayerManager.getWinningTeam(serverLevel, set, null);
        if (winningTeamData != null) {
            dominationGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
        }
        BFUtils.queueMusic(set, GameMusic.create().method_1538(BFMusicType.CONCLUDE));
    }

    @Override
    public void method_3911(@NotNull DominationGame dominationGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<DominationGame, DominationPlayerManager> createNextStage(@NotNull DominationGame dominationGame) {
        return new DominationIdleStage();
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((DominationGame)game, (DominationPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((DominationGame)abstractGame, serverLevel, (Set<UUID>)set);
    }
}

