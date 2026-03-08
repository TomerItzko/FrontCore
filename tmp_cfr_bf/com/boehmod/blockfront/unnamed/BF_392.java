/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class BF_392
extends AbstractTickableSoundInstance {
    private boolean field_1812 = false;

    public BF_392(@NotNull SoundEvent soundEvent, boolean bl, boolean bl2) {
        super(soundEvent, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.looping = bl;
        this.volume = bl2 ? 1.0f : 0.0f;
    }

    public void method_1401(boolean bl) {
        this.field_1812 = bl;
    }

    public boolean method_1402() {
        return this.field_1812;
    }

    public boolean isStopped() {
        return this.field_1812 && this.volume <= 0.0f;
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        this.volume = MathUtils.moveTowards(this.volume, this.field_1812 ? 0.0f : 1.0f, 0.015f);
    }
}

