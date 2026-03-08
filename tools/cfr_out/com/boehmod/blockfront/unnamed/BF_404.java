/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.floats.FloatFloatPair
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.TickableSoundInstance
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_622;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_404<V extends AbstractVehicleEntity>
extends SimpleSoundInstance
implements TickableSoundInstance {
    final V field_1840;

    public BF_404(@NotNull V v, @NotNull SoundEvent soundEvent, @NotNull Vec3 vec3, float f, float f2) {
        super(soundEvent, SoundSource.AMBIENT, f, f2, SoundInstance.createUnseededRandom(), vec3.x, vec3.y, vec3.z);
        this.field_1840 = v;
        this.looping = true;
    }

    public void method_1414(float f) {
        this.pitch = f;
    }

    public void method_1415(float f) {
        this.volume = f;
    }

    public boolean isStopped() {
        return !this.field_1840.isAlive();
    }

    public void tick() {
        Vec3 vec3 = this.field_1840.position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
        BF_622 bF_622 = ((AbstractVehicleEntity)this.field_1840).field_2617;
        FloatFloatPair floatFloatPair = bF_622.method_2369();
        boolean bl = floatFloatPair.firstFloat() > 0.01f || floatFloatPair.secondFloat() > 0.01f;
        this.volume = Mth.lerp((float)0.3f, (float)this.volume, (float)(bl ? 1.0f : 0.0f));
    }
}

