/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.unnamed.BF_228;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public final class VendorEntityRenderer
extends HumanoidMobRenderer<VendorEntity, BF_228> {
    public VendorEntityRenderer(EntityRendererProvider.Context context) {
        super(context, (HumanoidModel)new BF_228(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }

    @NotNull
    public ResourceLocation getTextureLocation(VendorEntity vendorEntity) {
        return vendorEntity.getTexture();
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(Entity entity) {
        return this.getTextureLocation((VendorEntity)entity);
    }
}

