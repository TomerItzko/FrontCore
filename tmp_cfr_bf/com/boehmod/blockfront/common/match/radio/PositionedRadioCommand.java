/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match.radio;

import com.boehmod.blockfront.game.event.GameEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PositionedRadioCommand
extends GameEvent {
    @NotNull
    protected final Vec3 field_3343;

    public PositionedRadioCommand(@Nullable ServerPlayer serverPlayer, @NotNull Vec3 vec3, int n, int n2) {
        super(serverPlayer, n, n2);
        this.field_3343 = vec3;
    }
}

