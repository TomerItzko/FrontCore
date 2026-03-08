/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ExplodeRandomatEvent
extends RandomatEvent {
    private int field_3482 = 600;

    @Override
    public void update(@NotNull TroubleTownGame game, @NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Set<UUID> players) {
        if (this.field_3482-- <= 0) {
            this.field_3482 = 600;
            this.method_3544(game, dataHandler, players);
        }
    }

    private void method_3544(@NotNull TroubleTownGame troubleTownGame, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Set<UUID> set) {
        ObjectArrayList objectArrayList = new ObjectArrayList(set);
        objectArrayList.removeIf(uUID -> {
            Object d = playerDataHandler.getPlayerData((UUID)uUID);
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            return serverPlayer == null || BFUtils.isPlayerUnavailable((Player)serverPlayer, d);
        });
        if (objectArrayList.isEmpty()) {
            return;
        }
        Collections.shuffle(objectArrayList);
        UUID uUID2 = (UUID)objectArrayList.getFirst();
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID2);
        if (serverPlayer == null) {
            return;
        }
        serverPlayer.kill();
        BFExplosionPacket bFExplosionPacket = new BFExplosionPacket(ExplosionType.GENERIC, serverPlayer.position());
        PacketUtils.sendToGamePlayers(bFExplosionPacket, troubleTownGame);
        MutableComponent mutableComponent = Component.literal((String)serverPlayer.getScoreboardName()).withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ttt.randomat.random.explode.player", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GOLD);
        BFUtils.sendNoticeMessage(set, (Component)mutableComponent2);
    }

    @Override
    public void activate(@NotNull TroubleTownGame game, @NotNull Player activator, @NotNull Level level, @NotNull Set<UUID> players) {
    }

    @Override
    public void finish(@NotNull TroubleTownGame game, @NotNull Level level, @NotNull Set<UUID> players) {
        for (UUID uUID : players) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            serverPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
        }
    }

    @Override
    @NotNull
    public String getName() {
        return "bf.message.gamemode.ttt.randomat.random.explode";
    }
}

