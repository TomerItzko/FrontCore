/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.game.element.TextWithIconGameElement;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class StatProgressGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends TextWithIconGameElement<G, P> {
    @NotNull
    private final BFStat stat;
    private final int max;

    public StatProgressGameElement(@NotNull ResourceLocation iconTexture, @NotNull BFStat stat, int max) {
        this.stat = stat;
        this.max = max;
        super.setIconTexture(iconTexture);
    }

    public StatProgressGameElement(@NotNull ResourceLocation resourceLocation, @NotNull BFStat bFStat) {
        this(resourceLocation, bFStat, 0);
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull G game, @NotNull P playerManager, @NotNull AbstractGameClient<G, P> gameClient, @NotNull LocalPlayer player) {
        int n = BFUtils.getPlayerStat(game, player.getUUID(), this.stat);
        MutableComponent mutableComponent = this.max > 0 ? Component.literal((String)StringUtils.formatLong(n)).append((Component)Component.literal((String)" / ").withStyle(ChatFormatting.GRAY)).append((Component)Component.literal((String)StringUtils.formatLong(this.max))) : Component.literal((String)StringUtils.formatLong(n));
        this.setText((Component)mutableComponent);
    }
}

