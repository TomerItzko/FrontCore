/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class RandomatEvent {
    public abstract void update(@NotNull TroubleTownGame var1, @NotNull PlayerDataHandler<?> var2, @NotNull Level var3, @NotNull Set<UUID> var4);

    public abstract void activate(@NotNull TroubleTownGame var1, @NotNull Player var2, @NotNull Level var3, @NotNull Set<UUID> var4);

    public abstract void finish(@NotNull TroubleTownGame var1, @NotNull Level var2, @NotNull Set<UUID> var3);

    @NotNull
    public abstract String getName();
}

