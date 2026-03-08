/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.top;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.top.ITopLevelRenderer;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ConcurrentModificationException;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public final class BulletTracerRenderer
implements ITopLevelRenderer {
    @Override
    public void render(@NotNull RenderLevelStageEvent event, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ClientPlayerDataHandler dataHandler, @NotNull PoseStack poseStack, @NotNull Frustum frustum, @NotNull GuiGraphics graphics, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Font font, @NotNull Camera camera, @NotNull Iterable<Entity> visibleEntities, @NotNull Random random, float delta) {
        try {
            RenderSystem.enableDepthTest();
            for (BulletTracer bulletTracer : BFClientTickSubscriber.BULLETS) {
                BFRendering.method_198(bulletTracer, poseStack, delta);
            }
            RenderSystem.disableDepthTest();
        }
        catch (NullPointerException | ConcurrentModificationException runtimeException) {
            // empty catch block
        }
    }
}

