/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.top;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.top.ITopLevelRenderer;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.util.debug.DebugBox;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class DebugBoxRenderer
implements ITopLevelRenderer {
    private static final int field_6830 = 4;

    @Override
    public void render(@NotNull RenderLevelStageEvent event, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ClientPlayerDataHandler dataHandler, @NotNull PoseStack poseStack, @NotNull Frustum frustum, @NotNull GuiGraphics graphics, MultiBufferSource.BufferSource buffer, @NotNull Font font, @NotNull Camera camera, @NotNull Iterable<Entity> visibleEntities, @NotNull Random random, float delta) {
        if (!minecraft.getDebugOverlay().showDebugScreen()) {
            return;
        }
        ServerCollisionTracker serverCollisionTracker = manager.getCollisionTracker();
        int n = (int)level.getGameTime();
        int n2 = minecraft.player.getId();
        for (int i = 0; i < 4; ++i) {
            float f = 0.5f - (float)i * 0.1f;
            for (EntityCollisionEntry entityCollisionEntry : serverCollisionTracker.method_5836(n - i * 4)) {
                AABB aABB;
                if (entityCollisionEntry.entityId() == n2) continue;
                boolean bl = entityCollisionEntry.isValid();
                this.renderDebugBox(camera, poseStack, entityCollisionEntry.boundingBox(), bl ? 0xFFFF00 : 0xFF0000, f);
                AABB aABB2 = entityCollisionEntry.headshotRegion();
                if (aABB2 != null) {
                    this.renderDebugBox(camera, poseStack, aABB2, bl ? 65280 : 0xFF0000, f);
                }
                if ((aABB = entityCollisionEntry.backpackRegion()) == null) continue;
                this.renderDebugBox(camera, poseStack, aABB, bl ? 255 : 0xFF0000, f);
            }
        }
        for (DebugBox debugBox : BFClientTickSubscriber.DEBUG_BOXES) {
            AABB aABB = debugBox.getBox();
            this.renderDebugBox(camera, poseStack, aABB, debugBox.getColor(), debugBox.getAlpha());
        }
    }

    private void renderDebugBox(@NotNull Camera camera, @NotNull PoseStack poseStack, @NotNull AABB box, int color, float alpha) {
        Vec3 vec3 = new Vec3(box.minX, box.minY, box.minZ);
        Vec3 vec32 = new Vec3(box.maxX, box.maxY, box.maxZ);
        Vec3[] vec3Array = new Vec3[]{new Vec3(vec3.x, vec3.y, vec3.z), new Vec3(vec32.x, vec3.y, vec3.z), new Vec3(vec32.x, vec32.y, vec3.z), new Vec3(vec3.x, vec32.y, vec3.z), new Vec3(vec3.x, vec3.y, vec32.z), new Vec3(vec32.x, vec3.y, vec32.z), new Vec3(vec32.x, vec32.y, vec32.z), new Vec3(vec3.x, vec32.y, vec32.z)};
        int n = (int)(alpha * 255.0f) << 24 | color & 0xFFFFFF;
        BFRendering.billboardLine(camera, poseStack, vec3Array[0], vec3Array[1], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[1], vec3Array[2], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[2], vec3Array[3], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[3], vec3Array[0], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[4], vec3Array[5], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[5], vec3Array[6], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[6], vec3Array[7], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[7], vec3Array[4], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[0], vec3Array[4], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[1], vec3Array[5], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[2], vec3Array[6], 2.0f, n, alpha);
        BFRendering.billboardLine(camera, poseStack, vec3Array[3], vec3Array[7], 2.0f, n, alpha);
    }
}

