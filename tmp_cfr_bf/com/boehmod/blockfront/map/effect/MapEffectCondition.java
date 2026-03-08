/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public final class MapEffectCondition
extends Enum<MapEffectCondition> {
    public static final /* enum */ MapEffectCondition DOMINATION_HALF_SCORE = new MapEffectCondition(game -> {
        if (game instanceof DominationGame) {
            DominationGame dominationGame = (DominationGame)game;
            DominationPlayerManager dominationPlayerManager = (DominationPlayerManager)dominationGame.getPlayerManager();
            GameTeam gameTeam = dominationPlayerManager.getTeamByName("Axis");
            assert (gameTeam != null);
            int n = gameTeam.getObjectInt(BFStats.SCORE);
            GameTeam gameTeam2 = dominationPlayerManager.getTeamByName("Allies");
            assert (gameTeam2 != null);
            int n2 = gameTeam2.getObjectInt(BFStats.SCORE);
            return n >= 250 || n2 >= 250;
        }
        return false;
    });
    @NotNull
    public final Function<AbstractGame<?, ?, ?>, Boolean> condition;
    private static final /* synthetic */ MapEffectCondition[] $VALUES;

    public static MapEffectCondition[] values() {
        return (MapEffectCondition[])$VALUES.clone();
    }

    public static MapEffectCondition valueOf(String string) {
        return Enum.valueOf(MapEffectCondition.class, string);
    }

    private MapEffectCondition(Function<AbstractGame<?, ?, ?>, Boolean> condition) {
        this.condition = condition;
    }

    private static /* synthetic */ MapEffectCondition[] $values() {
        return new MapEffectCondition[]{DOMINATION_HALF_SCORE};
    }

    static {
        $VALUES = MapEffectCondition.$values();
    }
}

