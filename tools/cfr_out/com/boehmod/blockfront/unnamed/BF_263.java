/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.util.Mth
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_1153;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public abstract class BF_263
extends BF_1153 {
    protected BF_263(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.gravity = 1.0f;
        this.quadSize = 0.02f;
        this.lifetime = 80;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.setSize(0.01f, 0.01f);
    }

    @Override
    public void tick() {
        super.method_1219();
        if (!this.removed) {
            this.quadSize = Mth.lerp((float)0.5f, (float)this.quadSize, (float)0.05f);
            if (this.onGround) {
                this.level.addParticle((ParticleOptions)this.method_1103().get(), this.x, this.y, this.z, 0.0, 0.0, 0.0);
                this.remove();
            }
        }
    }

    @NotNull
    abstract DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> method_1103();
}

