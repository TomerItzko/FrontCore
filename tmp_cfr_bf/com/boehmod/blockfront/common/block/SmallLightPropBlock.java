/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.LightPropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public final class SmallLightPropBlock
extends LightPropBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final VoxelShape field_2238 = Block.box((double)3.0, (double)8.0, (double)3.0, (double)13.0, (double)16.0, (double)13.0);

    public SmallLightPropBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_2238;
    }
}

