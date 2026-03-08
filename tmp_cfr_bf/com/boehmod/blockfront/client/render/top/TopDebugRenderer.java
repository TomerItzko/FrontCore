/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.top;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.top.ITopLevelRenderer;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_629;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.debug.DebugLine;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class TopDebugRenderer
implements ITopLevelRenderer {
    public static final ResourceLocation field_1865 = BFRes.loc("textures/misc/debug/vehicle_seat.png");

    @Override
    public void render(@NotNull RenderLevelStageEvent event, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ClientPlayerDataHandler dataHandler, @NotNull PoseStack poseStack, @NotNull Frustum frustum, @NotNull GuiGraphics graphics, MultiBufferSource.BufferSource buffer, @NotNull Font font, @NotNull Camera camera, @NotNull Iterable<Entity> visibleEntities, @NotNull Random random, float delta) {
        if (!minecraft.getDebugOverlay().showDebugScreen()) {
            return;
        }
        for (DebugLine debugLine : BFClientTickSubscriber.DEBUG_LINES) {
            BFRendering.billboardLine(camera, poseStack, debugLine.getStart(), debugLine.getEnd(), 3.0f, debugLine.getColor(), debugLine.getAlpha());
        }
        for (Entity entity : visibleEntities) {
            Vec3 vec3;
            Vec3 vec32;
            Vec3 vec33;
            if (!(entity instanceof AbstractVehicleEntity)) continue;
            AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
            float f = abstractVehicleEntity.getYRot();
            Vec3 vec34 = abstractVehicleEntity.position();
            BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
            for (BF_623 bF_623 : bF_624.field_2678) {
                vec33 = new Vec3(bF_623.field_2674).yRot(-f * ((float)Math.PI / 180));
                vec32 = new Vec3(bF_623.field_2669).yRot(-f * ((float)Math.PI / 180));
                vec3 = vec34.add(vec33).add(vec32);
                BFRendering.billboardTexture(poseStack, camera, graphics, field_1865, vec3.x, vec3.y, vec3.z, 16, 16, 1.0f, false);
            }
            for (BF_633 bF_633 : bF_624.field_2679) {
                vec33 = new Vector3f((Vector3fc)bF_633.field_2738).rotateY(-f * ((float)Math.PI / 180));
                vec32 = vec34.add((double)vec33.x, (double)vec33.y, (double)vec33.z);
                vec3 = bF_633.method_2469();
                BFRendering.billboardTexture(poseStack, camera, graphics, (ResourceLocation)vec3, vec32.x, vec32.y, vec32.z, 16, 16, 1.0f, false);
            }
            for (BF_629 bF_629 : bF_624.field_2680) {
                vec33 = bF_629.field_2708.yRot(-(f + 90.0f) * ((float)Math.PI / 180));
                vec32 = vec34.add(vec33.x, vec33.y, vec33.z);
                vec3 = bF_629.method_2439();
                BFRendering.billboardTexture(poseStack, camera, graphics, (ResourceLocation)vec3, vec32.x, vec32.y, vec32.z, 16, 16, 1.0f, false);
            }
        }
    }
}

