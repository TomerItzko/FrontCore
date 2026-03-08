/*
 * Decompiled with CFR.
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

public class BulletImpactSmokeParticle
extends BF_324 {
    protected BulletImpactSmokeParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.alpha = 0.5f;
    }

    @Override
    public float method_1218() {
        return 0.01f;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1667;

        public Provider(SpriteSet spriteSet) {
            this.field_1667 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new BulletImpactSmokeParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1667);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

