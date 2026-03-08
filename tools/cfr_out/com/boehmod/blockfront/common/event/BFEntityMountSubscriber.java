/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.event.entity.EntityMountEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.game.AbstractGame;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import org.jetbrains.annotations.NotNull;

public class BFEntityMountSubscriber {
    public static void onEntityMount(@NotNull EntityMountEvent event) {
        Player player;
        AbstractGame<?, ?, ?> abstractGame;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null);
        Entity entity = event.getEntityMounting();
        if (entity instanceof Player && (abstractGame = bFAbstractManager.getGameWithPlayer(player = (Player)entity)) != null && !(event.getEntityBeingMounted() instanceof AbstractVehicleEntity)) {
            event.setCanceled(true);
            return;
        }
        player = event.getEntity().getPersistentData();
        if (player.getBoolean("disableMount")) {
            event.setCanceled(true);
        }
    }
}

