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

public class FarSmokeParticle
extends BF_1153 {
    public FarSmokeParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.bCol = 0.05f;
        this.gCol = 0.05f;
        this.rCol = 0.05f;
        this.setSize(1.0f, 1.0f);
        this.quadSize = 1.0f;
        this.alpha = 1.0f;
        this.lifetime = 300;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.004f);
        this.xd -= (double)0.0016f;
        this.yd = 0.4f;
        this.zd += (double)0.0016f;
        if (this.age <= 5) {
            this.yd = 0.0;
        }
        if (this.rCol < 1.0f) {
            this.gCol = this.bCol += 0.003f;
            this.rCol = this.bCol;
        }
        this.quadSize += 0.13f;
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        if (this.alpha <= 0.001f) {
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
        public static SpriteSet field_1680;

        public Provider(@NotNull SpriteSet spriteSet) {
            field_1680 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new FarSmokeParticle(clientLevel, d, d2, d3, d4, d5, d6, field_1680);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

