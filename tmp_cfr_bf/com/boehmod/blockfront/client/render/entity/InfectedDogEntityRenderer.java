/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.common.entity.InfectedDogEntity;
import com.boehmod.blockfront.unnamed.BF_226;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import org.jetbrains.annotations.NotNull;

public final class InfectedDogEntityRenderer
extends MobRenderer<InfectedDogEntity, BF_226> {
    private static final ResourceLocation field_1780 = BFRes.loc("textures/models/entities/infected/dog.png");

    public InfectedDogEntityRenderer(EntityRendererProvider.Context context) {
        super(context, (EntityModel)new BF_226(context.bakeLayer(ModelLayers.WOLF)), 0.5f);
    }

    public void render(@NotNull InfectedDogEntity infectedDogEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        WalkAnimationState walkAnimationState = infectedDogEntity.walkAnimation;
        ((BF_226)this.model).prepareMobModel(infectedDogEntity, walkAnimationState.position(f2), walkAnimationState.speed(f2), f2);
        super.render((LivingEntity)infectedDogEntity, f, f2, poseStack, multiBufferSource, n);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull InfectedDogEntity infectedDogEntity) {
        return field_1780;
    }

    public /* synthetic */ void render(@NotNull LivingEntity livingEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((InfectedDogEntity)livingEntity, f, f2, poseStack, multiBufferSource, n);
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((InfectedDogEntity)entity);
    }

    public /* synthetic */ void render(@NotNull Entity entity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((InfectedDogEntity)entity, f, f2, poseStack, multiBufferSource, n);
    }
}

