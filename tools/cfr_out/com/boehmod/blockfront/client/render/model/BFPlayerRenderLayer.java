/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelLayerLocation
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.model;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.model.BackpackModel;
import com.boehmod.blockfront.client.render.model.BotRenderLayer;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.unnamed.BF_229;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.HumanoidModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BFPlayerRenderLayer
extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    @NotNull
    private static final Map<String, ResourceLocation> field_1765 = new Object2ObjectOpenHashMap();
    @NotNull
    public final PlayerModel<AbstractClientPlayer> playerModel;
    @NotNull
    public final BackpackModel<AbstractClientPlayer> backpackModel;
    @NotNull
    public final BF_229<AbstractClientPlayer> field_1761;

    public BFPlayerRenderLayer(@NotNull EntityRendererProvider.Context context, @NotNull RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent, @NotNull BF_229<AbstractClientPlayer> bF_229, boolean bl) {
        super(renderLayerParent);
        ModelLayerLocation modelLayerLocation = bl ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER;
        this.playerModel = new BF_229<AbstractClientPlayer>(context.bakeLayer(modelLayerLocation), bl);
        this.backpackModel = new BackpackModel(context.bakeLayer(BotRenderLayer.GAME_BACKPACK));
        this.field_1761 = bF_229;
    }

    @NotNull
    public PlayerModel<AbstractClientPlayer> method_1277() {
        return this.playerModel;
    }

    @NotNull
    public static ResourceLocation method_1279(@NotNull String string) {
        return field_1765.computeIfAbsent(string, BFRes::loc);
    }

    private void method_1278(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull EntityModel<AbstractClientPlayer> entityModel, @NotNull ResourceLocation resourceLocation) {
        RenderType renderType = this.field_1761.renderType(resourceLocation);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
        entityModel.renderToBuffer(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY, ColorReferences.COLOR_WHITE_SOLID);
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull AbstractClientPlayer abstractClientPlayer, float f, float f2, float f3, float f4, float f5, float f6) {
        ItemStack itemStack;
        Object object;
        if (abstractClientPlayer instanceof FakePlayer && ((FakePlayer)((Object)(object = (FakePlayer)abstractClientPlayer))).method_473()) {
            return;
        }
        object = BFClientManager.getInstance();
        assert (object != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)((BFAbstractManager)object).getPlayerDataHandler();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData((Player)abstractClientPlayer);
        if (!abstractClientPlayer.isAlive() || bFClientPlayerData.isOutOfGame()) {
            return;
        }
        PlayerModel playerModel = (PlayerModel)this.getParentModel();
        playerModel.leftSleeve.zScale = 1.0f;
        playerModel.leftSleeve.yScale = 1.0f;
        playerModel.leftSleeve.xScale = 1.0f;
        playerModel.rightSleeve.zScale = 1.0f;
        playerModel.rightSleeve.yScale = 1.0f;
        playerModel.rightSleeve.xScale = 1.0f;
        playerModel.leftPants.zScale = 1.0f;
        playerModel.leftPants.yScale = 1.0f;
        playerModel.leftPants.xScale = 1.0f;
        playerModel.rightPants.zScale = 1.0f;
        playerModel.rightPants.yScale = 1.0f;
        playerModel.rightPants.xScale = 1.0f;
        playerModel.jacket.zScale = 1.0f;
        playerModel.jacket.yScale = 1.0f;
        playerModel.jacket.xScale = 1.0f;
        playerModel.hat.zScale = 1.0f;
        playerModel.hat.yScale = 1.0f;
        playerModel.hat.xScale = 1.0f;
        playerModel.copyPropertiesTo(this.playerModel);
        this.playerModel.prepareMobModel((LivingEntity)abstractClientPlayer, f, f2, f3);
        this.playerModel.setupAnim((LivingEntity)abstractClientPlayer, f, f2, f4, f5, f6);
        HumanoidModelUtils.match(this.playerModel, playerModel);
        this.playerModel.leftSleeve.zScale = 1.0f;
        this.playerModel.leftSleeve.yScale = 1.0f;
        this.playerModel.leftSleeve.xScale = 1.0f;
        this.playerModel.rightSleeve.zScale = 1.0f;
        this.playerModel.rightSleeve.yScale = 1.0f;
        this.playerModel.rightSleeve.xScale = 1.0f;
        this.playerModel.leftPants.zScale = 1.0f;
        this.playerModel.leftPants.yScale = 1.0f;
        this.playerModel.leftPants.xScale = 1.0f;
        this.playerModel.rightPants.zScale = 1.0f;
        this.playerModel.rightPants.yScale = 1.0f;
        this.playerModel.rightPants.xScale = 1.0f;
        this.playerModel.jacket.zScale = 1.0f;
        this.playerModel.jacket.yScale = 1.0f;
        this.playerModel.jacket.xScale = 1.0f;
        float f7 = 1.05f;
        this.playerModel.jacket.visible = true;
        this.playerModel.head.zScale = 1.05f;
        this.playerModel.head.yScale = 1.05f;
        this.playerModel.head.xScale = 1.05f;
        this.playerModel.hat.zScale = 1.03f;
        this.playerModel.hat.yScale = 1.03f;
        this.playerModel.hat.xScale = 1.03f;
        this.backpackModel.field_1373 = this.playerModel.crouching;
        this.backpackModel.setupAnim(abstractClientPlayer, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        playerModel.leftSleeve.visible = false;
        playerModel.rightSleeve.visible = false;
        playerModel.jacket.visible = false;
        playerModel.leftPants.visible = false;
        playerModel.rightPants.visible = false;
        ResourceLocation resourceLocation = bFClientPlayerData.method_1154();
        if (resourceLocation != null) {
            this.method_1278(poseStack, multiBufferSource, n, (EntityModel<AbstractClientPlayer>)this.playerModel, resourceLocation);
        }
        boolean bl = true;
        AbstractGameClient<?, ?> abstractGameClient = ((BFClientManager)object).getGameClient();
        if (abstractGameClient != null && !abstractGameClient.method_2713(abstractClientPlayer)) {
            bl = false;
        }
        if (!(itemStack = abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST)).isEmpty()) {
            bl = false;
        }
        ResourceLocation resourceLocation2 = bFClientPlayerData.method_1155();
        if (bl && resourceLocation2 != null) {
            this.method_1278(poseStack, multiBufferSource, n, (EntityModel<AbstractClientPlayer>)this.backpackModel, resourceLocation2);
        }
    }
}

