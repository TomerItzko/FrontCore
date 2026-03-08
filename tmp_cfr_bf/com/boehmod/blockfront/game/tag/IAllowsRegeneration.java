/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.tag;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public interface IAllowsRegeneration {
    public boolean playerCanRegenerate(@NotNull Player var1);

    public float method_3418(@NotNull Player var1);

    public int method_3419(@NotNull Player var1);
}

