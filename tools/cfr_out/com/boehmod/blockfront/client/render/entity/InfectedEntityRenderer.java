/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.HumanoidMobRenderer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.model.InfectedEyesRenderLayer;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.unnamed.BF_227;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class InfectedEntityRenderer
extends HumanoidMobRenderer<InfectedEntity, BF_227> {
    public InfectedEntityRenderer(EntityRendererProvider.Context context) {
        super(context, (HumanoidModel)new BF_227(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        this.addLayer(new InfectedEyesRenderLayer(this, context));
    }

    @NotNull
    public final ResourceLocation getTextureLocation(InfectedEntity infectedEntity) {
        return infectedEntity.method_2073();
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(Entity entity) {
        return this.getTextureLocation((InfectedEntity)entity);
    }
}

