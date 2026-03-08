/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.OverridingMethodsMustInvokeSuper
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
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.block;

import com.mojang.serialization.MapCodec;
import javax.annotation.OverridingMethodsMustInvokeSuper;
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
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GermanRadarBlock
extends BaseEntityBlock {
    @NotNull
    private static final MapCodec<GermanRadarBlock> field_6554 = GermanRadarBlock.simpleCodec(properties -> new GermanRadarBlock((BlockBehaviour.Properties)properties, null));
    @NotNull
    private static final DirectionProperty field_6555 = BlockStateProperties.HORIZONTAL_FACING;
    @Nullable
    private final DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends BlockEntity>> field_6553;

    public GermanRadarBlock(@NotNull BlockBehaviour.Properties properties, @Nullable DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<? extends BlockEntity>> deferredHolder) {
        super(properties);
        this.field_6553 = deferredHolder;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState;
        if (!blockPlaceContext.replacingClickedOnBlock() && (blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().offset(blockPlaceContext.getClickedFace().getOpposite().getNormal()))).getBlock() == this && blockState.getValue((Property)field_6555) == blockPlaceContext.getClickedFace()) {
            return null;
        }
        blockState = this.defaultBlockState();
        for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;
            blockState = (BlockState)blockState.setValue((Property)field_6555, (Comparable)direction);
        }
        return blockState;
    }

    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return field_6554;
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        if (this.field_6553 == null) {
            return null;
        }
        return ((BlockEntityType)this.field_6553.get()).create(blockPos, blockState);
    }

    @NotNull
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue((Property)field_6555, (Comparable)rotation.rotate((Direction)blockState.getValue((Property)field_6555)));
    }

    @NotNull
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue((Property)field_6555)));
    }

    @OverridingMethodsMustInvokeSuper
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{field_6555});
    }
}

