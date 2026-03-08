/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FartRandomatEvent
extends RandomatEvent {
    private int field_3485 = 10;
    private boolean field_3484 = false;

    @Override
    public void update(@NotNull TroubleTownGame game, @NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Set<UUID> players) {
        if (this.field_3485-- <= 0 && !this.field_3484) {
            this.field_3484 = true;
            Set<UUID> set = game.method_3516(TTTPlayerRole.TRAITOR, players);
            ObjectArrayList objectArrayList = new ObjectArrayList(set);
            Collections.shuffle(objectArrayList);
            for (UUID uUID : objectArrayList) {
                Object obj;
                ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
                if (serverPlayer == null || BFUtils.isPlayerUnavailable((Player)serverPlayer, obj = dataHandler.getPlayerData((Player)serverPlayer))) continue;
                serverPlayer.playSound((SoundEvent)BFSounds.MATCH_GAMEMODE_TTT_RANDOMAT_FART.get(), 8.0f, (float)((double)0.8f + (double)0.3f * Math.random()));
                return;
            }
        }
    }

    @Override
    public void activate(@NotNull TroubleTownGame game, @NotNull Player activator, @NotNull Level level, @NotNull Set<UUID> players) {
    }

    @Override
    public void finish(@NotNull TroubleTownGame game, @NotNull Level level, @NotNull Set<UUID> players) {
    }

    @Override
    @NotNull
    public String getName() {
        return "bf.message.gamemode.ttt.traitor.fart";
    }
}

