/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.event;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameStatus;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class GameEvent {
    @Nullable
    protected final ServerPlayer field_3429;
    protected int field_3431;
    protected int field_3432;
    private boolean field_3430 = false;

    public GameEvent(@Nullable ServerPlayer serverPlayer, int n, int n2) {
        this.field_3429 = serverPlayer;
        this.field_3431 = n;
        this.field_3432 = n2;
    }

    public boolean method_3444(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        if (abstractGame.getStatus() != GameStatus.GAME) {
            return false;
        }
        if (this.field_3431 > 0) {
            --this.field_3431;
        } else {
            if (!this.field_3430) {
                this.field_3430 = true;
                this.method_3446(serverLevel, abstractGame, set);
            }
            if (this.field_3432-- > 0) {
                this.method_3447(serverLevel, abstractGame, set);
            } else {
                this.method_3445((Level)serverLevel, abstractGame, set);
                return false;
            }
        }
        return true;
    }

    abstract void method_3446(@NotNull ServerLevel var1, @NotNull AbstractGame<?, ?, ?> var2, @NotNull Set<UUID> var3);

    abstract void method_3447(@NotNull ServerLevel var1, @NotNull AbstractGame<?, ?, ?> var2, @NotNull Set<UUID> var3);

    abstract void method_3445(@NotNull Level var1, @NotNull AbstractGame<?, ?, ?> var2, @NotNull Set<UUID> var3);

    @Nullable
    public abstract Component getMessage();
}

