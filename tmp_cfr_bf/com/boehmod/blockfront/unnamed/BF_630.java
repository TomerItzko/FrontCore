/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_629;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_630<V extends AbstractVehicleEntity>
extends BF_629<V> {
    public static final ResourceLocation field_2710 = BFRes.loc("textures/misc/debug/vehicle_wheel_particle.png");

    public BF_630(Vec3 vec3) {
        super(vec3);
    }

    @Override
    public ParticleOptions method_2433(@NotNull Level level, @NotNull V v) {
        Vec3 vec3 = v.position();
        Vec3 vec32 = this.field_2708.yRot(-v.getYRot() * ((float)Math.PI / 180) - 1.5707964f);
        Vec3 vec33 = new Vec3(vec3.x + vec32.x, vec3.y + vec32.y, vec3.z + vec32.z);
        BlockState blockState = level.getBlockState(BlockPos.containing((Position)vec33).offset(0, -1, 0));
        return new BlockParticleOption(ParticleTypes.BLOCK, blockState);
    }

    @Override
    public boolean method_2435(V v) {
        return ((AbstractVehicleEntity)v).method_2333() || ((AbstractVehicleEntity)v).method_2334() || ((AbstractVehicleEntity)v).method_2335() || ((AbstractVehicleEntity)v).method_2337();
    }

    @Override
    @NotNull
    public ResourceLocation method_2439() {
        return field_2710;
    }
}

