/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.MobRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.unnamed.BF_225;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class HumanEntityRenderer
extends MobRenderer<HumanEntity, BF_225<HumanEntity>> {
    public HumanEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BF_225(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer((RenderLayer)new HumanoidArmorLayer((RenderLayerParent)this, new BF_225(context.bakeLayer(ModelLayers.PLAYER)), new BF_225(context.bakeLayer(ModelLayers.PLAYER)), context.getModelManager()));
    }

    @NotNull
    public ResourceLocation getTextureLocation(HumanEntity humanEntity) {
        return humanEntity.method_1972();
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(Entity entity) {
        return this.getTextureLocation((HumanEntity)entity);
    }
}

