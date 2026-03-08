/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.common.gun.GunSoundConfig;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class GunBarrelType {
    @NotNull
    public static final String DEFAULT_ID = "default";
    @NotNull
    public static final GunBarrelType DEFAULT = new GunBarrelType((Component)Component.translatable((String)"bf.item.gun.barrel.type.default"));
    @Nullable
    private GunSoundConfig soundConfig = null;
    @NotNull
    private final Component displayName;
    private boolean field_3985 = true;

    public GunBarrelType(@NotNull Component displayName) {
        this.displayName = displayName;
    }

    @NotNull
    public Component getDisplayName() {
        return this.displayName;
    }

    @NotNull
    public GunBarrelType method_4099(boolean bl) {
        this.field_3985 = bl;
        return this;
    }

    public boolean method_4100() {
        return this.field_3985;
    }

    @NotNull
    public GunBarrelType setSoundConfig(@Nullable GunSoundConfig config) {
        this.soundConfig = config;
        return this;
    }

    @Nullable
    public GunSoundConfig getSoundConfig() {
        return this.soundConfig;
    }
}

