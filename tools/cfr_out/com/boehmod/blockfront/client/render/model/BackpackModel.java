/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.AgeableListModel
 *  net.minecraft.client.model.ArmedModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BackpackModel<T extends LivingEntity>
extends AgeableListModel<T>
implements ArmedModel {
    @NotNull
    public final ModelPart backpackTop;
    @NotNull
    public final ModelPart backpackTopStraps;
    @NotNull
    public final ModelPart backpack;
    @NotNull
    public final ModelPart backpackStraps;
    public boolean field_1373;
    public float field_1375;

    public BackpackModel(@NotNull ModelPart modelPart) {
        this(modelPart, RenderType::entityCutoutNoCull);
    }

    public BackpackModel(@NotNull ModelPart modelPart, @NotNull Function<ResourceLocation, RenderType> function) {
        super(function, false, 16.0f, 0.0f, 2.0f, 2.0f, 2.0f);
        this.backpackTop = modelPart.getChild("backpack_top");
        this.backpackTopStraps = modelPart.getChild("backpack_top_straps");
        this.backpack = modelPart.getChild("backpack");
        this.backpackStraps = modelPart.getChild("backpack_straps");
    }

    @NotNull
    public static MeshDefinition createMesh(@NotNull CubeDeformation deformation) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("backpack_top", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5f, -25.75f, 1.75f, 9.0f, 5.0f, 5.0f, deformation), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        partDefinition.addOrReplaceChild("backpack_top_straps", CubeListBuilder.create().texOffs(0, 10).addBox(-4.0f, -20.75f, 2.0f, 8.0f, 8.0f, 4.0f, deformation), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        partDefinition.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(24, 10).addBox(-4.0f, -20.75f, 2.0f, 8.0f, 8.0f, 4.0f, deformation.extend(0.25f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        partDefinition.addOrReplaceChild("backpack_straps", CubeListBuilder.create().texOffs(28, 0).addBox(-4.5f, -25.75f, 1.75f, 9.0f, 5.0f, 5.0f, deformation.extend(0.25f)), PartPose.offset((float)0.0f, (float)0.0f, (float)0.0f));
        return meshDefinition;
    }

    public void renderToBuffer(PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int n, int n2, int n3) {
        poseStack.pushPose();
        if (this.field_1373) {
            poseStack.translate(0.0f, 1.35f, 0.7f);
        } else {
            poseStack.translate(0.0f, 1.5f, 0.0f);
        }
        for (ModelPart modelPart : this.bodyParts()) {
            modelPart.render(poseStack, vertexConsumer, n, n2, n3);
        }
        poseStack.popPose();
    }

    @NotNull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @NotNull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of((Object)this.backpackTop, (Object)this.backpackTopStraps, (Object)this.backpack, (Object)this.backpackStraps);
    }

    public void translateToHand(@NotNull HumanoidArm humanoidArm, @NotNull PoseStack poseStack) {
    }

    public void setupAnim(@NotNull T t, float f, float f2, float f3, float f4, float f5) {
        this.backpack.yRot = 0.0f;
        this.method_1009(t);
        if (this.field_1373) {
            this.backpack.xRot = 0.5f;
            this.backpack.y = 3.2f;
        } else {
            this.backpack.xRot = 0.0f;
            this.backpack.y = 0.0f;
        }
        this.backpackTop.copyFrom(this.backpack);
        this.backpackTopStraps.copyFrom(this.backpack);
        this.backpackStraps.copyFrom(this.backpack);
    }

    public void prepareMobModel(@NotNull T t, float f, float f2, float f3) {
        this.field_1375 = t.getSwimAmount(f3);
        super.prepareMobModel(t, f, f2, f3);
    }

    protected void method_1009(@NotNull T t) {
        if (!(this.attackTime <= 0.0f)) {
            this.backpack.yRot = Mth.sin((float)(Mth.sqrt((float)this.attackTime) * ((float)Math.PI * 2))) * 0.2f;
            if (this.method_1008(t) == HumanoidArm.LEFT) {
                this.backpack.yRot *= -1.0f;
            }
        }
    }

    @NotNull
    private HumanoidArm method_1008(@NotNull T t) {
        return ((LivingEntity)t).swingingArm == InteractionHand.MAIN_HAND ? t.getMainArm() : t.getMainArm().getOpposite();
    }

    public /* synthetic */ void prepareMobModel(@NotNull Entity entity, float f, float f2, float f3) {
        this.prepareMobModel((LivingEntity)entity, f, f2, f3);
    }
}

