/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidArmorModel
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.RenderLayerParent
 *  net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
 *  net.minecraft.client.renderer.entity.layers.RenderLayer
 *  net.minecraft.client.renderer.entity.player.PlayerRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaterniond
 *  org.joml.Quaterniondc
 *  org.joml.Vector3d
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.ClientCorpseManager;
import com.boehmod.blockfront.client.corpse.CorpseBodyPart;
import com.boehmod.blockfront.client.corpse.CorpseContext;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayer;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayerType;
import com.boehmod.blockfront.client.corpse.render.IRagdollEntityRenderer;
import com.boehmod.blockfront.client.particle.corpse.EmittingCorpseParticle;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.entity.layer.BFCapeLayer;
import com.boehmod.blockfront.client.render.entity.layer.BFItemInHandLayer;
import com.boehmod.blockfront.client.render.model.BFPlayerRenderLayer;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFSpawnCorpsePacket;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.unnamed.BF_229;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public final class BFPlayerRenderer
extends PlayerRenderer
implements IRagdollEntityRenderer<AbstractClientPlayer> {
    public static final ResourceLocation field_1791 = BFRes.loc("textures/models/entities/corpse/burned.png");
    @NotNull
    private final BFPlayerRenderLayer renderLayer;

    public BFPlayerRenderer(@NotNull EntityRendererProvider.Context context, boolean bl) {
        super(context, bl);
        BF_229<AbstractClientPlayer> bF_229 = new BF_229<AbstractClientPlayer>(context.bakeLayer(bl ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), bl);
        this.model = bF_229;
        this.layers.clear();
        this.addLayer((RenderLayer)new HumanoidArmorLayer((RenderLayerParent)this, new HumanoidModel(context.bakeLayer(bl ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(bl ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer((RenderLayer)new BFItemInHandLayer(this, context.getItemInHandRenderer()));
        this.addLayer((RenderLayer)new BFCapeLayer((RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)this));
        this.addLayer((RenderLayer)new HumanoidArmorLayer((RenderLayerParent)this, (HumanoidModel)new HumanoidArmorModel(context.bakeLayer(bl ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), (HumanoidModel)new HumanoidArmorModel(context.bakeLayer(bl ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.renderLayer = new BFPlayerRenderLayer(context, (RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)this, bF_229, bl);
        this.addLayer(this.renderLayer);
    }

    @Nullable
    public static ResourceLocation method_1358(@NotNull UUID uUID, @Nullable AbstractGameClient<?, ?> abstractGameClient, @NotNull String string, @NotNull Set<UUID> set) {
        return abstractGameClient == null ? null : abstractGameClient.method_2696(uUID, string, set);
    }

    private static void method_1360(@NotNull ModelPart modelPart, @NotNull ModelPart modelPart2) {
        modelPart.xRot = 0.0f;
        modelPart.yRot = 0.0f;
        modelPart.zRot = 0.0f;
        modelPart2.xRot = 0.0f;
        modelPart2.yRot = 0.0f;
        modelPart2.zRot = 0.0f;
        modelPart.y = 0.0f;
        modelPart.x = 0.0f;
        modelPart.z = 0.0f;
        modelPart2.y = 0.0f;
        modelPart2.x = 0.0f;
        modelPart2.z = 0.0f;
    }

    public void method_1356(@Nullable AbstractGame<?, ?, ?> abstractGame, @Nullable AbstractGameClient<?, ?> abstractGameClient, @NotNull AbstractClientPlayer abstractClientPlayer, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, boolean bl) {
        ModelPart modelPart = bl ? ((PlayerModel)this.model).leftArm : ((PlayerModel)this.model).rightArm;
        ModelPart modelPart2 = bl ? ((PlayerModel)this.model).leftSleeve : ((PlayerModel)this.model).rightSleeve;
        BF_229 bF_229 = (BF_229)this.getModel();
        bF_229.setAllVisible(true);
        if (abstractGame != null) {
            ((PlayerModel)this.model).jacket.visible = false;
            ((PlayerModel)this.model).rightPants.visible = false;
            ((PlayerModel)this.model).leftPants.visible = false;
            ((PlayerModel)this.model).rightSleeve.visible = false;
            ((PlayerModel)this.model).leftSleeve.visible = false;
        }
        bF_229.crouching = false;
        bF_229.attackTime = 0.0f;
        bF_229.swimAmount = 0.0f;
        modelPart.xRot = 0.0f;
        modelPart.yRot = -0.47f;
        modelPart.zRot = -0.37f;
        modelPart2.xRot = 0.0f;
        modelPart2.yRot = -0.47f;
        modelPart2.zRot = -0.37f;
        ((PlayerModel)this.model).copyPropertiesTo(this.renderLayer.playerModel);
        ((PlayerModel)this.model).copyPropertiesTo(this.renderLayer.field_1761);
        BFPlayerRenderer.method_1360(modelPart, modelPart2);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)this.getTextureLocation(abstractClientPlayer)));
        poseStack.pushPose();
        if (bF_229.method_1020()) {
            poseStack.translate(-0.05f, 0.0f, 0.0f);
        }
        modelPart.render(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY);
        modelPart2.render(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY);
        if (abstractGame != null && abstractGameClient != null) {
            this.method_1357(poseStack, multiBufferSource, n, modelPart, modelPart2, abstractGame, abstractGameClient, abstractClientPlayer);
        }
        poseStack.popPose();
        ((PlayerModel)this.model).jacket.visible = true;
        ((PlayerModel)this.model).rightPants.visible = true;
        ((PlayerModel)this.model).leftPants.visible = true;
        ((PlayerModel)this.model).rightSleeve.visible = true;
        ((PlayerModel)this.model).leftSleeve.visible = true;
    }

    private void method_1357(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n, @NotNull ModelPart modelPart, @NotNull ModelPart modelPart2, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull AbstractClientPlayer abstractClientPlayer) {
        UUID uUID = abstractClientPlayer.getUUID();
        FDSTagCompound fDSTagCompound = abstractGame.getPlayerStatData(uUID);
        int n2 = fDSTagCompound.getInteger(BFStats.CLASS.getKey(), -1);
        if (n2 == -1) {
            return;
        }
        boolean bl = modelPart.visible;
        boolean bl2 = modelPart2.visible;
        modelPart.visible = true;
        modelPart2.visible = true;
        Set<UUID> set = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs();
        ResourceLocation resourceLocation = BFPlayerRenderer.method_1358(uUID, abstractGameClient, MatchClass.values()[n2].getKey(), set);
        if (resourceLocation == null) {
            return;
        }
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent((ResourceLocation)resourceLocation));
        modelPart.render(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY);
        modelPart2.render(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY);
        modelPart.visible = bl;
        modelPart2.visible = bl2;
    }

    protected float getShadowRadius(@NotNull AbstractClientPlayer abstractClientPlayer) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getPlayerData((Player)abstractClientPlayer);
        if (bFClientPlayerData.method_1132() > 0) {
            return 0.0f;
        }
        return super.getShadowRadius((LivingEntity)abstractClientPlayer);
    }

    public void render(@NotNull AbstractClientPlayer abstractClientPlayer, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getPlayerData((Player)abstractClientPlayer);
        if (bFClientPlayerData.method_1132() > 0) {
            return;
        }
        Pose pose = abstractClientPlayer.getPose();
        ((PlayerModel)this.model).jacket.visible = false;
        ((PlayerModel)this.model).rightPants.visible = false;
        ((PlayerModel)this.model).leftPants.visible = false;
        ((PlayerModel)this.model).rightSleeve.visible = false;
        ((PlayerModel)this.model).leftSleeve.visible = false;
        super.render(abstractClientPlayer, f, f2, poseStack, multiBufferSource, n);
        if (!this.layers.contains((Object)this.renderLayer)) {
            this.addLayer(this.renderLayer);
        }
        abstractClientPlayer.setPose(pose);
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull AbstractClientPlayer abstractClientPlayer) {
        if (abstractClientPlayer instanceof FakePlayer) {
            FakePlayer fakePlayer = (FakePlayer)abstractClientPlayer;
            Minecraft minecraft = Minecraft.getInstance();
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            return BFRendering.getSkinTexture(minecraft, bFClientManager, fakePlayer.getGameProfile());
        }
        return super.getTextureLocation(abstractClientPlayer);
    }

    @Override
    @NotNull
    public Collection<CorpsePartPhysics> createParts(@NotNull ClientCorpseManager clientCorpseManager, @NotNull BFSpawnCorpsePacket.Context context, @NotNull AbstractClientPlayer abstractClientPlayer, @NotNull CorpseContext corpseContext) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Mod manager is null!";
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getPlayerData((Player)abstractClientPlayer);
        ResourceLocation resourceLocation = context.burned() ? field_1791 : this.getTextureLocationPlayer(abstractClientPlayer);
        ResourceLocation resourceLocation2 = bFClientPlayerData.method_1154();
        ObjectArrayList objectArrayList = new ObjectArrayList();
        double d = 1.0;
        double d2 = 0.4;
        CorpsePartPhysics corpsePartPhysics = this.method_1355(CorpseBodyPart.BODY, corpseContext, resourceLocation, resourceLocation2, playerModel -> playerModel.body, playerModel -> playerModel.jacket, 1.0);
        objectArrayList.add((Object)corpsePartPhysics);
        CorpsePartPhysics corpsePartPhysics2 = this.method_1355(CorpseBodyPart.HEAD, corpseContext, resourceLocation, resourceLocation2, playerModel -> playerModel.head, playerModel -> playerModel.hat, 0.4);
        objectArrayList.add((Object)corpsePartPhysics2);
        CorpsePartPhysics corpsePartPhysics3 = this.method_1355(CorpseBodyPart.ARM, corpseContext, resourceLocation, resourceLocation2, playerModel -> playerModel.leftArm, playerModel -> playerModel.leftSleeve, 1.0);
        objectArrayList.add((Object)corpsePartPhysics3);
        CorpsePartPhysics corpsePartPhysics4 = this.method_1355(CorpseBodyPart.ARM, corpseContext, resourceLocation, resourceLocation2, playerModel -> playerModel.rightArm, playerModel -> playerModel.rightSleeve, 1.0);
        objectArrayList.add((Object)corpsePartPhysics4);
        CorpsePartPhysics corpsePartPhysics5 = this.method_1355(CorpseBodyPart.LEG, corpseContext, resourceLocation, resourceLocation2, playerModel -> playerModel.leftLeg, playerModel -> playerModel.leftPants, 1.0);
        objectArrayList.add((Object)corpsePartPhysics5);
        CorpsePartPhysics corpsePartPhysics6 = this.method_1355(CorpseBodyPart.LEG, corpseContext, resourceLocation, resourceLocation2, playerModel -> playerModel.rightLeg, playerModel -> playerModel.rightPants, 1.0);
        objectArrayList.add((Object)corpsePartPhysics6);
        Vector3dc vector3dc = CorpsePartPhysics.field_37;
        Vector3dc vector3dc2 = CorpsePartPhysics.field_40;
        boolean bl = context.dismembered();
        corpsePartPhysics2.method_36(corpsePartPhysics, CorpsePartPhysics.field_42, (Quaterniondc)new Quaterniond());
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        if (!bl || threadLocalRandom.nextFloat() < 0.8f) {
            corpsePartPhysics6.method_36(corpsePartPhysics, vector3dc, (Quaterniondc)new Quaterniond().rotateZ(-0.39269908169872414));
        } else {
            corpsePartPhysics6.method_37(new EmittingCorpseParticle(100, 20, 2, (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get()));
        }
        if (!bl || threadLocalRandom.nextFloat() < 0.8f) {
            corpsePartPhysics5.method_36(corpsePartPhysics, vector3dc, (Quaterniondc)new Quaterniond().rotateZ(0.39269908169872414));
        } else {
            corpsePartPhysics5.method_37(new EmittingCorpseParticle(100, 20, 2, (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get()));
        }
        if (!bl || threadLocalRandom.nextFloat() < 0.8f) {
            corpsePartPhysics4.method_36(corpsePartPhysics, vector3dc2, (Quaterniondc)new Quaterniond().rotateZ(-1.5707963705062866));
        } else {
            corpsePartPhysics4.method_37(new EmittingCorpseParticle(100, 20, 2, (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get()));
        }
        if (!bl || threadLocalRandom.nextFloat() < 0.8f) {
            corpsePartPhysics3.method_36(corpsePartPhysics, vector3dc2, (Quaterniondc)new Quaterniond().rotateZ(1.5707963705062866));
        } else {
            corpsePartPhysics3.method_37(new EmittingCorpseParticle(100, 20, 2, (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get()));
        }
        corpsePartPhysics.method_37(new EmittingCorpseParticle(100, 20, 2, (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get()));
        ResourceLocation resourceLocation3 = bFClientPlayerData.method_1155();
        if (resourceLocation3 != null) {
            CorpsePartPhysics corpsePartPhysics7 = ClientCorpseManager.createPart(corpseContext, CorpseBodyPart.BACKPACK, 1.0, PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f), CorpsePartLayer.of(resourceLocation3, this.renderLayer.backpackModel.backpack, CorpsePartLayerType.BASIC), CorpsePartLayer.of(resourceLocation3, this.renderLayer.backpackModel.backpackStraps, CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation3, this.renderLayer.backpackModel.backpackTop, CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation3, this.renderLayer.backpackModel.backpackTopStraps, CorpsePartLayerType.COSMETIC));
            if (!bl || Math.random() < (double)0.8f) {
                corpsePartPhysics7.method_36(corpsePartPhysics, (Vector3dc)new Vector3d(), (Quaterniondc)new Quaterniond().rotateZ(0.0));
            }
            objectArrayList.add((Object)corpsePartPhysics7);
        }
        return Collections.unmodifiableCollection(objectArrayList);
    }

    private CorpsePartPhysics method_1355(@NotNull CorpseBodyPart corpseBodyPart, @NotNull CorpseContext corpseContext, @NotNull ResourceLocation resourceLocation, @Nullable ResourceLocation resourceLocation2, @NotNull Function<PlayerModel<AbstractClientPlayer>, ModelPart> function, @NotNull Function<PlayerModel<AbstractClientPlayer>, ModelPart> function2, double d) {
        ModelPart modelPart = function.apply((PlayerModel<AbstractClientPlayer>)((PlayerModel)this.model));
        if (resourceLocation2 != null) {
            PlayerModel<AbstractClientPlayer> playerModel = this.renderLayer.method_1277();
            return ClientCorpseManager.createPart(corpseContext, corpseBodyPart, d, modelPart.storePose(), CorpsePartLayer.of(resourceLocation, modelPart, CorpsePartLayerType.BASIC), CorpsePartLayer.of(resourceLocation, function2.apply((PlayerModel<AbstractClientPlayer>)((PlayerModel)this.model)), CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation2, function.apply(playerModel), CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation2, function2.apply(playerModel), CorpsePartLayerType.COSMETIC));
        }
        return ClientCorpseManager.createPart(corpseContext, corpseBodyPart, d, modelPart.storePose(), CorpsePartLayer.of(resourceLocation, modelPart, CorpsePartLayerType.BASIC), CorpsePartLayer.of(resourceLocation, function2.apply((PlayerModel<AbstractClientPlayer>)((PlayerModel)this.model)), CorpsePartLayerType.COSMETIC));
    }

    @NotNull
    public ResourceLocation getTextureLocationPlayer(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return abstractClientPlayer.getSkin().texture();
    }

    protected /* synthetic */ float getShadowRadius(@NotNull LivingEntity livingEntity) {
        return this.getShadowRadius((AbstractClientPlayer)livingEntity);
    }

    protected /* synthetic */ float getShadowRadius(@NotNull Entity entity) {
        return this.getShadowRadius((AbstractClientPlayer)entity);
    }
}

