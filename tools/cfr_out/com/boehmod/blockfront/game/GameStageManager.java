/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.util.BFLog;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class GameStageManager<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>> {
    @NotNull
    private final G game;
    @NotNull
    private AbstractGameStage<G, P> currentStage;
    private boolean canChangeStage = false;

    public GameStageManager(@NotNull G game) {
        this.game = game;
        this.currentStage = ((AbstractGame)game).createFirstStage();
    }

    public void update(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> playerData, @NotNull P playerHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        if (this.currentStage.isStageOver(this.game, playerHandler, manager, level, players) || this.canChangeStage) {
            this.canChangeStage = false;
            BFLog.log("[STAGE] Stage '%s' has ended for game '%s'!", this.currentStage.getClass().getSimpleName(), ((AbstractGame)this.game).getName());
            this.currentStage.stageEnded(this.game, playerHandler, manager, playerData, level, players);
            this.currentStage = this.currentStage.createNextStage(this.game);
            this.currentStage.stageStarted(this.game, playerHandler, manager, playerData, level, players);
            BFLog.log("[STAGE] Stage '%s' has started for game '%s'!", this.currentStage.getClass().getSimpleName(), ((AbstractGame)this.game).getName());
        }
        ((AbstractGame)this.game).setStatus(this.currentStage.getStatus());
        this.currentStage.update(this.game, playerHandler, manager, playerData, level, players);
    }

    public void setCanChangeStage() {
        this.canChangeStage = true;
    }

    public void initFirstStage() {
        this.currentStage = ((AbstractGame)this.game).createFirstStage();
    }

    @NotNull
    public AbstractGameStage<G, P> getCurrentStage() {
        return this.currentStage;
    }
}

