/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.RandomCollection
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.bflib.common.RandomCollection;
import com.boehmod.blockfront.common.entity.NextbotEntity;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class NextbotRandomatEvent
extends RandomatEvent {
    private int spawnTimer = 400;
    private boolean spawned = false;
    private Vec3 spawnPosition = Vec3.ZERO;
    private NextbotEntity entity = null;

    @Override
    public void update(@NotNull TroubleTownGame game, @NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Set<UUID> players) {
        if (this.spawnTimer-- <= 0 && !this.spawned) {
            this.spawned = true;
            this.spawnNextbot(game, level, players);
        }
    }

    private void spawnNextbot(TroubleTownGame game, Level level, Set<UUID> players) {
        RandomCollection randomCollection = new RandomCollection();
        randomCollection.add(0.5, (Object)((EntityType)BFEntityTypes.OBUNGA.get()));
        randomCollection.add(0.5, (Object)((EntityType)BFEntityTypes.MICHAEL.get()));
        NextbotEntity nextbotEntity = (NextbotEntity)((EntityType)randomCollection.getRandom()).create(level);
        if (nextbotEntity != null) {
            nextbotEntity.teleportTo(this.spawnPosition.x, this.spawnPosition.y, this.spawnPosition.z);
            nextbotEntity.setGame(game);
            level.addFreshEntity((Entity)nextbotEntity);
            this.entity = nextbotEntity;
        }
        BFUtils.playSound(players, SoundEvents.WITHER_DEATH, SoundSource.AMBIENT);
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.ttt.randomat.nextbot.spawn").withStyle(ChatFormatting.GOLD);
        BFUtils.sendNoticeMessage(players, (Component)mutableComponent);
    }

    @Override
    public void activate(@NotNull TroubleTownGame game, @NotNull Player activator, @NotNull Level level, @NotNull Set<UUID> players) {
        this.spawnPosition = activator.position();
    }

    @Override
    public void finish(@NotNull TroubleTownGame game, @NotNull Level level, @NotNull Set<UUID> players) {
        if (this.entity != null && this.entity.isAlive() && !this.entity.isRemoved()) {
            this.entity.discard();
        }
    }

    @Override
    @NotNull
    public String getName() {
        return "bf.message.gamemode.ttt.randomat.nextbot";
    }
}

