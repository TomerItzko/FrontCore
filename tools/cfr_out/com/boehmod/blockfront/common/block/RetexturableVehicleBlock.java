/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.Direction
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.IntegerProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.VehicleBlock;
import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetexturableVehicleBlock
extends VehicleBlock {
    @NotNull
    private static final MapCodec<RetexturableVehicleBlock> field_2244 = RetexturableVehicleBlock.simpleCodec(properties -> new RetexturableVehicleBlock((BlockBehaviour.Properties)properties, null));
    @NotNull
    private static final IntegerProperty field_2243 = BFBlockProperties.BODY_ROTATION;
    @NotNull
    private static final IntegerProperty field_2245 = BFBlockProperties.HEAD_ROTATION;
    @NotNull
    private static final IntegerProperty field_2248 = BFBlockProperties.TEXTURE_INDEX;
    @NotNull
    private static final DirectionProperty field_2247 = BlockStateProperties.HORIZONTAL_FACING;

    public RetexturableVehicleBlock(@NotNull BlockBehaviour.Properties properties, @Nullable DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends BlockEntity>> deferredHolder) {
        super(properties, deferredHolder);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_2247, (Comparable)Direction.NORTH)).setValue((Property)field_2243, (Comparable)Integer.valueOf(0))).setValue((Property)field_2245, (Comparable)Integer.valueOf(0))).setValue((Property)field_2248, (Comparable)Integer.valueOf(0)));
    }

    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState;
        if (!blockPlaceContext.replacingClickedOnBlock() && (blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().offset(blockPlaceContext.getClickedFace().getOpposite().getNormal()))).getBlock() == this && blockState.getValue((Property)field_2247) == blockPlaceContext.getClickedFace()) {
            return null;
        }
        blockState = this.defaultBlockState();
        for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;
            blockState = (BlockState)blockState.setValue((Property)field_2247, (Comparable)direction);
        }
        return blockState;
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return field_2244;
    }

    @Override
    @NotNull
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue((Property)field_2247, (Comparable)rotation.rotate((Direction)blockState.getValue((Property)field_2247)));
    }

    @Override
    @NotNull
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue((Property)field_2247)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{field_2247, field_2243, field_2245, field_2248});
    }
}

