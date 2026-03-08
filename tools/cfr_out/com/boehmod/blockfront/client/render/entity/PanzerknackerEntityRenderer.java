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

import com.boehmod.blockfront.common.entity.PanzerknackerEntity;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class PanzerknackerEntityRenderer
extends HumanoidMobRenderer<PanzerknackerEntity, HumanoidModel<PanzerknackerEntity>> {
    private static final ResourceLocation field_1781 = BFRes.loc("textures/models/entities/panzerknacker/panzerknacker.png");

    public PanzerknackerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_SLIM)), 0.5f);
    }

    @NotNull
    public final ResourceLocation getTextureLocation(@NotNull PanzerknackerEntity panzerknackerEntity) {
        return field_1781;
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((PanzerknackerEntity)entity);
    }
}

