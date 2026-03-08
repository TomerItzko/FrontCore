/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PropBlock
extends Block
implements SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PropBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)HORIZONTAL_FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, (Comparable)Boolean.FALSE));
    }

    @NotNull
    public BlockState updateShape(BlockState blockState, Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (direction.getOpposite() == blockState.getValue((Property)HORIZONTAL_FACING) && !blockState.canSurvive((LevelReader)levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (((Boolean)blockState.getValue((Property)WATERLOGGED)).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState;
        Level level = blockPlaceContext.getLevel();
        if (!blockPlaceContext.replacingClickedOnBlock() && (blockState = level.getBlockState(blockPlaceContext.getClickedPos().offset(blockPlaceContext.getClickedFace().getOpposite().getNormal()))).getBlock() == this && blockState.getValue((Property)HORIZONTAL_FACING) == blockPlaceContext.getClickedFace()) {
            return null;
        }
        blockState = this.defaultBlockState();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        FluidState fluidState = level.getFluidState(blockPlaceContext.getClickedPos());
        for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal() || !(blockState = (BlockState)blockState.setValue((Property)HORIZONTAL_FACING, (Comparable)direction.getOpposite())).canSurvive((LevelReader)level, blockPos)) continue;
            return (BlockState)blockState.setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidState.getType() == Fluids.WATER));
        }
        return null;
    }

    @NotNull
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue((Property)HORIZONTAL_FACING, (Comparable)rotation.rotate((Direction)blockState.getValue((Property)HORIZONTAL_FACING)));
    }

    @NotNull
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue((Property)HORIZONTAL_FACING)));
    }

    @OverridingMethodsMustInvokeSuper
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HORIZONTAL_FACING, WATERLOGGED});
    }

    @NotNull
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.getValue((Property)WATERLOGGED) != false ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}

