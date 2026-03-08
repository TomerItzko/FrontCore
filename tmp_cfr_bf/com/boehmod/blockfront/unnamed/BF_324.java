/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_262;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public abstract class BF_324
extends BF_262 {
    protected final float field_1683 = (float)Math.random();

    protected BF_324(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
        this.setSize(0.05f, 0.05f);
        this.setParticleSpeed(d4, d5, d6);
        this.field_1685 = this.quadSize = 0.5f + this.field_1683;
    }

    public abstract float method_1218();

    @Override
    public void tick() {
        super.tick();
        if (this.alpha <= 0.0f) {
            this.remove();
            return;
        }
        this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, this.method_1218());
        if (this.age > 5) {
            this.quadSize = Mth.lerp((float)0.02f, (float)this.quadSize, (float)(2.0f + this.field_1683));
        }
        this.xd = Mth.lerp((double)0.3f, (double)this.xd, (double)0.0);
        this.yd = Mth.lerp((double)0.3f, (double)this.yd, (double)0.0);
        this.zd = Mth.lerp((double)0.3f, (double)this.zd, (double)0.0);
        this.setPos(this.x + this.xd, this.y + this.yd, this.z + this.zd);
    }
}

