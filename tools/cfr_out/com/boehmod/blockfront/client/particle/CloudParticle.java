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
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(value=Dist.CLIENT)
public class CloudParticle
extends BF_1153 {
    private final SpriteSet field_1675;

    protected CloudParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, 0.0, 0.0, 0.0, spriteSet);
        float f;
        this.friction = 0.96f;
        this.field_1675 = spriteSet;
        this.xd *= (double)0.1f;
        this.yd *= (double)0.1f;
        this.zd *= (double)0.1f;
        this.xd += d4;
        this.yd += d5;
        this.zd += d6;
        this.rCol = f = 1.0f - (float)(Math.random() * (double)0.3f);
        this.gCol = f;
        this.bCol = f;
        this.quadSize *= 1.875f;
        int n = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max((float)n * 2.5f, 1.0f);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public float getQuadSize(float f) {
        return this.quadSize * Mth.clamp((float)(((float)this.age + f) / (float)this.lifetime * 32.0f), (float)0.0f, (float)1.0f);
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_6490;
    }

    @Override
    public void tick() {
        super.method_1219();
        if (!this.removed) {
            double d;
            this.setSpriteFromAge(this.field_1675);
            Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
            if (player != null && this.y > (d = player.getY())) {
                this.y += (d - this.y) * 0.2;
                this.yd += (player.getDeltaMovement().y - this.yd) * 0.2;
                this.setPos(this.x, this.y, this.z);
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1676;

        public Provider(SpriteSet spriteSet) {
            this.field_1676 = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new CloudParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1676);
        }

        public /* synthetic */ Particle createParticle(ParticleOptions particleOptions, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

