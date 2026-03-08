/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.render.effect;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum WeatherEffectType {
    SNOW("snow"),
    LIGHT_SNOW("light_snow"),
    FALLING_LEAVES("falling_leaves"),
    FALLING_ASH("falling_ash"),
    CITY_WAR("city_war"),
    SPUKEN("spuken"),
    INFECTED_NEZHIT("infected_nezhit"),
    RAINSTORM("rainstorm"),
    DESERT_DUST("desert_dust"),
    BLIZZARD("blizzard");

    @NotNull
    private final String id;
    @NotNull
    private static final Map<String, WeatherEffectType> BY_ID;

    private WeatherEffectType(String id) {
        this.id = id;
    }

    @Nullable
    public static WeatherEffectType fromId(@NotNull String id) {
        return BY_ID.get(id);
    }

    @Nonnull
    public String getId() {
        return this.id;
    }

    static {
        BY_ID = Arrays.stream(WeatherEffectType.values()).collect(Collectors.toMap(WeatherEffectType::getId, Function.identity()));
    }
}

