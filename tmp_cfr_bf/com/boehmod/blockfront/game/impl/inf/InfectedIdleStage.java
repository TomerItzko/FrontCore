/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.IdleGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.game.impl.inf.InfectedPreStage;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class InfectedIdleStage
extends IdleGameStage<InfectedGame, InfectedPlayerManager> {
    @Override
    @NotNull
    public AbstractGameStage<InfectedGame, InfectedPlayerManager> createNextStage(@NotNull InfectedGame infectedGame) {
        return new InfectedPreStage();
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull InfectedGame infectedGame, @NotNull UUID uUID) {
    }
}

