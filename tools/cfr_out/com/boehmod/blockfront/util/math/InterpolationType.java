/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.blockfront.util.math.IInterpolator;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public enum InterpolationType {
    LINEAR(t -> t),
    EASE_IN(t -> t * t),
    EASE_OUT(t -> 1.0f - (1.0f - t) * (1.0f - t)),
    EASE_IN_OUT(t -> (double)t < 0.5 ? 2.0f * t * t : (float)(1.0 - Math.pow(-2.0f * t + 2.0f, 2.0) / 2.0));

    @NotNull
    private final IInterpolator interpolator;

    private InterpolationType(IInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public float apply(float t) {
        return this.interpolator.interpolate(Mth.clamp((float)t, (float)0.0f, (float)1.0f));
    }
}

