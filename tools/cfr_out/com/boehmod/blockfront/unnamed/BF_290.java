/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.util.Mth
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public abstract class BF_290
extends BF_1153 {
    protected BF_290(ClientLevel clientLevel, double d, double d2, double d3, float f, float f2, float f3, double d4, double d5, double d6, float f4, SpriteSet spriteSet, int n, float f5, boolean bl) {
        super(clientLevel, d, d2, d3, 0.0, 0.0, 0.0, spriteSet);
        this.friction = 0.96f;
        this.gravity = f5;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd *= (double)f;
        this.yd *= (double)f2;
        this.zd *= (double)f3;
        this.xd += d4;
        this.yd += d5;
        this.zd += d6;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.quadSize *= 0.1f * f4;
        this.lifetime = (int)((double)n / ((double)clientLevel.random.nextFloat() * 0.8 + 0.2) * (double)f4);
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = bl;
        this.alpha = 0.0f;
    }

    @Override
    public float getQuadSize(float f) {
        return this.quadSize * Mth.clamp((float)(((float)this.age + f) / (float)this.lifetime * 32.0f), (float)0.0f, (float)1.0f);
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.method_5930();
        boolean bl = this.age > this.lifetime || this.onGround;
        float f = bl ? 0.02f : 0.6f;
        this.alpha = MathUtils.moveTowards(this.alpha, bl ? 0.0f : 0.25f, f);
        if (bl && this.alpha <= 0.0f) {
            this.remove();
        }
    }

    protected void method_5930() {
        this.yd -= 0.03 * (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
            this.xd *= 1.1;
            this.zd *= 1.1;
        }
        this.xd *= (double)this.friction;
        this.yd *= (double)this.friction;
        this.zd *= (double)this.friction;
        if (this.onGround) {
            this.xd *= (double)0.7f;
            this.zd *= (double)0.7f;
        }
    }
}

