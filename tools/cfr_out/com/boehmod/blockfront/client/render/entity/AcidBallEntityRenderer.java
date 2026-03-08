/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.common.entity.AcidBallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class AcidBallEntityRenderer
extends EntityRenderer<AcidBallEntity> {
    private final ItemRenderer field_1792;

    public AcidBallEntityRenderer(EntityRendererProvider.Context context, ItemRenderer itemRenderer) {
        super(context);
        this.field_1792 = itemRenderer;
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }

    protected int getBlockLightLevel(@NotNull AcidBallEntity acidBallEntity, @NotNull BlockPos blockPos) {
        return 15;
    }

    public void render(@NotNull AcidBallEntity acidBallEntity, float f, float f2, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        poseStack.pushPose();
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        ItemStack itemStack = new ItemStack((ItemLike)acidBallEntity.getDefaultItem());
        BakedModel bakedModel = this.field_1792.getModel(itemStack, acidBallEntity.level(), null, acidBallEntity.getId());
        poseStack.mulPose(Axis.YP.rotationDegrees(-localPlayer.getYHeadRot()));
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.pushPose();
        poseStack.translate(0.05f, 0.0f, -0.15f);
        this.field_1792.render(itemStack, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, n, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
        poseStack.popPose();
        super.render((Entity)acidBallEntity, f, f2, poseStack, multiBufferSource, n);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull AcidBallEntity acidBallEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((AcidBallEntity)entity);
    }

    public /* synthetic */ void render(@NotNull Entity entity, float f, float f2, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((AcidBallEntity)entity, f, f2, poseStack, multiBufferSource, n);
    }

    protected /* synthetic */ int getBlockLightLevel(@NotNull Entity entity, @NotNull BlockPos blockPos) {
        return this.getBlockLightLevel((AcidBallEntity)entity, blockPos);
    }
}

