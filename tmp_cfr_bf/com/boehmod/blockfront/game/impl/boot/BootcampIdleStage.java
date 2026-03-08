/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.IdleGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.boot.BootcampGame;
import com.boehmod.blockfront.game.impl.boot.BootcampGameStage;
import com.boehmod.blockfront.game.impl.boot.BootcampPlayerManager;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class BootcampIdleStage
extends IdleGameStage<BootcampGame, BootcampPlayerManager> {
    @Override
    @NotNull
    public AbstractGameStage<BootcampGame, BootcampPlayerManager> createNextStage(@NotNull BootcampGame bootcampGame) {
        return new BootcampGameStage();
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull BootcampGame bootcampGame, @NotNull UUID uUID) {
    }
}

