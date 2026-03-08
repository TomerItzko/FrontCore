/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.item.BFConsumableItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface IHasConsumables {
    public boolean canUseConsumable(@NotNull Level var1, @NotNull Player var2, @NotNull BFConsumableItem var3, @NotNull ItemStack var4);

    public void method_3427(@NotNull ServerLevel var1, @NotNull Player var2, @NotNull BFConsumableItem var3, @NotNull ItemStack var4);
}

