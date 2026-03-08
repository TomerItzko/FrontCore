/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.PreGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedGameStage;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class InfectedPreStage
extends PreGameStage<InfectedGame, InfectedPlayerManager> {
    public static final int field_3448 = 20;

    public InfectedPreStage() {
        super(20);
    }

    @Override
    public void stageStarted(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        infectedGame.doors.forEach(infectedDoor -> infectedDoor.method_3697((Level)serverLevel, false));
        BFUtils.discardMatchEntities(serverLevel, infectedGame, infectedPlayerManager);
        infectedGame.refreshShopItems();
        infectedGame.relocateVendor(serverLevel, infectedGame.vendorSpawns.getFirst());
    }

    @Override
    public void stageEnded(@NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.method_2977(playerDataHandler, set);
        BFUtils.playSound(set, (SoundEvent)infectedGame.waveType.getRoundStartSound().get(), SoundSource.MUSIC);
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(infectedGame.currentRound)).withStyle(ChatFormatting.RED);
        BFUtils.sendNoticeMessage(set, (Component)Component.translatable((String)"bf.message.gamemode.infected.popup.wave", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD));
        BFUtils.initPlayersForGame(serverLevel, set, playerDataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<InfectedGame, InfectedPlayerManager> createNextStage(@NotNull InfectedGame infectedGame) {
        return new InfectedGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.PRE_GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull InfectedGame infectedGame, @NotNull UUID uUID) {
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((InfectedGame)game, (InfectedPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((InfectedGame)game, (InfectedPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

