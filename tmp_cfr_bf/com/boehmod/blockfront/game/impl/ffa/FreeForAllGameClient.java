/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.ffa;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.StatProgressGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamGameElement;
import com.boehmod.blockfront.client.render.game.element.TimeGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGame;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllPlayerManager;
import com.boehmod.blockfront.unnamed.BF_552;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class FreeForAllGameClient
extends AbstractGameClient<FreeForAllGame, FreeForAllPlayerManager> {
    @NotNull
    private static final ResourceLocation field_6720 = BFRes.loc("textures/gui/game/topelement/kills.png");

    public FreeForAllGameClient(@NotNull BFClientManager bFClientManager, @NotNull FreeForAllGame freeForAllGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, freeForAllGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_552(freeForAllGame));
    }

    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(30));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.ffa.tip.0", (Object[])new Object[]{mutableComponent}));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.ffa.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.ffa.tip.2"));
        return objectArrayList;
    }

    @Override
    @NotNull
    protected List<ClientGameElement<FreeForAllGame, FreeForAllPlayerManager>> getGameElements() {
        return List.of(new TeamGameElement(), new StatProgressGameElement(field_6720, BFStats.KILLS, 30), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return true;
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        return false;
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
        FreeForAllPlayerManager freeForAllPlayerManager = (FreeForAllPlayerManager)((FreeForAllGame)this.game).getPlayerManager();
        String string = null;
        GameTeam gameTeam = freeForAllPlayerManager.getPlayerTeam(localPlayer.getUUID());
        Object object2 = renderNameTagEvent.getEntity();
        if (object2 instanceof Player && (object2 = freeForAllPlayerManager.getPlayerTeam((object = (Player)object2).getUUID())) != null) {
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
        return ((FreeForAllGame)this.game).getStatus() != GameStatus.GAME;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("K", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.KILLS.getKey())))).column("A", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.ASSISTS.getKey())))).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((FreeForAllGame)this.game, bl);
    }
}

