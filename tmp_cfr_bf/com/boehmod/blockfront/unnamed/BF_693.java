/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.sound.entity.VehicleSoundInstance;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_404;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_694;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BF_693
extends BF_694<ArtilleryVehicleEntity> {
    public DeferredHolder<SoundEvent, SoundEvent> field_2962 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_MOVE;
    public DeferredHolder<SoundEvent, SoundEvent> field_2963 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_MOVE_START;
    public DeferredHolder<SoundEvent, SoundEvent> field_2964 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_MOVE_STOP;
    public DeferredHolder<SoundEvent, SoundEvent> field_2965 = BFSounds.ENTITY_VEHICLE_TURRET_FIRE_INTERNAL;
    public DeferredHolder<SoundEvent, SoundEvent> field_2966 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE;
    public DeferredHolder<SoundEvent, SoundEvent> field_2967 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_ADD;
    public DeferredHolder<SoundEvent, SoundEvent> field_2968 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_DISTANT;
    public DeferredHolder<SoundEvent, SoundEvent> field_2969 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_HIFI;
    public DeferredHolder<SoundEvent, SoundEvent> field_2970 = BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_RELOAD;
    public DeferredHolder<SoundEvent, SoundEvent> field_2971 = BFSounds.ENTITY_VEHICLE_ZOOM_IN;
    public DeferredHolder<SoundEvent, SoundEvent> field_2972 = BFSounds.ENTITY_VEHICLE_ZOOM_OUT;
    public DeferredHolder<SoundEvent, SoundEvent> field_2973 = BFSounds.ENTITY_VEHICLE_SHERMAN_TRACKS;
    public DeferredHolder<SoundEvent, SoundEvent> field_2974 = BFSounds.ENTITY_VEHICLE_SHERMAN_TRACKS_TURN;
    public DeferredHolder<SoundEvent, SoundEvent> field_2976 = BFSounds.ENTITY_VEHICLE_TILT;
    @OnlyIn(value=Dist.CLIENT)
    @Nullable
    public VehicleSoundInstance<ArtilleryVehicleEntity> field_2977;
    @OnlyIn(value=Dist.CLIENT)
    @Nullable
    public VehicleSoundInstance<ArtilleryVehicleEntity> field_2978;
    @OnlyIn(value=Dist.CLIENT)
    @Nullable
    public BF_404<ArtilleryVehicleEntity> field_2975;

    public BF_693(ArtilleryVehicleEntity artilleryVehicleEntity) {
        super(artilleryVehicleEntity);
    }

    public BF_693 method_3035(DeferredHolder<SoundEvent, SoundEvent> deferredHolder, DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, DeferredHolder<SoundEvent, SoundEvent> deferredHolder3, DeferredHolder<SoundEvent, SoundEvent> deferredHolder4) {
        this.field_2966 = deferredHolder;
        this.field_2967 = deferredHolder2;
        this.field_2968 = deferredHolder3;
        this.field_2969 = deferredHolder4;
        return this;
    }

    public BF_693 method_3033(DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.field_2970 = deferredHolder;
        return this;
    }

    public BF_693 method_3034(DeferredHolder<SoundEvent, SoundEvent> deferredHolder, DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this.field_2973 = deferredHolder;
        this.field_2974 = deferredHolder2;
        return this;
    }

    public BF_693 method_3036(DeferredHolder<SoundEvent, SoundEvent> deferredHolder, DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, DeferredHolder<SoundEvent, SoundEvent> deferredHolder3, DeferredHolder<SoundEvent, SoundEvent> deferredHolder4) {
        this.field_2962 = deferredHolder;
        this.field_2963 = deferredHolder2;
        this.field_2964 = deferredHolder3;
        this.field_2970 = deferredHolder4;
        return this;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void method_3044(@NotNull Minecraft minecraft) {
        super.method_3044(minecraft);
        BF_624<AbstractVehicleEntity> bF_624 = ((ArtilleryVehicleEntity)this.field_3000).method_2343();
        Vec3 vec3 = ((ArtilleryVehicleEntity)this.field_3000).position();
        SoundManager soundManager = minecraft.getSoundManager();
        for (BF_633 bF_633 : bF_624.field_2679) {
            bF_633.method_2470(minecraft);
        }
        if (!(this.field_2973 == null || this.field_2977 != null && soundManager.isActive(this.field_2977))) {
            this.field_2977 = new VehicleSoundInstance<ArtilleryVehicleEntity>((ArtilleryVehicleEntity)this.field_3000, true, false, false, true, (SoundEvent)this.field_2973.get(), vec3, 2.5f, 1.0f);
            soundManager.play(this.field_2977);
        }
        if (!(this.field_2974 == null || this.field_2978 != null && soundManager.isActive(this.field_2978))) {
            this.field_2978 = new VehicleSoundInstance<ArtilleryVehicleEntity>((ArtilleryVehicleEntity)this.field_3000, false, true, false, true, (SoundEvent)this.field_2974.get(), vec3, 2.5f, 1.0f);
            soundManager.play(this.field_2978);
        }
        if (!(this.field_2976 == null || this.field_2975 != null && soundManager.isActive(this.field_2975))) {
            this.field_2975 = new BF_404<ArtilleryVehicleEntity>((ArtilleryVehicleEntity)this.field_3000, (SoundEvent)this.field_2976.get(), vec3, 2.5f, 1.0f);
            soundManager.play(this.field_2975);
        }
    }
}

