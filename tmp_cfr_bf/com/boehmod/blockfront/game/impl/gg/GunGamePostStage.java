/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.gg;

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
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.gg.GunGameIdleStage;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class GunGamePostStage
extends PostGameStage<GunGame, GunGamePlayerManager>
implements TimedStage<GunGame, GunGamePlayerManager> {
    @Override
    public void stageStarted(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        super.stageStarted(gunGame, gunGamePlayerManager, bFAbstractManager, playerDataHandler, serverLevel, set);
        WinningTeamData winningTeamData = gunGamePlayerManager.getWinningTeam(serverLevel, set, null);
        if (winningTeamData != null) {
            gunGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
            BFUtils.queueMusic(set, GameMusic.create().method_1540(BFMusicType.CONCLUDE_GENERIC).method_1536(5));
        }
    }

    @Override
    public void method_3911(@NotNull GunGame gunGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<GunGame, GunGamePlayerManager> createNextStage(@NotNull GunGame gunGame) {
        return new GunGameIdleStage();
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((GunGame)game, (GunGamePlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((GunGame)abstractGame, serverLevel, (Set<UUID>)set);
    }
}

