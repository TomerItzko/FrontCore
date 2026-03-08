/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.match;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFGameCalloutPacket;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class MatchCalloutScreen
extends BFScreen {
    private static final Component TITLE = Component.translatable((String)"bf.screen.callout");

    public MatchCalloutScreen() {
        super(TITLE);
    }

    protected void init() {
        super.init();
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = 64;
        int n4 = 2;
        int n5 = n - 96 - 2;
        int n6 = n2 - 96 - 2;
        for (MatchCallout matchCallout : MatchCallout.values()) {
            Vector2i vector2i = matchCallout.getButtonPosition();
            int n7 = 1 + vector2i.x;
            int n8 = 1 - vector2i.y;
            MutableComponent mutableComponent = Component.translatable((String)(matchCallout.getTranslation() + ".title"));
            this.addRenderableWidget((GuiEventListener)new BFButton(n5 + 64 * n7 + 2 * n7, n6 + 64 * n8 + 2 * n8, 64, 64, (Component)mutableComponent, button -> this.doCallout(matchCallout)).displayType(BFButton.DisplayType.FANCY).texture(matchCallout.getTexture()).method_391(0, -4).size(32).method_368(0.0f, 20.0f));
        }
    }

    public void tick() {
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame == null || abstractGame.getStatus() != GameStatus.GAME) {
            this.onClose();
        }
    }

    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) {
            return;
        }
        BFRendering.rectangle(graphics, 0, 0, this.width, this.height, BFRendering.translucentBlack());
        super.render(graphics, mouseX, mouseY, delta);
        int n = this.width / 2;
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        if (abstractGame instanceof IAllowsWarCry) {
            UUID uUID = this.minecraft.player.getUUID();
            int n2 = BFUtils.getPlayerStat(abstractGame, uUID, BFStats.WAR_CRY_COMMANDER);
            int n3 = BFUtils.getPlayerStat(abstractGame, uUID, BFStats.CLASS);
            if (n3 == MatchClass.CLASS_COMMANDER.ordinal()) {
                if (n2 <= 0) {
                    MutableComponent mutableComponent = Component.translatable((String)MatchCallout.FORWARD.getTranslation()).withColor(0xFFFFFF);
                    MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.ingame.warcry.commander", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
                    BFRendering.centeredString(this.font, graphics, (Component)mutableComponent2, n, 30);
                } else {
                    MutableComponent mutableComponent = Component.literal((String)String.valueOf(n2 / 20)).withColor(0xFFFFFF);
                    MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.ingame.warcry.commander.cooldown", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
                    BFRendering.centeredString(this.font, graphics, (Component)mutableComponent3, n, 30);
                }
            }
        }
    }

    public void doCallout(@NotNull MatchCallout callout) {
        this.onClose();
        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) {
            return;
        }
        BFClientPlayerData bFClientPlayerData = this.playerDataHandler.getPlayerData(this.minecraft);
        if (BFUtils.isPlayerUnavailable((Player)localPlayer, bFClientPlayerData)) {
            return;
        }
        if (bFClientPlayerData.calloutCooldown > 0) {
            return;
        }
        bFClientPlayerData.calloutCooldown = 80;
        BFGameCalloutPacket bFGameCalloutPacket = new BFGameCalloutPacket(callout);
        PacketUtils.sendToServer(bFGameCalloutPacket);
    }
}

