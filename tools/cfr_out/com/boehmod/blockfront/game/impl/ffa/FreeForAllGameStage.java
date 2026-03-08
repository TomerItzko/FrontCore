/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ffa;

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
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGame;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllPlayerManager;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllPostStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class FreeForAllGameStage
extends AbstractGameStage<FreeForAllGame, FreeForAllPlayerManager>
implements TimedStage<FreeForAllGame, FreeForAllPlayerManager> {
    private static final int field_3494 = 10;
    @NotNull
    private final GameStageTimer field_3493 = new GameStageTimer(10, 0).warningTime(20);

    @Override
    public void stageStarted(@NotNull FreeForAllGame freeForAllGame, @NotNull FreeForAllPlayerManager freeForAllPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
    }

    @Override
    public void onSecondPassed(@NotNull FreeForAllGame freeForAllGame, @NotNull FreeForAllPlayerManager freeForAllPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.field_3493.update(set);
    }

    @Override
    public boolean isStageOver(@NotNull FreeForAllGame freeForAllGame, @NotNull FreeForAllPlayerManager freeForAllPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        WinningTeamData winningTeamData = freeForAllPlayerManager.getWinningTeam(serverLevel, set, this.field_3493);
        return winningTeamData != null;
    }

    @Override
    public void stageEnded(@NotNull FreeForAllGame freeForAllGame, @NotNull FreeForAllPlayerManager freeForAllPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.method_2870(playerDataHandler, set);
    }

    @Override
    public void method_3911(@NotNull FreeForAllGame freeForAllGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<FreeForAllGame, FreeForAllPlayerManager> createNextStage(@NotNull FreeForAllGame freeForAllGame) {
        return new FreeForAllPostStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull FreeForAllGame freeForAllGame, @NotNull UUID uUID) {
        BFUtils.queueMusic(uUID, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull FreeForAllGame freeForAllGame) {
        return this.field_3493;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((FreeForAllGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((FreeForAllGame)game, (FreeForAllPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((FreeForAllGame)game, (FreeForAllPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

