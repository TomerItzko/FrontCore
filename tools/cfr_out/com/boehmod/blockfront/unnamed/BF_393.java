/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.unnamed.BF_398;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class BF_393
extends BF_398 {
    @NotNull
    private final GunItem field_1813;

    public BF_393(@NotNull SoundEvent soundEvent, @NotNull GunItem gunItem) {
        super(soundEvent);
        this.field_1813 = gunItem;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public GunItem method_1403() {
        return this.field_1813;
    }
}

