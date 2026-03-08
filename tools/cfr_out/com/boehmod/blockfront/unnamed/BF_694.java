/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.EntityBoundSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.sound.entity.VehicleSoundInstance;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.registry.BFSounds;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BF_694<V extends AbstractVehicleEntity> {
    final V field_3000;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2979 = BFSounds.ENTITY_VEHICLE_ALARM;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2980 = BFSounds.ENTITY_VEHICLE_ALARM_INTERNAL;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2981 = BFSounds.ENTITY_VEHICLE_HATCH;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2982 = BFSounds.ENTITY_VEHICLE_DAMAGE;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2983 = BFSounds.ENTITY_VEHICLE_DAMAGE_DISTANT;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2984 = BFSounds.ENTITY_VEHICLE_DAMAGE_INTERNAL;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2985 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_START;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2986 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_STOP;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2987 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_IDLE;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2988 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_FORWARD;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2989 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_FORWARD_START;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2990 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_FORWARD_STOP;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2991 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_START_DISTANT;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2992 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_STOP_DISTANT;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2993 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_IDLE_DISTANT;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2994 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_FORWARD_DISTANT;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2995 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_FORWARD_START_DISTANT;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2996 = BFSounds.ENTITY_VEHICLE_SHERMAN_ENGINE_FORWARD_STOP_DISTANT;
    public DeferredHolder<SoundEvent, SoundEvent> field_2997 = BFSounds.ENTITY_VEHICLE_DESTROYED_FIRE;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2998 = null;
    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> field_2999 = null;
    @Nullable
    @OnlyIn(value=Dist.CLIENT)
    public VehicleSoundInstance<V> field_3001;
    @Nullable
    @OnlyIn(value=Dist.CLIENT)
    public VehicleSoundInstance<V> field_3002;
    @Nullable
    @OnlyIn(value=Dist.CLIENT)
    public VehicleSoundInstance<V> field_3007;
    @Nullable
    @OnlyIn(value=Dist.CLIENT)
    public VehicleSoundInstance<V> field_3008;
    @Nullable
    @OnlyIn(value=Dist.CLIENT)
    public VehicleSoundInstance<V> field_3009;
    private boolean field_3003 = false;
    private boolean field_3004 = true;
    private boolean field_3005 = false;
    private boolean field_3006 = true;

    public BF_694(@NotNull V v) {
        this.field_3000 = v;
    }

    public BF_694<V> method_3038(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this.field_2998 = deferredHolder;
        this.field_2999 = deferredHolder2;
        return this;
    }

    public BF_694<V> method_3041(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this.field_2979 = deferredHolder;
        this.field_2980 = deferredHolder2;
        return this;
    }

    public BF_694<V> method_3037(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.field_2981 = deferredHolder;
        return this;
    }

    public BF_694<V> method_3042(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this.field_2985 = deferredHolder;
        this.field_2986 = deferredHolder2;
        return this;
    }

    public BF_694<V> method_3040(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        this.field_2987 = deferredHolder;
        return this;
    }

    public BF_694<V> method_3043(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        this.field_2987 = deferredHolder;
        this.field_2993 = deferredHolder2;
        return this;
    }

    public BF_694<V> method_3039(@Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder2, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder3, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder4, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder5, @Nullable DeferredHolder<SoundEvent, SoundEvent> deferredHolder6) {
        this.field_2988 = deferredHolder;
        this.field_2994 = deferredHolder2;
        this.field_2990 = deferredHolder5;
        this.field_2996 = deferredHolder6;
        this.field_2989 = deferredHolder3;
        this.field_2995 = deferredHolder4;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3045(@NotNull Minecraft minecraft) {
        boolean bl;
        RandomSource randomSource = RandomSource.create();
        this.method_3044(minecraft);
        SoundManager soundManager = minecraft.getSoundManager();
        boolean bl2 = ((AbstractVehicleEntity)this.field_3000).method_2332();
        boolean bl3 = bl = ((AbstractVehicleEntity)this.field_3000).method_2333() || ((AbstractVehicleEntity)this.field_3000).method_2334();
        if (bl2) {
            this.field_3004 = false;
            if (!this.field_3003) {
                this.field_3003 = true;
                if (this.field_2985 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2985.get(), SoundSource.NEUTRAL, 2.5f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
                if (this.field_2991 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2991.get(), SoundSource.NEUTRAL, 6.0f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
            }
        } else {
            this.field_3003 = false;
            if (!this.field_3004) {
                this.field_3004 = true;
                if (this.field_2986 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2986.get(), SoundSource.NEUTRAL, 2.5f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
                if (this.field_2992 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2992.get(), SoundSource.NEUTRAL, 6.0f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
            }
        }
        if (bl) {
            this.field_3006 = false;
            if (!this.field_3005) {
                this.field_3005 = true;
                if (this.field_2989 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2989.get(), SoundSource.NEUTRAL, 2.5f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
                if (this.field_2995 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2995.get(), SoundSource.NEUTRAL, 6.0f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
            }
        } else {
            this.field_3005 = false;
            if (!this.field_3006) {
                this.field_3006 = true;
                if (this.field_2990 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2990.get(), SoundSource.NEUTRAL, 2.5f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
                if (this.field_2996 != null) {
                    soundManager.play((SoundInstance)new EntityBoundSoundInstance((SoundEvent)this.field_2996.get(), SoundSource.NEUTRAL, 6.0f, 1.0f, this.field_3000, randomSource.nextLong()));
                }
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3044(@NotNull Minecraft minecraft) {
        SoundManager soundManager = minecraft.getSoundManager();
        Vec3 vec3 = this.field_3000.position();
        if (!(this.field_2987 == null || this.field_3001 != null && soundManager.isActive(this.field_3001))) {
            this.field_3001 = new VehicleSoundInstance<V>(this.field_3000, false, false, false, true, (SoundEvent)this.field_2987.get(), vec3, 2.5f, 1.0f);
            soundManager.play(this.field_3001);
        }
        if (!(this.field_2988 == null || this.field_3002 != null && soundManager.isActive(this.field_3002))) {
            this.field_3002 = new VehicleSoundInstance<V>(this.field_3000, true, false, false, true, (SoundEvent)this.field_2988.get(), vec3, 2.5f, 1.0f);
            soundManager.play(this.field_3002);
        }
        if (!(this.field_2997 == null || this.field_3009 != null && soundManager.isActive(this.field_3009))) {
            this.field_3009 = new VehicleSoundInstance<V>(this.field_3000, false, false, true, true, (SoundEvent)this.field_2997.get(), vec3, 2.5f, 1.0f);
            soundManager.play(this.field_3009);
        }
        if (!(this.field_2993 == null || this.field_3007 != null && soundManager.isActive(this.field_3007))) {
            this.field_3007 = new VehicleSoundInstance<V>(this.field_3000, false, false, false, true, (SoundEvent)this.field_2993.get(), vec3, 6.0f, 1.0f);
            soundManager.play(this.field_3007);
        }
        if (!(this.field_2994 == null || this.field_3008 != null && soundManager.isActive(this.field_3008))) {
            this.field_3008 = new VehicleSoundInstance<V>(this.field_3000, true, false, false, true, (SoundEvent)this.field_2994.get(), vec3, 6.0f, 1.0f);
            soundManager.play(this.field_3008);
        }
    }
}

