/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.LevelRenderer
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.ClientHooks
 *  org.spongepowered.asm.mixin.Debug
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.boehmod.blockfront.mixin;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export=true)
@Mixin(value={ClientHooks.class})
public class ClientHooksMixin {
    @Inject(method={"isBlockEntityRendererVisible"}, at={@At(value="HEAD")}, cancellable=true)
    private static void bf$preIsBlockEntityRendererVisible(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, Frustum frustum, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        BlockEntityRenderer blockEntityRenderer;
        Object object;
        if (blockEntity instanceof BFBlockEntity && !((BFBlockEntity)((Object)(object = (BFBlockEntity)blockEntity))).method_1882()) {
            callbackInfoReturnable.setReturnValue((Object)false);
            return;
        }
        object = Minecraft.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = object.getEntityRenderDispatcher();
        if (entityRenderDispatcher.shouldRenderHitBoxes() && (blockEntityRenderer = blockEntityRenderDispatcher.getRenderer(blockEntity)) != null) {
            Vec3 vec3 = ((Minecraft)object).gameRenderer.getMainCamera().getPosition();
            AABB aABB = blockEntityRenderer.getRenderBoundingBox(blockEntity).move(vec3.scale(-1.0));
            PoseStack poseStack = new PoseStack();
            LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)object.renderBuffers().bufferSource().getBuffer(RenderType.lines()), (AABB)aABB, (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

