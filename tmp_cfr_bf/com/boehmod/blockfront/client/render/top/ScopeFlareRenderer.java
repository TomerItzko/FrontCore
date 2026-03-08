/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.top;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.top.ITopLevelRenderer;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public final class ScopeFlareRenderer
implements ITopLevelRenderer {
    private static final float field_1866 = 0.75f;

    @Override
    public void render(@NotNull RenderLevelStageEvent event, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull ClientLevel level, @NotNull ClientPlayerDataHandler dataHandler, @NotNull PoseStack poseStack, @NotNull Frustum frustum, @NotNull GuiGraphics graphics, MultiBufferSource.BufferSource buffer, @NotNull Font font, @NotNull Camera camera, @NotNull Iterable<Entity> visibleEntities, @NotNull Random random, float delta) {
        for (Entity entity : visibleEntities) {
            if (!(entity instanceof AbstractClientPlayer)) continue;
            AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)entity;
            ScopeFlareRenderer.renderScopeFlare(dataHandler, poseStack, graphics, camera, abstractClientPlayer, delta);
        }
    }

    private static void renderScopeFlare(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Camera camera, AbstractClientPlayer abstractClientPlayer, float f) {
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData((Player)abstractClientPlayer);
        float f2 = (abstractClientPlayer.getYRot() + 15.0f) * ((float)Math.PI / 180);
        float f3 = -Mth.sin((float)f2);
        float f4 = -Mth.sin((float)(abstractClientPlayer.getXRot() * ((float)Math.PI / 180)));
        float f5 = Mth.cos((float)f2);
        Vec3 vec3 = abstractClientPlayer.getEyePosition(f);
        float f6 = (float)abstractClientPlayer.distanceToSqr(vec3.x + (double)f3, vec3.y + (double)f4, vec3.z + (double)f5);
        float f7 = MathUtils.lerpf1(bFClientPlayerData.field_1167, bFClientPlayerData.field_1168, f);
        if (f7 <= 0.0f) {
            return;
        }
        double d = vec3.x + (double)(f3 * 0.75f);
        double d2 = vec3.y + (double)0.1f + (double)(f4 * 0.75f);
        double d3 = vec3.z + (double)(f5 * 0.75f);
        int n = (int)(32.0f * f6);
        BFRendering.billboardTexture(poseStack, camera, guiGraphics, BFRenderHandSubscriber.SCOPEFLARE_TEXTURE, d, d2, d3, n, n, f7, false);
    }
}

