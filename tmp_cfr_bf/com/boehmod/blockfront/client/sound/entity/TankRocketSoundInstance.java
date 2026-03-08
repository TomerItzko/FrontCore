/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.sound.entity;

import com.boehmod.blockfront.client.sound.entity.EntitySoundInstance;
import com.boehmod.blockfront.common.entity.TankRocketEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class TankRocketSoundInstance
extends EntitySoundInstance<TankRocketEntity> {
    private static final int field_1830 = 160;
    private int field_1829 = 0;

    public TankRocketSoundInstance(@NotNull TankRocketEntity tankRocketEntity, @NotNull SoundEvent soundEvent, @NotNull SoundSource soundSource, float f, float f2) {
        super(tankRocketEntity, soundEvent, soundSource, f, f2);
    }

    @Override
    protected boolean method_1411() {
        return this.field_1829++ >= 160 || ((TankRocketEntity)this.field_1832).isRemoved();
    }
}

