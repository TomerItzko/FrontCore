/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFShaders;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.unnamed.BF_1153;
import com.boehmod.blockfront.unnamed.BF_266;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;

public class BF_262
extends BF_1153 {
    public BF_262(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6, spriteSet);
    }

    @Override
    @NotNull
    public BF_266 method_1120() {
        return BFClientSettings.EXPERIMENTAL_TOGGLE_SMOKE_SOFT.isEnabled() ? BF_266.field_1588 : BF_266.field_1587;
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return BFClientSettings.EXPERIMENTAL_TOGGLE_SMOKE_SOFT.isEnabled() ? BFShaders.INSTANCE : ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

