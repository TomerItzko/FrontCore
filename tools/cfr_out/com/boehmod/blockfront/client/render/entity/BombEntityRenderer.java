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
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.common.entity.BombEntity;
import com.boehmod.blockfront.registry.BFItems;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public final class BombEntityRenderer
extends EntityRenderer<BombEntity> {
    @NotNull
    private final ItemRenderer field_1775;

    public BombEntityRenderer(@NotNull EntityRendererProvider.Context context, @NotNull ItemRenderer itemRenderer) {
        super(context);
        this.field_1775 = itemRenderer;
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }

    public void render(@NotNull BombEntity bombEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        ItemStack itemStack = new ItemStack((ItemLike)this.method_1304());
        BakedModel bakedModel = this.field_1775.getModel(itemStack, bombEntity.level(), null, bombEntity.getId());
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-bombEntity.getYRot()));
        poseStack.translate(0.0f, 0.23f, 0.0f);
        this.field_1775.render(itemStack, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, n, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
        super.render((Entity)bombEntity, f, f2, poseStack, multiBufferSource, n);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull BombEntity bombEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    private Item method_1304() {
        return (Item)BFItems.BOMB.get();
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocation((BombEntity)entity);
    }

    public /* synthetic */ void render(@NotNull Entity entity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((BombEntity)entity, f, f2, poseStack, multiBufferSource, n);
    }
}

