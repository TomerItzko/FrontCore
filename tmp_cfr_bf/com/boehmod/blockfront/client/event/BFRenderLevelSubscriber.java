/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.top.BulletTracerRenderer;
import com.boehmod.blockfront.client.render.top.CinematicsRenderer;
import com.boehmod.blockfront.client.render.top.DebugBoxRenderer;
import com.boehmod.blockfront.client.render.top.ITopLevelRenderer;
import com.boehmod.blockfront.client.render.top.ScopeFlareRenderer;
import com.boehmod.blockfront.client.render.top.TopDebugRenderer;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.unnamed.BF_29;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFRenderLevelSubscriber {
    private static final float field_388 = 0.5f;
    private static final float field_389 = 0.2f;
    private static final List<ITopLevelRenderer> TOP_RENDERERS = new ObjectArrayList();

    @SubscribeEvent
    public static void onRenderLevelStage(@NotNull RenderLevelStageEvent event) {
        Object object;
        boolean bl;
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        Font font = minecraft.font;
        GameRenderer gameRenderer = minecraft.gameRenderer;
        Camera camera = gameRenderer.getMainCamera();
        float f = BFRendering.getRenderTime();
        float f2 = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        GuiGraphics guiGraphics = new GuiGraphics(minecraft, bufferSource);
        PoseStack poseStack = guiGraphics.pose();
        boolean bl2 = minecraft.gui.getDebugOverlay().showDebugScreen();
        poseStack.mulPose(event.getPoseStack().last().pose());
        BFRendering.cameraOrigin(camera, poseStack);
        RenderLevelStageEvent.Stage stage = event.getStage();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        if (stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            RenderSystem.enableDepthTest();
            bFClientManager.getCorpseManager().render(minecraft, bFClientManager, bFClientPlayerData, clientLevel, localPlayer, poseStack, f2);
        }
        if (stage == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            bFClientManager.getParticleManager().render(minecraft, gameRenderer, camera, f2);
        }
        boolean bl3 = bl = stage == RenderLevelStageEvent.Stage.AFTER_PARTICLES;
        if (stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            BFRenderLevelSubscriber.renderAfterLevel(minecraft, f2);
        }
        Iterable iterable = clientLevel.entitiesForRendering();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Frustum frustum = minecraft.levelRenderer.getFrustum();
        if (stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            object = TOP_RENDERERS.iterator();
            while (object.hasNext()) {
                ITopLevelRenderer iTopLevelRenderer = (ITopLevelRenderer)object.next();
                iTopLevelRenderer.render(event, minecraft, bFClientManager, clientLevel, clientPlayerDataHandler, poseStack, frustum, guiGraphics, bufferSource, font, camera, iterable, threadLocalRandom, f2);
            }
        }
        if ((object = bFClientManager.getGame()) != null) {
            BFRenderLevelSubscriber.renderForGame(event, minecraft, bFClientManager, clientLevel, localPlayer, object, poseStack, frustum, guiGraphics, font, camera, bl, f, f2);
        }
        if (bl && bl2) {
            bFClientManager.method_5926(poseStack, guiGraphics, camera);
        }
    }

    private static void renderForGame(@NotNull RenderLevelStageEvent renderLevelStageEvent, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull PoseStack poseStack, @NotNull Frustum frustum, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull Camera camera, boolean bl, float f, float f2) {
        BlockRenderDispatcher blockRenderDispatcher = minecraft.getBlockRenderer();
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
        boolean bl2 = Minecraft.useFancyGraphics();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        mapEnvironment.render(renderLevelStageEvent, minecraft, bFClientManager, localPlayer, clientLevel, blockRenderDispatcher, bufferSource, poseStack, guiGraphics, font, camera, f, f2);
        if (bl) {
            Object obj = abstractGame.getPlayerManager();
            boolean bl3 = minecraft.getDebugOverlay().showDebugScreen();
            AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
            if (abstractGameClient != null && !bFClientManager.getCinematics().isSequencePlaying()) {
                abstractGameClient.renderDebug((AbstractGamePlayerManager<?>)obj, minecraft, clientLevel, localPlayer, renderLevelStageEvent, bufferSource, poseStack, frustum, font, guiGraphics, camera, bl3, f, f2);
            }
        }
    }

    private static void renderAfterLevel(@NotNull Minecraft minecraft, float f) {
        float f2;
        float f3 = 0.0f;
        if (BFClientSettings.EXPERIMENTAL_INSPECTION_BLUR.isEnabled()) {
            f2 = MathUtils.lerpf1(PlayerTickable.inspectionBlurPrev, PlayerTickable.inspectionBlur, f);
            f3 += f2 * 0.5f;
        }
        if ((f3 += (f2 = Math.max(0.0f, MathUtils.lerpf1(BF_29.field_119, BF_29.field_120, f) - 1.0f))) > 0.0f) {
            BFRendering.backgroundBlur(minecraft, f, Math.min((int)(10.0f * f3), 10));
        }
    }

    static {
        TOP_RENDERERS.add(new ScopeFlareRenderer());
        TOP_RENDERERS.add(new BulletTracerRenderer());
        TOP_RENDERERS.add(new TopDebugRenderer());
        TOP_RENDERERS.add(new DebugBoxRenderer());
        TOP_RENDERERS.add(new CinematicsRenderer());
    }
}

