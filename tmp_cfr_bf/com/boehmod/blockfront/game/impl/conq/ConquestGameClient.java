/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.bflib.common.ColorReferences;
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
import com.boehmod.blockfront.client.screen.match.conq.ConquestSelectClassScreen;
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
import com.boehmod.blockfront.game.impl.conq.ConquestBoundary;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import com.boehmod.blockfront.unnamed.BF_797;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.MathUtils;
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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class ConquestGameClient
extends CapturePointGameClient<ConquestGame, ConquestPlayerManager> {
    private static final ResourceLocation field_3369 = BFRes.loc("textures/misc/bfneutralicon.png");
    private static final ResourceLocation field_3370 = BFRes.loc("textures/misc/worldpoint/ammo.png");
    private static final ResourceLocation field_3371 = BFRes.loc("textures/gui/compass/waypoint_pp_shoot.png");
    private static final ResourceLocation field_3372 = BFRes.loc("textures/gui/compass/waypoint_pp_enemy_shoot.png");
    private static final ResourceLocation field_3373 = BFRes.loc("textures/gui/game/radio/bg.png");
    public static final Component field_3376 = Component.translatable((String)"bf.message.spawn.axis");
    public static final Component field_3377 = Component.translatable((String)"bf.message.spawn.allies");
    public static final Component field_3378 = Component.translatable((String)"bf.message.gamemode.conquest.bounds.title").withStyle(BFStyles.BOLD);
    private float field_3375;
    private float field_3379 = 0.0f;
    private boolean field_3374 = false;

    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.conquest.tip.0"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.conquest.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.conquest.tip.2"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.conquest.tip.3"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.domination.tip.4", (Object[])new Object[]{BFKeyMappings.MATCH_CLASS_SELECTION.getKey().getDisplayName().copy().withStyle(ChatFormatting.WHITE), BFKeyMappings.CALLOUT.getKey().getDisplayName().copy().withStyle(ChatFormatting.WHITE)}));
        return objectArrayList;
    }

    public ConquestGameClient(@NotNull BFClientManager bFClientManager, @NotNull ConquestGame conquestGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, conquestGame, clientPlayerDataHandler);
    }

    @Override
    @NotNull
    protected Screen method_2686(@NotNull GameTeam gameTeam) {
        return new ConquestSelectClassScreen(null, gameTeam, (ConquestGame)this.game, this);
    }

    @Override
    @NotNull
    protected List<ClientGameElement<ConquestGame, ConquestPlayerManager>> getGameElements() {
        return List.of(new TeamGameElement(), new TeamProgressGameElement(500, 64, "Allies", false), new CapturePointGameElement(), new TeamProgressGameElement(500, 64, "Axis", true), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return true;
    }

    @Override
    protected float method_2726(@NotNull LocalPlayer localPlayer) {
        return 0.05f;
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull Random random, @NotNull RandomSource randomSource, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientManager manager, @NotNull BFClientPlayerData playerData, @NotNull Set<UUID> players, float f, @NotNull Vec3 vec3, @NotNull BlockPos blockPos) {
        Object object;
        super.update(minecraft, random, randomSource, player, level, manager, playerData, players, f, vec3, blockPos);
        this.currentCapturePoint = ((ConquestGame)this.game).method_3269((Level)level, (Player)player, players);
        Object object2 = ((ConquestGame)this.game).getCapturePoints().iterator();
        while (object2.hasNext()) {
            object = object2.next();
            object.method_5501(player, level, this.game);
        }
        this.method_3295(player, playerData);
        if ((playerData.isOutOfGame() || !player.isAlive()) && minecraft.screen == null && (object2 = ((ConquestPlayerManager)((ConquestGame)this.game).getPlayerManager()).getPlayerTeam(player.getUUID())) != null) {
            object = new ConquestSelectClassScreen(null, (GameTeam)object2, (ConquestGame)this.game, this).method_911();
            minecraft.setScreen((Screen)object);
        }
    }

    private void method_3295(@NotNull LocalPlayer localPlayer, @NotNull BFClientPlayerData bFClientPlayerData) {
        this.field_3379 = this.field_3375;
        this.field_3374 = !((ConquestGame)this.game).field_3356.method_3132() && !bFClientPlayerData.isOutOfGame() && !((ConquestGame)this.game).field_3356.method_3128((float)localPlayer.getX(), (float)localPlayer.getZ());
        this.field_3375 = Mth.lerp((float)0.2f, (float)this.field_3375, (float)(this.field_3374 ? 1.0f : 0.0f));
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        super.renderSpecific(minecraft, manager, player, level, playerData, graphics, font, poseStack, bufferSource, players, width, height, midX, midY, renderTime, delta);
        float f = MathUtils.lerpf1(this.field_3375, this.field_3379, delta);
        BFRendering.rectangle(graphics, 0, 0, width, height, 0, 0.7f * f);
        if (this.field_3374) {
            this.method_3296(player, poseStack, graphics, font, midX, midY);
        }
    }

    private void method_3296(@NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2) {
        int n3 = BFUtils.getPlayerStat(this.game, localPlayer.getUUID(), BF_797.field_3382);
        int n4 = n2 - 50;
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n3)).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.conquest.bounds.subtitle", (Object[])new Object[]{mutableComponent});
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_3378, n, n4, 2.0f);
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent2, n, n4 + 20, 1.5f);
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
        List<ConquestCapturePoint> list = ((ConquestGame)this.game).getCapturePoints();
        for (ConquestCapturePoint conquestCapturePoint : list) {
            conquestCapturePoint.method_5497(this.game, frustum, graphics, font, camera, f);
        }
        boolean bl = minecraft.getEntityRenderDispatcher().shouldRenderHitBoxes();
        if (BFClientSettings.UI_RENDER_WAYPOINTS.isEnabled()) {
            int n = 20;
            for (MinimapWaypoint minimapWaypoint : this.method_2727()) {
                AABB aABB = minimapWaypoint.method_359();
                if (bl) {
                    LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines()), (AABB)aABB, (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                }
                if (!minimapWaypoint.method_364() || !frustum.isVisible(aABB)) continue;
                Vec3 vec3 = minimapWaypoint.method_360();
                BFRendering.billboardTexture(poseStack, camera, graphics, minimapWaypoint.getTexture(), vec3.x, vec3.y, vec3.z, 20, 20, 0.5f, false);
            }
        }
        if (bl) {
            for (ConquestCapturePoint conquestCapturePoint : ((ConquestGame)this.game).getCapturePoints()) {
                LevelRenderer.renderLineBox((PoseStack)poseStack, (VertexConsumer)minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines()), (AABB)conquestCapturePoint.getCaptureArea(), (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
        }
        if (renderGameInfo && player.isCreative()) {
            Vec3 vec3 = null;
            for (ConquestCapturePoint conquestCapturePoint : list) {
                if (vec3 != null) {
                    BFRendering.billboardLine(camera, poseStack, conquestCapturePoint.position, vec3, 1.0f, 0xFF0000);
                }
                vec3 = conquestCapturePoint.position;
            }
            int n = 10;
            int n2 = 12;
            boolean bl2 = false;
            int n3 = 3;
            int n4 = ColorReferences.COLOR_WHITE_SOLID;
            for (int i = 0; i < 12; ++i) {
                int n5 = 10 * (i + 1);
                for (ConquestBoundary.WallLine wallLine : ((ConquestGame)this.game).field_3356.method_3127()) {
                    Vec2 vec2 = wallLine.method_3134();
                    Vec2 vec22 = wallLine.method_3135();
                    Vec3 vec32 = new Vec3((double)vec2.x, (double)n5, (double)vec2.y);
                    Vec3 vec33 = new Vec3((double)vec2.x, 0.0, (double)vec2.y);
                    Vec3 vec34 = new Vec3((double)vec22.x, (double)n5, (double)vec22.y);
                    Vec3 vec35 = new Vec3((double)vec22.x, 0.0, (double)vec22.y);
                    BFRendering.billboardLine(camera, poseStack, vec32, vec33, 3.0f, n4);
                    BFRendering.billboardLine(camera, poseStack, vec34, vec35, 3.0f, n4);
                    BFRendering.billboardLine(camera, poseStack, vec32, vec34, 3.0f, n4);
                    Vec3[] vec3Array = new Vec3[]{vec33, vec32, vec34, vec35};
                    BFRendering.quad(poseStack, vec3Array, 1.0f, 1.0f, 1.0f, 0.5f);
                }
            }
        }
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        UUID uUID;
        GameTeam gameTeam;
        ConquestPlayerManager conquestPlayerManager = (ConquestPlayerManager)((ConquestGame)this.game).getPlayerManager();
        GameTeam gameTeam2 = conquestPlayerManager.getPlayerTeam(localPlayer.getUUID());
        String string = null;
        if (livingEntity instanceof Player && (gameTeam = conquestPlayerManager.getPlayerTeam(uUID = (object = (Player)livingEntity).getUUID())) != null) {
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
        ConquestPlayerManager conquestPlayerManager = (ConquestPlayerManager)((ConquestGame)this.game).getPlayerManager();
        ObjectArrayList objectArrayList = new ObjectArrayList();
        GameTeam gameTeam = conquestPlayerManager.getPlayerTeam(player.getUUID());
        if (gameTeam == null) {
            return objectArrayList;
        }
        GameTeam gameTeam2 = conquestPlayerManager.getTeamByName("Axis");
        GameTeam gameTeam3 = conquestPlayerManager.getTeamByName("Allies");
        if (gameTeam2 == null || gameTeam3 == null) {
            return objectArrayList;
        }
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        for (FDSPose fDSPose : gameTeam3.getPlayerSpawns()) {
            objectArrayList2.add(new Vec2((float)((int)fDSPose.position.x), (float)((int)fDSPose.position.z)));
        }
        ObjectArrayList objectArrayList3 = new ObjectArrayList();
        for (FDSPose fDSPose : gameTeam2.getPlayerSpawns()) {
            objectArrayList3.add(new Vec2((float)((int)fDSPose.position.x), (float)((int)fDSPose.position.z)));
        }
        int n = objectArrayList2.size();
        int n2 = objectArrayList3.size();
        float f = 0.0f;
        for (Object object : objectArrayList2) {
            f += ((Vec2)object).x;
        }
        f /= (float)Math.max(n, 1);
        float f2 = 0.0f;
        for (Object object : objectArrayList2) {
            f2 += ((Vec2)object).y;
        }
        objectArrayList.add(new MinimapWaypoint(BFRenderFrameSubscriber.ALLIES_ICON_TEXTURE, new Vec2(f, f2 /= (float)Math.max(n, 1))).method_353(9).component(field_3377));
        float f3 = 0.0f;
        for (Object object : objectArrayList3) {
            f3 += ((Vec2)object).x;
        }
        f3 /= (float)Math.max(n2, 1);
        float f4 = 0.0f;
        for (Vec2 vec2 : objectArrayList3) {
            f4 += vec2.y;
        }
        objectArrayList.add(new MinimapWaypoint(BFRenderFrameSubscriber.AXIS_ICON_TEXTURE, new Vec2(f3, f4 /= (float)Math.max(n2, 1))).method_353(9).component(field_3376));
        for (GameRadioPoint gameRadioPoint : this.getRadioPoints()) {
            Vec3 vec3 = gameRadioPoint.method_3181();
            objectArrayList.add(new MinimapWaypoint(field_3373, vec3).method_353(28));
            objectArrayList.add(new MinimapWaypoint(gameRadioPoint.getIcon(), vec3).method_353(14));
        }
        for (ConquestCapturePoint conquestCapturePoint : ((ConquestGame)this.game).getCapturePoints()) {
            int n3 = conquestCapturePoint.method_3143(this.game);
            ResourceLocation resourceLocation = conquestCapturePoint.getIcon();
            if (resourceLocation == null) continue;
            objectArrayList.add(new MinimapWaypoint(resourceLocation, conquestCapturePoint.position).icon(field_3369, n3).method_353(6).component((Component)Component.literal((String)conquestCapturePoint.name)));
        }
        for (AmmoPoint ammoPoint : ((ConquestGame)this.game).method_3271()) {
            objectArrayList.add(new MinimapWaypoint(field_3370, ammoPoint.position.add(0.0, 0.5, 0.0)).method_353(4));
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
                        botEntity = field_3372;
                    } else if (!player.hasLineOfSight((Entity)object)) {
                        bl = false;
                    }
                } else if (((BotEntity)object).method_2001() > 0) {
                    botEntity = field_3371;
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
        ConquestPlayerManager conquestPlayerManager = (ConquestPlayerManager)((ConquestGame)this.game).getPlayerManager();
        String string = null;
        GameTeam gameTeam = conquestPlayerManager.getPlayerTeam(localPlayer.getUUID());
        Object object2 = renderNameTagEvent.getEntity();
        if (object2 instanceof Player && (object2 = conquestPlayerManager.getPlayerTeam((object = (Player)object2).getUUID())) != null) {
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
        return ((ConquestGame)this.game).getStatus() != GameStatus.GAME;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("K", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.KILLS.getKey())))).column("A", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.ASSISTS.getKey())))).column("CP", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.CPOINTS.getKey())))).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((ConquestGame)this.game, bl);
    }
}

