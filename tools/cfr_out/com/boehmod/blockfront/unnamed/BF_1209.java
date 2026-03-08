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
 *  net.minecraft.util.RandomSource
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_266;
import com.boehmod.blockfront.unnamed.BF_290;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class BF_1209
extends BF_290 {
    private static final float field_6998 = 2.4f;
    private static final int field_7001 = 40;
    private static final float field_6999 = 0.5f;
    private static final float field_7000 = 0.03f;

    protected BF_1209(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, float f, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, ThreadLocalRandom.current().nextFloat(-2.4f, 2.4f), 2.0f, ThreadLocalRandom.current().nextFloat(-2.4f, 2.4f), d4, d5, d6, f, spriteSet, 40, 0.5f, true);
        this.quadSize = 0.03f;
        this.setAlpha(1.0f);
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_1587;
    }

    public static class BF_1210
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_7002;

        public BF_1210(SpriteSet spriteSet) {
            this.field_7002 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            RandomSource randomSource = clientLevel.random;
            double d7 = (double)randomSource.nextFloat() * -1.9 * (double)randomSource.nextFloat() * 0.1;
            double d8 = (double)randomSource.nextFloat() * -0.5 * (double)randomSource.nextFloat() * 0.1 * 5.0;
            double d9 = (double)randomSource.nextFloat() * -1.9 * (double)randomSource.nextFloat() * 0.1;
            return new BF_1209(clientLevel, d, d2, d3, d7, d8, d9, 1.0f, this.field_7002);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

