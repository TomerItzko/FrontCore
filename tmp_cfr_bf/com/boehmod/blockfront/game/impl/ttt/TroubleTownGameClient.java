/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.TTTPlayerRoleGameElement;
import com.boehmod.blockfront.client.render.game.element.TTTPointsGameElement;
import com.boehmod.blockfront.client.render.game.element.TimeGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.ttt.TTTDeadBody;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.unnamed.BF_552;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class TroubleTownGameClient
extends AbstractGameClient<TroubleTownGame, TroubleTownPlayerManager> {
    private static final ResourceLocation field_3471 = BFRes.loc("textures/skins/game/troubletown/innocent.png");
    private static final ResourceLocation field_3472 = BFRes.loc("textures/skins/game/troubletown/detective.png");
    private static final ResourceLocation field_3473 = BFRes.loc("textures/misc/worldpoint/murder_info.png");

    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.troubletown.tip.0"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.troubletown.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.troubletown.tip.2"));
        return objectArrayList;
    }

    public TroubleTownGameClient(@NotNull BFClientManager bFClientManager, @NotNull TroubleTownGame troubleTownGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, troubleTownGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_552(troubleTownGame));
    }

    @Override
    @NotNull
    protected List<ClientGameElement<TroubleTownGame, TroubleTownPlayerManager>> getGameElements() {
        return List.of(new TTTPlayerRoleGameElement(), new TTTPointsGameElement(), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return false;
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        this.method_3533(font, player, level, graphics, midX, midY);
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        return false;
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
        BFClientPlayerData bFClientPlayerData = this.dataHandler.getPlayerData(minecraft);
        TTTPlayerRole tTTPlayerRole = ((TroubleTownGame)this.game).getPlayerRole(player.getUUID());
        boolean bl = BFUtils.isPlayerUnavailable((Player)player, bFClientPlayerData);
        for (Object object : level.entitiesForRendering()) {
            Player player2;
            if (!(object instanceof Player) || (player2 = (Player)object).equals((Object)player) || !player.hasLineOfSight(object)) continue;
            this.method_3532(this.dataHandler, graphics, poseStack, camera, (Entity)object, player2, tTTPlayerRole, f2, bl);
        }
        for (Object object : ((TroubleTownGame)this.game).field_3468) {
            this.method_3534(player, poseStack, graphics, font, camera, (TTTDeadBody)object);
        }
    }

    private void method_3534(@NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull Camera camera, @NotNull TTTDeadBody tTTDeadBody) {
        double d = tTTDeadBody.position.x;
        double d2 = tTTDeadBody.position.y;
        double d3 = tTTDeadBody.position.z;
        BFRendering.billboardTexture(poseStack, camera, guiGraphics, field_3473, d, d2, d3, 32, 32, 0.5f, true);
        double d4 = Mth.sqrt((float)((float)localPlayer.distanceToSqr(tTTDeadBody.position)));
        if (d4 > 2.0) {
            return;
        }
        TTTPlayerRole tTTPlayerRole = ((TroubleTownGame)this.game).getPlayerRole(tTTDeadBody.getUUID());
        if (tTTDeadBody.isIdentified()) {
            MutableComponent mutableComponent = Component.literal((String)tTTDeadBody.getUsername()).withStyle(ChatFormatting.GRAY);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ttt.murderinfo.title", (Object[])new Object[]{mutableComponent});
            MutableComponent mutableComponent3 = Component.literal((String)tTTPlayerRole.getKey()).withColor(tTTPlayerRole.getColor());
            MutableComponent mutableComponent4 = Component.translatable((String)"bf.message.gamemode.ttt.murderinfo.role", (Object[])new Object[]{mutableComponent3});
            BFRendering.component(poseStack, font, camera, guiGraphics, (Component)mutableComponent2, d, d2 + 1.0, d3, 0.5f);
            BFRendering.component(poseStack, font, camera, guiGraphics, (Component)mutableComponent4, d, d2 + (double)0.8f, d3, 0.5f);
            if (!tTTDeadBody.getItem().isEmpty()) {
                MutableComponent mutableComponent5 = Component.translatable((String)tTTDeadBody.getItem()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent6 = Component.translatable((String)"bf.message.gamemode.ttt.murderinfo.weapon", (Object[])new Object[]{mutableComponent5});
                BFRendering.component(poseStack, font, camera, guiGraphics, (Component)mutableComponent6, d, d2 + (double)0.6f, d3, 0.5f);
            } else {
                MutableComponent mutableComponent7 = Component.translatable((String)"bf.message.gamemode.ttt.murderinfo.weapon.unknown").withStyle(ChatFormatting.GRAY);
                BFRendering.component(poseStack, font, camera, guiGraphics, (Component)mutableComponent7, d, d2 + (double)0.6f, d3, 0.5f);
            }
        } else {
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.ttt.murderinfo.title.unidentified").withStyle(ChatFormatting.RED);
            BFRendering.component(poseStack, font, camera, guiGraphics, (Component)mutableComponent, d, d2 + 0.5, d3, 0.5f);
        }
    }

    private void method_3532(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull Camera camera, @NotNull Entity entity, @NotNull Player player, @NotNull TTTPlayerRole tTTPlayerRole, float f, boolean bl) {
        UUID uUID = player.getUUID();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player);
        if (BFUtils.isPlayerUnavailable(player, bFClientPlayerData)) {
            return;
        }
        TTTPlayerRole tTTPlayerRole2 = ((TroubleTownGame)this.game).getPlayerRole(uUID);
        if (!tTTPlayerRole2.displayOnNameplate()) {
            return;
        }
        Vec3 vec3 = entity.getEyePosition(f);
        boolean bl2 = false;
        if (tTTPlayerRole2 == TTTPlayerRole.DETECTIVE) {
            bl2 = true;
        } else if (tTTPlayerRole2 == TTTPlayerRole.TRAITOR && (tTTPlayerRole == TTTPlayerRole.TRAITOR || bl)) {
            bl2 = true;
        } else if (tTTPlayerRole2 == TTTPlayerRole.JESTER && (tTTPlayerRole == TTTPlayerRole.TRAITOR || bl)) {
            bl2 = true;
        }
        if (!bl2) {
            return;
        }
        ResourceLocation resourceLocation = tTTPlayerRole2.getTexture();
        if (resourceLocation != null) {
            BFRendering.billboardTexture(poseStack, camera, guiGraphics, resourceLocation, vec3.x, vec3.y + (double)1.15f, vec3.z, 32, 32);
        }
    }

    @Override
    public boolean method_2719() {
        return true;
    }

    @Override
    @NotNull
    public List<MinimapWaypoint> getSpecificMinimapWaypoints(@NotNull Minecraft minecraft, @NotNull Set<UUID> players, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        return new ObjectArrayList();
    }

    @Override
    public boolean method_2718() {
        return false;
    }

    @Override
    public void method_2710(@NotNull Minecraft minecraft, @NotNull RenderNameTagEvent renderNameTagEvent, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        TroubleTownPlayerManager troubleTownPlayerManager = (TroubleTownPlayerManager)((TroubleTownGame)this.game).getPlayerManager();
        String string = null;
        GameTeam gameTeam = troubleTownPlayerManager.getPlayerTeam(localPlayer.getUUID());
        Object object2 = renderNameTagEvent.getEntity();
        if (object2 instanceof Player && (object2 = troubleTownPlayerManager.getPlayerTeam((object = (Player)object2).getUUID())) != null) {
            string = ((GameTeam)object2).getName();
        }
        if ((object2 = renderNameTagEvent.getEntity()) instanceof BotEntity) {
            object = (BotEntity)object2;
            string = ((BotEntity)object).method_2031();
        }
        if (string != null && gameTeam != null && string.equals(gameTeam.getName()) && localPlayer.distanceTo(renderNameTagEvent.getEntity()) <= 20.0f) {
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
        return ((TroubleTownGame)this.game).getStatus() != GameStatus.GAME;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) {
                return "-";
            }
            BFClientPlayerData bFClientPlayerData2 = this.dataHandler.getPlayerData(minecraft);
            boolean bl = BFUtils.isPlayerUnavailable((Player)minecraft.player, bFClientPlayerData2);
            return bl ? StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey())) : "-";
        }));
    }

    @Override
    public ResourceLocation method_2696(@NotNull UUID uUID, @NotNull String string, @NotNull Set<UUID> set) {
        TTTPlayerRole tTTPlayerRole = ((TroubleTownGame)this.game).getPlayerRole(uUID);
        return tTTPlayerRole == TTTPlayerRole.DETECTIVE ? field_3472 : field_3471;
    }

    private void method_3533(@NotNull Font font, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull GuiGraphics guiGraphics, int n, int n2) {
        Player player;
        Entity entity2;
        Vec3 vec3 = localPlayer.getEyePosition();
        Vec3 vec32 = localPlayer.getLookAngle();
        Vec3 vec33 = vec3.add(vec32.x * 100.0, vec32.y * 100.0, vec32.z * 100.0);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult((Level)clientLevel, (Entity)localPlayer, (Vec3)vec3, (Vec3)vec33, (AABB)new AABB(vec3, vec33).inflate(1.0), entity -> {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)this.dataHandler.getPlayerData(player);
                return !BFUtils.isPlayerUnavailable(player, bFClientPlayerData);
            }
            return false;
        });
        if (entityHitResult != null && (entity2 = entityHitResult.getEntity()) instanceof Player && localPlayer.hasLineOfSight((Entity)(player = (Player)entity2))) {
            entity2 = this.method_3535(player);
            BFRendering.centeredString(font, guiGraphics, (Component)Component.literal((String)player.getScoreboardName()), n, n2 + 15);
            BFRendering.centeredString(font, guiGraphics, (Component)entity2, n, n2 + 25);
        }
    }

    @NotNull
    public MutableComponent method_3535(@NotNull Player player) {
        float f = player.getHealth();
        String string = "bf.message.gamemode.ttt.health.healthy";
        ChatFormatting chatFormatting = ChatFormatting.GREEN;
        if (f < 20.0f) {
            if (f >= 14.0f) {
                string = "bf.message.gamemode.ttt.health.hurt";
                chatFormatting = ChatFormatting.YELLOW;
            } else if (f >= 10.0f) {
                string = "bf.message.gamemode.ttt.health.wounded";
                chatFormatting = ChatFormatting.GOLD;
            } else if (f >= 4.0f) {
                string = "bf.message.gamemode.ttt.health.wounded.badly";
                chatFormatting = ChatFormatting.RED;
            } else {
                string = "bf.message.gamemode.ttt.health.near.death";
                chatFormatting = ChatFormatting.DARK_RED;
            }
        }
        return Component.translatable((String)string).withStyle(chatFormatting);
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((TroubleTownGame)this.game, bl);
    }
}

