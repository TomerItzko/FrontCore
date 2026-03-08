/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.HumanoidMobRenderer
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaterniond
 *  org.joml.Quaterniondc
 *  org.joml.Vector3d
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.ClientCorpseManager;
import com.boehmod.blockfront.client.corpse.CorpseBodyPart;
import com.boehmod.blockfront.client.corpse.CorpseContext;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayer;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayerType;
import com.boehmod.blockfront.client.corpse.render.IRagdollEntityRenderer;
import com.boehmod.blockfront.client.particle.corpse.EmittingCorpseParticle;
import com.boehmod.blockfront.client.render.model.BFArmorModel;
import com.boehmod.blockfront.client.render.model.BotRenderLayer;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.net.packet.BFSpawnCorpsePacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public final class BotRenderer
extends HumanoidMobRenderer<BotEntity, BFArmorModel>
implements IRagdollEntityRenderer<BotEntity> {
    public static final ResourceLocation field_1779 = BFRes.loc("textures/models/entities/corpse/burned.png");
    @NotNull
    private final BFArmorModel field_1776;
    @NotNull
    private final BotRenderLayer field_1777;

    public BotRenderer(EntityRendererProvider.Context context) {
        super(context, (HumanoidModel)new BFArmorModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        this.field_1776 = new BFArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.field_1777 = new BotRenderLayer(this, context);
        this.addLayer(this.field_1777);
    }

    public void render(BotEntity botEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        if (botEntity.isDeadOrDying()) {
            return;
        }
        BFArmorModel bFArmorModel = (BFArmorModel)this.model;
        if (botEntity.method_2030()) {
            this.model = this.field_1776;
        }
        super.render((LivingEntity)botEntity, f, f2, poseStack, multiBufferSource, n);
        this.model = bFArmorModel;
    }

    protected float getShadowRadius(@NotNull BotEntity botEntity) {
        if (botEntity.isDeadOrDying()) {
            return 0.0f;
        }
        return super.getShadowRadius((Mob)botEntity);
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(BotEntity botEntity) {
        return botEntity.method_2024();
    }

    @Override
    @NotNull
    public Collection<CorpsePartPhysics> createParts(@NotNull ClientCorpseManager clientCorpseManager, @NotNull BFSpawnCorpsePacket.Context context, @NotNull BotEntity botEntity, @NotNull CorpseContext corpseContext) {
        ResourceLocation resourceLocation;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        ResourceLocation resourceLocation2 = context.burned() ? field_1779 : this.getTextureLocationAlt(botEntity);
        ResourceLocation resourceLocation3 = abstractGame != null ? botEntity.method_1984(abstractGame) : null;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        double d = 1.0;
        double d2 = 0.4;
        CorpsePartPhysics corpsePartPhysics = this.method_1306(CorpseBodyPart.BODY, corpseContext, botEntity, resourceLocation2, resourceLocation3, bFArmorModel -> bFArmorModel.body, bFArmorModel -> bFArmorModel.field_1382, 1.0);
        objectArrayList.add((Object)corpsePartPhysics);
        CorpsePartPhysics corpsePartPhysics2 = this.method_1306(CorpseBodyPart.HEAD, corpseContext, botEntity, resourceLocation2, resourceLocation3, bFArmorModel -> bFArmorModel.head, bFArmorModel -> bFArmorModel.hat, 0.4);
        objectArrayList.add((Object)corpsePartPhysics2);
        CorpsePartPhysics corpsePartPhysics3 = this.method_1306(CorpseBodyPart.ARM, corpseContext, botEntity, resourceLocation2, resourceLocation3, bFArmorModel -> bFArmorModel.leftArm, bFArmorModel -> bFArmorModel.field_1378, 1.0);
        objectArrayList.add((Object)corpsePartPhysics3);
        CorpsePartPhysics corpsePartPhysics4 = this.method_1306(CorpseBodyPart.ARM, corpseContext, botEntity, resourceLocation2, resourceLocation3, bFArmorModel -> bFArmorModel.rightArm, bFArmorModel -> bFArmorModel.field_1379, 1.0);
        objectArrayList.add((Object)corpsePartPhysics4);
        CorpsePartPhysics corpsePartPhysics5 = this.method_1306(CorpseBodyPart.LEG, corpseContext, botEntity, resourceLocation2, resourceLocation3, bFArmorModel -> bFArmorModel.leftLeg, bFArmorModel -> bFArmorModel.field_1380, 1.0);
        objectArrayList.add((Object)corpsePartPhysics5);
        CorpsePartPhysics corpsePartPhysics6 = this.method_1306(CorpseBodyPart.LEG, corpseContext, botEntity, resourceLocation2, resourceLocation3, bFArmorModel -> bFArmorModel.rightLeg, bFArmorModel -> bFArmorModel.field_1381, 1.0);
        objectArrayList.add((Object)corpsePartPhysics6);
        Vector3dc vector3dc = CorpsePartPhysics.field_37;
        Vector3dc vector3dc2 = CorpsePartPhysics.field_40;
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        boolean bl = BFClientSettings.CONTENT_GORE.isEnabled() && context.dismembered();
        corpsePartPhysics2.method_36(corpsePartPhysics, CorpsePartPhysics.field_42, (Quaterniondc)new Quaterniond());
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
        if (BFClientSettings.CONTENT_GORE.isEnabled()) {
            corpsePartPhysics.method_37(new EmittingCorpseParticle(100, 20, 2, (SimpleParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get()));
        }
        ResourceLocation resourceLocation4 = resourceLocation = abstractGame != null ? botEntity.method_2017(abstractGame) : null;
        if (resourceLocation != null) {
            CorpsePartPhysics corpsePartPhysics7 = ClientCorpseManager.createPart(corpseContext, CorpseBodyPart.BACKPACK, 1.0, PartPose.offset((float)0.0f, (float)24.0f, (float)0.0f), CorpsePartLayer.of(resourceLocation, this.field_1777.field_1741.backpack, CorpsePartLayerType.BASIC), CorpsePartLayer.of(resourceLocation, this.field_1777.field_1741.backpackStraps, CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation, this.field_1777.field_1741.backpackTop, CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation, this.field_1777.field_1741.backpackTopStraps, CorpsePartLayerType.COSMETIC));
            if (!bl || threadLocalRandom.nextFloat() < 0.8f) {
                corpsePartPhysics7.method_36(corpsePartPhysics, (Vector3dc)new Vector3d(), (Quaterniondc)new Quaterniond().rotateZ(0.0));
            }
            objectArrayList.add((Object)corpsePartPhysics7);
        }
        return Collections.unmodifiableCollection(objectArrayList);
    }

    private CorpsePartPhysics method_1306(@NotNull CorpseBodyPart corpseBodyPart, @NotNull CorpseContext corpseContext, @NotNull BotEntity botEntity, @NotNull ResourceLocation resourceLocation, @Nullable ResourceLocation resourceLocation2, @NotNull Function<BFArmorModel, ModelPart> function, @NotNull Function<BFArmorModel, ModelPart> function2, double d) {
        BFArmorModel bFArmorModel = botEntity.method_2030() ? this.field_1776 : (BFArmorModel)this.model;
        ModelPart modelPart = function.apply(bFArmorModel);
        if (resourceLocation2 != null) {
            BFArmorModel bFArmorModel2 = this.field_1777.method_1269(botEntity);
            return ClientCorpseManager.createPart(corpseContext, corpseBodyPart, d, modelPart.storePose(), CorpsePartLayer.of(resourceLocation, modelPart, CorpsePartLayerType.BASIC), CorpsePartLayer.of(resourceLocation, function2.apply(bFArmorModel), CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation2, function.apply(bFArmorModel2), CorpsePartLayerType.COSMETIC), CorpsePartLayer.of(resourceLocation2, function2.apply(bFArmorModel2), CorpsePartLayerType.COSMETIC));
        }
        return ClientCorpseManager.createPart(corpseContext, corpseBodyPart, d, modelPart.storePose(), CorpsePartLayer.of(resourceLocation, modelPart, CorpsePartLayerType.BASIC), CorpsePartLayer.of(resourceLocation, function2.apply(bFArmorModel), CorpsePartLayerType.COSMETIC));
    }

    @NotNull
    public ResourceLocation getTextureLocationAlt(@NotNull BotEntity botEntity) {
        return botEntity.method_2024();
    }

    protected /* synthetic */ float getShadowRadius(@NotNull Mob mob) {
        return this.getShadowRadius((BotEntity)mob);
    }

    protected /* synthetic */ float getShadowRadius(@NotNull LivingEntity livingEntity) {
        return this.getShadowRadius((BotEntity)livingEntity);
    }

    public /* synthetic */ void render(LivingEntity livingEntity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((BotEntity)livingEntity, f, f2, poseStack, multiBufferSource, n);
    }

    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocationEntity(Entity entity) {
        return this.getTextureLocation((BotEntity)entity);
    }

    protected /* synthetic */ float getShadowRadius(@NotNull Entity entity) {
        return this.getShadowRadius((BotEntity)entity);
    }

    public /* synthetic */ void render(Entity entity, float f, float f2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int n) {
        this.render((BotEntity)entity, f, f2, poseStack, multiBufferSource, n);
    }

    @Override
    @NotNull
    public /* synthetic */ ResourceLocation getTextureLocation(@NotNull Entity entity) {
        return this.getTextureLocationAlt((BotEntity)entity);
    }
}

