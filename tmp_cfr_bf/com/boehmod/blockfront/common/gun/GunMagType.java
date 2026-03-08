/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public record GunMagType(boolean isDefault, Component displayName, int capacity, int maxAmmo) {
    @NotNull
    public static final String DEFAULT_ID = "default";
    @NotNull
    public static final Component DEFAULT_NAME = Component.translatable((String)"bf.item.gun.mag.type.default");
    @NotNull
    public static final GunMagType DEFAULT = new GunMagType(true, DEFAULT_NAME, 1, 1);

    public GunMagType(@NotNull Component component, int n, int n2) {
        this(false, component, n, n2);
    }
}

