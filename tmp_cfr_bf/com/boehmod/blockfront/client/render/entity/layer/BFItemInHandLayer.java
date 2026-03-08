/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity.layer;

import com.boehmod.blockfront.registry.BFItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BFItemInHandLayer<T extends Player, M extends EntityModel<T> & HeadedModel>
extends PlayerItemInHandLayer<T, M> {
    public static final float field_1759 = -0.55f;
    public static final float field_1760 = -0.9f;
    @NotNull
    private final ItemInHandRenderer field_1758;

    public BFItemInHandLayer(@NotNull RenderLayerParent<T, M> renderLayerParent, @NotNull ItemInHandRenderer itemInHandRenderer) {
        super(renderLayerParent, itemInHandRenderer);
        this.field_1758 = itemInHandRenderer;
    }

    protected void renderArmWithItem(@NotNull LivingEntity livingEntity, ItemStack itemStack, @NotNull ItemDisplayContext itemDisplayContext, @NotNull HumanoidArm humanoidArm, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        if (itemStack.is(BFItems.BINOCULARS) && livingEntity.getUseItem() == itemStack && livingEntity.swingTime == 0) {
            this.method_1276(livingEntity, itemStack, poseStack, multiBufferSource, n);
        } else {
            super.renderArmWithItem(livingEntity, itemStack, itemDisplayContext, humanoidArm, poseStack, multiBufferSource, n);
        }
    }

    private void method_1276(@NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        poseStack.pushPose();
        ModelPart modelPart = ((HeadedModel)this.getParentModel()).getHead();
        float f = modelPart.xRot;
        modelPart.xRot = Mth.clamp((float)modelPart.xRot, (float)-0.5235988f, (float)1.5707964f);
        modelPart.translateAndRotate(poseStack);
        modelPart.xRot = f;
        CustomHeadLayer.translateToHead((PoseStack)poseStack, (boolean)false);
        poseStack.translate(0.0f, -0.55f, -0.9f);
        this.field_1758.renderItem(livingEntity, itemStack, ItemDisplayContext.HEAD, false, poseStack, multiBufferSource, n);
        poseStack.popPose();
    }
}

