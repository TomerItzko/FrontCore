/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.ffa;

import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.PreGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGame;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGameStage;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class FreeForAllPreStage
extends PreGameStage<FreeForAllGame, FreeForAllPlayerManager> {
    public static final int field_3495 = 20;

    public FreeForAllPreStage() {
        super(20);
    }

    @Override
    public void stageStarted(@NotNull FreeForAllGame freeForAllGame, @NotNull FreeForAllPlayerManager freeForAllPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.queueMusic(set, GameMusic.create().method_1541(BFMusicType.WARMUP));
    }

    @Override
    public void stageEnded(@NotNull FreeForAllGame freeForAllGame, @NotNull FreeForAllPlayerManager freeForAllPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        BFUtils.method_2977(playerDataHandler, set);
        freeForAllPlayerManager.method_3567(playerDataHandler);
        BFUtils.method_2944(serverLevel, freeForAllGame, set);
        BFUtils.initPlayersForGame(serverLevel, set, playerDataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<FreeForAllGame, FreeForAllPlayerManager> createNextStage(@NotNull FreeForAllGame freeForAllGame) {
        return new FreeForAllGameStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.PRE_GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull FreeForAllGame freeForAllGame, @NotNull UUID uUID) {
        BFUtils.queueMusic(uUID, GameMusic.create().method_1541(BFMusicType.WARMUP));
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

