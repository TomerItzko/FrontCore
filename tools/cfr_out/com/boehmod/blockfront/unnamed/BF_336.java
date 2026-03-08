/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.world.entity.Entity
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.entity.AbstractVehicleEntityRenderer;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BasicVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_624;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class BF_336
extends AbstractVehicleEntityRenderer<BasicVehicleEntity> {
    public BF_336(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void method_1385(BasicVehicleEntity basicVehicleEntity, BF_624<?> bF_624, PoseStack poseStack) {
        Entity entity = basicVehicleEntity.method_2319();
        if (entity == null) {
            return;
        }
        BF_624<AbstractVehicleEntity> bF_6242 = basicVehicleEntity.method_2343();
        float f = basicVehicleEntity.method_2313() / bF_6242.field_2691;
        float f2 = basicVehicleEntity.getYRot();
        float f3 = entity.getYHeadRot();
        poseStack.mulPose(Axis.ZP.rotationDegrees((f2 - f3) * 0.35f * f));
    }
}

