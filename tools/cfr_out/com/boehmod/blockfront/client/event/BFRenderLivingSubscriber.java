/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderLivingEvent$Pre
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFRenderLivingSubscriber {
    @SubscribeEvent
    public static void onRenderLivingPre(@NotNull RenderLivingEvent.Pre<?, ?> event) {
        AbstractVehicleEntity abstractVehicleEntity;
        LivingEntity livingEntity = event.getEntity();
        Entity entity = livingEntity.getVehicle();
        if (entity instanceof AbstractVehicleEntity && (abstractVehicleEntity = (AbstractVehicleEntity)entity).method_2353((Entity)livingEntity)) {
            event.setCanceled(true);
        }
    }
}

