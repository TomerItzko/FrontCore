/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.render.effect;

import com.boehmod.blockfront.client.render.effect.AbstractParticleEffect;
import com.boehmod.blockfront.client.render.effect.FallingAshEffect;
import com.boehmod.blockfront.client.render.effect.FallingLeavesEffect;
import com.boehmod.blockfront.client.render.effect.FastSnowParticleEffect;
import com.boehmod.blockfront.client.render.effect.InfectedNezhitEffect;
import com.boehmod.blockfront.client.render.effect.LightSnowEffect;
import com.boehmod.blockfront.client.render.effect.RainstormEffect;
import com.boehmod.blockfront.client.render.effect.SnowParticleEffect;
import com.boehmod.blockfront.client.render.effect.WeatherEffectType;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFParticleEffects {
    @NotNull
    private static final Map<WeatherEffectType, AbstractParticleEffect> BY_TYPE = new EnumMap<WeatherEffectType, AbstractParticleEffect>(WeatherEffectType.class);

    private static void add(@NotNull WeatherEffectType type, @NotNull AbstractParticleEffect renderer) {
        BY_TYPE.put(type, renderer);
    }

    @Nullable
    public static AbstractParticleEffect get(@NotNull WeatherEffectType type) {
        return BY_TYPE.get((Object)type);
    }

    static {
        BFParticleEffects.add(WeatherEffectType.SNOW, new SnowParticleEffect());
        BFParticleEffects.add(WeatherEffectType.LIGHT_SNOW, new LightSnowEffect());
        BFParticleEffects.add(WeatherEffectType.FALLING_LEAVES, new FallingLeavesEffect());
        BFParticleEffects.add(WeatherEffectType.FALLING_ASH, new FallingAshEffect());
        BFParticleEffects.add(WeatherEffectType.INFECTED_NEZHIT, new InfectedNezhitEffect());
        BFParticleEffects.add(WeatherEffectType.RAINSTORM, new RainstormEffect());
        BFParticleEffects.add(WeatherEffectType.BLIZZARD, new FastSnowParticleEffect());
    }
}

