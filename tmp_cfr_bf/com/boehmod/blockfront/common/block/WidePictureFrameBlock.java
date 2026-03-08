/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.util.math.HorizontalShapeRotator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public final class WidePictureFrameBlock
extends PropBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty field_6685 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty field_6686 = BlockStateProperties.WATERLOGGED;
    @NotNull
    private final HorizontalShapeRotator field_6684 = new HorizontalShapeRotator(1.0f, 3.0f, 15.0f, 15.0f, 14.0f, 16.0f);

    public WidePictureFrameBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_6685, (Comparable)Direction.NORTH)).setValue((Property)field_6686, (Comparable)Boolean.FALSE));
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.field_6684.getByDirection((Direction)blockState.getValue((Property)field_6685));
    }
}

