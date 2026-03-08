/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.event;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.event.GameEvent;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AvalancheRocksGameEvent
extends GameEvent {
    private int field_3437 = 0;

    public AvalancheRocksGameEvent(int n, int n2) {
        super(null, n, n2);
    }

    @Override
    void method_3446(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    void method_3447(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        ++this.field_3437;
        if (this.field_3437 % 5 == 0) {
            BFUtils.playSound(set, (SoundEvent)BFSounds.MATCH_EVENT_AVALANCHE_RUMBLE.get(), SoundSource.AMBIENT);
            if (serverLevel.random.nextFloat() < 0.3f) {
                BFUtils.playSound(set, (SoundEvent)BFSounds.MATCH_EVENT_AVALANCHE_ROCKS.get(), SoundSource.AMBIENT, 1.0f, 0.5f);
            }
        }
    }

    @Override
    void method_3445(@NotNull Level level, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    @Nullable
    public Component getMessage() {
        return null;
    }
}

