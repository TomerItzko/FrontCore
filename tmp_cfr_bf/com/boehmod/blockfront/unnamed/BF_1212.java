/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.util.math.MathUtils;
import javax.annotation.Nonnull;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class BF_1212
extends AbstractTickableSoundInstance {
    public static final int field_7027 = 6;
    private boolean field_7025 = false;
    private final float field_7026;

    public BF_1212(@Nonnull SoundEvent soundEvent, @Nonnull SoundSource soundSource, @Nonnull SoundInstance.Attenuation attenuation, float f, float f2, RandomSource randomSource, double d, double d2, double d3) {
        super(soundEvent, soundSource, randomSource);
        this.volume = f;
        this.pitch = f2;
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.looping = false;
        this.delay = 0;
        this.attenuation = attenuation;
        this.relative = false;
        this.field_7026 = f / 6.0f;
    }

    public void method_5954() {
        this.field_7025 = true;
    }

    public void tick() {
        if (this.field_7025) {
            this.volume = MathUtils.moveTowards(this.volume, 0.0f, this.field_7026);
        }
        if (this.volume <= 0.0f) {
            this.stop();
        }
    }
}

