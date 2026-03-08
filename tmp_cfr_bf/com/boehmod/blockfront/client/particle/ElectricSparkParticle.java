/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class ElectricSparkParticle
extends TextureSheetParticle {
    protected ElectricSparkParticle(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6);
        this.setPower(1.0f);
        this.pickSprite(spriteSet);
        this.gravity = 0.5f;
        this.quadSize = Math.random() < 0.5 ? 0.02f : 0.01f;
        this.lifetime = 300;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.setSize(0.01f, 0.01f);
    }

    public void tick() {
        super.tick();
        if (this.onGround) {
            this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.025f);
        }
        if (this.onGround && this.alpha == 0.0f) {
            this.remove();
        }
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    protected int getLightColor(float f) {
        return 0xF000F0;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1677;

        public Provider(SpriteSet spriteSet) {
            this.field_1677 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new ElectricSparkParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1677);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

