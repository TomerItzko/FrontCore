/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_629;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_628<V extends AbstractVehicleEntity>
extends BF_629<V> {
    public static final ResourceLocation field_2707 = BFRes.loc("textures/misc/debug/vehicle_engine_particle.png");
    public final SimpleParticleType field_2706;

    public BF_628(SimpleParticleType simpleParticleType, Vec3 vec3) {
        super(vec3);
        this.field_2706 = simpleParticleType;
    }

    public SimpleParticleType method_2433(@NotNull Level level, @NotNull V v) {
        return this.field_2706;
    }

    @Override
    public boolean method_2435(V v) {
        return ((AbstractVehicleEntity)v).method_2333() || ((AbstractVehicleEntity)v).method_2334();
    }

    @Override
    @NotNull
    public ResourceLocation method_2439() {
        return field_2707;
    }
}

