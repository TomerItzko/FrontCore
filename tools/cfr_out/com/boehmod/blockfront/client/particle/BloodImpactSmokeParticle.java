/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_324;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BloodImpactSmokeParticle
extends BF_324 {
    protected BloodImpactSmokeParticle(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.alpha = 0.3f;
    }

    @Override
    public float method_1218() {
        return 0.0075f;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1664;

        public Provider(SpriteSet spriteSet) {
            this.field_1664 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new BloodImpactSmokeParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1664);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

