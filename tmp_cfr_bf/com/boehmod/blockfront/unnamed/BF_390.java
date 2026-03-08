/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_390
extends AbstractTickableSoundInstance {
    @NotNull
    private final BulletTracer field_1811;

    public BF_390(@NotNull BulletTracer bulletTracer, @NotNull SoundEvent soundEvent, @NotNull SoundSource soundSource, float f, float f2) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.field_1811 = bulletTracer;
        this.looping = false;
        this.pitch = f;
        this.volume = f2;
        Vec3 vec3 = bulletTracer.getStartPos();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }

    public boolean canPlaySound() {
        return !this.field_1811.method_1178();
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        if (this.field_1811.method_1178()) {
            this.stop();
        } else {
            Vec3 vec3 = this.field_1811.getStartPos();
            this.x = vec3.x;
            this.y = vec3.y;
            this.z = vec3.z;
            this.volume = 1.0f;
        }
    }
}

