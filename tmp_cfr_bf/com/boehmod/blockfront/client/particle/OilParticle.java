/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.unnamed.BF_266;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class OilParticle
extends BF_1153 {
    protected OilParticle(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.gravity = 1.0f;
        this.quadSize = 0.02f;
        this.lifetime = 80;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.xd = d4;
        this.yd = d5;
        this.zd = d6;
        this.xd += (Math.random() - 0.5) * 0.05;
        this.yd += (Math.random() - 0.5) * 0.05;
        this.zd += (Math.random() - 0.5) * 0.05;
        this.setSize(0.01f, 0.01f);
    }

    @Override
    public void tick() {
        super.method_1219();
        if (!this.removed) {
            this.quadSize = MathUtils.moveTowards(this.quadSize, 0.1f, 0.01f);
            this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.15f);
            if (this.alpha <= 0.0f) {
                this.remove();
            }
            if (this.onGround) {
                this.remove();
            }
        }
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_1587;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_6660;

        public Provider(SpriteSet spriteSet) {
            this.field_6660 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new OilParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_6660);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

