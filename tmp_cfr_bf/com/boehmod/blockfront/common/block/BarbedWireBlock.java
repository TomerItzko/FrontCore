/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.PropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class BarbedWireBlock
extends PropBlock
implements SimpleWaterloggedBlock {
    public static final float field_2099 = 0.5f;

    public BarbedWireBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Entity entity) {
        entity.makeStuckInBlock(blockState, new Vec3(0.45, 0.65, 0.45));
        if (entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().cactus(), 0.5f);
        }
    }
}

