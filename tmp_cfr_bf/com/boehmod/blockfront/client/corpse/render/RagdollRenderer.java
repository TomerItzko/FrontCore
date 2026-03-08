/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.corpse.render;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import com.boehmod.blockfront.client.corpse.render.CorpsePartLayer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.type.CorpseRenderType;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.unnamed.BF_14;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.Box3d;
import com.boehmod.blockfront.util.math.IBox3d;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public class RagdollRenderer {
    @Nonnull
    public static final ResourceLocation field_6977 = BFRes.loc("textures/models/entities/corpse/blood.png");
    public static final double field_4 = 120.0;
    public static final double field_6 = 10.0;
    public static final int field_12 = 256;
    @NotNull
    private final List<CorpsePartPhysics> parts;
    @NotNull
    private final Set<ResourceLocation> field_10 = new ObjectArraySet();
    @NotNull
    private final Box3d field_5;
    private double field_7;
    private double field_8;

    public RagdollRenderer(@NotNull Collection<CorpsePartPhysics> collection) {
        this.parts = new ObjectArrayList(collection);
        for (CorpsePartPhysics corpsePartPhysics : this.parts) {
            for (CorpsePartLayer corpsePartLayer : corpsePartPhysics.method_35()) {
                ResourceLocation resourceLocation = corpsePartLayer.texture();
                this.field_10.add(resourceLocation);
            }
        }
        this.field_5 = this.method_3();
    }

    public boolean method_11(@NotNull Frustum frustum) {
        double d;
        if (frustum.isVisible(this.field_5.asAABB())) {
            double d2;
            this.field_7 += 0.05;
            return d2 > 120.0;
        }
        this.field_8 += 0.05;
        return d > 10.0;
    }

    @NotNull
    private Box3d method_3() {
        IBox3d iBox3d = null;
        for (CorpsePartPhysics corpsePartPhysics : this.parts) {
            Vector3dc vector3dc = corpsePartPhysics.method_31().getPosition();
            if (iBox3d == null) {
                iBox3d = new Box3d(vector3dc.x(), vector3dc.y(), vector3dc.z(), vector3dc.x(), vector3dc.y(), vector3dc.z());
            }
            iBox3d.setAndExtend(vector3dc, (Box3d)iBox3d);
        }
        assert (iBox3d != null) : "Ragdoll missing parts!";
        iBox3d.expand(1.0, (Box3d)iBox3d);
        return iBox3d;
    }

    public void method_6(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, BFClientPlayerData bFClientPlayerData, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, float f) {
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        boolean bl = entityRenderDispatcher.shouldRenderHitBoxes();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        int n = OverlayTexture.NO_OVERLAY;
        Function<ResourceLocation, RenderType> function = CorpseRenderType.RENDER_TYPE_CONSTRUCTOR;
        if (BFClientSettings.CONTENT_GORE.isEnabled()) {
            RenderSystem.setShaderTexture((int)3, (ResourceLocation)field_6977);
        }
        if (bl) {
            this.method_5(poseStack, f, bufferSource);
        } else {
            for (ResourceLocation resourceLocation : this.field_10) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(function.apply(resourceLocation));
                this.method_7(minecraft, bFClientManager, bFClientPlayerData, clientLevel, localPlayer, poseStack, resourceLocation, vertexConsumer, n, f);
            }
        }
    }

    private void method_7(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull BFClientPlayerData bFClientPlayerData, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull ResourceLocation resourceLocation, @NotNull VertexConsumer vertexConsumer, int n, float f) {
        for (CorpsePartPhysics corpsePartPhysics : this.parts) {
            BF_14 bF_14 = corpsePartPhysics.method_31();
            Vector3dc vector3dc = bF_14.getPosition();
            BlockPos blockPos = BlockPos.containing((double)vector3dc.x(), (double)vector3dc.y(), (double)vector3dc.z());
            int n2 = LevelRenderer.getLightColor((BlockAndTintGetter)clientLevel, (BlockPos)blockPos);
            corpsePartPhysics.method_41(minecraft, bFClientManager, bFClientPlayerData, poseStack, vertexConsumer, localPlayer, f, n2, n, -1, resourceLocation);
        }
    }

    private void method_5(@NotNull PoseStack poseStack, float f, @NotNull MultiBufferSource.BufferSource bufferSource) {
        RenderType.CompositeRenderType compositeRenderType = RenderType.LINES;
        VertexConsumer vertexConsumer = bufferSource.getBuffer((RenderType)compositeRenderType);
        RenderSystem.disableDepthTest();
        for (CorpsePartPhysics corpsePartPhysics : this.parts) {
            poseStack.pushPose();
            corpsePartPhysics.method_40(poseStack, f);
            Vector3dc vector3dc = corpsePartPhysics.method_34();
            Vector3dc vector3dc2 = corpsePartPhysics.method_31().method_115();
            poseStack.translate(vector3dc2.x(), vector3dc2.y(), vector3dc2.z());
            LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)vertexConsumer, (double)(-vector3dc.x()), (double)(-vector3dc.y()), (double)(-vector3dc.z()), (double)vector3dc.x(), (double)vector3dc.y(), (double)vector3dc.z(), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            poseStack.scale(0.2f, 0.2f, 0.2f);
            LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)vertexConsumer, (double)0.0, (double)0.0, (double)0.0, (double)1.0, (double)0.0, (double)0.0, (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)vertexConsumer, (double)0.0, (double)0.0, (double)0.0, (double)0.0, (double)1.0, (double)0.0, (float)0.0f, (float)1.0f, (float)0.0f, (float)1.0f);
            LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)vertexConsumer, (double)0.0, (double)0.0, (double)0.0, (double)0.0, (double)0.0, (double)1.0, (float)0.0f, (float)0.0f, (float)1.0f, (float)1.0f);
            poseStack.popPose();
        }
        RenderSystem.enableDepthTest();
    }

    @NotNull
    public List<CorpsePartPhysics> getParts() {
        return this.parts;
    }

    @NotNull
    public IBox3d method_4() {
        return this.field_5;
    }

    public boolean method_9(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull Frustum frustum) {
        this.parts.forEach(corpsePartPhysics -> corpsePartPhysics.method_42(minecraft, bFClientManager, clientLevel));
        this.field_5.fixAndSetOther(this.method_3());
        if (this.method_10(localPlayer) || this.method_11(frustum)) {
            this.clear();
            return true;
        }
        return false;
    }

    private boolean method_10(@NotNull LocalPlayer localPlayer) {
        Vec3 vec3 = localPlayer.position();
        double d = vec3.x();
        double d2 = vec3.y();
        double d3 = vec3.z();
        double d4 = this.field_5.maxX();
        double d5 = this.field_5.maxY();
        double d6 = this.field_5.maxZ();
        double d7 = this.field_5.minX();
        double d8 = this.field_5.minY();
        double d9 = this.field_5.minZ();
        int n = 65536;
        return (d - d4) * (d - d4) + (d2 - d5) * (d2 - d5) + (d3 - d6) * (d3 - d6) > 65536.0 || (d - d7) * (d - d7) + (d2 - d8) * (d2 - d8) + (d3 - d9) * (d3 - d9) > 65536.0;
    }

    public void clear() {
        for (CorpsePartPhysics corpsePartPhysics : this.parts) {
            corpsePartPhysics.clear();
        }
    }

    public void method_14() {
        for (CorpsePartPhysics corpsePartPhysics : this.parts) {
            corpsePartPhysics.clearCamera();
        }
    }
}

