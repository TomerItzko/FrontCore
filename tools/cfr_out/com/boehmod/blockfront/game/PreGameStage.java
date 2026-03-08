/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.server.level.ServerLevel
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public abstract class PreGameStage<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends AbstractGameStage<G, P>
implements TimedStage<G, P> {
    public final int field_3774;
    @NotNull
    private final GameStageTimer field_3775;

    public PreGameStage(int n) {
        this.field_3774 = n;
        this.field_3775 = new GameStageTimer(0, n).warningTime(10);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onSecondPassed(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        if (((AbstractGame)game).hasMinimumPlayers(players)) {
            this.field_3775.update(players);
        } else {
            this.field_3775.setSecondsRemaining(this.field_3774);
            BFUtils.method_2977(dataHandler, players);
        }
        if (this.field_3775.isRunning() && this.field_3775.getSecondsRemaining() <= 5) {
            BFUtils.method_2870(dataHandler, players);
        }
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void method_3911(@NotNull G g, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.field_3775.restart();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public boolean isStageOver(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> modManager, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        return players.isEmpty() || !this.field_3775.isRunning();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public GameStageTimer getStageTimer(@NotNull G game) {
        return this.field_3775;
    }
}

