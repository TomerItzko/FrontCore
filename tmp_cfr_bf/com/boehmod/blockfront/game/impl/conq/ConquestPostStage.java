/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.PostGameStage;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestIdleStage;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class ConquestPostStage
extends PostGameStage<ConquestGame, ConquestPlayerManager>
implements TimedStage<ConquestGame, ConquestPlayerManager> {
    @Override
    public void method_3911(@NotNull ConquestGame conquestGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<ConquestGame, ConquestPlayerManager> createNextStage(@NotNull ConquestGame conquestGame) {
        return new ConquestIdleStage();
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((ConquestGame)abstractGame, serverLevel, (Set<UUID>)set);
    }
}

