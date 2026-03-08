/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.multiplayer.ClientChunkCache
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderer
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.core.SectionPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.chunk.LevelChunk
 *  net.minecraft.world.level.chunk.LevelChunkSection
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Math
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaterniond
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3d
 *  org.joml.Vector3dc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package com.boehmod.blockfront.client.corpse;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.CorpseBodyPart;
import com.boehmod.blockfront.client.corpse.CorpseChunkLastUpdate;
import com.boehmod.blockfront.client.corpse.CorpseContext;
import com.boehmod.blockfront.client.corpse.physics.BlockPhysicsBakery;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import com.boehmod.blockfront.client.corpse.physics.CorpsePhysicsBody;
import com.boehmod.blockfront.client.corpse.physics.RustCorpsePhysics;
import com.boehmod.blockfront.client.corpse.physics.a;
import com.boehmod.blockfront.client.corpse.physics.c;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayer;
import com.boehmod.blockfront.client.corpse.render.IRagdollEntityRenderer;
import com.boehmod.blockfront.client.corpse.render.RagdollRenderer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.common.entity.CameraEntity;
import com.boehmod.blockfront.common.net.packet.BFSpawnCorpsePacket;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.unnamed.BF_13;
import com.boehmod.blockfront.unnamed.BF_14;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.math.Box3i;
import com.boehmod.blockfront.util.math.IBox3d;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ClientCorpseManager {
    public static final int field_29 = 100;
    public static final int field_30 = 20;
    public static final int field_13 = 2;
    public static final double field_20 = -26.0;
    public static final double field_22 = 0.05;
    public static final int field_14 = 3;
    public static final float field_21 = 16.0f;
    public static final float field_23 = 1.0f;
    public static final float field_26 = 0.0f;
    public static final float field_27 = 20.0f;
    public static final float field_28 = 15.0f;
    public static final int field_16 = 64;
    public static final double field_25 = 5.0;
    @NotNull
    private final List<RagdollRenderer> ragdollRenderers = new ObjectArrayList();
    @NotNull
    private final Map<SectionPos, CorpseChunkLastUpdate> lastUpdates = new Object2ObjectOpenHashMap();
    @Nullable
    private BlockPhysicsBakery bakery;
    @NotNull
    private final AtomicInteger field_19 = new AtomicInteger(0);

    public ClientCorpseManager() {
        RustCorpsePhysics.initialize(0.0, -26.0, 0.0);
    }

    @NotNull
    private <T extends LivingEntity> RagdollRenderer ragdollEntity(@NotNull Minecraft minecraft, @NotNull BFSpawnCorpsePacket.Context data, @NotNull T entity, float delta) {
        Object object;
        EntityRenderer entityRenderer = minecraft.getEntityRenderDispatcher().getRenderer(entity);
        if (!(entityRenderer instanceof IRagdollEntityRenderer)) {
            throw new IllegalArgumentException("Cannot ragdoll an entity whose renderer does not implement RagdollableEntityRenderer!");
        }
        IRagdollEntityRenderer iRagdollEntityRenderer = (IRagdollEntityRenderer)entityRenderer;
        double d = -1.501;
        float f = 1.0f;
        if (entity instanceof AbstractClientPlayer) {
            f = 0.9375f;
        }
        Vec3 vec3 = entityRenderer.getRenderOffset(entity, delta);
        Vec3 vec32 = entity.getPosition(delta).add(vec3);
        float f2 = (float)entity.tickCount + delta;
        float f3 = entity.getPreciseBodyRotation(delta);
        PoseStack poseStack = new PoseStack();
        poseStack.translate(vec32.x, vec32.y, vec32.z);
        if (entityRenderer instanceof LivingEntityRenderer) {
            object = (LivingEntityRenderer)entityRenderer;
            object.setupRotations(entity, poseStack, f2, f3, delta, entity.getScale());
        }
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.scale(f, f, f);
        poseStack.translate(0.0, -1.501, 0.0);
        poseStack.scale(0.0625f, 0.0625f, 0.0625f);
        if (entity instanceof LocalPlayer && entityRenderer instanceof LivingEntityRenderer) {
            object = (LivingEntityRenderer)entityRenderer;
            float f4 = Mth.rotLerp((float)delta, (float)entity.yBodyRotO, (float)entity.yBodyRot);
            float f5 = Mth.rotLerp((float)delta, (float)entity.yHeadRotO, (float)entity.yHeadRot);
            float f6 = f5 - f4;
            float f7 = Mth.lerp((float)delta, (float)entity.xRotO, (float)entity.getXRot());
            object.getModel().setupAnim(entity, 0.0f, 0.0f, (float)entity.tickCount + delta, f6, f7);
        }
        object = new CorpseContext(this, poseStack, f);
        Collection<CorpsePartPhysics> collection = iRagdollEntityRenderer.createParts(this, data, entity, (CorpseContext)object);
        return new RagdollRenderer(collection);
    }

    @Contract(value="_, _, _, _, _ -> new")
    @NotNull
    public static CorpsePartPhysics createPart(@NotNull CorpseContext context, @NotNull CorpseBodyPart bodyPart, double d, @NotNull PartPose partPose, CorpsePartLayer ... layers) {
        PoseStack poseStack = context.transformStack();
        float f = context.scaleFactor();
        poseStack.pushPose();
        poseStack.translate(partPose.x, partPose.y, partPose.z);
        poseStack.mulPose(new Quaternionf().rotateZYX(partPose.zRot, partPose.yRot, partPose.xRot));
        Matrix4f matrix4f = poseStack.last().pose();
        Vector3f vector3f = matrix4f.getTranslation(new Vector3f());
        Quaternionf quaternionf = new Quaternionf().setFromUnnormalized((Matrix4fc)matrix4f);
        Vector3f vector3f2 = new Vector3f(f);
        BF_13 bF_13 = new BF_13(new Vector3d((Vector3fc)vector3f), new Quaterniond((Quaternionfc)quaternionf), new Vector3d(), new Vector3d(1.0));
        CorpsePartPhysics corpsePartPhysics = new CorpsePartPhysics(bodyPart, bF_13, layers, vector3f2.x, d);
        poseStack.popPose();
        return corpsePartPhysics;
    }

    public void update(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        this.method_25(clientLevel);
        for (int i = 0; i < 3; ++i) {
            RustCorpsePhysics.tick(0.016666666666666666);
            for (RagdollRenderer ragdollRenderer2 : this.ragdollRenderers) {
                for (CorpsePartPhysics corpsePartPhysics : ragdollRenderer2.getParts()) {
                    corpsePartPhysics.applyFluidDrag(clientLevel);
                }
            }
        }
        Frustum frustum = minecraft.levelRenderer.getFrustum();
        this.ragdollRenderers.removeIf(ragdollRenderer -> ragdollRenderer.method_9(minecraft, bFClientManager, clientLevel, localPlayer, frustum));
    }

    private void method_25(@NotNull ClientLevel clientLevel) {
        long l = clientLevel.getGameTime();
        ClientChunkCache clientChunkCache = clientLevel.getChunkSource();
        int n = clientLevel.getSectionsCount();
        this.lastUpdates.entrySet().removeIf(entry -> {
            SectionPos sectionPos = (SectionPos)entry.getKey();
            CorpseChunkLastUpdate corpseChunkLastUpdate = (CorpseChunkLastUpdate)entry.getValue();
            return corpseChunkLastUpdate.lastInhabitedTick() < l - 20L || !clientChunkCache.hasChunk(sectionPos.x(), sectionPos.z());
        });
        for (RagdollRenderer ragdollRenderer : this.ragdollRenderers) {
            IBox3d iBox3d = ragdollRenderer.method_4();
            Box3i box3i = new Box3i(Mth.floor((double)iBox3d.minX()) >> 4, Mth.floor((double)iBox3d.minY()) >> 4, Mth.floor((double)iBox3d.minZ()) >> 4, Mth.floor((double)iBox3d.maxX()) >> 4, Mth.floor((double)iBox3d.maxY()) >> 4, Mth.floor((double)iBox3d.maxZ()) >> 4);
            int n2 = box3i.maxX();
            for (int i = box3i.minX(); i <= n2; ++i) {
                int n3 = box3i.maxZ();
                for (int j = box3i.minZ(); j <= n3; ++j) {
                    LevelChunk levelChunk = clientLevel.getChunk(i, j);
                    int n4 = box3i.maxY();
                    for (int k = box3i.minY(); k <= n4; ++k) {
                        SectionPos sectionPos = SectionPos.of((int)i, (int)k, (int)j);
                        int n5 = clientLevel.getSectionIndexFromSectionY(k);
                        if (n5 < 0 || n5 >= n || this.lastUpdates.containsKey(sectionPos)) continue;
                        this.addChunkSection(levelChunk.getSection(n5), i, k, j);
                        this.lastUpdates.put(sectionPos, new CorpseChunkLastUpdate(sectionPos, l));
                    }
                }
            }
        }
    }

    private void addChunkSection(@NotNull LevelChunkSection section, int n, int n2, int n3) {
        if (this.bakery == null) {
            BFLog.logWarn("[BlockFront] Warning: Attempted to add chunk section without a block physics bakery!", new Object[0]);
            return;
        }
        int[] nArray = new int[4096];
        if (section.hasOnlyAir()) {
            return;
        }
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    BlockState blockState = section.getBlockState(i, k, j);
                    a a2 = this.bakery.method_787(blockState);
                    int n4 = i + (j << 4) + (k << 8);
                    nArray[n4] = a2 == null ? 0 : a2.handle() + 1;
                }
            }
        }
        RustCorpsePhysics.addChunk(n, n2, n3, nArray);
    }

    public void method_16(@NotNull RagdollRenderer ragdollRenderer) {
        Object object;
        this.ragdollRenderers.add(ragdollRenderer);
        List<CorpsePartPhysics> list = ragdollRenderer.getParts();
        for (CorpsePartPhysics corpsePartPhysics : list) {
            object = corpsePartPhysics.method_34();
            BF_14 bF_14 = corpsePartPhysics.method_31();
            double d = corpsePartPhysics.method_29();
            CorpsePhysicsBody corpsePhysicsBody = RustCorpsePhysics.method_779(this.field_19.getAndIncrement(), bF_14, (Vector3dc)object, d);
            corpsePartPhysics.method_38(corpsePhysicsBody);
        }
        for (CorpsePartPhysics corpsePartPhysics : list) {
            object = corpsePartPhysics.getBody();
            for (c c2 : corpsePartPhysics.method_33()) {
                ((CorpsePhysicsBody)object).addRagdollConstraint(c2.other().getBody(), c2.pivotA(), c2.pivotB(), c2.rotationA(), c2.rotationB(), c2.rotationLimits(), 5.0);
            }
        }
        if (this.ragdollRenderers.size() > 64) {
            this.method_24(this.ragdollRenderers.removeFirst());
        }
    }

    public void method_24(@NotNull RagdollRenderer ragdollRenderer) {
        this.ragdollRenderers.remove(ragdollRenderer);
        ragdollRenderer.clear();
    }

    public void clear() {
        for (RagdollRenderer ragdollRenderer : this.ragdollRenderers) {
            ragdollRenderer.clear();
        }
        this.ragdollRenderers.clear();
    }

    public void spawnCorpse(@NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull LivingEntity entity, @NotNull BFSpawnCorpsePacket.Context data, @NotNull Random random) {
        RagdollRenderer ragdollRenderer2;
        boolean bl;
        if (player.distanceTo((Entity)entity) > 256.0f) {
            return;
        }
        boolean bl2 = bl = entity == minecraft.player;
        if (bl) {
            for (RagdollRenderer ragdollRenderer2 : this.ragdollRenderers) {
                ragdollRenderer2.method_14();
            }
        }
        RagdollRenderer ragdollRenderer3 = this.ragdollEntity(minecraft, data, entity, 1.0f);
        ragdollRenderer2 = this.ragdollEntity(minecraft, data, entity, 0.0f);
        this.method_16(ragdollRenderer3);
        List<CorpsePartPhysics> list = ragdollRenderer3.getParts();
        List<CorpsePartPhysics> list2 = ragdollRenderer2.getParts();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            CameraEntity cameraEntity;
            CorpsePartPhysics corpsePartPhysics = list.get(i);
            BF_14 bF_14 = corpsePartPhysics.method_31();
            CorpsePartPhysics corpsePartPhysics2 = list2.get(i);
            BF_14 bF_142 = corpsePartPhysics2.method_31();
            Quaterniond quaterniond = bF_142.method_109().div(bF_14.method_109(), new Quaterniond());
            Vector3d vector3d = new Vector3d(quaterniond.x, quaterniond.y, quaterniond.z);
            if (vector3d.lengthSquared() <= 1.0E-15) {
                vector3d.mul(2.0 / quaterniond.w);
            } else {
                vector3d.normalize().mul(2.0 * Math.safeAcos((double)quaterniond.w));
            }
            Vector3d vector3d2 = bF_142.getPosition().sub(bF_14.getPosition(), new Vector3d()).div(0.05).negate();
            vector3d2.add((Vector3fc)data.velocity());
            vector3d.div(0.05).negate();
            if (corpsePartPhysics.method_30() == CorpseBodyPart.BODY && data.explosion()) {
                float f = (random.nextFloat() * 2.0f - 1.0f) * 20.0f;
                float f2 = (random.nextFloat() * 2.0f - 1.0f) * 20.0f;
                float f3 = (random.nextFloat() * 2.0f - 1.0f) * 20.0f;
                vector3d.add((double)f, (double)f2, (double)f3);
            }
            if (corpsePartPhysics.method_30() == CorpseBodyPart.HEAD && bl && (cameraEntity = (CameraEntity)((EntityType)BFEntityTypes.CAMERA.get()).create((Level)level)) != null) {
                ClientUtils.spawnEntity(cameraEntity, level);
                corpsePartPhysics.method_39(cameraEntity);
                Camera camera = minecraft.gameRenderer.getMainCamera();
                camera.eyeHeight = 0.0f;
                camera.eyeHeightOld = 0.0f;
            }
            Vector3d vector3d3 = MathUtils.clampPartsd(vector3d2, -15.0, 15.0);
            Vector3d vector3d4 = MathUtils.clampPartsd(vector3d, -15.0, 15.0);
            corpsePartPhysics.getBody().addLinearAngularVelocities((Vector3dc)vector3d3, (Vector3dc)vector3d4);
        }
    }

    public void instantiateCollider(@NotNull ClientLevel level) {
        if (this.bakery != null) {
            BFLog.logWarn("[BlockFront] Warning: Attempted to instantiate level collider when one already exists!", new Object[0]);
            return;
        }
        this.bakery = new BlockPhysicsBakery(level);
    }

    public void freeCollider() {
        if (this.bakery == null) {
            BFLog.logWarn("[BlockFront] Warning: Attempted to free null level collider!", new Object[0]);
            return;
        }
        this.clear();
        RustCorpsePhysics.clearLevel();
        this.bakery = null;
    }

    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFClientPlayerData playerData, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull PoseStack poseStack, float deltaTick) {
        Frustum frustum = minecraft.levelRenderer.getFrustum();
        for (RagdollRenderer ragdollRenderer : this.ragdollRenderers) {
            if (!frustum.isVisible(ragdollRenderer.method_4().asAABB())) continue;
            ragdollRenderer.method_6(minecraft, manager, playerData, level, player, poseStack, deltaTick);
        }
    }
}

