/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class BF_629<V extends AbstractVehicleEntity> {
    public Vec3 field_2708;
    public Vec3 field_2709 = Vec3.ZERO;

    public BF_629(Vec3 vec3) {
        this.field_2708 = vec3;
    }

    public BF_629<V> method_2434(Vec3 vec3) {
        this.field_2709 = vec3;
        return this;
    }

    public abstract ParticleOptions method_2433(@NotNull Level var1, @NotNull V var2);

    public abstract boolean method_2435(V var1);

    public void method_2436(V v) {
    }

    public void method_2437(V v) {
        if (this.method_2435(v)) {
            this.method_2438(v);
        }
    }

    private void method_2438(V v) {
        Level level = v.level();
        Vec3 vec3 = v.position();
        Vec3 vec32 = this.field_2708.yRot(-v.getYRot() * ((float)Math.PI / 180) - 1.5707964f);
        level.addParticle(this.method_2433(level, v), true, vec3.x + vec32.x, vec3.y + vec32.y, vec3.z + vec32.z, this.field_2709.x, this.field_2709.y, this.field_2709.z);
    }

    @NotNull
    public abstract ResourceLocation method_2439();
}

