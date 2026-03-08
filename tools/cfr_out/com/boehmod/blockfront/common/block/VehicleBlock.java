/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.core.BlockPos
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

import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import com.boehmod.blockfront.common.block.entity.BakedEntityBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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

public class VehicleBlock
extends BaseEntityBlock
implements BakedEntityBlock {
    @NotNull
    private static final MapCodec<VehicleBlock> field_6541 = VehicleBlock.simpleCodec(properties -> new VehicleBlock((BlockBehaviour.Properties)properties, null));
    @NotNull
    private static final IntegerProperty field_6542 = BFBlockProperties.BODY_ROTATION;
    @NotNull
    private static final IntegerProperty field_6544 = BFBlockProperties.HEAD_ROTATION;
    @NotNull
    private static final DirectionProperty field_6545 = BlockStateProperties.HORIZONTAL_FACING;
    @Nullable
    private final DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends BlockEntity>> field_6543;

    public VehicleBlock(@NotNull BlockBehaviour.Properties properties, @Nullable DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends BlockEntity>> deferredHolder) {
        super(properties);
        this.field_6543 = deferredHolder;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_6545, (Comparable)Direction.NORTH)).setValue((Property)field_6542, (Comparable)Integer.valueOf(0))).setValue((Property)field_6544, (Comparable)Integer.valueOf(0)));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState;
        if (!blockPlaceContext.replacingClickedOnBlock() && (blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().offset(blockPlaceContext.getClickedFace().getOpposite().getNormal()))).getBlock() == this && blockState.getValue((Property)field_6545) == blockPlaceContext.getClickedFace()) {
            return null;
        }
        blockState = this.defaultBlockState();
        for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;
            blockState = (BlockState)blockState.setValue((Property)field_6545, (Comparable)direction);
        }
        return blockState;
    }

    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return field_6541;
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return this.field_6543 == null ? null : ((BlockEntityType)this.field_6543.get()).create(blockPos, blockState);
    }

    @NotNull
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue((Property)field_6545, (Comparable)rotation.rotate((Direction)blockState.getValue((Property)field_6545)));
    }

    @NotNull
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue((Property)field_6545)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{field_6545, field_6542, field_6544});
    }
}

