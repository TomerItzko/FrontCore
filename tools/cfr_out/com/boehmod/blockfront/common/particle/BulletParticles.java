/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.particle;

import com.boehmod.blockfront.registry.BFParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BulletParticles {
    @NotNull
    private DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> IMPACT_FLASH = BFParticleTypes.BULLET_IMPACT_FLASH_BASIC;
    @NotNull
    private DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> BASE = BFParticleTypes.BULLET_PARTICLE;

    @NotNull
    public DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> getImpactFlash() {
        return this.IMPACT_FLASH;
    }

    public BulletParticles setImpactFlash(@NotNull DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> particleType) {
        this.IMPACT_FLASH = particleType;
        return this;
    }

    @NotNull
    public DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> getImpactSmoke() {
        return this.BASE;
    }

    public BulletParticles getBase(@NotNull DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> particleType) {
        this.BASE = particleType;
        return this;
    }
}

