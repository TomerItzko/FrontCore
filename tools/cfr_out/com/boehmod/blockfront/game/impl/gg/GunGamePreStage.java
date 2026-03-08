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
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.PreGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import com.boehmod.blockfront.game.impl.gg.GunGameStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class GunGamePreStage
extends PreGameStage<GunGame, GunGamePlayerManager> {
    public static final int field_3521 = 20;

    public GunGamePreStage() {
        super(20);
    }

    @Override
    public void stageStarted(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1541(BFMusicType.WARMUP));
    }

    @Override
    public void stageEnded(@NotNull GunGame gunGame, @NotNull GunGamePlayerManager gunGamePlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.method_2977(playerDataHandler, set);
        GameTeam gameTeam = gunGamePlayerManager.getTeamByName("Axis");
        GameTeam gameTeam2 = gunGamePlayerManager.getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        gameTeam.teleportPlayersToSpawns(playerDataHandler, gunGame);
        gameTeam2.teleportPlayersToSpawns(playerDataHandler, gunGame);
        for (UUID uUID : set) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            gunGamePlayerManager.method_3605(serverPlayer);
        }
        BFUtils.initPlayersForGame(serverLevel, gunGamePlayerManager.getPlayerUUIDs(), playerDataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<GunGame, GunGamePlayerManager> createNextStage(@NotNull GunGame gunGame) {
        return new GunGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.PRE_GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull GunGame gunGame, @NotNull UUID uUID) {
        BFUtils.queueMusic(uUID, GameMusic.create().method_1541(BFMusicType.WARMUP));
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

