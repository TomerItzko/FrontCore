/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import org.jetbrains.annotations.NotNull;

public class BF_441 {
    private int field_2029;
    @NotNull
    private final SoundInstance field_2028;

    public BF_441(int n, @NotNull SoundInstance soundInstance) {
        this.field_2029 = n;
        this.field_2028 = soundInstance;
    }

    public boolean method_1544(@NotNull SoundManager soundManager) {
        if (this.field_2029-- <= 0) {
            soundManager.stop(this.field_2028);
            return true;
        }
        return false;
    }
}

