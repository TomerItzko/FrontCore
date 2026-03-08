/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.game.element.TextGameElement;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class InfectedPointsGameElement
extends TextGameElement<InfectedGame, InfectedPlayerManager> {
    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull InfectedGame infectedGame, @NotNull InfectedPlayerManager infectedPlayerManager, @NotNull AbstractGameClient<InfectedGame, InfectedPlayerManager> abstractGameClient, @NotNull LocalPlayer localPlayer) {
        int n = BFUtils.getPlayerStat(infectedGame, localPlayer.getUUID(), BFStats.POINTS);
        MutableComponent mutableComponent = Component.literal((String)("$" + StringUtils.formatLong(n))).withStyle(ChatFormatting.GREEN);
        this.setText((Component)mutableComponent);
    }
}

