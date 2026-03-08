/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface IAllowsSoundboard {
    public int getMaximumPlayerSounds(@NotNull ServerPlayer var1);

    public int getSoundboardCooldown();
}

