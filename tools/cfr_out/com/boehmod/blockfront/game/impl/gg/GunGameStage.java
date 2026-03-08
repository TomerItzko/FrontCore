/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.gg;

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
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import com.boehmod.blockfront.game.impl.gg.GunGamePostStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class GunGameStage
extends AbstractGameStage<GunGame, GunGamePlayerManager>
implements TimedStage<GunGame, GunGamePlayerManager> {
    public static final int field_3519 = 15;
    @NotNull
    private final GameStageTimer field_3518 = new GameStageTimer(15, 0).warningTime(20);

    @Override
    public void stageStarted(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
    }

    @Override
    public void onSecondPassed(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.field_3518.update(set);
        BFUtils.method_2977(playerDataHandler, set);
    }

    @Override
    public boolean isStageOver(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        WinningTeamData winningTeamData = gunGamePlayerManager.getWinningTeam(serverLevel, set, this.field_3518);
        return winningTeamData != null;
    }

    @Override
    public void stageEnded(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void method_3911(@NotNull GunGame gunGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<GunGame, GunGamePlayerManager> createNextStage(@NotNull GunGame gunGame) {
        return new GunGamePostStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull GunGame gunGame, @NotNull UUID uUID) {
        BFUtils.queueMusic(uUID, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull GunGame gunGame) {
        return this.field_3518;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((GunGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((GunGame)game, (GunGamePlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((GunGame)game, (GunGamePlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

