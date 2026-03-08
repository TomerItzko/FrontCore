/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.unnamed.BF_266;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;

public abstract class BF_1133
extends BF_1153 {
    protected BF_1133(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.quadSize = 0.1f;
        this.field_1685 = 0.1f;
        this.lifetime = 3;
        this.alpha = this.method_5518();
        this.bCol = 1.0f;
        this.gCol = 1.0f;
        this.rCol = 1.0f;
        this.zd = 0.0;
        this.yd = 0.0;
        this.xd = 0.0;
        this.setSize(0.001f, 0.001f);
    }

    protected abstract float method_5518();

    protected abstract float method_5519();

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
        this.quadSize += this.method_5519();
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
}

