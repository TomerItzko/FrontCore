/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.gun.GunReloadConfig;
import com.boehmod.blockfront.common.item.GunItem;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GunDynamicReloadConfig
extends GunReloadConfig {
    @NotNull
    private static final String field_6946 = "reload_start";
    @NotNull
    private static final String field_6943 = "reload";
    @NotNull
    private static final String field_6944 = "reload_stop";
    @NotNull
    private static final String field_6945 = "idle";
    private final float field_3869;
    private final float field_3870;
    private final float field_3871;

    public GunDynamicReloadConfig(float f, float f2, float f3) {
        this.field_3869 = f;
        this.field_3870 = f2;
        this.field_3871 = f3;
    }

    private int getNumReloadAnims(@NotNull GunItem item, @NotNull ItemStack itemStack) {
        GunMagType gunMagType = item.getMagTypeOrDefault(itemStack);
        int n = gunMagType.capacity();
        int n2 = n - GunItem.getAmmoLoaded(itemStack);
        int n3 = GunItem.getAmmo(itemStack);
        if (n3 < n2) {
            n2 = n3;
        }
        return n2;
    }

    @Override
    public void addReloadAnimations(@NotNull GunItem item, @NotNull ItemStack itemStack, @NotNull List<String> animations) {
        int n = this.getNumReloadAnims(item, itemStack);
        animations.add(field_6946);
        for (int i = 0; i < n; ++i) {
            animations.add(field_6943);
        }
        animations.add(field_6944);
        animations.add(field_6945);
    }

    @Override
    public float getReloadTime(@NotNull GunItem item, @NotNull ItemStack itemStack) {
        int n = this.getNumReloadAnims(item, itemStack);
        return (this.field_3869 + this.field_3871 + this.field_3870 * (float)n) * 20.0f;
    }

    @Override
    public boolean hasEmptyDuration() {
        return false;
    }
}

