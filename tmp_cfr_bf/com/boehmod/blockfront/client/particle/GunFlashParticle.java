/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.unnamed.BF_266;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class GunFlashParticle
extends BF_1153 {
    protected GunFlashParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.quadSize = 0.1f;
        this.field_1685 = 0.1f;
        this.lifetime = 2;
        this.alpha = 0.45f;
        this.bCol = 1.0f;
        this.gCol = 1.0f;
        this.rCol = 1.0f;
        this.zd = 0.0;
        this.yd = 0.0;
        this.xd = 0.0;
        this.setSize(0.15f, 0.15f);
    }

    @Override
    protected int getLightColor(float f) {
        return 0xF000F0;
    }

    @Override
    public int method_1121(float f) {
        return super.method_1121(f);
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize += 4.0f;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_6491;
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1666;

        public Provider(SpriteSet spriteSet) {
            this.field_1666 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new GunFlashParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1666);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

