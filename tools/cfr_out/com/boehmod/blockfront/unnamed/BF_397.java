/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.map.effect.data.AmbientVehiclePath;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_397
extends SimpleSoundInstance
implements TickableSoundInstance {
    @NotNull
    final AmbientVehiclePath field_1816;
    private final boolean field_1817;
    private float field_1819 = 0.0f;

    public BF_397(@NotNull AmbientVehiclePath ambientVehiclePath, @NotNull SoundEvent soundEvent, @NotNull Vec3 vec3, boolean bl, float f, float f2) {
        super(soundEvent, SoundSource.AMBIENT, f, f2, SoundInstance.createUnseededRandom(), vec3.x, vec3.y, vec3.z);
        this.field_1816 = ambientVehiclePath;
        this.field_1817 = bl;
        this.looping = true;
    }

    public void method_1404(float f) {
        this.pitch = f;
    }

    public void method_1405(float f) {
        this.volume = f;
    }

    public boolean isStopped() {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null);
        return bFClientManager.getGame() == null;
    }

    public void tick() {
        float f;
        boolean bl = !this.field_1816.method_3371();
        this.field_1819 = Mth.clamp((float)(this.field_1819 + 0.1f), (float)0.0f, (float)1.0f);
        this.x = this.field_1816.field_3421.x;
        this.y = this.field_1816.field_3421.y;
        this.z = this.field_1816.field_3421.z;
        float f2 = 1.0f;
        this.pitch = MathUtils.moveTowards(this.pitch, 1.0f, 0.01f);
        float f3 = f = bl ? 0.05f : -0.05f;
        if (this.field_1817) {
            f = -f;
        }
        this.volume = Mth.clamp((float)(this.volume + f), (float)0.0f, (float)1.0f) * this.field_1819;
    }
}

