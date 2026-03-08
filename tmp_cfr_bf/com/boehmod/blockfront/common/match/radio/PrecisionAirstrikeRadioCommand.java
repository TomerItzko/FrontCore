/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match.radio;

import com.boehmod.blockfront.common.entity.PrecisionAirstrikeRocketEntity;
import com.boehmod.blockfront.common.match.radio.PositionedRadioCommand;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
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
import org.jetbrains.annotations.Nullable;

public final class PrecisionAirstrikeRadioCommand
extends PositionedRadioCommand {
    public PrecisionAirstrikeRadioCommand(@NotNull ServerPlayer serverPlayer, @NotNull Vec3 vec3, int n, int n2) {
        super(serverPlayer, vec3, n, n2);
    }

    void method_3446(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        if (this.field_3429 == null) {
            return;
        }
        PrecisionAirstrikeRocketEntity precisionAirstrikeRocketEntity = (PrecisionAirstrikeRocketEntity)((EntityType)BFEntityTypes.PRECISION_AIRSTRIKE_ROCKET.get()).create((Level)serverLevel);
        if (precisionAirstrikeRocketEntity == null) {
            return;
        }
        precisionAirstrikeRocketEntity.method_1934((Player)this.field_3429, 0.0f, new ItemStack((ItemLike)BFItems.RADIO.get()), 0.0f, 0.0f);
        precisionAirstrikeRocketEntity.setPos(this.field_3343.x, serverLevel.getHeight(), this.field_3343.z);
        serverLevel.addFreshEntity((Entity)precisionAirstrikeRocketEntity);
        Vec3 vec3 = this.field_3343.add(255.0, 0.0, 255.0);
        BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_AIRSTRIKE_FIRE.get(), SoundSource.AMBIENT, 25.0f, vec3);
    }

    void method_3447(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    void method_3445(@NotNull Level level, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    @Nullable
    public Component getMessage() {
        return Component.translatable((String)"bf.message.gamemode.radio.command.airstrike.precision");
    }
}

