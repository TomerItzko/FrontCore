/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.game.element.TextWithIconGameElement;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TTTPlayerRoleGameElement
extends TextWithIconGameElement<TroubleTownGame, TroubleTownPlayerManager> {
    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull TroubleTownGame troubleTownGame, @NotNull TroubleTownPlayerManager troubleTownPlayerManager, @NotNull AbstractGameClient<TroubleTownGame, TroubleTownPlayerManager> abstractGameClient, @NotNull LocalPlayer localPlayer) {
        TTTPlayerRole tTTPlayerRole = troubleTownGame.getPlayerRole(localPlayer.getUUID());
        if (tTTPlayerRole != null) {
            this.setIconTexture(tTTPlayerRole.getTexture());
            this.setText((Component)Component.literal((String)tTTPlayerRole.getKey()).withColor(tTTPlayerRole.getColor()));
        }
    }
}

