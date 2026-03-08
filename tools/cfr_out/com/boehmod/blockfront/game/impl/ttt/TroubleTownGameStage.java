/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.level.Level
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
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerTeam;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPostStage;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownWaitingStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TroubleTownGameStage
extends AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager>
implements TimedStage<TroubleTownGame, TroubleTownPlayerManager> {
    private final GameStageTimer field_3650 = new GameStageTimer(5, 0).warningTime(15);
    private final GameStageTimer field_3651 = new GameStageTimer(0, 15).warningTime(5);
    private boolean field_3649 = false;

    @Override
    public void stageStarted(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        for (UUID uUID : set) {
            BFUtils.setPlayerStat(troubleTownGame, uUID, BFStats.POINTS, 0);
        }
        troubleTownGame.assignRoles(serverLevel, set);
    }

    @Override
    public void onSecondPassed(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.field_3650.update(set);
        if (!this.field_3649) {
            TTTPlayerTeam tTTPlayerTeam = troubleTownGame.method_3514();
            if (!this.field_3650.isRunning()) {
                tTTPlayerTeam = TTTPlayerTeam.GOOD;
            }
            if (tTTPlayerTeam != null) {
                this.method_3787(bFAbstractManager, troubleTownPlayerManager, troubleTownGame, tTTPlayerTeam, set, serverLevel);
                this.field_3649 = true;
            }
        } else {
            this.field_3651.update(set);
        }
    }

    @Override
    public boolean isStageOver(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        return !this.field_3651.isRunning();
    }

    @Override
    public void stageEnded(@NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        troubleTownGame.method_3521((Level)serverLevel, set);
        BFUtils.discardMatchEntities(serverLevel, troubleTownGame, troubleTownPlayerManager);
        ++troubleTownGame.field_3469;
    }

    @Override
    public void method_3911(@NotNull TroubleTownGame troubleTownGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager> createNextStage(@NotNull TroubleTownGame troubleTownGame) {
        return troubleTownGame.field_3469 >= 10 ? new TroubleTownPostStage() : new TroubleTownWaitingStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull TroubleTownGame troubleTownGame, @NotNull UUID uUID) {
    }

    @Override
    public GameStageTimer getStageTimer(@NotNull TroubleTownGame troubleTownGame) {
        return this.field_3649 ? this.field_3651 : this.field_3650;
    }

    public void method_3787(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull TroubleTownGame troubleTownGame, @NotNull TTTPlayerTeam tTTPlayerTeam, @NotNull Set<UUID> set, @NotNull ServerLevel serverLevel) {
        BFUtils.discardMatchEntities(serverLevel, troubleTownGame, troubleTownPlayerManager);
        BFUtils.playSound(set, (SoundEvent)tTTPlayerTeam.getWinSound().get(), SoundSource.MUSIC);
        BFUtils.sendFancyMessage(set, (Component)Component.empty(), (Component)Component.empty());
        BFUtils.sendFancyMessage(set, (Component)Component.empty(), "/c " + String.valueOf(ChatFormatting.WHITE) + String.valueOf(ChatFormatting.BOLD) + troubleTownGame.getDisplayName().getString());
        int n = tTTPlayerTeam.getColor();
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)n));
        BFUtils.sendFancyMessage(set, (Component)Component.empty(), (Component)Component.literal((String)("/c " + tTTPlayerTeam.getName() + " Won!")).setStyle(style).withStyle(ChatFormatting.BOLD));
        BFUtils.sendFancyMessage(set, (Component)Component.empty(), (Component)Component.empty());
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

