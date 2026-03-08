/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  net.neoforged.neoforge.common.util.TriState
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.nametag.BFAbstractNameTagRenderer;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFRenderNameTagSubscriber {
    @SubscribeEvent
    public static void onRenderNameTag(@NotNull RenderNameTagEvent event) {
        Object object;
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        if (!BFClientSettings.EXPERIMENTAL_TOGGLE_NAMETAGS.isEnabled()) {
            event.setCanRender(TriState.FALSE);
            return;
        }
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient == null) {
            return;
        }
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(localPlayer.getUUID());
        if (gameTeam == null) {
            return;
        }
        Entity entity = event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        GuiGraphics guiGraphics = new GuiGraphics(minecraft, minecraft.renderBuffers().bufferSource());
        guiGraphics.pose().mulPose(poseStack.last().pose());
        int n = gameTeam.getColor();
        if (entity instanceof FakePlayer) {
            event.setCanRender(TriState.FALSE);
            return;
        }
        if (entity instanceof LivingEntity && !BFRenderNameTagSubscriber.method_433(event, minecraft, clientLevel, localPlayer, (LivingEntity)(object = (LivingEntity)entity), abstractGameClient)) {
            event.setCanRender(TriState.FALSE);
            return;
        }
        object = BFAbstractNameTagRenderer.method_1338(entity.getType());
        if (object == null) {
            return;
        }
        if (((BFAbstractNameTagRenderer)object).method_1340(event, minecraft, clientPlayerDataHandler, localPlayer, entity, abstractGame, abstractGameClient, gameTeam)) {
            float f = BFRendering.getRenderTime();
            ((BFAbstractNameTagRenderer)object).method_1336(bFClientManager, event, minecraft, clientPlayerDataHandler, entity, guiGraphics, abstractGame, abstractGameClient, (AbstractGamePlayerManager<?>)obj, gameTeam, n, f, event.getPartialTick());
        } else {
            event.setCanRender(TriState.FALSE);
        }
    }

    private static boolean method_433(@NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull LivingEntity livingEntity, @NotNull AbstractGameClient<? extends AbstractGame<?, ?, ?>, ?> abstractGameClient) {
        if (!abstractGameClient.method_2709(minecraft, livingEntity, localPlayer, clientLevel)) {
            return false;
        }
        abstractGameClient.method_2710(minecraft, renderNameTagEvent, localPlayer, clientLevel);
        return true;
    }
}

