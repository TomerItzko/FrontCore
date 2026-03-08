/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match.radio;

import com.boehmod.blockfront.common.entity.AirstrikeRocketEntity;
import com.boehmod.blockfront.common.match.radio.PositionedRadioCommand;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class AirstrikeRadioCommand
extends PositionedRadioCommand {
    private static final int field_3433 = 3;
    private static final int field_3434 = 15;
    private static final int field_3435 = 10;
    private int field_3436 = 0;

    public AirstrikeRadioCommand(@NotNull ServerPlayer serverPlayer, @NotNull Vec3 vec3, int n, int n2) {
        super(serverPlayer, vec3, n, n2);
    }

    void method_3446(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    void method_3447(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        if (this.field_3436-- > 0) {
            return;
        }
        this.field_3436 = serverLevel.random.nextInt(3, 15);
        Vec3 vec3 = MathUtils.stepUpAndDrift(serverLevel.random, this.field_3343, 10);
        AirstrikeRocketEntity airstrikeRocketEntity = (AirstrikeRocketEntity)((EntityType)BFEntityTypes.AIRSTRIKE_ROCKET.get()).create((Level)serverLevel);
        if (airstrikeRocketEntity != null && this.field_3429 != null) {
            airstrikeRocketEntity.method_1934((Player)this.field_3429, 0.0f, new ItemStack((ItemLike)BFItems.RADIO.get()), 0.0f, 0.0f);
            airstrikeRocketEntity.setPos(vec3.x, serverLevel.getHeight(), vec3.z);
            serverLevel.addFreshEntity((Entity)airstrikeRocketEntity);
            Vec3 vec32 = this.field_3343.add(64.0, 0.0, 64.0);
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_AIRSTRIKE_FIRE.get(), SoundSource.AMBIENT, 25.0f, vec32);
        }
    }

    void method_3445(@NotNull Level level, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public Component getMessage() {
        return Component.translatable((String)"bf.message.gamemode.radio.command.airstrike");
    }
}

