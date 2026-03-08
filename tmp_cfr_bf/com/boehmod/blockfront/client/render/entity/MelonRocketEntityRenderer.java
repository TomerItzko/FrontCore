/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.MelonRocketEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public final class MelonRocketEntityRenderer
extends EntityRenderer<MelonRocketEntity> {
    public static final float field_1794 = 0.2f;
    public static final float field_1795 = 8.0f;
    public static final float field_1796 = 9.0f;

    public MelonRocketEntityRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }

    public void render(@NotNull MelonRocketEntity melonRocketEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        assert (clientLevel != null);
        BlockRenderDispatcher blockRenderDispatcher = minecraft.getBlockRenderer();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        poseStack.pushPose();
        poseStack.scale(0.2f, 0.2f, 0.2f);
        poseStack.mulPose(Axis.XP.rotationDegrees(((float)melonRocketEntity.tickCount + f2) * 8.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(((float)melonRocketEntity.tickCount + f2) * 9.0f));
        BFRendering.block(clientLevel, blockRenderDispatcher, bufferSource, Blocks.MELON.defaultBlockState(), poseStack, -0.5, -0.5, -0.5);
        poseStack.popPose();
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull MelonRocketEntity melonRocketEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((MelonRocketEntity)entity);
    }

    public /* synthetic */ void render(@NotNull Entity entity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((MelonRocketEntity)entity, f, f2, poseStack, multiBufferSource, n);
    }
}

