/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.IdleGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPreStage;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class TroubleTownIdleStage
extends IdleGameStage<TroubleTownGame, TroubleTownPlayerManager> {
    @Override
    @NotNull
    public AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager> createNextStage(@NotNull TroubleTownGame troubleTownGame) {
        return new TroubleTownPreStage();
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull TroubleTownGame troubleTownGame, @NotNull UUID uUID) {
    }
}

