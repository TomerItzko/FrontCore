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
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.unnamed.BF_266;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(value=Dist.CLIENT)
public class PoofParticle
extends BF_1153 {
    private final SpriteSet field_1691;

    protected PoofParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        float f;
        this.gravity = -0.1f;
        this.friction = 0.9f;
        this.field_1691 = spriteSet;
        this.xd = d4 + (Math.random() * 2.0 - 1.0) * (double)0.05f;
        this.yd = d5 + (Math.random() * 2.0 - 1.0) * (double)0.05f;
        this.zd = d6 + (Math.random() * 2.0 - 1.0) * (double)0.05f;
        this.rCol = f = this.random.nextFloat() * 0.3f + 0.7f;
        this.gCol = f;
        this.bCol = f;
        this.quadSize = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 6.0f + 1.0f);
        this.lifetime = (int)(16.0 / ((double)this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        super.method_1219();
        this.setSpriteFromAge(this.field_1691);
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_6490;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1692;

        public Provider(SpriteSet spriteSet) {
            this.field_1692 = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new PoofParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1692);
        }

        public /* synthetic */ Particle createParticle(ParticleOptions particleOptions, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

