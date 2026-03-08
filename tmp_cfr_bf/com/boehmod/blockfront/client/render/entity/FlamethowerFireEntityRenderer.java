/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.FlamethrowerFireEntity;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public final class FlamethowerFireEntityRenderer
extends EntityRenderer<FlamethrowerFireEntity> {
    private static final List<ResourceLocation> field_1782 = new ObjectArrayList();
    public static final int field_1783 = 5;

    public FlamethowerFireEntityRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }

    private static void method_1332(VertexConsumer vertexConsumer, Matrix4f matrix4f, PoseStack.Pose pose, float f, float f2, float f3, float f4, float f5) {
        vertexConsumer.addVertex(matrix4f, f, f2, 0.0f).setColor(1.0f, 1.0f, 1.0f, f3).setUv(f4, f5).setOverlay(OverlayTexture.NO_OVERLAY).setUv2(0xF000F0, 0xF000F0).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }

    public boolean shouldRender(@NotNull FlamethrowerFireEntity flamethrowerFireEntity, @NotNull Frustum frustum, double d, double d2, double d3) {
        return true;
    }

    public void render(FlamethrowerFireEntity flamethrowerFireEntity, float f, float f2, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        poseStack.pushPose();
        float f3 = 0.0f;
        float f4 = 1.0f;
        float f5 = 0.0f;
        float f6 = 1.0f;
        float f7 = Mth.clamp((float)(0.1f + 0.15f * (float)flamethrowerFireEntity.tickCount), (float)0.01f, (float)1.0f);
        float f8 = BFRendering.getRenderTime();
        float f9 = Math.min(1.5f, 0.1f * (float)flamethrowerFireEntity.tickCount);
        poseStack.scale(f9, f9, f9);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        int n2 = (int)(f8 / 3.0f % 4.0f);
        Minecraft minecraft = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        RenderType renderType = RenderType.itemEntityTranslucentCull((ResourceLocation)field_1782.get(n2));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        FlamethowerFireEntityRenderer.method_1332(vertexConsumer, matrix4f, pose, -0.5f, -0.25f, f7, 0.0f, 1.0f);
        FlamethowerFireEntityRenderer.method_1332(vertexConsumer, matrix4f, pose, 0.5f, -0.25f, f7, 1.0f, 1.0f);
        FlamethowerFireEntityRenderer.method_1332(vertexConsumer, matrix4f, pose, 0.5f, 0.75f, f7, 1.0f, 0.0f);
        FlamethowerFireEntityRenderer.method_1332(vertexConsumer, matrix4f, pose, -0.5f, 0.75f, f7, 0.0f, 0.0f);
        poseStack.popPose();
        super.render((Entity)flamethrowerFireEntity, f, f2, poseStack, multiBufferSource, 0xF000F0);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull FlamethrowerFireEntity flamethrowerFireEntity) {
        return field_1782.getFirst();
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((FlamethrowerFireEntity)entity);
    }

    public /* synthetic */ void render(Entity entity, float f, float f2, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((FlamethrowerFireEntity)entity, f, f2, poseStack, multiBufferSource, n);
    }

    public /* synthetic */ boolean shouldRender(@NotNull Entity entity, @NotNull Frustum frustum, double d, double d2, double d3) {
        return this.shouldRender((FlamethrowerFireEntity)entity, frustum, d, d2, d3);
    }

    static {
        for (int i = 0; i < 5; ++i) {
            field_1782.add(BFRes.loc("textures/models/entities/flame" + i + ".png"));
        }
    }
}

