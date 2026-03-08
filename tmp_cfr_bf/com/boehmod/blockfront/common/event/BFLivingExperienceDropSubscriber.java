/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import org.jetbrains.annotations.NotNull;

public final class BFLivingExperienceDropSubscriber {
    public static void onLivingExperienceDrop(@NotNull LivingExperienceDropEvent event) {
        Player player;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player && bFAbstractManager.getGameWithPlayer((player = (Player)livingEntity).getUUID()) != null) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof IntegratedGameEntity) {
            event.setCanceled(true);
        }
        if (event.getEntity().getPersistentData().hasUUID("matchId")) {
            event.setCanceled(true);
        }
    }
}

