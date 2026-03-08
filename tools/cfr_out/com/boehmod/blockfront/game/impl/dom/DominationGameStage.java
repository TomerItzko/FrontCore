/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.dom;

import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import com.boehmod.blockfront.game.impl.dom.DominationPostStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class DominationGameStage
extends AbstractGameStage<DominationGame, DominationPlayerManager>
implements TimedStage<DominationGame, DominationPlayerManager> {
    private boolean field_3412 = false;
    private boolean field_3413 = false;
    private boolean field_3414 = false;
    private boolean field_3415 = false;
    @NotNull
    private final GameStageTimer timer = new GameStageTimer(20, 0).warningTime(20);

    @Override
    public void stageStarted(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1538(BFMusicType.START).method_1536(5));
    }

    @Override
    public void onSecondPassed(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        boolean bl;
        this.timer.update(set);
        GameTeam gameTeam = dominationPlayerManager.getTeamByName("Axis");
        assert (gameTeam != null);
        int n = gameTeam.getObjectInt(BFStats.SCORE);
        GameTeam gameTeam2 = dominationPlayerManager.getTeamByName("Allies");
        assert (gameTeam2 != null);
        int n2 = gameTeam2.getObjectInt(BFStats.SCORE);
        BFUtils.method_2977(playerDataHandler, set);
        dominationGame.method_3325().forEach(ammoPoint -> ammoPoint.update(serverLevel, set));
        dominationGame.method_3333(bFAbstractManager, playerDataHandler, serverLevel, set);
        int n3 = 440;
        int n4 = 460;
        int n5 = 480;
        int n6 = this.timer.getSecondsRemaining();
        boolean bl2 = n >= 440 || n2 >= 440 || n6 <= 120;
        boolean bl3 = n >= 460 || n2 >= 460 || n6 <= 60;
        boolean bl4 = bl = n >= 480 || n2 >= 480 || n6 <= 30;
        if (bl2 && !this.field_3413) {
            this.field_3413 = true;
            BFUtils.queueMusic(set, GameMusic.create().method_1541(BFMusicType.SUSPENSE_ONE));
        }
        if (bl3 && !this.field_3414) {
            this.field_3414 = true;
            BFUtils.queueMusic(set, GameMusic.create().method_1541(BFMusicType.SUSPENSE_TWO));
        }
        if (bl && !this.field_3415) {
            this.field_3415 = true;
            BFUtils.queueMusic(set, GameMusic.create().method_1541(BFMusicType.SUSPENSE_THREE));
        }
        if (!this.field_3412) {
            int n7 = 250;
            if (n >= 250) {
                this.field_3412 = true;
                DivisionData divisionData = gameTeam.getDivisionData(dominationGame);
                MutableComponent mutableComponent = Component.literal((String)divisionData.getCountry().getName()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.winning", (Object[])new Object[]{mutableComponent}).withStyle(gameTeam.getStyleText());
                BFUtils.sendFancyMessage(set, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent2);
            } else if (n2 >= 250) {
                this.field_3412 = true;
                DivisionData divisionData = gameTeam2.getDivisionData(dominationGame);
                MutableComponent mutableComponent = Component.literal((String)divisionData.getCountry().getName()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.winning", (Object[])new Object[]{mutableComponent}).withStyle(gameTeam2.getStyleText());
                BFUtils.sendFancyMessage(set, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent3);
            }
        }
    }

    @Override
    public boolean isStageOver(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        WinningTeamData winningTeamData = dominationPlayerManager.getWinningTeam(serverLevel, set, this.timer);
        return winningTeamData != null;
    }

    @Override
    public void stageEnded(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void method_3911(@NotNull DominationGame dominationGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<DominationGame, DominationPlayerManager> createNextStage(@NotNull DominationGame dominationGame) {
        return new DominationPostStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull DominationGame dominationGame, @NotNull UUID uUID) {
        if (teamJoinType == TeamJoinType.NEW) {
            BFUtils.queueMusic(uUID, GameMusic.create().method_1540(BFMusicType.START_GENERIC).method_1536(5));
        }
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull DominationGame dominationGame) {
        return this.timer;
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((DominationGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((DominationGame)game, (DominationPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((DominationGame)game, (DominationPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

