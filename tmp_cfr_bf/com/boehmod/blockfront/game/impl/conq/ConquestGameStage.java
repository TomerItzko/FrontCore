/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import com.boehmod.blockfront.game.impl.conq.ConquestPostStage;
import com.boehmod.blockfront.unnamed.BF_797;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ConquestGameStage
extends AbstractGameStage<ConquestGame, ConquestPlayerManager>
implements TimedStage<ConquestGame, ConquestPlayerManager> {
    private boolean field_3384 = false;
    @NotNull
    private final GameStageTimer field_3383 = new GameStageTimer(20, 0).warningTime(20);
    private int field_3385 = 0;

    @Override
    public void stageStarted(@NotNull ConquestGame conquestGame, @NotNull ConquestPlayerManager conquestPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void onSecondPassed(@NotNull ConquestGame conquestGame, @NotNull ConquestPlayerManager conquestPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.field_3383.update(set);
        GameTeam gameTeam = conquestPlayerManager.getTeamByName("Axis");
        int n = gameTeam.getObjectInt(BF_797.field_3381);
        GameTeam gameTeam2 = conquestPlayerManager.getTeamByName("Allies");
        int n2 = gameTeam2.getObjectInt(BF_797.field_3381);
        BFUtils.method_2977(playerDataHandler, set);
        conquestGame.method_3271().forEach(ammoPoint -> ammoPoint.update(serverLevel, set));
        conquestGame.getVehicleSpawns().forEach(vehicleSpawn -> vehicleSpawn.method_3189(conquestGame, (Level)serverLevel));
        for (ConquestCapturePoint object : conquestGame.getCapturePoints()) {
            object.onUpdate(bFAbstractManager, playerDataHandler, conquestGame, serverLevel, set);
        }
        if (this.field_3385-- <= 0) {
            this.field_3385 = 5;
            conquestGame.method_3272();
        }
        if (!this.field_3384) {
            int n3 = 250;
            if (n2 <= 250) {
                this.field_3384 = true;
                DivisionData divisionData = gameTeam.getDivisionData(conquestGame);
                MutableComponent mutableComponent = Component.literal((String)divisionData.getCountry().getName()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.winning", (Object[])new Object[]{mutableComponent}).withStyle(gameTeam.getStyleText());
                BFUtils.sendFancyMessage(set, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent2);
            } else if (n <= 250) {
                this.field_3384 = true;
                DivisionData divisionData = gameTeam2.getDivisionData(conquestGame);
                MutableComponent mutableComponent = Component.literal((String)divisionData.getCountry().getName()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.winning", (Object[])new Object[]{mutableComponent}).withStyle(gameTeam2.getStyleText());
                BFUtils.sendFancyMessage(set, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent3);
            }
        }
    }

    @Override
    public boolean isStageOver(@NotNull ConquestGame conquestGame, @NotNull ConquestPlayerManager conquestPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        GameTeam gameTeam = conquestPlayerManager.getTeamByName("Axis");
        int n = gameTeam.getObjectInt(BF_797.field_3381, 500);
        GameTeam gameTeam2 = conquestPlayerManager.getTeamByName("Allies");
        int n2 = gameTeam2.getObjectInt(BF_797.field_3381, 500);
        return set.isEmpty() || !this.field_3383.isRunning() || n <= 0 || n2 <= 0;
    }

    @Override
    public void stageEnded(@NotNull ConquestGame conquestGame, @NotNull ConquestPlayerManager conquestPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        WinningTeamData winningTeamData = conquestPlayerManager.getWinningTeam(serverLevel, set, this.field_3383);
        if (winningTeamData != null) {
            conquestGame.finishMatch(serverLevel, bFAbstractManager, BFStats.SCORE, true, winningTeamData, set);
        }
    }

    @Override
    public void method_3911(@NotNull ConquestGame conquestGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<ConquestGame, ConquestPlayerManager> createNextStage(@NotNull ConquestGame conquestGame) {
        return new ConquestPostStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull ConquestGame conquestGame, @NotNull UUID uUID) {
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull ConquestGame conquestGame) {
        return this.field_3383;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((ConquestGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((ConquestGame)game, (ConquestPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((ConquestGame)game, (ConquestPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

