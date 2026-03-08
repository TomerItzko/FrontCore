/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.bus.api.IEventBus
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.setup;

import com.boehmod.blockfront.common.event.BFBlocksSubscriber;
import com.boehmod.blockfront.common.event.BFEntityMountSubscriber;
import com.boehmod.blockfront.common.event.BFEntityTickSubscriber;
import com.boehmod.blockfront.common.event.BFExplosionKnockbackSubscriber;
import com.boehmod.blockfront.common.event.BFFarmlandTrampleSubscriber;
import com.boehmod.blockfront.common.event.BFGunTriggerSubscriber;
import com.boehmod.blockfront.common.event.BFItemEntityPickupSubscriber;
import com.boehmod.blockfront.common.event.BFLivingDamageSubscriber;
import com.boehmod.blockfront.common.event.BFLivingDeathSubscriber;
import com.boehmod.blockfront.common.event.BFLivingDropsSubscriber;
import com.boehmod.blockfront.common.event.BFLivingExperienceDropSubscriber;
import com.boehmod.blockfront.common.event.BFLivingIncomingDamageSubscriber;
import com.boehmod.blockfront.common.event.BFLivingKnockBackSubscriber;
import com.boehmod.blockfront.common.event.BFMobDespawnSubscriber;
import com.boehmod.blockfront.common.event.BFPlayerLoggedInSubscriber;
import com.boehmod.blockfront.common.event.BFPlayerLoggedOutSubscriber;
import com.boehmod.blockfront.common.event.BFPlayerRespawnSubscriber;
import com.boehmod.blockfront.common.event.BFProjectileImpactSubscriber;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public class BFEventSetup {
    public static void register(@NotNull IEventBus bus) {
        bus.addListener(BFEntityTickSubscriber::onEntityTickPost);
        bus.addListener(BFLivingKnockBackSubscriber::onLivingKnockBack);
        bus.addListener(BFLivingDeathSubscriber::onLivingDeath);
        bus.addListener(BFLivingIncomingDamageSubscriber::onLivingIncomingDamage);
        bus.addListener(BFLivingDamageSubscriber::onLivingDamagePre);
        bus.addListener(BFPlayerLoggedInSubscriber::onPlayerLoggedIn);
        bus.addListener(BFPlayerLoggedOutSubscriber::onPlayerLoggedOut);
        bus.addListener(BFPlayerRespawnSubscriber::onPlayerRespawn);
        bus.addListener(BFLivingDropsSubscriber::onLivingDrops);
        bus.addListener(BFItemEntityPickupSubscriber::onItemEntityPickupPre);
        bus.addListener(BFEntityMountSubscriber::onEntityMount);
        bus.addListener(BFLivingExperienceDropSubscriber::onLivingExperienceDrop);
        bus.addListener(BFMobDespawnSubscriber::onMobDespawn);
        bus.addListener(BFBlocksSubscriber::onBreak);
        bus.addListener(BFBlocksSubscriber::onEntityPlace);
        bus.addListener(BFBlocksSubscriber::onRightClickBlock);
        bus.addListener(BFFarmlandTrampleSubscriber::onFarmlandTrample);
        bus.addListener(BFExplosionKnockbackSubscriber::onExplosionKnockback);
        bus.addListener(BFGunTriggerSubscriber::onEntityTriggerGun);
        bus.addListener(BFProjectileImpactSubscriber::onProjectileImpact);
    }
}

