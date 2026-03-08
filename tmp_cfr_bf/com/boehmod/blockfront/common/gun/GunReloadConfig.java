/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.common.item.GunItem;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GunReloadConfig {
    public abstract void addReloadAnimations(@NotNull GunItem var1, @NotNull ItemStack var2, @NotNull List<String> var3);

    public abstract float getReloadTime(@NotNull GunItem var1, @NotNull ItemStack var2);

    public abstract boolean hasEmptyDuration();
}

