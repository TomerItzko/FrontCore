/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.model;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.entity.BotRenderer;
import com.boehmod.blockfront.client.render.model.BFArmorModel;
import com.boehmod.blockfront.client.render.model.BackpackModel;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.HumanoidModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BotRenderLayer
extends RenderLayer<BotEntity, BFArmorModel> {
    public static final ModelLayerLocation EYES = new ModelLayerLocation(BFRes.loc("bot"), "eyes_layer");
    public static final ModelLayerLocation GAME_ARMOR = new ModelLayerLocation(BFRes.loc("bot"), "game_armor_layer");
    public static final ModelLayerLocation GAME_ARMOR_SLIM = new ModelLayerLocation(BFRes.loc("bot"), "game_armor_layer_slim");
    public static final ModelLayerLocation GAME_BACKPACK = new ModelLayerLocation(BFRes.loc("bot"), "game_backpack_layer");
    private static final ResourceLocation field_1749 = BFRes.loc("textures/models/entities/bot/eyes0.png");
    private static final ResourceLocation field_1750 = BFRes.loc("textures/models/entities/bot/eyes0left.png");
    private static final ResourceLocation field_1751 = BFRes.loc("textures/models/entities/bot/eyes0right.png");
    private static final ResourceLocation field_1752 = BFRes.loc("textures/models/entities/bot/mouth0.png");
    private static final ResourceLocation field_1753 = BFRes.loc("textures/models/entities/bot/mouth1.png");
    @NotNull
    public final HumanoidModel<BotEntity> field_1742;
    @NotNull
    public final BFArmorModel field_1744;
    @NotNull
    public final BFArmorModel field_1747;
    @NotNull
    public final BackpackModel<BotEntity> field_1741;

    public BotRenderLayer(@NotNull BotRenderer botRenderer, @NotNull EntityRendererProvider.Context context) {
        super((RenderLayerParent)botRenderer);
        this.field_1742 = new HumanoidModel(context.bakeLayer(EYES));
        this.field_1744 = new BFArmorModel(context.bakeLayer(GAME_ARMOR), false);
        this.field_1747 = new BFArmorModel(context.bakeLayer(GAME_ARMOR_SLIM), true);
        this.field_1741 = new BackpackModel(context.bakeLayer(GAME_BACKPACK));
    }

    public BFArmorModel method_1269(@NotNull BotEntity botEntity) {
        return botEntity.method_2030() ? this.field_1747 : this.field_1744;
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull BotEntity botEntity, float f, float f2, float f3, float f4, float f5, float f6) {
        poseStack.pushPose();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        BFArmorModel bFArmorModel = (BFArmorModel)this.getParentModel();
        BFArmorModel bFArmorModel2 = this.method_1269(botEntity);
        bFArmorModel.copyPropertiesTo(this.field_1742);
        bFArmorModel.copyPropertiesTo(bFArmorModel2);
        this.field_1742.prepareMobModel((LivingEntity)botEntity, f, f2, f3);
        bFArmorModel2.prepareMobModel((LivingEntity)botEntity, f, f2, f3);
        this.field_1742.setupAnim((LivingEntity)botEntity, f, f2, f4, f5, f6);
        bFArmorModel2.setupAnim(botEntity, f, f2, f4, f5, f6);
        HumanoidModelUtils.match(this.field_1742, bFArmorModel);
        HumanoidModelUtils.match(bFArmorModel2, bFArmorModel);
        this.field_1742.hat.visible = true;
        bFArmorModel2.hat.visible = true;
        this.field_1741.field_1373 = bFArmorModel2.crouching;
        this.field_1741.setupAnim(botEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        this.method_1270(botEntity, poseStack, multiBufferSource, n);
        this.method_1271(bFClientManager, botEntity, poseStack, multiBufferSource, bFArmorModel2, n);
        poseStack.popPose();
    }

    private void method_1270(@NotNull BotEntity botEntity, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        ResourceLocation resourceLocation;
        if (botEntity.method_1999() <= 0) {
            resourceLocation = botEntity.field_2363 ? (botEntity.field_2361 ? field_1750 : field_1751) : field_1749;
            this.method_1273(poseStack, multiBufferSource, (EntityModel<BotEntity>)this.field_1742, resourceLocation, n);
        }
        if (botEntity.method_2007() > 0) {
            resourceLocation = botEntity.method_2002() ? field_1753 : field_1752;
            this.method_1273(poseStack, multiBufferSource, (EntityModel<BotEntity>)this.field_1742, resourceLocation, n);
        }
    }

    private void method_1271(@NotNull BFClientManager bFClientManager, @NotNull BotEntity botEntity, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull HumanoidModel<BotEntity> humanoidModel, int n) {
        ResourceLocation resourceLocation;
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        ResourceLocation resourceLocation2 = botEntity.method_1984(abstractGame);
        if (resourceLocation2 != null) {
            this.method_1273(poseStack, multiBufferSource, (EntityModel<BotEntity>)humanoidModel, resourceLocation2, n);
        }
        if ((resourceLocation = botEntity.method_2017(abstractGame)) != null) {
            this.method_1273(poseStack, multiBufferSource, (EntityModel<BotEntity>)this.field_1741, resourceLocation, n);
        }
    }

    private void method_1273(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull EntityModel<BotEntity> entityModel, @NotNull ResourceLocation resourceLocation, int n) {
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer((MultiBufferSource)multiBufferSource, (RenderType)RenderType.entityCutoutNoCull((ResourceLocation)resourceLocation), (boolean)false, (boolean)false);
        entityModel.renderToBuffer(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY, ColorReferences.COLOR_WHITE_SOLID);
    }

    public /* synthetic */ void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        this.render(poseStack, multiBufferSource, n, (BotEntity)entity, f, f2, f3, f4, f5, f6);
    }
}

