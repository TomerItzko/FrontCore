/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IHasWeight {
    public boolean method_3728(@NotNull Player var1, @NotNull ItemStack var2);

    public float getWeight(@NotNull ItemStack var1);
}

