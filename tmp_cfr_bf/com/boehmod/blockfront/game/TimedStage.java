/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public interface TimedStage<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>> {
    @Nullable
    public GameStageTimer getStageTimer(@NotNull G var1);
}

