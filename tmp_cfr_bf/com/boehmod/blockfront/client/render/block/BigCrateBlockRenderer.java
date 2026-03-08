/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.blockfront.common.block.entity.CrateGunBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public class BigCrateBlockRenderer
implements BlockEntityRenderer<CrateGunBlockEntity> {
    public BigCrateBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public boolean shouldRenderOffScreen(@NotNull CrateGunBlockEntity crateGunBlockEntity) {
        return true;
    }

    public int getViewDistance() {
        return 128;
    }

    public void render(CrateGunBlockEntity crateGunBlockEntity, float f, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2) {
        ItemStack itemStack = crateGunBlockEntity.getHeldItem();
        if (itemStack.isEmpty()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        BlockState blockState = crateGunBlockEntity.getBlockState();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        Direction direction = (Direction)blockState.getValue((Property)HorizontalDirectionalBlock.FACING);
        BakedModel bakedModel = itemRenderer.getModel(itemStack, (Level)clientLevel, null, 0);
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.85f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
        itemRenderer.render(itemStack, ItemDisplayContext.FIXED, false, poseStack, multiBufferSource, n, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
    }

    public /* synthetic */ boolean shouldRenderOffScreen(@NotNull BlockEntity blockEntity) {
        return this.shouldRenderOffScreen((CrateGunBlockEntity)blockEntity);
    }

    public /* synthetic */ void render(BlockEntity blockEntity, float f, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, int n2) {
        this.render((CrateGunBlockEntity)blockEntity, f, poseStack, multiBufferSource, n, n2);
    }
}

