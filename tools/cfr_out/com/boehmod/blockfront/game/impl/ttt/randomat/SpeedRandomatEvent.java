/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SpeedRandomatEvent
extends RandomatEvent {
    @Override
    public void update(@NotNull TroubleTownGame game, @NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Set<UUID> players) {
    }

    @Override
    public void activate(@NotNull TroubleTownGame game, @NotNull Player activator, @NotNull Level level, @NotNull Set<UUID> players) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (UUID uUID : players) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, threadLocalRandom.nextInt(2400), threadLocalRandom.nextInt(4)));
        }
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
        return "bf.message.gamemode.ttt.randomat.speed";
    }
}

