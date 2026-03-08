/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.sound.entity;

import com.boehmod.blockfront.client.sound.entity.VehicleSoundInstance;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_631;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class ArtilleryVehicleSoundInstance
extends VehicleSoundInstance<ArtilleryVehicleEntity> {
    private final BF_631 field_1831;

    public ArtilleryVehicleSoundInstance(ArtilleryVehicleEntity artilleryVehicleEntity, BF_631 bF_631, SoundEvent soundEvent, float f, float f2, Vec3 vec3) {
        super(artilleryVehicleEntity, false, false, false, true, soundEvent, vec3, f, f2);
        this.field_1831 = bF_631;
    }

    @Override
    public void tick() {
        Vec3 vec3 = ((ArtilleryVehicleEntity)this.field_1833).position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
        ArtilleryVehicleEntity.BF_620 bF_620 = ((ArtilleryVehicleEntity)this.field_1833).method_2358(this.field_1831);
        float f = bF_620.method_2362(((ArtilleryVehicleEntity)this.field_1833).getEntityData()) ? 1.0f : 0.0f;
        this.volume = Mth.lerp((float)0.3f, (float)this.volume, (float)f);
    }
}

