/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public final class StreetLampBlock
extends Block {
    private static final BooleanProperty field_2158 = BlockStateProperties.UP;
    private static final BooleanProperty field_2159 = BlockStateProperties.DOWN;
    private static final BooleanProperty field_2161 = BlockStateProperties.WATERLOGGED;
    private static final BooleanProperty field_2163 = BlockStateProperties.LIT;
    private static final VoxelShape field_2160 = Block.box((double)6.0, (double)0.0, (double)6.0, (double)10.0, (double)12.0, (double)10.0);
    private static final VoxelShape field_2162 = Block.box((double)6.0, (double)0.0, (double)6.0, (double)10.0, (double)16.0, (double)10.0);

    public StreetLampBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_2158, (Comparable)Boolean.TRUE)).setValue((Property)field_2159, (Comparable)Boolean.TRUE)).setValue((Property)field_2161, (Comparable)Boolean.FALSE)).setValue((Property)field_2163, (Comparable)Boolean.TRUE));
    }

    private boolean method_1806(@NotNull BlockState blockState) {
        return blockState.getBlock() instanceof StreetLampBlock;
    }

    protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    public boolean propagatesSkylightDown(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return (Boolean)blockState.getValue((Property)field_2161) == false;
    }

    @NotNull
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.getValue((Property)field_2161) != false ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return (Boolean)blockState.getValue((Property)field_2159) != false && (Boolean)blockState.getValue((Property)field_2158) == false ? field_2160 : field_2162;
    }

    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return (Boolean)blockState.getValue((Property)field_2159) != false && (Boolean)blockState.getValue((Property)field_2158) == false ? field_2162 : field_2160;
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        BlockPos blockPos2 = blockPos.above();
        BlockPos blockPos3 = blockPos.below();
        BlockState blockState = level.getBlockState(blockPos2);
        BlockState blockState2 = level.getBlockState(blockPos3);
        boolean bl = this.method_1806(blockState);
        boolean bl2 = this.method_1806(blockState2);
        return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)field_2158, (Comparable)Boolean.valueOf(bl))).setValue((Property)field_2159, (Comparable)Boolean.valueOf(bl2))).setValue((Property)field_2161, (Comparable)Boolean.valueOf(fluidState.getType() == Fluids.WATER));
    }

    @NotNull
    public BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (((Boolean)blockState.getValue((Property)field_2161)).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)levelAccessor));
        }
        boolean bl = direction == Direction.UP ? this.method_1806(blockState2) : ((Boolean)blockState.getValue((Property)field_2158)).booleanValue();
        boolean bl2 = direction == Direction.DOWN ? this.method_1806(blockState2) : ((Boolean)blockState.getValue((Property)field_2159)).booleanValue();
        return (BlockState)((BlockState)blockState.setValue((Property)field_2158, (Comparable)Boolean.valueOf(bl))).setValue((Property)field_2159, (Comparable)Boolean.valueOf(bl2));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{field_2158, field_2159, field_2161, field_2163});
    }

    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return (Boolean)blockState.getValue((Property)field_2158) == false && (Boolean)blockState.getValue((Property)field_2159) != false ? super.getLightEmission(blockState, blockGetter, blockPos) : 0;
    }
}

