/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.nametag;

import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_379;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.jetbrains.annotations.NotNull;

public class BFBotNameTagRenderer
extends BF_379<BotEntity> {
    public boolean method_1349(@NotNull RenderNameTagEvent renderNameTagEvent, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @Nullable LocalPlayer localPlayer, @NotNull BotEntity botEntity, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull GameTeam gameTeam) {
        if (!super.method_1340(renderNameTagEvent, minecraft, clientPlayerDataHandler, localPlayer, botEntity, abstractGame, abstractGameClient, gameTeam)) {
            return false;
        }
        MutableComponent mutableComponent = Component.literal((String)botEntity.getScoreboardName());
        GameTeam gameTeam2 = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getTeamByName(botEntity.method_2031());
        if (gameTeam2 != null) {
            mutableComponent.withStyle(gameTeam2.getStyleText());
        }
        renderNameTagEvent.setContent((Component)Component.literal((String)"BOT ").append((Component)mutableComponent));
        return true;
    }

    @Override
    public String method_1348(@NotNull BotEntity botEntity, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        return botEntity.method_2031();
    }

    @Override
    public MatchClass method_1347(@NotNull BotEntity botEntity, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        return botEntity.getCurrentClass();
    }
}

