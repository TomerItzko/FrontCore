/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.sound.entity;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class VehicleSoundInstance<V extends AbstractVehicleEntity>
extends SimpleSoundInstance
implements TickableSoundInstance {
    public static final float field_1837 = 2.5f;
    public static final float field_1838 = 6.0f;
    @NotNull
    final V field_1833;
    private final boolean field_1834;
    private final boolean field_1835;
    private final boolean field_1836;
    private float field_1839 = 0.0f;

    public VehicleSoundInstance(@NotNull V v, boolean bl, boolean bl2, boolean bl3, boolean bl4, @NotNull SoundEvent soundEvent, @NotNull Vec3 vec3, float f, float f2) {
        super(soundEvent, SoundSource.AMBIENT, f, f2, SoundInstance.createUnseededRandom(), vec3.x, vec3.y, vec3.z);
        this.field_1834 = bl;
        this.field_1835 = bl2;
        this.field_1836 = bl3;
        this.field_1833 = v;
        this.looping = bl4;
        this.volume = 0.0f;
    }

    public void method_1412(float f) {
        this.pitch = f;
    }

    public void method_1413(float f) {
        this.volume = f;
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean isStopped() {
        return !this.field_1833.isAlive() || this.field_1833.isRemoved();
    }

    public void tick() {
        boolean bl = ((AbstractVehicleEntity)this.field_1833).method_2332();
        boolean bl2 = ((AbstractVehicleEntity)this.field_1833).method_2333() || ((AbstractVehicleEntity)this.field_1833).method_2334();
        boolean bl3 = ((AbstractVehicleEntity)this.field_1833).method_2339();
        boolean bl4 = ((AbstractVehicleEntity)this.field_1833).method_2335() || ((AbstractVehicleEntity)this.field_1833).method_2337();
        boolean bl5 = ((AbstractVehicleEntity)this.field_1833).method_2330();
        boolean bl6 = false;
        Vec3 vec3 = this.field_1833.position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
        this.field_1839 = Mth.clamp((float)(this.field_1839 + (this.field_1836 || bl ? 0.1f : -0.025f)), (float)0.0f, (float)1.0f);
        if (this.field_1835) {
            if (bl4) {
                bl6 = true;
            }
        } else if (this.field_1836) {
            if (bl5) {
                bl6 = true;
            }
        } else if (this.field_1834 && bl2) {
            bl6 = true;
        } else if (!this.field_1834 && !bl2) {
            bl6 = true;
        }
        float f = bl3 && !bl4 ? 1.1f : 1.0f;
        this.pitch = MathUtils.moveTowards(this.pitch, f, 0.01f);
        float f2 = bl6 ? 0.05f : -0.05f;
        this.volume = Mth.clamp((float)(this.volume + f2), (float)0.0f, (float)1.0f) * this.field_1839;
    }
}

