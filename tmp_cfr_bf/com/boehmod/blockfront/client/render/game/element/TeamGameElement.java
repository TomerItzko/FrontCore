/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.game.element.TextWithIconGameElement;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TeamGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends TextWithIconGameElement<G, P> {
    private static final ResourceLocation ALLIES_ICON = BFRes.loc("textures/text/team_allies.png");
    private static final ResourceLocation AXIS_ICON = BFRes.loc("textures/text/team_axis.png");
    public static final Component NO_TEAM_COMPONENT = Component.literal((String)"No Team");

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull G game, @NotNull P playerManager, @NotNull AbstractGameClient<G, P> gameClient, @NotNull LocalPlayer player) {
        GameTeam gameTeam = ((AbstractGamePlayerManager)((AbstractGame)game).getPlayerManager()).getPlayerTeam(player.getUUID());
        if (gameTeam != null) {
            MutableComponent mutableComponent = Component.literal((String)gameTeam.getName()).withColor(gameTeam.getColor());
            this.setIconTexture(gameTeam.isAllies() ? ALLIES_ICON : AXIS_ICON);
            this.setText((Component)mutableComponent);
        } else {
            this.setIconTexture(null);
            this.setText(NO_TEAM_COMPONENT);
        }
    }
}

