/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.renderer.GeoBlockRenderer
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public abstract class AbstractVehicleEntityRenderer<V extends AbstractVehicleEntity>
extends EntityRenderer<V> {
    public AbstractVehicleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(@NotNull V v, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        BFBlockEntity bFBlockEntity;
        BlockEntityRenderDispatcher blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        BlockEntityRenderer blockEntityRenderer = blockEntityRenderDispatcher.getRenderer((BlockEntity)(bFBlockEntity = ((AbstractVehicleEntity)v).method_2330() ? ((AbstractVehicleEntity)v).method_2342() : ((AbstractVehicleEntity)v).method_2315()));
        if (!(blockEntityRenderer instanceof GeoBlockRenderer)) {
            return;
        }
        GeoBlockRenderer geoBlockRenderer = (GeoBlockRenderer)blockEntityRenderer;
        float f3 = BFRendering.getRenderTime();
        float f4 = Mth.sin((float)(f3 / 3.0f));
        float f5 = Mth.sin((float)(f3 / 4.0f));
        float f6 = ((AbstractVehicleEntity)v).method_2355(f2);
        float f7 = 0.7f * f4 * f6;
        float f8 = 0.7f * f5 * f6;
        float f9 = v.getViewYRot(f2);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - f9));
        ((AbstractVehicleEntity)v).method_2316().method_2374(poseStack, f2);
        poseStack.mulPose(Axis.XP.rotationDegrees(f7));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f8));
        poseStack.translate(-0.5f, 0.0f, -0.5f);
        BF_624<AbstractVehicleEntity> bF_624 = ((AbstractVehicleEntity)v).method_2343();
        this.method_1385(v, bF_624, poseStack);
        int n2 = LevelRenderer.getLightColor((BlockAndTintGetter)v.level(), (BlockPos)v.getOnPos().above());
        geoBlockRenderer.render((BlockEntity)bFBlockEntity, f2, poseStack, multiBufferSource, n2, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(v, f, f2, poseStack, multiBufferSource, n);
    }

    public abstract void method_1385(V var1, BF_624<?> var2, PoseStack var3);

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull V v) {
        return BFRes.loc("null");
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((V)((AbstractVehicleEntity)entity));
    }

    public /* synthetic */ void render(@NotNull Entity entity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((V)((AbstractVehicleEntity)entity), f, f2, poseStack, multiBufferSource, n);
    }
}

