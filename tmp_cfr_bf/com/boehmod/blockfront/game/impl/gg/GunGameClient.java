/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.gg;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamGameElement;
import com.boehmod.blockfront.client.render.game.element.TimeGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import com.boehmod.blockfront.unnamed.BF_552;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class GunGameClient
extends AbstractGameClient<GunGame, GunGamePlayerManager> {
    private static final Component field_3504 = Component.literal((String)"<<< ").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD);
    private static final Component field_3505 = Component.literal((String)" >>>").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GREEN);
    private static final Component field_3506 = Component.translatable((String)"bf.message.gamemode.gungame.weapon.previous").withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
    private static final Component field_3507 = Component.translatable((String)"bf.message.gamemode.gungame.weapon.next").withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
    private static final Component field_3508 = Component.translatable((String)"bf.message.gamemode.gungame.weapon.current").withStyle(BFStyles.BOLD);
    private static final Component field_3509 = Component.empty().append(field_3507).append(field_3505).withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
    private static final Component field_3510 = Component.empty().append(field_3504).append(field_3506).withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
    private static final Component field_3511 = Component.translatable((String)"bf.message.gamemode.gungame.kill.last.title").withStyle(BFStyles.BOLD);
    private static final Component field_3512 = Component.translatable((String)"bf.message.gamemode.gungame.kill.last.subtitle").withStyle(ChatFormatting.YELLOW);
    private static final ResourceLocation field_3501 = BFRes.loc("textures/gui/backshadow.png");
    private static final ResourceLocation field_3502 = BFRes.loc("textures/gui/effects/fade.png");
    private static final ResourceLocation field_3503 = BFRes.loc("textures/gui/effects/starburst.png");

    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.gungame.tip.0"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.gungame.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.gungame.tip.2"));
        return objectArrayList;
    }

    public GunGameClient(@NotNull BFClientManager bFClientManager, @NotNull GunGame gunGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, gunGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_552(gunGame));
    }

    private void method_3600(@NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, int n, float f) {
        int n2 = BFUtils.getPlayerStat(this.game, localPlayer.getUUID(), BFStats.GG_STAGE);
        if (n2 > ((GunGame)this.game).GUN_STAGES.size()) {
            return;
        }
        GunItem gunItem = ((GunGame)this.game).getCurrentGun(n2);
        GunItem gunItem2 = ((GunGame)this.game).getPrevGun(n2);
        GunItem gunItem3 = ((GunGame)this.game).getNextGun(n2);
        boolean bl = n2 >= ((GunGame)this.game).GUN_STAGES.size();
        int n3 = 256;
        BFRendering.centeredTexture(poseStack, guiGraphics, field_3501, n, -10, 256, 256, 0.3f);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_3502, n, -10, 256, 256);
        BFRendering.centeredTexture(poseStack, guiGraphics, field_3502, (float)n, -10.0f, 128.0f, 128.0f, 0.5f);
        float f2 = 256.0f;
        float f3 = f / 5.0f;
        BFRendering.centeredTexture(poseStack, guiGraphics, field_3503, (float)n, -60.0f, 256.0f, 256.0f, f3, 0.5f);
        int n4 = 50;
        if (bl) {
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_3511, n, 20, 2.0f);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_3512, n, 45, 1.0f);
        } else {
            int n5;
            if (gunItem != null) {
                BFRendering.item(poseStack, guiGraphics, new ItemStack((ItemLike)gunItem), n, 50.0f, 4.0f);
            }
            if (gunItem3 != null) {
                n5 = n + 60;
                BFRendering.item(poseStack, guiGraphics, new ItemStack((ItemLike)gunItem3), n5, 50.0f, 2.0f);
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_3509, n5, 60, 0.5f);
            }
            if (gunItem2 != null) {
                n5 = n - 60;
                BFRendering.item(poseStack, guiGraphics, new ItemStack((ItemLike)gunItem2), n5, 50.0f, 2.0f);
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_3510, n5, 60, 0.5f);
            }
            if (gunItem != null) {
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, field_3508, n, 20, 1.5f);
                MutableComponent mutableComponent = Component.translatable((String)gunItem.asItem().getDescriptionId()).withStyle(ChatFormatting.BOLD);
                BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent, n, 70);
            }
        }
    }

    @Override
    @NotNull
    protected List<ClientGameElement<GunGame, GunGamePlayerManager>> getGameElements() {
        return List.of(new TeamGameElement(), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return true;
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        if (((GunGame)this.game).getStatus() == GameStatus.GAME) {
            this.method_3600(player, poseStack, graphics, font, midX, renderTime);
        }
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        UUID uUID;
        GameTeam gameTeam;
        GunGamePlayerManager gunGamePlayerManager = (GunGamePlayerManager)((GunGame)this.game).getPlayerManager();
        GameTeam gameTeam2 = gunGamePlayerManager.getPlayerTeam(localPlayer.getUUID());
        String string = null;
        if (livingEntity instanceof Player && (gameTeam = gunGamePlayerManager.getPlayerTeam(uUID = (object = (Player)livingEntity).getUUID())) != null) {
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
        GunGamePlayerManager gunGamePlayerManager = (GunGamePlayerManager)((GunGame)this.game).getPlayerManager();
        GameTeam gameTeam = gunGamePlayerManager.getPlayerTeam(player.getUUID());
        if (gameTeam == null) {
            return new ObjectArrayList();
        }
        Set<UUID> set = gameTeam.getPlayers();
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (AbstractClientPlayer abstractClientPlayer : level.players()) {
            BFClientPlayerData bFClientPlayerData;
            ResourceLocation resourceLocation;
            UUID uUID = abstractClientPlayer.getUUID();
            if (!players.contains(uUID) || abstractClientPlayer.equals((Object)player)) continue;
            boolean bl = true;
            boolean bl2 = set.contains(uUID);
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
        GunGamePlayerManager gunGamePlayerManager = (GunGamePlayerManager)((GunGame)this.game).getPlayerManager();
        String string = null;
        GameTeam gameTeam = gunGamePlayerManager.getPlayerTeam(localPlayer.getUUID());
        Object object2 = renderNameTagEvent.getEntity();
        if (object2 instanceof Player && (object2 = gunGamePlayerManager.getPlayerTeam((object = (Player)object2).getUUID())) != null) {
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
        return ((GunGame)this.game).getStatus() != GameStatus.GAME;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("K", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.KILLS.getKey())))).column("A", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.ASSISTS.getKey())))).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((GunGame)this.game, bl);
    }
}

