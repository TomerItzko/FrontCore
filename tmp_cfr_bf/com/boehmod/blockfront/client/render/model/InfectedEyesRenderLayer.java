/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.model;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.entity.InfectedEntityRenderer;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.unnamed.BF_227;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.HumanoidModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class InfectedEyesRenderLayer
extends RenderLayer<InfectedEntity, BF_227> {
    public static final ModelLayerLocation EYES = new ModelLayerLocation(BFRes.loc("infected"), "eyes_layer");
    public final HumanoidModel<InfectedEntity> field_1755;

    public InfectedEyesRenderLayer(InfectedEntityRenderer infectedEntityRenderer, EntityRendererProvider.Context context) {
        super((RenderLayerParent)infectedEntityRenderer);
        this.field_1755 = new HumanoidModel(context.bakeLayer(EYES));
    }

    private void method_1274(@NotNull InfectedEntity infectedEntity, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull EntityModel<InfectedEntity> entityModel, int n) {
        ResourceLocation resourceLocation = infectedEntity.method_2074();
        RenderType renderType = RenderType.eyes((ResourceLocation)resourceLocation);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
        entityModel.renderToBuffer(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY, ColorReferences.COLOR_WHITE_SOLID);
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull InfectedEntity infectedEntity, float f, float f2, float f3, float f4, float f5, float f6) {
        poseStack.pushPose();
        BF_227 bF_227 = (BF_227)this.getParentModel();
        bF_227.copyPropertiesTo(this.field_1755);
        this.field_1755.prepareMobModel((LivingEntity)infectedEntity, f, f2, f3);
        this.field_1755.setupAnim((LivingEntity)infectedEntity, f, f2, f4, f5, f6);
        HumanoidModelUtils.match(this.field_1755, bF_227);
        this.field_1755.hat.visible = true;
        this.method_1274(infectedEntity, poseStack, multiBufferSource, (EntityModel<InfectedEntity>)this.field_1755, 256);
        poseStack.popPose();
    }

    public /* synthetic */ void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.render(poseStack, multiBufferSource, n, (InfectedEntity)entity, f, f2, f3, f4, f5, f6);
    }
}

