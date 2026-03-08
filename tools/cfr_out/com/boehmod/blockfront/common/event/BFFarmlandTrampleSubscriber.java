/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.event.level.BlockEvent$FarmlandTrampleEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.common.entity.MatchEntity;
import com.boehmod.blockfront.game.AbstractGame;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

public final class BFFarmlandTrampleSubscriber {
    public static void onFarmlandTrample(@NotNull BlockEvent.FarmlandTrampleEvent event) {
        Object object = event.getEntity();
        if (object instanceof Player) {
            Player player = (Player)object;
            object = BlockFront.getInstance().getManager();
            assert (object != null) : "Mod manager is null!";
            AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(player);
            if (abstractGame != null) {
                event.setCanceled(true);
            }
        }
        if (event.getEntity() instanceof IntegratedGameEntity) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof MatchEntity) {
            event.setCanceled(true);
        }
    }
}

