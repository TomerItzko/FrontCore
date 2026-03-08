/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.common.gun.GunReloadConfig;
import com.boehmod.blockfront.common.item.GunItem;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GunStaticReloadConfig
extends GunReloadConfig {
    private final float seconds;
    private final float secondsEmpty;
    private final boolean hasEmptyDuration;

    public GunStaticReloadConfig(float seconds) {
        this(seconds, -1.0f, false);
    }

    public GunStaticReloadConfig(float seconds, float secondsEmpty) {
        this(seconds, secondsEmpty, true);
    }

    private GunStaticReloadConfig(float seconds, float secondsEmpty, boolean hasEmptyDuration) {
        this.seconds = seconds;
        this.secondsEmpty = secondsEmpty;
        this.hasEmptyDuration = hasEmptyDuration;
    }

    @Override
    public void addReloadAnimations(@NotNull GunItem item, @NotNull ItemStack itemStack, @NotNull List<String> animations) {
        boolean bl = this.hasEmptyDuration && GunItem.getAmmoLoaded(itemStack) <= 0;
        animations.add(bl ? "reload_empty" : "reload");
        animations.add("idle");
    }

    @Override
    public float getReloadTime(@NotNull GunItem item, @NotNull ItemStack itemStack) {
        boolean bl = this.hasEmptyDuration && GunItem.getAmmoLoaded(itemStack) <= 0;
        return (bl ? this.secondsEmpty : this.seconds) * 20.0f;
    }

    @Override
    public boolean hasEmptyDuration() {
        return this.hasEmptyDuration;
    }
}

