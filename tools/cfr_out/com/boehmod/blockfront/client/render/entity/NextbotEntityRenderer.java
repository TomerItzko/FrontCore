/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.NextbotEntity;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class NextbotEntityRenderer
extends EntityRenderer<NextbotEntity> {
    public NextbotEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }

    public void render(@NotNull NextbotEntity nextbotEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        RenderSystem.enableDepthTest();
        poseStack.pushPose();
        float f3 = MathUtils.lerpf1(minecraft.player.yHeadRot, minecraft.player.yHeadRotO, f2);
        poseStack.mulPose(Axis.YP.rotationDegrees(-f3));
        poseStack.translate(1.5f, 0.0f, 0.0f);
        poseStack.scale(3.0f, 3.0f, 3.0f);
        poseStack.translate(0.0f, 1.0f, 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        GuiGraphics guiGraphics = new GuiGraphics(minecraft, minecraft.renderBuffers().bufferSource());
        guiGraphics.pose().mulPose(poseStack.last().pose());
        BFRendering.centeredTexture(poseStack, guiGraphics, this.getTextureLocation(nextbotEntity), 0, 0, 1, 1);
        poseStack.popPose();
        super.render((Entity)nextbotEntity, f, f2, poseStack, multiBufferSource, n);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull NextbotEntity nextbotEntity) {
        return BFRes.loc("textures/models/entities/nextbot/" + nextbotEntity.getTexture() + ".png");
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((NextbotEntity)entity);
    }

    public /* synthetic */ void render(@NotNull Entity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.render((NextbotEntity)entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

