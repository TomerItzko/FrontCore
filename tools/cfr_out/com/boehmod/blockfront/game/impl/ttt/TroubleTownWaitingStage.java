/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.GameType
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt;

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
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGameStage;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;

public class TroubleTownWaitingStage
extends AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager>
implements TimedStage<TroubleTownGame, TroubleTownPlayerManager> {
    private final GameStageTimer field_3652 = new GameStageTimer(0, 15).warningTime(5);

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull TroubleTownGame troubleTownGame, @NotNull UUID uUID) {
        serverPlayer.setGameMode(GameType.SPECTATOR);
    }

    @Override
    public void stageStarted(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        troubleTownGame.field_3468.clear();
        BFUtils.method_2996(set);
        BFUtils.method_2977(playerDataHandler, set);
        troubleTownPlayerManager.method_3539(playerDataHandler);
        BFUtils.initPlayersForGame(serverLevel, set, playerDataHandler);
        BFUtils.clearInventories(set);
        for (UUID uUID : set) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer != null) {
                BFUtils.teleportPlayer(playerDataHandler, serverPlayer, troubleTownPlayerManager.method_3541());
                serverPlayer.setGameMode(GameType.ADVENTURE);
            }
            troubleTownGame.setRoleStat(uUID, TTTPlayerRole.PENDING);
            BFUtils.setPlayerStat(troubleTownGame, uUID, BFStats.POINTS, 0);
        }
        troubleTownGame.method_3526(serverLevel);
    }

    @Override
    public void onSecondPassed(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        if (troubleTownGame.hasMinimumPlayers(set)) {
            this.field_3652.update(set);
        } else {
            this.field_3652.restart();
        }
        if (this.field_3652.getSecondsRemaining() <= 5) {
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(this.field_3652.getSecondsRemaining())).withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ttt.role.reminder", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GRAY);
            BFUtils.sendNoticeMessage(set, (Component)mutableComponent2);
        }
    }

    @Override
    public boolean isStageOver(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        return !this.field_3652.isRunning();
    }

    @Override
    public void stageEnded(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void method_3911(@NotNull TroubleTownGame troubleTownGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager> createNextStage(@NotNull TroubleTownGame troubleTownGame) {
        return new TroubleTownGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull TroubleTownGame troubleTownGame) {
        return this.field_3652;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((TroubleTownGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((TroubleTownGame)game, (TroubleTownPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((TroubleTownGame)game, (TroubleTownPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

