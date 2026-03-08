/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.unnamed.BF_263;
import com.boehmod.blockfront.unnamed.BF_266;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class AcidSplatParticle
extends BF_263 {
    protected AcidSplatParticle(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
    }

    @NotNull
    DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> method_1103() {
        return BFParticleTypes.ACID_GROUND_SPLAT_PARTICLE;
    }

    @Override
    protected int getLightColor(float f) {
        return 0xF000F0;
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_6490;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        @NotNull
        private final SpriteSet field_1662;

        public Provider(@NotNull SpriteSet spriteSet) {
            this.field_1662 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new AcidSplatParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1662);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

