/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.overlay.ClientStateFixer;
import com.boehmod.blockfront.client.render.overlay.DisclaimerOverlayRenderer;
import com.boehmod.blockfront.client.render.overlay.FlashOverlayRenderer;
import com.boehmod.blockfront.client.render.overlay.GameOverlayRenderer;
import com.boehmod.blockfront.client.render.overlay.PartyOverlayRenderer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.WindowUtils;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFRenderFrameSubscriber {
    @NotNull
    public static final ResourceLocation ALLIES_ICON_TEXTURE = BFRes.loc("textures/misc/bfalliesicon.png");
    @NotNull
    public static final ResourceLocation AXIS_ICON_TEXTURE = BFRes.loc("textures/misc/bfaxisicon.png");
    @NotNull
    public static final ResourceLocation NEUTRAL_ICON_TEXTURE = BFRes.loc("textures/misc/bfneutralicon.png");
    @NotNull
    private static final List<GameOverlayRenderer> RENDERERS = (List)Util.make((Object)new ObjectArrayList(), list -> {
        list.add((Object)new FlashOverlayRenderer());
        list.add((Object)new DisclaimerOverlayRenderer());
        list.add((Object)new PartyOverlayRenderer());
        list.add((Object)new ClientStateFixer());
    });
    public static float field_334;
    public static float field_336;
    public static float field_338;
    public static float field_339;
    public static float field_340;
    public static float field_341;

    @SubscribeEvent
    public static void method_5853(RenderFrameEvent.Pre pre) {
        Minecraft minecraft = Minecraft.getInstance();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null && minecraft.player != null) {
            abstractGameClient.method_2704(minecraft, minecraft.player);
        }
    }

    @SubscribeEvent
    public static void onRenderFrame(RenderFrameEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        GuiGraphics guiGraphics = new GuiGraphics(minecraft, minecraft.renderBuffers().bufferSource());
        PoseStack poseStack = guiGraphics.pose();
        float f = event.getPartialTick().getGameTimeDeltaPartialTick(false);
        float f2 = BFRendering.getRenderTime();
        poseStack.pushPose();
        poseStack.setIdentity();
        poseStack.translate(0.0f, 0.0f, 10000.0f - ClientHooks.getGuiFarPlane());
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
        boolean bl = clientLevel != null && localPlayer != null;
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        Window window = minecraft.getWindow();
        int n = guiGraphics.guiWidth();
        int n2 = guiGraphics.guiHeight();
        WindowUtils.setWindowIcon(bFClientManager, window);
        WindowUtils.setWindowTitle(bFClientManager, minecraft, playerCloudData.method_1718().getString());
        if (bl) {
            bFClientManager.getCinematics().method_2202(minecraft, poseStack, guiGraphics, font, n, n2, f);
        }
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        for (GameOverlayRenderer gameOverlayRenderer : RENDERERS) {
            gameOverlayRenderer.render(event, minecraft, localPlayer, clientLevel, guiGraphics.pose(), font, guiGraphics, bFClientManager, playerCloudData, bFClientPlayerData, abstractGame, n, n2, bl, f2, f);
        }
        guiGraphics.flush();
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    static {
        field_336 = 0.0f;
        field_339 = 0.0f;
        field_341 = 0.0f;
    }
}

