/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.world;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public class BiomeManager {
    @NotNull
    private static final Object2IntMap<ResourceKey<Biome>> SKY_COLORS = new Object2IntOpenHashMap();
    @NotNull
    private static final Object2IntMap<ResourceKey<Biome>> WATER_COLORS = new Object2IntOpenHashMap();

    public static void setSkyColor(@NotNull Holder<Biome> biome, int color) {
        if (biome.unwrapKey().isEmpty()) {
            return;
        }
        SKY_COLORS.put((Object)((ResourceKey)biome.unwrapKey().get()), color);
    }

    public static void setWaterColor(@NotNull Holder<Biome> biome, int color) {
        if (biome.unwrapKey().isEmpty()) {
            return;
        }
        WATER_COLORS.put((Object)((ResourceKey)biome.unwrapKey().get()), color);
    }

    public static int getSkyColor(@NotNull Holder<Biome> biome) {
        if (biome.unwrapKey().isEmpty()) {
            return -1;
        }
        return SKY_COLORS.getOrDefault(biome.unwrapKey().get(), -1);
    }

    public static int getWaterColor(@NotNull Holder<Biome> biome) {
        if (biome.unwrapKey().isEmpty()) {
            return -1;
        }
        return WATER_COLORS.getOrDefault(biome.unwrapKey().get(), -1);
    }

    public static void resetBiome(@NotNull Holder<Biome> biome) {
        if (biome.unwrapKey().isEmpty()) {
            return;
        }
        ResourceKey resourceKey = (ResourceKey)biome.unwrapKey().get();
        SKY_COLORS.removeInt((Object)resourceKey);
        WATER_COLORS.removeInt((Object)resourceKey);
    }
}

