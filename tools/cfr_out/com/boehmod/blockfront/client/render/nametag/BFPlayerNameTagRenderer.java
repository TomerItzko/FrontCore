/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  com.boehmod.bflib.common.ColorReferences
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec2
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.nametag;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_379;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.ClanUtils;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.jetbrains.annotations.NotNull;

public class BFPlayerNameTagRenderer
extends BF_379<Player> {
    private static final ResourceLocation field_1785 = BFRes.loc("textures/misc/waypoints/waypoint_player_injured.png");

    @Override
    public boolean method_1340(@NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @Nullable LocalPlayer localPlayer, @NotNull Player player, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull GameTeam gameTeam) {
        MutableComponent mutableComponent;
        UUID uUID = player.getUUID();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player);
        if (BFUtils.isPlayerUnavailable(player, bFClientPlayerData) || localPlayer != null && localPlayer.getUUID().equals(uUID)) {
            return false;
        }
        if (bFClientPlayerData.getCalloutTimer() <= 0 && localPlayer != null && !localPlayer.hasLineOfSight((Entity)player)) {
            return false;
        }
        if (!this.method_1348(player, abstractGame).equals(gameTeam.getName()) || player.getVehicle() != null) {
            return false;
        }
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(player);
        PlayerRank playerRank = playerCloudData.getRank();
        MutableComponent mutableComponent2 = playerCloudData.method_1715();
        if (playerCloudData.getClanId() != null) {
            mutableComponent = ClanUtils.getClanPrefix(playerCloudData);
            mutableComponent2 = mutableComponent.append(" ").append((Component)mutableComponent2);
        }
        mutableComponent = Component.literal((String)(playerRank.getGraphic() + " ")).append((Component)mutableComponent2);
        renderNameTagEvent.setContent((Component)mutableComponent);
        return true;
    }

    @Override
    public void method_1345(@NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, @NotNull Player player, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, LocalPlayer localPlayer, @NotNull Vec2 vec2, int n, int n2, int n3, float f, float f2, float f3, float f4, float f5) {
        ResourceLocation resourceLocation = this.method_1347(player, abstractGame).getIcon();
        ResourceLocation resourceLocation2 = field_1786;
        float f6 = 0.5f;
        UUID uUID = player.getUUID();
        if (localPlayer != null) {
            Object object;
            UUID uUID2 = localPlayer.getUUID();
            int n4 = BFUtils.getPlayerStat(abstractGame, uUID2, BFStats.CLASS);
            if (n4 != -1 && (object = MatchClass.fromIndex(n4)) == MatchClass.CLASS_MEDIC && !uUID.equals(uUID2) && BFUtils.isSameTeam(bFClientManager, uUID, uUID2) && player.getHealth() < 15.0f) {
                resourceLocation = field_1785;
                resourceLocation2 = null;
                f -= 0.2f;
                f6 = 1.0f;
            }
            if (((BFClientPlayerData)(object = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player))).getCalloutTimer() > 0) {
                resourceLocation = ((BFClientPlayerData)object).getWaypointTexture();
                n = ColorReferences.COLOR_THEME_YELLOW_SOLID;
                float f7 = Mth.sin((float)(f5 / 10.0f));
                f6 = Math.abs(f7);
            }
        }
        BFPlayerNameTagRenderer.method_1337(guiGraphics, n, n2, n3, f, vec2, f2, f3, f4, resourceLocation, resourceLocation2, f6);
    }

    @Override
    public String method_1348(@NotNull Player player, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(player.getUUID());
        return gameTeam == null ? "unknown" : gameTeam.getName();
    }

    @Override
    public MatchClass method_1347(@NotNull Player player, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        int n = abstractGame.getPlayerStatData(player.getUUID()).getInteger(BFStats.CLASS_ALIVE.getKey(), -1);
        MatchClass[] matchClassArray = MatchClass.values();
        return n >= 0 && n < matchClassArray.length ? matchClassArray[n] : MatchClass.CLASS_RIFLEMAN;
    }
}

