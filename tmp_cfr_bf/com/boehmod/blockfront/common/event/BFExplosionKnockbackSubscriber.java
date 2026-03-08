/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.ExplosionKnockbackEvent;
import org.jetbrains.annotations.NotNull;

public final class BFExplosionKnockbackSubscriber {
    public static void onExplosionKnockback(@NotNull ExplosionKnockbackEvent event) {
        Object object = event.getAffectedEntity();
        if (!(object instanceof Player)) {
            return;
        }
        Player player = (Player)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        if (((BFAbstractManager)object).getGameWithPlayer(player) != null) {
            event.setKnockbackVelocity(Vec3.ZERO);
        }
    }
}

