/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface IProducedProjectileEntity {
    public void method_1934(@NotNull Player var1, float var2, @NotNull ItemStack var3, float var4, float var5);

    public void method_1935(@NotNull Vec3 var1, float var2, @NotNull ItemStack var3, float var4, float var5);
}

