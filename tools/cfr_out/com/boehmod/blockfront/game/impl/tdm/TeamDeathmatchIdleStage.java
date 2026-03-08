/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.IdleGameStage;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPreStage;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class TeamDeathmatchIdleStage
extends IdleGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> {
    @Override
    @NotNull
    public AbstractGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> createNextStage(@NotNull TeamDeathmatchGame teamDeathmatchGame) {
        return new TeamDeathmatchPreStage();
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull UUID uUID) {
    }
}

