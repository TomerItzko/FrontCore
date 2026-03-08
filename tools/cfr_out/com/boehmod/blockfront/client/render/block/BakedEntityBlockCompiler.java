/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.SectionPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.client.model.data.ModelData
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector2d
 *  org.joml.Vector2dc
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.constant.DataTickets
 *  software.bernie.geckolib.constant.dataticket.DataTicket
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.blockfront.client.render.block.BFBlockRenderer;
import com.boehmod.blockfront.unnamed.BF_232;
import com.boehmod.blockfront.unnamed.BF_364;
import com.boehmod.blockfront.util.BFLog;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public class BakedEntityBlockCompiler {
    public static boolean compile(@NotNull BlockRenderDispatcher blockRenderDispatcher, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull BlockAndTintGetter blockAndTintGetter, @NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, boolean bl, @NotNull RandomSource randomSource, @NotNull ModelData modelData, RenderType renderType, @NotNull Operation<Void> operation) {
        BlockEntity blockEntity = blockAndTintGetter.getBlockEntity(blockPos);
        if (blockEntity == null || blockEntity.isRemoved()) {
            operation.call(new Object[]{blockRenderDispatcher, blockState, blockPos, blockAndTintGetter, poseStack, vertexConsumer, bl, randomSource, modelData, renderType});
            return false;
        }
        Minecraft minecraft = Minecraft.getInstance();
        BlockEntityRenderer blockEntityRenderer = minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
        if (!(blockEntityRenderer instanceof BFBlockRenderer)) {
            operation.call(new Object[]{blockRenderDispatcher, blockState, blockPos, blockAndTintGetter, poseStack, vertexConsumer, bl, randomSource, modelData, renderType});
            return false;
        }
        BFBlockRenderer bFBlockRenderer = (BFBlockRenderer)blockEntityRenderer;
        if (!bFBlockRenderer.method_1284(blockEntity)) {
            if (bFBlockRenderer.method_5953()) {
                operation.call(new Object[]{blockRenderDispatcher, blockState, blockPos, blockAndTintGetter, poseStack, vertexConsumer, bl, randomSource, modelData, renderType});
            }
            return false;
        }
        if (minecraft.level == null) {
            return false;
        }
        BF_232 bF_232 = (BF_232)bFBlockRenderer.getGeoModel();
        BlockEntity blockEntity2 = (BlockEntity)((GeoAnimatable)blockEntity);
        ResourceLocation resourceLocation = bF_232.method_1031(blockEntity2);
        TextureAtlasSprite textureAtlasSprite = (TextureAtlasSprite)minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resourceLocation);
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate((float)SectionPos.sectionRelative((int)blockPos.getX()), (float)SectionPos.sectionRelative((int)blockPos.getY()), (float)SectionPos.sectionRelative((int)blockPos.getZ()));
        Vector2d vector2d = new Vector2d((double)textureAtlasSprite.getU0(), (double)textureAtlasSprite.getV0());
        Vector2d vector2d2 = new Vector2d((double)textureAtlasSprite.getU1(), (double)textureAtlasSprite.getV1());
        float f = 1.0f;
        long l = bFBlockRenderer.getInstanceId(blockEntity);
        BF_364 bF_364 = new BF_364(minecraft.level, vertexConsumer, (Vector2dc)vector2d, (Vector2dc)vector2d2);
        int n = LevelRenderer.getLightColor((BlockAndTintGetter)blockAndTintGetter, (BlockPos)blockPos.above());
        AnimationState animationState = new AnimationState((GeoAnimatable)blockEntity2, 0.0f, 0.0f, 1.0f, false);
        animationState.setData(DataTickets.TICK, (Object)((GeoAnimatable)blockEntity2).getTick((Object)blockEntity2));
        animationState.setData(DataTickets.BLOCK_ENTITY, (Object)blockEntity2);
        try {
            bF_232.addAdditionalStateData((GeoAnimatable)blockEntity2, l, (object, object2) -> animationState.setData((DataTicket)object, object2));
            bF_232.handleAnimations((GeoAnimatable)blockEntity2, l, animationState, 1.0f);
            bFBlockRenderer.defaultRender(poseStack2, (GeoAnimatable)blockEntity2, (MultiBufferSource)minecraft.renderBuffers().bufferSource(), null, bF_364, 0.0f, 1.0f, n);
        }
        catch (RuntimeException runtimeException) {
            BFLog.logThrowable("Failed to compile baked entity block", runtimeException, new Object[0]);
            return false;
        }
        if (minecraft.player != null && minecraft.player.isCreative()) {
            operation.call(new Object[]{blockRenderDispatcher, blockState, blockPos, blockAndTintGetter, poseStack, vertexConsumer, bl, randomSource, modelData, renderType});
        }
        return true;
    }
}

