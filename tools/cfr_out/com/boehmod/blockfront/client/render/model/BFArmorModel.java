/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Iterables
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.HumanoidModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.model;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.util.math.MathUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BFArmorModel
extends HumanoidModel<BotEntity> {
    @NotNull
    public final ModelPart field_1378;
    @NotNull
    public final ModelPart field_1379;
    @NotNull
    public final ModelPart field_1380;
    @NotNull
    public final ModelPart field_1381;
    @NotNull
    public final ModelPart field_1382;
    private final boolean field_1377;

    public BFArmorModel(@NotNull ModelPart modelPart, boolean bl) {
        super(modelPart, RenderType::entityTranslucent);
        this.field_1377 = bl;
        this.field_1378 = modelPart.getChild("left_sleeve");
        this.field_1379 = modelPart.getChild("right_sleeve");
        this.field_1380 = modelPart.getChild("left_pants");
        this.field_1381 = modelPart.getChild("right_pants");
        this.field_1382 = modelPart.getChild("jacket");
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, boolean hasRightArm, float yOffset) {
        MeshDefinition meshDefinition = HumanoidModel.createMesh((CubeDeformation)deformation, (float)0.0f);
        PartDefinition partDefinition = meshDefinition.getRoot();
        if (hasRightArm) {
            partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, deformation), PartPose.offset((float)5.0f, (float)(2.5f + yOffset), (float)0.0f));
            partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, deformation), PartPose.offset((float)-5.0f, (float)(2.5f + yOffset), (float)0.0f));
            partDefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)5.0f, (float)(2.5f + yOffset), (float)0.0f));
            partDefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)-5.0f, (float)(2.5f + yOffset), (float)0.0f));
        } else {
            partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation), PartPose.offset((float)5.0f, (float)(2.0f + yOffset), (float)0.0f));
            partDefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)5.0f, (float)(2.0f + yOffset), (float)0.0f));
            partDefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)-5.0f, (float)(2.0f + yOffset), (float)0.0f));
        }
        partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation), PartPose.offset((float)1.9f, (float)(12.0f + yOffset), (float)0.0f));
        partDefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)1.9f, (float)(12.0f + yOffset), (float)0.0f));
        partDefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)-1.9f, (float)(12.0f + yOffset), (float)0.0f));
        partDefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)0.0f, (float)yOffset, (float)0.0f));
        return meshDefinition;
    }

    @NotNull
    protected Iterable<ModelPart> bodyParts() {
        return Iterables.concat((Iterable)super.bodyParts(), (Iterable)ImmutableList.of((Object)this.field_1380, (Object)this.field_1381, (Object)this.field_1378, (Object)this.field_1379, (Object)this.field_1382));
    }

    public void setupAnim(@NotNull BotEntity botEntity, float f, float f2, float f3, float f4, float f5) {
        float f6;
        super.setupAnim((LivingEntity)botEntity, f, f2, f3, f4, f5);
        float f7 = BFRendering.getRenderTime() + (float)botEntity.getId();
        ItemStack itemStack = botEntity.getMainHandItem();
        float f8 = MathUtils.getTickDelta(Minecraft.getInstance());
        if (!itemStack.isEmpty()) {
            Item item;
            Item item2 = item = itemStack.getItem();
            Objects.requireNonNull(item2);
            Item item3 = item2;
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{GunItem.class, GrenadeFragItem.class, RadioItem.class}, (Object)item3, n)) {
                case 0: {
                    GunItem gunItem = (GunItem)item3;
                    boolean bl = botEntity.method_2006();
                    boolean bl2 = botEntity.method_2004();
                    boolean bl3 = botEntity.isSprinting();
                    float f9 = 0.05f * (float)(botEntity.method_2001() > 0 ? 1 : 0);
                    float f10 = !bl3 ? 0.0f : Mth.sin((float)(f7 / 4.0f)) / 14.0f;
                    this.rightArm.xRot = (!bl2 ? -0.5f + f10 : -1.6f + this.head.xRot) - f9;
                    this.rightArm.yRot = !bl2 ? -1.2f + f10 : this.head.yRot;
                    float f11 = this.rightArm.zRot = !bl2 ? -0.4f + f10 : this.head.zRot;
                    if (gunItem.isSecondary()) {
                        this.leftArm.xRot = !bl2 ? -0.2f + f10 : -1.6f + this.head.xRot;
                        this.leftArm.yRot = !bl2 ? -0.5f + f10 : 0.9f + this.head.yRot;
                        this.leftArm.zRot = !bl2 ? -0.4f + f10 : this.head.zRot;
                    } else if (!bl) {
                        this.leftArm.xRot = !bl2 ? -0.4f + f10 : -1.6f + this.head.xRot;
                        this.leftArm.yRot = !bl2 ? -0.5f + f10 : 0.9f + this.head.yRot;
                        float f12 = this.leftArm.zRot = !bl2 ? 0.6f + f10 : this.head.zRot;
                    }
                    if (!bl) break;
                    float f13 = Mth.sin((float)(f7 / 25.0f));
                    float f14 = Mth.sin((float)(f7 / 5.0f));
                    float f15 = Mth.sin((float)(f7 / 15.0f));
                    this.rightArm.xRot = 0.2f * (f13 *= f14 * f15) - 0.5f;
                    this.rightArm.yRot = -0.4f;
                    this.rightArm.zRot = 0.0f;
                    this.leftArm.xRot = -0.6f;
                    this.leftArm.yRot = 0.6f + 0.2f * f13;
                    this.leftArm.zRot = 0.6f;
                    break;
                }
                case 1: {
                    GrenadeFragItem grenadeFragItem = (GrenadeFragItem)item3;
                    this.leftArm.xRot = -1.6f + this.head.xRot;
                    this.leftArm.yRot = this.head.yRot;
                    this.rightArm.xRot = -2.6f + this.head.xRot;
                    this.rightArm.yRot = this.head.yRot;
                    this.rightArm.zRot = -0.2f;
                    break;
                }
                case 2: {
                    RadioItem radioItem = (RadioItem)item3;
                    this.leftArm.xRot = -1.6f + this.head.xRot;
                    this.leftArm.yRot = this.head.yRot;
                    this.rightArm.xRot = -1.2f + this.head.xRot;
                    this.rightArm.yRot = this.head.yRot;
                    this.rightArm.zRot = 0.8f;
                    break;
                }
            }
        }
        if (botEntity.method_2008()) {
            this.hat.xRot = 1.0f;
            this.head.xRot = 1.0f;
            this.leftArm.xRot = -0.7f;
            this.leftArm.yRot = 0.75f;
            this.leftArm.zRot = 0.7f;
        }
        this.head.yRot = f6 = 0.75f * MathUtils.lerpf1(botEntity.field_2372, botEntity.field_2373, f8);
        this.hat.yRot = f6;
        this.field_1380.copyFrom(this.leftLeg);
        this.field_1381.copyFrom(this.rightLeg);
        this.field_1378.copyFrom(this.leftArm);
        this.field_1379.copyFrom(this.rightArm);
        this.field_1382.copyFrom(this.body);
    }

    public void setAllVisible(boolean bl) {
        super.setAllVisible(bl);
        this.field_1378.visible = bl;
        this.field_1379.visible = bl;
        this.field_1380.visible = bl;
        this.field_1381.visible = bl;
        this.field_1382.visible = bl;
    }

    public void translateToHand(@NotNull HumanoidArm humanoidArm, @NotNull PoseStack poseStack) {
        ModelPart modelPart = this.getArm(humanoidArm);
        if (this.field_1377) {
            float f = 0.5f * (float)(humanoidArm == HumanoidArm.RIGHT ? 1 : -1);
            modelPart.x += f;
            modelPart.translateAndRotate(poseStack);
            modelPart.x -= f;
        } else {
            modelPart.translateAndRotate(poseStack);
        }
    }

    public /* synthetic */ void setupAnim(@NotNull LivingEntity livingEntity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((BotEntity)livingEntity, f, f2, f3, f4, f5);
    }

    public /* synthetic */ void setupAnim(@NotNull Entity entity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((BotEntity)entity, f, f2, f3, f4, f5);
    }
}

