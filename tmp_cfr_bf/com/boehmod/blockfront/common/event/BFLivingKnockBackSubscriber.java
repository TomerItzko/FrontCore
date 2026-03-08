/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import org.jetbrains.annotations.NotNull;

public class BFLivingKnockBackSubscriber {
    public static void onLivingKnockBack(@NotNull LivingKnockBackEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            event.setCanceled(bFAbstractManager.isPlayerInGame(player.getUUID()));
        }
    }
}

