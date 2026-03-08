/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.client.particle.BFSimpleParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFParticleTypes {
    @NotNull
    public static final DeferredRegister<ParticleType<?>> DR = DeferredRegister.create((ResourceKey)Registries.PARTICLE_TYPE, (String)"bf");
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> FAR_SMOKE_PARTICLE = DR.register("far_smoke_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BLOOD_SPLAT_PARTICLE = DR.register("blood_splat_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BLOOD_GROUND_SPLAT_PARTICLE = DR.register("blood_ground_splat_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> FALLING_LEAF_PARTICLE = DR.register("falling_leaf_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> ACID_SPLAT_PARTICLE = DR.register("acid_splat_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> ACID_GROUND_SPLAT_PARTICLE = DR.register("acid_ground_splat_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> GRENADE_FLASH = DR.register("grenade_flash", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> LONG_GRENADE_SMOKE = DR.register("long_grenade_smoke", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_PARTICLE = DR.register("bullet_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> SURRENDER_PAPER = DR.register("surrender_paper", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> PAPER = DR.register("paper", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> POWERFUL_BLOOD_SPLAT_PARTICLE = DR.register("powerful_blood_splat_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> ELECTRIC_SPARK_PARTICLE = DR.register("electric_spark_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_SPARK_PARTICLE = DR.register("bullet_spark_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_IMPACT_SMOKE = DR.register("bullet_impact_smoke", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_IMPACT_SMOKE_SNOW = DR.register("bullet_impact_smoke_snow", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_IMPACT_LARGE_SMOKE = DR.register("bullet_impact_large_smoke", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_IMPACT_SNOW = DR.register("bullet_impact_snow", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> SHRAPNEL_SMOKE = DR.register("shrapnel_smoke", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BLOOD_IMPACT_SMOKE = DR.register("blood_impact_smoke", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> ALERT = DR.register("alert", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> HEAL = DR.register("heal", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_IMPACT_FLASH_BASIC = DR.register("bullet_impact_flash_basic", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BULLET_IMPACT_FLASH_STRONG = DR.register("bullet_impact_flash_strong", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> GUN_FLASH = DR.register("gun_flash", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> FALLING_SNOW_PARTICLE = DR.register("falling_snow_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> FALLING_SNOW_FAST_PARTICLE = DR.register("falling_snow_fast_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> POOF_PARTICLE = DR.register("poof_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> CLOUD_PARTICLE = DR.register("cloud_particle", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> RAINDROP = DR.register("raindrop", () -> new BFSimpleParticle(true));
    public static final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> OIL_PARTICLE = DR.register("oil_particle", () -> new BFSimpleParticle(true));
}

