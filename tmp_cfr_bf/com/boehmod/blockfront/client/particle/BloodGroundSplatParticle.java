/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_321;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BloodGroundSplatParticle
extends BF_321 {
    protected BloodGroundSplatParticle(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        @NotNull
        private final SpriteSet field_1663;

        public Provider(@NotNull SpriteSet spriteSet) {
            this.field_1663 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new BloodGroundSplatParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1663);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

