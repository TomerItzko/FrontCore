/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.unnamed.BF_1161;
import com.boehmod.blockfront.util.ClientUtils;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BF_1162
extends BF_1161 {
    private final DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> field_6602;
    private final float field_6604 = 1.0f;
    private Vec3 field_6603 = Vec3.ZERO;

    public BF_1162(@NotNull Vec3 vec3, @NotNull DeferredHolder<ParticleType<?>, ? extends SimpleParticleType> deferredHolder, int n) {
        super(vec3, n);
        this.field_6602 = deferredHolder;
    }

    public BF_1162 method_5620(@NotNull Vec3 vec3) {
        this.field_6603 = vec3;
        return this;
    }

    @Override
    public boolean method_5619(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel) {
        double d = Math.random();
        Objects.requireNonNull(this);
        if (d <= 1.0) {
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)this.field_6602.get(), this.field_6601.x, this.field_6601.y, this.field_6601.z, this.field_6603.x, this.field_6603.y, this.field_6603.z);
        }
        return super.method_5619(minecraft, bFClientManager, clientLevel);
    }
}

