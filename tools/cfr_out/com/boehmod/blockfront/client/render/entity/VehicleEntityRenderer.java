/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.entity.AbstractVehicleEntityRenderer;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_624;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VehicleEntityRenderer
extends AbstractVehicleEntityRenderer<ArtilleryVehicleEntity> {
    public VehicleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void method_1385(ArtilleryVehicleEntity artilleryVehicleEntity, BF_624<?> bF_624, PoseStack poseStack) {
        if (artilleryVehicleEntity.method_2333() || !artilleryVehicleEntity.method_2334()) {
            // empty if block
        }
    }
}

