/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  net.neoforged.neoforge.common.util.TriState
 *  org.apache.commons.lang3.function.TriFunction
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamScoreGameElement;
import com.boehmod.blockfront.client.render.game.element.TimeGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import com.boehmod.blockfront.unnamed.BF_552;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class TeamDeathmatchGameClient
extends AbstractGameClient<TeamDeathmatchGame, TeamDeathmatchPlayerManager> {
    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(50)).withStyle(ChatFormatting.WHITE);
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.tdm.tip.0", (Object[])new Object[]{mutableComponent}));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.tdm.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.tdm.tip.2"));
        return objectArrayList;
    }

    public TeamDeathmatchGameClient(@NotNull BFClientManager bFClientManager, @NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, teamDeathmatchGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_552(teamDeathmatchGame));
    }

    @Override
    @NotNull
    protected List<ClientGameElement<TeamDeathmatchGame, TeamDeathmatchPlayerManager>> getGameElements() {
        return List.of(new TeamGameElement(), new TeamScoreGameElement(), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return true;
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        GameTeam gameTeam;
        TeamDeathmatchPlayerManager teamDeathmatchPlayerManager = (TeamDeathmatchPlayerManager)((TeamDeathmatchGame)this.game).getPlayerManager();
        GameTeam gameTeam2 = teamDeathmatchPlayerManager.getPlayerTeam(localPlayer.getUUID());
        String string = null;
        if (livingEntity instanceof Player && (gameTeam = teamDeathmatchPlayerManager.getPlayerTeam((object = (Player)livingEntity).getUUID())) != null) {
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
        ObjectArrayList objectArrayList = new ObjectArrayList();
        TeamDeathmatchPlayerManager teamDeathmatchPlayerManager = (TeamDeathmatchPlayerManager)((TeamDeathmatchGame)this.game).getPlayerManager();
        GameTeam gameTeam = teamDeathmatchPlayerManager.getPlayerTeam(player.getUUID());
        if (gameTeam == null) {
            return objectArrayList;
        }
        UUID uUID = minecraft.getUser().getProfileId();
        for (AbstractClientPlayer abstractClientPlayer : level.players()) {
            BFClientPlayerData bFClientPlayerData;
            ResourceLocation resourceLocation;
            UUID uUID2 = abstractClientPlayer.getUUID();
            if (!players.contains(uUID2) || uUID2.equals(uUID)) continue;
            boolean bl = true;
            boolean bl2 = gameTeam.getPlayers().contains(uUID2);
            ResourceLocation resourceLocation2 = resourceLocation = bl2 ? MinimapWaypoint.TEXTURE_PLAYER : MinimapWaypoint.TEXTURE_ENEMY;
            if (!bl2 && (bFClientPlayerData = (BFClientPlayerData)this.dataHandler.getPlayerData((Player)abstractClientPlayer)).method_1131() <= 0) {
                bl = false;
            }
            if (!bl) continue;
            float f = abstractClientPlayer.getYRot() - 180.0f;
            objectArrayList.add(new MinimapWaypoint(resourceLocation, abstractClientPlayer.position()).method_353(4).method_352(f));
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
        GameTeam gameTeam;
        TeamDeathmatchPlayerManager teamDeathmatchPlayerManager = (TeamDeathmatchPlayerManager)((TeamDeathmatchGame)this.game).getPlayerManager();
        String string = null;
        GameTeam gameTeam2 = teamDeathmatchPlayerManager.getPlayerTeam(localPlayer.getUUID());
        Object object2 = renderNameTagEvent.getEntity();
        if (object2 instanceof Player && (gameTeam = teamDeathmatchPlayerManager.getPlayerTeam((UUID)(object2 = (object = (Player)object2).getUUID()))) != null) {
            string = gameTeam.getName();
        }
        if ((object2 = renderNameTagEvent.getEntity()) instanceof BotEntity) {
            object = (BotEntity)object2;
            string = ((BotEntity)object).method_2031();
        }
        if (string != null && gameTeam2 != null && string.equals(gameTeam2.getName()) && localPlayer.hasLineOfSight(renderNameTagEvent.getEntity()) && localPlayer.distanceTo(renderNameTagEvent.getEntity()) <= 20.0f) {
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
        return ((TeamDeathmatchGame)this.game).getStatus() != GameStatus.GAME;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("K", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.KILLS.getKey())))).column("A", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.ASSISTS.getKey())))).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((TeamDeathmatchGame)this.game, bl);
    }
}

