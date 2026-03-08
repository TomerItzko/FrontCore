/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.sound.entity;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntitySoundInstance<E extends Entity>
extends AbstractTickableSoundInstance {
    @NotNull
    final E field_1832;

    public EntitySoundInstance(@NotNull E e, @NotNull SoundEvent soundEvent, @NotNull SoundSource soundSource, float f, float f2) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.field_1832 = e;
        this.looping = false;
        this.pitch = f;
        this.volume = f2;
        Vec3 vec3 = e.position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }

    protected boolean method_1411() {
        LivingEntity livingEntity;
        E e;
        return this.field_1832.isRemoved() || !this.field_1832.isAlive() || (e = this.field_1832) instanceof LivingEntity && (livingEntity = (LivingEntity)e).getHealth() <= 0.0f;
    }

    public boolean canPlaySound() {
        return !this.field_1832.isSilent();
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        if (this.method_1411()) {
            this.stop();
        } else {
            Vec3 vec3 = this.field_1832.position();
            this.x = vec3.x;
            this.y = vec3.y;
            this.z = vec3.z;
            this.volume = 1.0f;
        }
    }
}

