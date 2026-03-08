/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.game.element.TextGameElement;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class TTTPointsGameElement
extends TextGameElement<TroubleTownGame, TroubleTownPlayerManager> {
    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull AbstractGameClient<TroubleTownGame, TroubleTownPlayerManager> abstractGameClient, @NotNull LocalPlayer localPlayer) {
        UUID uUID = localPlayer.getUUID();
        int n = BFUtils.getPlayerStat(troubleTownGame, uUID, BFStats.POINTS);
        MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(n)).withStyle(ChatFormatting.WHITE);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ttt.points", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GRAY);
        this.setText((Component)mutableComponent2);
    }
}

