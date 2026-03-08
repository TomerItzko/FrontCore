/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public enum GunFireMode {
    AUTO("AUTO"),
    SEMI("SEMI");

    @NotNull
    private final String name;
    @NotNull
    private final Component displayName;

    private GunFireMode(String name) {
        this.name = name;
        this.displayName = Component.literal((String)name);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public Component getDisplayName() {
        return this.displayName;
    }
}

