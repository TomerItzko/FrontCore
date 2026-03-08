/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.game.element.TextGameElement;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class InfectedWaveGameElement
extends TextGameElement<InfectedGame, InfectedPlayerManager> {
    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull AbstractGameClient<InfectedGame, InfectedPlayerManager> abstractGameClient, @NotNull LocalPlayer localPlayer) {
        MutableComponent mutableComponent = Component.literal((String)("Wave " + StringUtils.formatLong(infectedGame.currentRound))).withStyle(ChatFormatting.RED);
        this.setText((Component)mutableComponent);
    }
}

