/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  javax.annotation.Nonnull
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.codec.ByteBufCodecs
 *  net.minecraft.resources.ResourceKey
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFDataComponents {
    @NotNull
    public static final DeferredRegister<DataComponentType<?>> DR = DeferredRegister.create((ResourceKey)Registries.DATA_COMPONENT_TYPE, (String)"bf");
    @Nonnull
    public static final Supplier<DataComponentType<Boolean>> HAS_TAG = DR.register("has_tag", () -> DataComponentType.builder().persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> STICKER0 = DR.register("sticker0", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> STICKER1 = DR.register("sticker1", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> STICKER2 = DR.register("sticker2", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Float>> SKIN_ID = DR.register("skin_id", () -> DataComponentType.builder().persistent((Codec)Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
    @Nonnull
    public static final Supplier<DataComponentType<String>> ORIGINAL_OWNER = DR.register("original_owner", () -> DataComponentType.builder().persistent((Codec)Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    @Nonnull
    public static final Supplier<DataComponentType<Boolean>> DISPLAY_GUN = DR.register("display_gun", () -> DataComponentType.builder().persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    @Nonnull
    public static final Supplier<DataComponentType<String>> NAME_TAG = DR.register("name_tag", () -> DataComponentType.builder().persistent((Codec)Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    @Nonnull
    public static final Supplier<DataComponentType<String>> MAG_TYPE = DR.register("mag_type", () -> DataComponentType.builder().persistent((Codec)Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    @Nonnull
    public static final Supplier<DataComponentType<String>> BARREL_TYPE = DR.register("barrel_type", () -> DataComponentType.builder().persistent((Codec)Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> AMMO = DR.register("ammo", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> AMMO_LOADED = DR.register("ammo_loaded", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> MAX_AMMO = DR.register("max_ammo", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> RELOAD_TICK = DR.register("reload_tick", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> MUZZLE_FLASH = DR.register("muzzle_flash", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Float>> HEAT = DR.register("heat", () -> DataComponentType.builder().persistent((Codec)Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Boolean>> SCOPE = DR.register("scope", () -> DataComponentType.builder().persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    @Nonnull
    public static final Supplier<DataComponentType<Boolean>> ACTIVE = DR.register("active", () -> DataComponentType.builder().persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> EXPLODE_TIME = DR.register("explode_time", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> BLOOD_AMOUNT = DR.register("blood_amount", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> MUD_AMOUNT = DR.register("mud_amount", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Double>> MINT = DR.register("mint", () -> DataComponentType.builder().persistent((Codec)Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).build());
    @Nonnull
    public static final Supplier<DataComponentType<Boolean>> HAS_PATTERN = DR.register("has_pattern", () -> DataComponentType.builder().persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> PATTERN_WIDTH = DR.register("pattern_width", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<Integer>> PATTERN_HEIGHT = DR.register("pattern_height", () -> DataComponentType.builder().persistent((Codec)Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    @Nonnull
    public static final Supplier<DataComponentType<String>> PATTERN_NAME = DR.register("pattern_name", () -> DataComponentType.builder().persistent((Codec)Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
}

