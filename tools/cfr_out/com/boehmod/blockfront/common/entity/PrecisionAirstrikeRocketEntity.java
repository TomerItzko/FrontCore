/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.AirstrikeRocketEntity;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public final class PrecisionAirstrikeRocketEntity
extends AirstrikeRocketEntity {
    private static final float field_2762 = 4.0f;

    public PrecisionAirstrikeRocketEntity(EntityType<? extends PrecisionAirstrikeRocketEntity> entityType, Level level) {
        super((EntityType<? extends AirstrikeRocketEntity>)entityType, level);
        this.method_2504((SoundEvent)BFSounds.ENTITY_ROCKET_FALL.get());
    }

    @Override
    public float method_1940() {
        return 4.0f;
    }

    @Override
    public int method_2498() {
        return 10;
    }

    @Override
    protected float method_1942() {
        return 8.5f;
    }
}

