/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.dom;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFKeyMappings;
import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.CapturePointGameElement;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamProgressGameElement;
import com.boehmod.blockfront.client.render.game.element.TimeGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AmmoPoint;
import com.boehmod.blockfront.game.CapturePointGameClient;
import com.boehmod.blockfront.game.GameRadioPoint;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.dom.DominationCapturePoint;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import com.boehmod.blockfront.unnamed.BF_552;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class DominationGameClient
extends CapturePointGameClient<DominationGame, DominationPlayerManager> {
    private static final Component field_3407 = Component.translatable((String)"bf.message.spawn.axis");
    private static final Component field_3408 = Component.translatable((String)"bf.message.spawn.allies");
    private static final ResourceLocation field_3402 = BFRes.loc("textures/misc/worldpoint/ammo.png");
    private static final ResourceLocation field_3403 = BFRes.loc("textures/gui/game/radio/bg.png");
    private static final ResourceLocation field_3404 = BFRes.loc("textures/gui/compass/waypoint_pp_enemy_shoot.png");
    private static final ResourceLocation field_3405 = BFRes.loc("textures/gui/compass/waypoint_pp_shoot.png");
    private static final ResourceLocation field_3406 = BFRes.loc("textures/misc/bfneutralicon.png");

    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.domination.tip.0"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.domination.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.domination.tip.2"));
        MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(500L)).withStyle(ChatFormatting.WHITE);
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.domination.tip.3", (Object[])new Object[]{mutableComponent}));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.domination.tip.4", (Object[])new Object[]{BFKeyMappings.MATCH_CLASS_SELECTION.getKey().getDisplayName().copy().withStyle(ChatFormatting.WHITE), BFKeyMappings.CALLOUT.getKey().getDisplayName().copy().withStyle(ChatFormatting.WHITE)}));
        return objectArrayList;
    }

    public DominationGameClient(@NotNull BFClientManager bFClientManager, @NotNull DominationGame dominationGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, dominationGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_552(dominationGame));
    }

    @Override
    @NotNull
    protected List<ClientGameElement<DominationGame, DominationPlayerManager>> getGameElements() {
        return List.of(new TeamGameElement(), new TeamProgressGameElement(500, 64, "Allies", false), new CapturePointGameElement(), new TeamProgressGameElement(500, 64, "Axis", true), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return true;
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull Random random, @NotNull RandomSource randomSource, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientManager manager, @NotNull BFClientPlayerData playerData, @NotNull Set<UUID> players, float f, @NotNull Vec3 vec3, @NotNull BlockPos blockPos) {
        super.update(minecraft, random, randomSource, player, level, manager, playerData, players, f, vec3, blockPos);
        this.currentCapturePoint = ((DominationGame)this.game).method_3331((Player)player, players);
        for (DominationCapturePoint dominationCapturePoint : ((DominationGame)this.game).getCapturePoints()) {
            dominationCapturePoint.method_5501(player, level, this.game);
        }
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        AABB aABB;
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
        for (DominationCapturePoint dominationCapturePoint : ((DominationGame)this.game).getCapturePoints()) {
            dominationCapturePoint.method_5497(this.game, frustum, graphics, font, camera, f);
        }
        boolean bl = minecraft.getEntityRenderDispatcher().shouldRenderHitBoxes();
        if (BFClientSettings.UI_RENDER_WAYPOINTS.isEnabled()) {
            int n = 20;
            for (MinimapWaypoint minimapWaypoint : this.method_2727()) {
                aABB = minimapWaypoint.method_359();
                if (bl) {
                    LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines()), (AABB)aABB, (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
                if (!minimapWaypoint.method_364() || !frustum.isVisible(aABB)) continue;
                Vec3 vec3 = minimapWaypoint.method_360();
                BFRendering.billboardTexture(poseStack, camera, graphics, minimapWaypoint.getTexture(), vec3.x, vec3.y, vec3.z, 20, 20, 0.5f, false);
            }
        }
        if (bl) {
            for (DominationCapturePoint dominationCapturePoint : ((DominationGame)this.game).getCapturePoints()) {
                LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines()), (AABB)dominationCapturePoint.getCaptureArea(), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
        }
        if (minecraft.getDebugOverlay().showDebugScreen() && player.isCreative()) {
            AABB aABB2 = null;
            for (DominationCapturePoint dominationCapturePoint : ((DominationGame)this.game).getCapturePoints()) {
                aABB = dominationCapturePoint.position;
                if (aABB2 != null) {
                    BFRendering.billboardLine(camera, poseStack, (Vec3)aABB, (Vec3)aABB2, 1.0f, 0xFF0000);
                }
                aABB2 = aABB;
            }
        }
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        UUID uUID;
        GameTeam gameTeam;
        DominationPlayerManager dominationPlayerManager = (DominationPlayerManager)((DominationGame)this.game).getPlayerManager();
        GameTeam gameTeam2 = dominationPlayerManager.getPlayerTeam(localPlayer.getUUID());
        String string = null;
        if (livingEntity instanceof Player && (gameTeam = dominationPlayerManager.getPlayerTeam(uUID = (object = (Player)livingEntity).getUUID())) != null) {
            string = gameTeam.getName();
        }
        if (livingEntity instanceof BotEntity) {
            object = (BotEntity)livingEntity;
            string = ((BotEntity)object).method_2031();
        }
        if (gameTeam2 != null && string != null) {
            return gameTeam2.getName().equalsIgnoreCase(string);
        }
        return false;
    }

    @Override
    public boolean method_2719() {
        return true;
    }

    @Override
    @NotNull
    public List<MinimapWaypoint> getSpecificMinimapWaypoints(@NotNull Minecraft minecraft, @NotNull Set<UUID> players, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        DominationPlayerManager dominationPlayerManager = (DominationPlayerManager)((DominationGame)this.game).getPlayerManager();
        GameTeam gameTeam = dominationPlayerManager.getPlayerTeam(player.getUUID());
        if (gameTeam == null) {
            return new ObjectArrayList();
        }
        GameTeam gameTeam2 = dominationPlayerManager.getTeamByName("Axis");
        GameTeam gameTeam3 = dominationPlayerManager.getTeamByName("Allies");
        if (gameTeam2 == null || gameTeam3 == null) {
            return new ObjectArrayList();
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        for (FDSPose object : gameTeam3.getPlayerSpawns()) {
            objectArrayList2.add(new Vec2((float)((int)object.position.x), (float)((int)object.position.z)));
        }
        ObjectArrayList objectArrayList3 = new ObjectArrayList();
        for (FDSPose n2 : gameTeam2.getPlayerSpawns()) {
            objectArrayList3.add(new Vec2((float)((int)n2.position.x), (float)((int)n2.position.z)));
        }
        int n = objectArrayList2.size();
        int n2 = objectArrayList3.size();
        float f = 0.0f;
        for (Object f3 : objectArrayList2) {
            f += ((Vec2)f3).x;
        }
        f /= (float)Math.max(n, 1);
        float f2 = 0.0f;
        for (Object f4 : objectArrayList2) {
            f2 += ((Vec2)f4).y;
        }
        objectArrayList.add(new MinimapWaypoint(BFRenderFrameSubscriber.ALLIES_ICON_TEXTURE, new Vec2(f, f2 /= (float)Math.max(n, 1))).method_353(9).component(field_3408));
        float f3 = 0.0f;
        for (Object object : objectArrayList3) {
            f3 += ((Vec2)object).x;
        }
        f3 /= (float)Math.max(n2, 1);
        float f4 = 0.0f;
        for (Vec2 vec2 : objectArrayList3) {
            f4 += vec2.y;
        }
        objectArrayList.add(new MinimapWaypoint(BFRenderFrameSubscriber.AXIS_ICON_TEXTURE, new Vec2(f3, f4 /= (float)Math.max(n2, 1))).method_353(9).component(field_3407));
        for (GameRadioPoint gameRadioPoint : this.getRadioPoints()) {
            Vec3 vec3 = gameRadioPoint.method_3181();
            objectArrayList.add(new MinimapWaypoint(field_3403, vec3).method_353(28));
            objectArrayList.add(new MinimapWaypoint(gameRadioPoint.getIcon(), vec3).method_353(14));
        }
        for (DominationCapturePoint dominationCapturePoint : ((DominationGame)this.game).getCapturePoints()) {
            int n3 = dominationCapturePoint.method_3143(this.game);
            ResourceLocation resourceLocation = dominationCapturePoint.getIcon();
            if (resourceLocation == null) continue;
            MutableComponent mutableComponent = Component.literal((String)dominationCapturePoint.name);
            objectArrayList.add(new MinimapWaypoint(resourceLocation, dominationCapturePoint.position).icon(field_3406, n3).method_353(6).component((Component)mutableComponent));
        }
        for (AmmoPoint ammoPoint : ((DominationGame)this.game).method_3325()) {
            objectArrayList.add(new MinimapWaypoint(field_3402, ammoPoint.position.add(0.0, 0.5, 0.0)).method_353(4).method_351());
        }
        for (Entity entity : level.entitiesForRendering()) {
            Object object;
            BFClientPlayerData bFClientPlayerData;
            BotEntity botEntity;
            UUID uUID = entity.getUUID();
            if ((!players.contains(uUID) || uUID.equals(minecraft.getUser().getProfileId())) && !(entity instanceof BotEntity)) continue;
            boolean bl = true;
            boolean bl2 = gameTeam.getPlayers().contains(uUID);
            if (entity instanceof BotEntity) {
                botEntity = (BotEntity)entity;
                bl2 = botEntity.method_2031().equalsIgnoreCase(gameTeam.getName());
            }
            Object object2 = botEntity = bl2 ? MinimapWaypoint.TEXTURE_PLAYER : MinimapWaypoint.TEXTURE_ENEMY;
            if (!bl2 && entity instanceof Player && (bFClientPlayerData = (BFClientPlayerData)this.dataHandler.getPlayerData((Player)(object = (Player)entity))).method_1131() <= 0) {
                bl = false;
            }
            if (entity instanceof BotEntity) {
                object = (BotEntity)entity;
                bl2 = gameTeam.getName().equalsIgnoreCase(((BotEntity)object).method_2031());
                if (!bl2) {
                    if (((BotEntity)object).method_2001() > 0) {
                        botEntity = field_3404;
                    } else if (!player.hasLineOfSight((Entity)object)) {
                        bl = false;
                    }
                } else if (((BotEntity)object).method_2001() > 0) {
                    botEntity = field_3405;
                }
            }
            if (!bl) continue;
            float f5 = entity.getYRot() - 180.0f;
            objectArrayList.add(new MinimapWaypoint((ResourceLocation)botEntity, entity.position()).method_353(4).method_352(f5));
        }
        return objectArrayList;
    }

    @Override
    public boolean method_2718() {
        return false;
    }

    @Override
    public void method_2710(@NotNull Minecraft minecraft, @NotNull RenderNameTagEvent renderNameTagEvent, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        DominationPlayerManager dominationPlayerManager = (DominationPlayerManager)((DominationGame)this.game).getPlayerManager();
        String string = null;
        GameTeam gameTeam = dominationPlayerManager.getPlayerTeam(localPlayer.getUUID());
        Object object2 = renderNameTagEvent.getEntity();
        if (object2 instanceof Player && (object2 = dominationPlayerManager.getPlayerTeam((object = (Player)object2).getUUID())) != null) {
            string = ((GameTeam)object2).getName();
        }
        if ((object2 = renderNameTagEvent.getEntity()) instanceof BotEntity) {
            object = (BotEntity)object2;
            string = ((BotEntity)object).method_2031();
        }
        if (string != null && gameTeam != null && string.equals(gameTeam.getName()) && localPlayer.hasLineOfSight(renderNameTagEvent.getEntity()) && localPlayer.distanceTo(renderNameTagEvent.getEntity()) <= 20.0f) {
            renderNameTagEvent.setCanRender(TriState.TRUE);
            return;
        }
        renderNameTagEvent.setCanRender(TriState.FALSE);
    }

    @Override
    public boolean method_2730(@NotNull Player player) {
        if (player.getVehicle() != null) {
            return true;
        }
        return ((DominationGame)this.game).getStatus() != GameStatus.GAME;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("K", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.KILLS.getKey())))).column("A", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.ASSISTS.getKey())))).column("CP", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.CPOINTS.getKey())))).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((DominationGame)this.game, bl);
    }
}

