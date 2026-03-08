/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.HorizontalRetexturableBlock;
import com.boehmod.blockfront.util.math.HorizontalShapeRotator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class WallRetexturableBlock
extends HorizontalRetexturableBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty field_6540 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final HorizontalShapeRotator field_2168 = new HorizontalShapeRotator(0.0f, 0.0f, 14.0f, 16.0f, 16.0f, 16.0f);

    public WallRetexturableBlock(@NotNull BlockBehaviour.Properties properties, DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends BlockEntity>> deferredHolder, int n) {
        super(properties, deferredHolder);
    }

    @NotNull
    public VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_2168.getByDirection((Direction)blockState.getValue((Property)field_6540));
    }
}

