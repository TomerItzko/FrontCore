/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.event.entity.living.LivingDropsEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;

public final class BFLivingDropsSubscriber {
    public static void onLivingDrops(@NotNull LivingDropsEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof IntegratedGameEntity) {
            event.setCanceled(true);
            return;
        }
        if (livingEntity.getPersistentData().hasUUID("matchId")) {
            event.setCanceled(true);
            return;
        }
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            AbstractGame<?, ?, ?> abstractGame = BlockFront.getInstance().getManager().getGameWithPlayer(player.getUUID());
            if (abstractGame != null && !player.level().isClientSide) {
                Collection collection = event.getDrops();
                List<ItemEntity> list = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2776(player, (List<ItemEntity>)new ObjectArrayList(collection));
                if (list == null || list.isEmpty()) {
                    event.setCanceled(true);
                } else {
                    collection.clear();
                    collection.addAll(list);
                }
            }
        }
    }
}

