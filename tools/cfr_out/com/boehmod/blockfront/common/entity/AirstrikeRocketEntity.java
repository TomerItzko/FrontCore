/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AirstrikeRocketEntity
extends RocketEntity {
    public AirstrikeRocketEntity(EntityType<? extends AirstrikeRocketEntity> entityType, Level level) {
        super((EntityType<? extends RocketEntity>)entityType, level);
        this.method_2504((SoundEvent)BFSounds.ENTITY_ROCKET_FALL.get());
    }

    public int method_2498() {
        return 5;
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(0.0, -this.method_2498(), 0.0);
    }

    @Override
    protected float method_1942() {
        return 4.5f;
    }

    @Override
    public int method_2508() {
        return 300;
    }

    @Override
    public void method_1957() {
        super.method_1957();
        Level level = this.level();
        level.addParticle((ParticleOptions)ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        level.playSound(null, this.blockPosition(), (SoundEvent)BFSounds.AMBIENT_WAR_ARTILLERY_CLOSE.get(), SoundSource.AMBIENT, 10.0f, 1.0f);
        level.setSkyFlashTime(1);
    }

    @Override
    protected boolean method_2506() {
        return false;
    }
}

