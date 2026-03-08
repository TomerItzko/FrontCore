/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.PreGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestGameStage;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BotVoice;
import com.boehmod.blockfront.unnamed.BF_797;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class ConquestPreStage
extends PreGameStage<ConquestGame, ConquestPlayerManager> {
    public static final int field_3387 = 45;

    public ConquestPreStage() {
        super(45);
    }

    @Override
    public void stageStarted(@NotNull ConquestGame conquestGame, @NotNull ConquestPlayerManager conquestPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        for (GameTeam gameTeam : conquestPlayerManager.getTeams()) {
            gameTeam.setObject(BF_797.field_3381, 500);
        }
    }

    @Override
    public void stageEnded(@NotNull ConquestGame conquestGame, @NotNull ConquestPlayerManager conquestPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        FDSPose fDSPose;
        BFUtils.method_2977(playerDataHandler, set);
        GameTeam gameTeam = conquestPlayerManager.getTeamByName("Axis");
        GameTeam gameTeam2 = conquestPlayerManager.getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        gameTeam.teleportPlayersToSpawns(playerDataHandler, conquestGame);
        gameTeam2.teleportPlayersToSpawns(playerDataHandler, conquestGame);
        conquestGame.method_3394(serverLevel);
        BFUtils.method_2944(serverLevel, conquestGame, set);
        BFUtils.initPlayersForGame(serverLevel, set, playerDataHandler);
        BlockPos blockPos = gameTeam2.getPlayerSpawns().getFirst().asBlockPos();
        DeferredHolder<BotVoice, ? extends BotVoice> deferredHolder = gameTeam2.getDivisionData(conquestGame).getCountry().getRandomBotVoice();
        if (deferredHolder != null) {
            serverLevel.playSound(null, blockPos, (SoundEvent)((BotVoice)deferredHolder.get()).introSound().get(), SoundSource.PLAYERS, 5.0f, 1.0f);
        }
        BlockPos blockPos2 = gameTeam.getPlayerSpawns().getFirst().asBlockPos();
        DeferredHolder<BotVoice, ? extends BotVoice> deferredHolder2 = gameTeam.getDivisionData(conquestGame).getCountry().getRandomBotVoice();
        if (deferredHolder2 != null) {
            serverLevel.playSound(null, blockPos2, (SoundEvent)((BotVoice)deferredHolder2.get()).introSound().get(), SoundSource.PLAYERS, 5.0f, 1.0f);
        }
        if ((fDSPose = conquestPlayerManager.getLobbySpawn()) != null) {
            serverLevel.playSound(null, fDSPose.asBlockPos(), (SoundEvent)BFSounds.AMBIENT_LSP_HUMAN_WHISTLE.get(), SoundSource.AMBIENT, 30.0f, 1.0f);
        }
    }

    @Override
    @NotNull
    public AbstractGameStage<ConquestGame, ConquestPlayerManager> createNextStage(@NotNull ConquestGame conquestGame) {
        return new ConquestGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.PRE_GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull ConquestGame conquestGame, @NotNull UUID uUID) {
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

