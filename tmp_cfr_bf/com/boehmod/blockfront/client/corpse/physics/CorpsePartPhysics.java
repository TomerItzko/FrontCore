/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.corpse.physics;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.CorpseBodyPart;
import com.boehmod.blockfront.client.corpse.physics.CorpsePhysicsBody;
import com.boehmod.blockfront.client.corpse.physics.c;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayer;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayerType;
import com.boehmod.blockfront.client.particle.corpse.CorpseParticle;
import com.boehmod.blockfront.client.particle.corpse.EmittingCorpseParticle;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.entity.CameraEntity;
import com.boehmod.blockfront.unnamed.BF_13;
import com.boehmod.blockfront.unnamed.BF_14;
import com.boehmod.blockfront.unnamed.BF_643;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.math.Box3d;
import com.boehmod.blockfront.util.math.IBox3d;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class CorpsePartPhysics {
    public static final float field_49 = 3.0f;
    public static final float field_51 = 7.0f;
    public static final float field_52 = 6.0f;
    public static final Vector3dc field_37 = new Vector3d(0.7853981633974483, 0.0, 0.39269908169872414);
    public static final Vector3dc field_40 = new Vector3d(0.39269908169872414, 0.39269908169872414, 2.5132741228718345);
    public static final Vector3dc field_42 = new Vector3d(1.0471975511965976, 1.5707963705062866, 0.5235987755982988);
    @NotNull
    private static final BF_13 field_33 = new BF_13();
    @NotNull
    private static final Quaternionf field_36 = new Quaternionf();
    private static final double field_45 = 0.1;
    public static final double field_47 = 0.01;
    @NotNull
    private final BF_13 field_39;
    @NotNull
    private final BF_13 field_41;
    @NotNull
    private final CorpsePartLayer[] field_38;
    private final double field_48;
    private final double field_50;
    @NotNull
    private final Vector3dc field_43;
    @NotNull
    private final List<c> field_44 = new ObjectArrayList();
    @NotNull
    private final List<CorpseParticle> corpseParticles = new ObjectArrayList();
    @NotNull
    private final CorpseBodyPart field_32;
    @Nullable
    private CorpsePhysicsBody body;
    @Nullable
    private CameraEntity camera = null;

    public CorpsePartPhysics(@NotNull CorpseBodyPart corpseBodyPart, @NotNull BF_14 bF_14, @NotNull CorpsePartLayer[] corpsePartLayerArray, double d, double d2) {
        this.field_32 = corpseBodyPart;
        this.field_39 = new BF_13(bF_14);
        this.field_38 = corpsePartLayerArray;
        this.field_48 = d;
        this.field_50 = d2;
        IBox3d iBox3d = null;
        for (CorpsePartLayer corpsePartLayer : corpsePartLayerArray) {
            if (corpsePartLayer.layerType() != CorpsePartLayerType.BASIC) continue;
            for (ModelPart.Cube cube : corpsePartLayer.cubes()) {
                if (iBox3d == null) {
                    iBox3d = new Box3d(cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.maxZ);
                    continue;
                }
                iBox3d.setAndExtend(cube.minX, cube.minY, cube.minZ, (Box3d)iBox3d);
                iBox3d.setAndExtend(cube.maxX, cube.maxY, cube.maxZ, (Box3d)iBox3d);
            }
        }
        assert (iBox3d != null) : "Ragdoll part must have at least one cube";
        double d3 = 0.0625 * d;
        Vector3d vector3d = iBox3d.center(new Vector3d()).mul(d3);
        this.field_39.method_115().set((Vector3dc)vector3d);
        this.field_39.getPosition().add((Vector3dc)this.field_39.method_109().transform(new Vector3d((Vector3dc)vector3d)));
        this.field_43 = iBox3d.size(new Vector3d()).mul(0.5 * d3);
        this.field_41 = new BF_13(this.field_39);
    }

    public void method_39(@NotNull CameraEntity cameraEntity) {
        this.camera = cameraEntity;
    }

    public void method_41(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull BFClientPlayerData bFClientPlayerData, @NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, @NotNull LocalPlayer localPlayer, float f, int n, int n2, int n3, @NotNull ResourceLocation resourceLocation) {
        poseStack.pushPose();
        this.method_40(poseStack, f);
        PoseStack.Pose pose = poseStack.last();
        for (CorpsePartLayer corpsePartLayer : this.field_38) {
            if (!corpsePartLayer.texture().equals((Object)resourceLocation)) continue;
            for (ModelPart.Cube cube : corpsePartLayer.cubes()) {
                cube.compile(pose, vertexConsumer, n, n2, n3);
            }
        }
        poseStack.popPose();
        if (this.camera != null) {
            boolean bl;
            BF_13 bF_13 = field_33.method_101(this.field_41).method_102(this.field_39, f);
            Vector3d vector3d = bF_13.getPosition();
            this.camera.setPos(vector3d.x, vector3d.y, vector3d.z);
            this.camera.xo = vector3d.x;
            this.camera.yo = vector3d.y;
            this.camera.zo = vector3d.z;
            this.camera.method_2490(BF_643.RAGDOLL);
            this.camera.field_2753.set((Quaterniondc)bF_13.method_109());
            int n4 = bFClientManager.getCinematics().isSequencePlaying() ? 1 : 0;
            boolean bl2 = bl = n4 == 0 && (bFClientPlayerData.isOutOfGame() || localPlayer.isDeadOrDying());
            if (bl && minecraft.getCameraEntity() != this.camera && minecraft.getCameraEntity() != this.camera) {
                ClientUtils.setCameraEntity(minecraft, this.camera);
            }
        }
    }

    public void method_40(@NotNull PoseStack poseStack, float f) {
        BF_13 bF_13 = field_33.method_101(this.field_41).method_102(this.field_39, f);
        Vector3d vector3d = bF_13.getPosition();
        Quaterniond quaterniond = bF_13.method_109();
        Vector3d vector3d2 = bF_13.method_115();
        Vector3d vector3d3 = bF_13.method_113();
        poseStack.translate(vector3d.x, vector3d.y, vector3d.z);
        poseStack.mulPose(field_36.set((Quaterniondc)quaterniond));
        poseStack.scale((float)(this.field_48 * vector3d3.x), (float)(this.field_48 * vector3d3.y), (float)(this.field_48 * vector3d3.z));
        poseStack.translate(-vector3d2.x, -vector3d2.y, -vector3d2.z);
    }

    public void method_42(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel) {
        assert (this.body != null) : "Ragdoll part must have a physics body to update";
        BF_14 bF_14 = this.method_31();
        Vector3dc vector3dc = bF_14.getPosition();
        this.corpseParticles.removeIf(corpseParticle -> corpseParticle.update(minecraft, bFClientManager, clientLevel, this, vector3dc));
        this.field_41.method_101(this.field_39);
        this.body.method_795(this.field_39);
    }

    public void method_36(@NotNull CorpsePartPhysics corpsePartPhysics, @NotNull Vector3dc vector3dc, @NotNull Quaterniondc quaterniondc) {
        BF_14 bF_14 = this.method_31();
        BF_14 bF_142 = corpsePartPhysics.method_31();
        Vector3d vector3d = bF_14.method_115().negate(new Vector3d());
        Vector3d vector3d2 = new Vector3d();
        bF_14.method_109().transform((Vector3dc)vector3d, vector3d2).add(bF_14.getPosition());
        bF_142.method_109().transformInverse(vector3d2.sub(bF_142.getPosition()));
        this.field_44.add(new c(corpsePartPhysics, quaterniondc, (Quaterniondc)new Quaterniond(), (Vector3dc)vector3d, (Vector3dc)vector3d2, vector3dc));
    }

    @NotNull
    public BF_14 method_31() {
        return this.field_39;
    }

    @NotNull
    public CorpsePhysicsBody getBody() {
        assert (this.body != null) : "Ragdoll part must have a physics body to get";
        return this.body;
    }

    public void method_38(@NotNull CorpsePhysicsBody corpsePhysicsBody) {
        this.body = Objects.requireNonNull(corpsePhysicsBody);
    }

    @NotNull
    public Vector3dc method_34() {
        return this.field_43;
    }

    @NotNull
    public Collection<c> method_33() {
        return Collections.unmodifiableList(this.field_44);
    }

    @NotNull
    public CorpsePartLayer[] method_35() {
        return this.field_38;
    }

    public double method_29() {
        return this.field_50;
    }

    @NotNull
    public CorpseBodyPart method_30() {
        return this.field_32;
    }

    public void clear() {
        if (this.camera != null) {
            this.camera = null;
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localPlayer = minecraft.player;
            if (minecraft.getCameraEntity() == this.camera && localPlayer != null) {
                ClientUtils.setCameraEntity(minecraft, (Entity)localPlayer);
            }
        }
        this.getBody().removeObject();
    }

    public void clearCamera() {
        this.camera = null;
    }

    public void method_37(@NotNull EmittingCorpseParticle emittingCorpseParticle) {
        this.corpseParticles.add(emittingCorpseParticle);
    }

    public void applyFluidDrag(@NotNull ClientLevel level) {
        Vector3d vector3d = this.field_39.getPosition();
        AABB aABB = new AABB(vector3d.x, vector3d.y, vector3d.z, vector3d.x, vector3d.y, vector3d.z).inflate(0.1);
        double d = aABB.getXsize() * aABB.getYsize() * aABB.getZsize();
        double d2 = 0.0;
        Iterable iterable = BlockPos.betweenClosed((int)Mth.floor((double)aABB.minX), (int)Mth.floor((double)aABB.minY), (int)Mth.floor((double)aABB.minZ), (int)Mth.floor((double)aABB.maxX), (int)Mth.floor((double)aABB.maxY), (int)Mth.floor((double)aABB.maxZ));
        for (BlockPos blockPos : iterable) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.getFluidState().isEmpty()) continue;
            AABB aABB2 = new AABB(blockPos).intersect(aABB);
            double d3 = aABB2.getXsize();
            double d4 = aABB2.getYsize();
            double d5 = aABB2.getZsize();
            if (!(d3 > 0.0) || !(d4 > 0.0) || !(d5 > 0.0)) continue;
            d2 += d3 * d4 * d5;
        }
        double d6 = d2 / d;
        double d7 = this.field_43.x() * this.field_43.y() * this.field_43.z() * 8.0;
        this.getBody().multiplyVelocities(1.0 - d6 / d7 * 0.01);
    }
}

