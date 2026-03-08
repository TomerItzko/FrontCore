/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_262;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class LongGrenadeSmokeParticle
extends BF_262 {
    protected LongGrenadeSmokeParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.setSize(0.1f, 0.1f);
        this.field_1685 = this.quadSize = 1.5f;
        this.alpha = 0.5f;
        this.bCol = 0.5f;
        this.gCol = 0.5f;
        this.rCol = 0.5f;
        this.zd = 0.0;
        this.yd = 0.0;
        this.xd = 0.0;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.002f);
        if (this.alpha <= 0.0f) {
            this.remove();
        }
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return super.getRenderType();
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1690;

        public Provider(SpriteSet spriteSet) {
            this.field_1690 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new LongGrenadeSmokeParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1690);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

