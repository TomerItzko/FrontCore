/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.gg;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.IdleGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import com.boehmod.blockfront.game.impl.gg.GunGamePreStage;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class GunGameIdleStage
extends IdleGameStage<GunGame, GunGamePlayerManager> {
    @Override
    @NotNull
    public AbstractGameStage<GunGame, GunGamePlayerManager> createNextStage(@NotNull GunGame gunGame) {
        return new GunGamePreStage();
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull GunGame gunGame, @NotNull UUID uUID) {
    }
}

