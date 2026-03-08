/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class BulletSparkParticle
extends TextureSheetParticle {
    private boolean field_1673 = true;

    protected BulletSparkParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6);
        this.setPower(5.0f);
        this.pickSprite(spriteSet);
        this.gravity = 0.5f;
        this.quadSize = 0.05f * ThreadLocalRandom.current().nextFloat();
        this.lifetime = 3;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.setSize(0.01f, 0.01f);
    }

    public void tick() {
        super.tick();
        this.field_1673 = this.random.nextBoolean();
        if (this.onGround) {
            this.remove();
        }
    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        if (this.field_1673) {
            super.render(vertexConsumer, camera, f);
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
        private final SpriteSet field_1674;

        public Provider(SpriteSet spriteSet) {
            this.field_1674 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new BulletSparkParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1674);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

