/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
 *  it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2ByteMap
 *  it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.AmbientVehicleMapEffect;
import com.boehmod.blockfront.map.effect.ArtilleryExplosionMapEffect;
import com.boehmod.blockfront.map.effect.ArtilleryGunMapEffect;
import com.boehmod.blockfront.map.effect.BlockEntityPlaneFlyByMapEffect;
import com.boehmod.blockfront.map.effect.BlockPlaneFlyByMapEffect;
import com.boehmod.blockfront.map.effect.BombingFlashMapEffect;
import com.boehmod.blockfront.map.effect.BulletTracerSpawnerMapEffect;
import com.boehmod.blockfront.map.effect.EntitySpawnMapEffect;
import com.boehmod.blockfront.map.effect.FallingArtilleryMapEffect;
import com.boehmod.blockfront.map.effect.FloorMistMapEffect;
import com.boehmod.blockfront.map.effect.GermanRadarMapEffect;
import com.boehmod.blockfront.map.effect.HangarPlaneMapEffect;
import com.boehmod.blockfront.map.effect.LightningMapEffect;
import com.boehmod.blockfront.map.effect.LoopingSoundPointMapEffect;
import com.boehmod.blockfront.map.effect.ParticleEmitterMapEffect;
import com.boehmod.blockfront.map.effect.ProximitySoundMapEffect;
import com.boehmod.blockfront.map.effect.ReichstagRocketMapEffect;
import com.boehmod.blockfront.map.effect.RocketMapEffect;
import com.boehmod.blockfront.map.effect.SurrenderPaperMapEffect;
import com.boehmod.blockfront.map.effect.WatchlightMapEffect;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import org.jetbrains.annotations.NotNull;

public class MapEffectRegistry {
    @NotNull
    private static final Byte2ObjectMap<Class<? extends AbstractMapEffect>> TYPE_TO_EFFECT = new Byte2ObjectOpenHashMap();
    @NotNull
    private static final Object2ByteMap<Class<? extends AbstractMapEffect>> EFFECT_TO_TYPE = new Object2ByteOpenHashMap();

    public static void registerEffect(byte type, @NotNull Class<? extends AbstractMapEffect> effectClass) {
        TYPE_TO_EFFECT.put(type, effectClass);
        EFFECT_TO_TYPE.put(effectClass, type);
    }

    public static Class<?> getEffect(byte type) {
        if (!TYPE_TO_EFFECT.containsKey(type)) {
            throw new IllegalArgumentException("Invalid map-effect section type byte: " + type);
        }
        return (Class)TYPE_TO_EFFECT.get(type);
    }

    public static byte getType(@NotNull Class<?> effectClass) {
        if (!EFFECT_TO_TYPE.containsKey(effectClass)) {
            throw new IllegalArgumentException("Invalid map-effect section type class: " + String.valueOf(effectClass));
        }
        return EFFECT_TO_TYPE.getByte(effectClass);
    }

    static {
        MapEffectRegistry.registerEffect((byte)1, GermanRadarMapEffect.class);
        MapEffectRegistry.registerEffect((byte)2, BlockPlaneFlyByMapEffect.class);
        MapEffectRegistry.registerEffect((byte)3, BlockEntityPlaneFlyByMapEffect.class);
        MapEffectRegistry.registerEffect((byte)4, FloorMistMapEffect.class);
        MapEffectRegistry.registerEffect((byte)5, AmbientVehicleMapEffect.class);
        MapEffectRegistry.registerEffect((byte)6, BombingFlashMapEffect.class);
        MapEffectRegistry.registerEffect((byte)7, BulletTracerSpawnerMapEffect.class);
        MapEffectRegistry.registerEffect((byte)8, LoopingSoundPointMapEffect.class);
        MapEffectRegistry.registerEffect((byte)9, ParticleEmitterMapEffect.class);
        MapEffectRegistry.registerEffect((byte)10, ReichstagRocketMapEffect.class);
        MapEffectRegistry.registerEffect((byte)11, RocketMapEffect.class);
        MapEffectRegistry.registerEffect((byte)12, HangarPlaneMapEffect.class);
        MapEffectRegistry.registerEffect((byte)13, SurrenderPaperMapEffect.class);
        MapEffectRegistry.registerEffect((byte)14, WatchlightMapEffect.class);
        MapEffectRegistry.registerEffect((byte)15, ArtilleryGunMapEffect.class);
        MapEffectRegistry.registerEffect((byte)16, FallingArtilleryMapEffect.class);
        MapEffectRegistry.registerEffect((byte)17, ArtilleryExplosionMapEffect.class);
        MapEffectRegistry.registerEffect((byte)18, LightningMapEffect.class);
        MapEffectRegistry.registerEffect((byte)19, ProximitySoundMapEffect.class);
        MapEffectRegistry.registerEffect((byte)20, EntitySpawnMapEffect.class);
    }
}

