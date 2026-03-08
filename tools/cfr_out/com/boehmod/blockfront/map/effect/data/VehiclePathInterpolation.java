/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect.data;

import com.boehmod.blockfront.map.effect.data.AmbientVehiclePath;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public enum VehiclePathInterpolation {
    LINEAR(VehiclePathInterpolation::linearInterpolation);

    @NotNull
    private final Interpolator interpolator;

    private VehiclePathInterpolation(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    @NotNull
    public static Vec3 linearInterpolation(@NotNull AmbientVehiclePath path, float t) {
        return MathUtils.moveTowards(path.field_3421, path.method_3376(), t);
    }

    @NotNull
    public Vec3 interpolate(@NotNull AmbientVehiclePath path, float t) {
        return this.interpolator.interpolate(path, t);
    }

    @FunctionalInterface
    public static interface Interpolator {
        @NotNull
        public Vec3 interpolate(@NotNull AmbientVehiclePath var1, float var2);
    }
}

