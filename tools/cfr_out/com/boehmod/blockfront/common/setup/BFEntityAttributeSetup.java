/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.EntityType
 *  net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.setup;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.common.entity.InfectedDogEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.entity.InfectedSpitterEntity;
import com.boehmod.blockfront.common.entity.InfectedStalkerEntity;
import com.boehmod.blockfront.common.entity.NextbotEntity;
import com.boehmod.blockfront.common.entity.PanzerknackerEntity;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.registry.BFEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.jetbrains.annotations.NotNull;

public class BFEntityAttributeSetup {
    public static void register(@NotNull EntityAttributeCreationEvent event) {
        event.put((EntityType)BFEntityTypes.HUMAN.get(), HumanEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.BOT.get(), BotEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.INFECTED.get(), InfectedEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.GUN_DEALER.get(), VendorEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.INFECTED_DOG.get(), InfectedDogEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.INFECTED_SPITTER.get(), InfectedSpitterEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.INFECTED_STALKER.get(), InfectedStalkerEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.OBUNGA.get(), NextbotEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.MICHAEL.get(), NextbotEntity.getMobAttributes().build());
        event.put((EntityType)BFEntityTypes.PANZERKNACKER.get(), PanzerknackerEntity.getMobAttributes().build());
    }
}

