/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.floats.Float2ObjectMap
 *  it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair
 *  it.unimi.dsi.fastutil.floats.FloatFloatPair
 *  it.unimi.dsi.fastutil.floats.FloatIterator
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.gun;

import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class GunDamageConfig {
    @NotNull
    private final Float2ObjectMap<FloatFloatPair> entries = new Float2ObjectOpenHashMap();

    public GunDamageConfig(float body, float head) {
        this.entries.put(0.0f, (Object)FloatFloatImmutablePair.of((float)body, (float)head));
    }

    @NotNull
    public GunDamageConfig add(float distance, float body, float head) {
        this.entries.put(distance, (Object)FloatFloatImmutablePair.of((float)body, (float)head));
        return this;
    }

    @NotNull
    public FloatFloatPair method_3950(float f) {
        float f2 = 0.0f;
        FloatIterator floatIterator = this.entries.keySet().iterator();
        while (floatIterator.hasNext()) {
            float f3 = ((Float)floatIterator.next()).floatValue();
            if (!(f3 <= f) || !(f3 > f2)) continue;
            f2 = f3;
        }
        return (FloatFloatPair)this.entries.get(f2);
    }

    @NotNull
    public Map<Float, FloatFloatPair> getEntries() {
        return Collections.unmodifiableMap(this.entries);
    }
}

