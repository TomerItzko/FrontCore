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
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ShrapnelSmokeParticle
extends BF_1153 {
    protected ShrapnelSmokeParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.method_1226(new Vec3(d4, d5, d6));
        this.hasPhysics = true;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.setSize(0.01f, 0.01f);
        this.quadSize = 0.3f;
        this.alpha = 0.2f;
        this.lifetime = 400;
    }

    public void method_1226(Vec3 vec3) {
        this.xd = vec3.x;
        this.yd = vec3.y;
        this.zd = vec3.z;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.age;
        this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.005f);
        this.move(this.xd, this.yd, this.zd);
        if (this.alpha <= 0.0f) {
            this.remove();
        }
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BF_266.field_6490;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet field_1694;

        public Provider(SpriteSet spriteSet) {
            this.field_1694 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            ShrapnelSmokeParticle shrapnelSmokeParticle = new ShrapnelSmokeParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1694);
            shrapnelSmokeParticle.method_1226(new Vec3(d4, d5, d6));
            return shrapnelSmokeParticle;
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

