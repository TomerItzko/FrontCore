/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.dom;

import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.PreGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationGameStage;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BotVoice;
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

public class DominationPreStage
extends PreGameStage<DominationGame, DominationPlayerManager> {
    public static final int field_3487 = 45;

    public DominationPreStage() {
        super(45);
    }

    @Override
    public void stageStarted(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        for (UUID uUID : set) {
            BFUtils.queueMusic(uUID, GameMusic.create().method_1541(BFMusicType.WARMUP));
        }
    }

    @Override
    public void onSecondPassed(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        super.onSecondPassed(dominationGame, dominationPlayerManager, bFAbstractManager, playerDataHandler, serverLevel, set);
    }

    @Override
    public void stageEnded(@NotNull DominationGame dominationGame, @NotNull DominationPlayerManager dominationPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        FDSPose fDSPose;
        BFUtils.method_2977(playerDataHandler, set);
        GameTeam gameTeam = dominationPlayerManager.getTeamByName("Axis");
        GameTeam gameTeam2 = dominationPlayerManager.getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        gameTeam.teleportPlayersToSpawns(playerDataHandler, dominationGame);
        gameTeam2.teleportPlayersToSpawns(playerDataHandler, dominationGame);
        dominationGame.method_3394(serverLevel);
        BFUtils.method_2944(serverLevel, dominationGame, set);
        BFUtils.initPlayersForGame(serverLevel, set, playerDataHandler);
        DeferredHolder<BotVoice, ? extends BotVoice> deferredHolder = gameTeam2.getDivisionData(dominationGame).getCountry().getRandomBotVoice();
        DeferredHolder<BotVoice, ? extends BotVoice> deferredHolder2 = gameTeam.getDivisionData(dominationGame).getCountry().getRandomBotVoice();
        BlockPos blockPos = gameTeam2.getPlayerSpawns().getFirst().asBlockPos();
        BlockPos blockPos2 = gameTeam.getPlayerSpawns().getFirst().asBlockPos();
        if (deferredHolder != null) {
            serverLevel.playSound(null, blockPos, (SoundEvent)((BotVoice)deferredHolder.get()).introSound().get(), SoundSource.PLAYERS, 5.0f, 1.0f);
        }
        if (deferredHolder2 != null) {
            serverLevel.playSound(null, blockPos2, (SoundEvent)((BotVoice)deferredHolder2.get()).introSound().get(), SoundSource.PLAYERS, 5.0f, 1.0f);
        }
        if ((fDSPose = dominationPlayerManager.getLobbySpawn()) != null) {
            serverLevel.playSound(null, fDSPose.asBlockPos(), (SoundEvent)BFSounds.AMBIENT_LSP_HUMAN_WHISTLE.get(), SoundSource.AMBIENT, 30.0f, 1.0f);
        }
    }

    @Override
    @NotNull
    public AbstractGameStage<DominationGame, DominationPlayerManager> createNextStage(@NotNull DominationGame dominationGame) {
        return new DominationGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.PRE_GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull DominationGame dominationGame, @NotNull UUID uUID) {
        if (teamJoinType == TeamJoinType.NEW) {
            BFUtils.queueMusic(uUID, GameMusic.create().method_1541(BFMusicType.WARMUP));
        }
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

