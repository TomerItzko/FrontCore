/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_1144;
import com.boehmod.blockfront.unnamed.BF_266;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class RaindropParticle
extends BF_1144 {
    protected RaindropParticle(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.gravity = 1.5f;
        this.quadSize = 0.35f;
        this.lifetime = 80;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.alpha = 0.0f;
        this.setSize(0.01f, 0.01f);
    }

    @Override
    public void tick() {
        super.method_1219();
        this.xd = 0.0;
        this.yd = -this.gravity;
        this.zd = 0.0;
        this.alpha = MathUtils.moveTowards(this.alpha, 0.45f, 0.05f);
        if (this.onGround || this.age++ >= this.lifetime) {
            this.level.addParticle((ParticleOptions)ParticleTypes.RAIN, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            this.remove();
        }
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_1587;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_6472;

        public Provider(SpriteSet spriteSet) {
            this.field_6472 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new RaindropParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_6472);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

