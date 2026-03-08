/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
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
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class MedicalBagEntityRenderer<T extends Entity>
extends EntityRenderer<T> {
    private static final float field_1650 = 1.5f;
    private static final float field_1651 = 0.5f;
    @NotNull
    private final ItemRenderer field_1649;
    @NotNull
    private final ItemStack field_1652;

    public MedicalBagEntityRenderer(@NotNull EntityRendererProvider.Context context, @NotNull ItemRenderer itemRenderer, @NotNull DeferredHolder<Item, ? extends Item> deferredHolder) {
        super(context);
        this.field_1649 = itemRenderer;
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
        this.field_1652 = new ItemStack((ItemLike)deferredHolder.get());
    }

    public void render(@NotNull T t, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        super.render(t, f, f2, poseStack, multiBufferSource, n);
        BakedModel bakedModel = this.field_1649.getModel(this.field_1652, t.level(), null, t.getId());
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-t.getViewYRot(f2)));
        poseStack.mulPose(Axis.XP.rotationDegrees(t.getViewXRot(f2)));
        float f3 = this.method_1181();
        poseStack.scale(f3, f3, f3);
        Vec3 vec3 = this.method_1180();
        poseStack.translate(vec3.x(), vec3.y(), vec3.z());
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.5f, 0.0f);
        this.field_1649.render(this.field_1652, ItemDisplayContext.NONE, false, poseStack, multiBufferSource, n, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
        poseStack.popPose();
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull T t) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    public float method_1181() {
        return 1.5f;
    }

    @NotNull
    public Vec3 method_1180() {
        return Vec3.ZERO;
    }
}

