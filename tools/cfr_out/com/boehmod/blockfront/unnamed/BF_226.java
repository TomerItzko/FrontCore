/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.InfectedDogEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class BF_226
extends EntityModel<InfectedDogEntity> {
    private final ModelPart field_1383;
    @NotNull
    private final ModelPart field_1384;
    @NotNull
    private final ModelPart field_1385;
    @NotNull
    private final ModelPart field_1386;
    @NotNull
    private final ModelPart field_1387;
    @NotNull
    private final ModelPart field_1388;
    @NotNull
    private final ModelPart field_1389;
    @NotNull
    private final ModelPart field_1390;
    @NotNull
    private final ModelPart field_1391;
    @NotNull
    private final ModelPart field_1392;

    public BF_226(@NotNull ModelPart modelPart) {
        this.field_1383 = modelPart.getChild("head");
        this.field_1384 = this.field_1383.getChild("real_head");
        this.field_1385 = modelPart.getChild("body");
        this.field_1392 = modelPart.getChild("upper_body");
        this.field_1386 = modelPart.getChild("right_hind_leg");
        this.field_1387 = modelPart.getChild("left_hind_leg");
        this.field_1388 = modelPart.getChild("right_front_leg");
        this.field_1389 = modelPart.getChild("left_front_leg");
        this.field_1390 = modelPart.getChild("tail");
        this.field_1391 = this.field_1390.getChild("real_tail");
    }

    @NotNull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of((Object)this.field_1383);
    }

    @NotNull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of((Object)this.field_1385, (Object)this.field_1386, (Object)this.field_1387, (Object)this.field_1388, (Object)this.field_1389, (Object)this.field_1390, (Object)this.field_1392);
    }

    public void prepareMobModel(@NotNull InfectedDogEntity infectedDogEntity, float f, float f2, float f3) {
        this.field_1385.setPos(0.0f, 14.0f, 2.0f);
        this.field_1385.xRot = 1.5707964f;
        this.field_1392.setPos(-1.0f, 14.0f, -3.0f);
        this.field_1392.xRot = this.field_1385.xRot;
        this.field_1390.setPos(-1.0f, 12.0f, 8.0f);
        this.field_1386.setPos(-2.5f, 16.0f, 7.0f);
        this.field_1387.setPos(0.5f, 16.0f, 7.0f);
        this.field_1388.setPos(-2.5f, 16.0f, -4.0f);
        this.field_1389.setPos(0.5f, 16.0f, -4.0f);
        this.field_1386.xRot = Mth.cos((float)(f * 0.6662f)) * 1.4f * f2;
        this.field_1387.xRot = Mth.cos((float)(f * 0.6662f + (float)Math.PI)) * 1.4f * f2;
        this.field_1388.xRot = Mth.cos((float)(f * 0.6662f + (float)Math.PI)) * 1.4f * f2;
        this.field_1389.xRot = Mth.cos((float)(f * 0.6662f)) * 1.4f * f2;
        float f4 = BFRendering.getRenderTime() + (float)infectedDogEntity.getId();
        float f5 = Mth.sin((float)(f4 / 4.0f));
        float f6 = 0.2f * f5;
        float f7 = Mth.sin((float)(f4 / 7.0f));
        float f8 = 0.1f * f7;
        this.field_1383.xRot = f6;
        this.field_1383.zRot = f8;
        this.field_1384.zRot = f8 + f6;
        this.field_1392.zRot = f6;
        this.field_1385.zRot = f6;
        this.field_1391.zRot = f6;
    }

    public void setupAnim(@NotNull InfectedDogEntity infectedDogEntity, float f, float f2, float f3, float f4, float f5) {
        float f6 = BFRendering.getRenderTime() + (float)infectedDogEntity.getId();
        float f7 = Mth.sin((float)(f6 / 3.0f)) * 0.5f;
        float f8 = Mth.sin((float)(f6 / 5.0f));
        this.field_1390.xRot = 1.2f + f7;
        this.field_1390.yRot = 0.0f + f8;
        this.field_1390.zRot = 0.0f;
        this.field_1383.xRot = f5 * ((float)Math.PI / 180);
        this.field_1383.yRot = f4 * ((float)Math.PI / 180);
    }

    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int n, int n2, int n3) {
        this.headParts().forEach(modelPart -> modelPart.render(poseStack, vertexConsumer, n, n2, n3));
        this.bodyParts().forEach(modelPart -> modelPart.render(poseStack, vertexConsumer, n, n2, n3));
    }

    public /* synthetic */ void prepareMobModel(@NotNull Entity entity, float f, float f2, float f3) {
        this.prepareMobModel((InfectedDogEntity)entity, f, f2, f3);
    }

    public /* synthetic */ void setupAnim(@NotNull Entity entity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((InfectedDogEntity)entity, f, f2, f3, f4, f5);
    }
}

