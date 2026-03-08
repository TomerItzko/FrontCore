/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.Particle
 *  net.minecraft.client.particle.ParticleProvider
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.util.FastColor$ARGB32
 *  net.minecraft.world.level.biome.Biome
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.unnamed.BF_266;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

public class FallingLeafParticle
extends BF_1153 {
    protected FallingLeafParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.quadSize = 0.1f;
        this.lifetime = 400;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.setSize(0.01f, 0.01f);
        Holder holder = clientLevel.getBiome(BlockPos.containing((double)d, (double)d2, (double)d3));
        int n = ((Biome)holder.value()).getFoliageColor();
        this.rCol = (float)FastColor.ARGB32.blue((int)n) / 255.0f;
        this.gCol = (float)FastColor.ARGB32.green((int)n) / 255.0f;
        this.bCol = (float)FastColor.ARGB32.red((int)n) / 255.0f;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        float f = BFRendering.getRenderTime();
        float f2 = (float)Math.sin(((double)f + this.x) / 240.0);
        float f3 = (float)Math.sin(((double)f + this.y) / 260.0);
        this.xd = (double)-0.02f + 0.06 * (double)f2;
        this.yd = -0.05f;
        this.zd = (double)-0.02f + 0.06 * (double)f3;
        super.method_1219();
        this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.004f);
        if (this.alpha <= 0.0f) {
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
        private final SpriteSet field_1678;

        public Provider(SpriteSet spriteSet) {
            this.field_1678 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new FallingLeafParticle(clientLevel, d, d2, d3, d4, d5, d6, this.field_1678);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

