/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFAttackEntitySubscriber {
    @SubscribeEvent
    public static void method_5494(@NotNull AttackEntityEvent attackEntityEvent) {
        Player player = attackEntityEvent.getEntity();
        if (player.getMainHandItem().getItem() instanceof GunItem) {
            attackEntityEvent.setCanceled(true);
        }
    }
}

