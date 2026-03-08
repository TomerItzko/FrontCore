/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.tag;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface IAllowsSoundboard {
    public int getMaximumPlayerSounds(@NotNull ServerPlayer var1);

    public int getSoundboardCooldown();
}

