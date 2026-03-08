/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.PropBlock;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BFButtonBlock
extends PropBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final BooleanProperty field_2252 = BlockStateProperties.POWERED;
    @NotNull
    private static final DirectionProperty field_6531 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty field_6532 = BlockStateProperties.WATERLOGGED;
    @NotNull
    private final DeferredHolder<SoundEvent, SoundEvent> field_2251;
    @NotNull
    private final DeferredHolder<SoundEvent, SoundEvent> field_2253;

    public BFButtonBlock(@NotNull BlockBehaviour.Properties properties, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder, @NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder2) {
        super(properties);
        this.field_2251 = deferredHolder;
        this.field_2253 = deferredHolder2;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)field_6531, (Comparable)Direction.NORTH)).setValue((Property)field_6532, (Comparable)Boolean.valueOf(false))).setValue((Property)field_2252, (Comparable)Boolean.valueOf(false)));
    }

    private void method_1892(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {
        level.updateNeighborsAt(blockPos, (Block)this);
        level.updateNeighborsAt(blockPos.relative(((Direction)blockState.getValue((Property)field_6531)).getOpposite()), (Block)this);
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{field_2252});
    }

    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        blockState = (BlockState)blockState.cycle((Property)field_2252);
        boolean bl = (Boolean)blockState.getValue((Property)field_2252);
        level.setBlock(blockPos, blockState, 3);
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = bl ? this.field_2251 : this.field_2253;
        level.playSound(null, blockPos, (SoundEvent)deferredHolder.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        this.method_1892(blockState, level, blockPos);
        return InteractionResult.CONSUME;
    }

    public boolean isSignalSource(@NotNull BlockState blockState) {
        return true;
    }

    public int getSignal(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        return (Boolean)blockState.getValue((Property)field_2252) != false ? 15 : 0;
    }

    public int getDirectSignal(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        if (((Boolean)blockState.getValue((Property)field_2252)).booleanValue() && blockState.getValue((Property)field_6531) == direction) {
            return 15;
        }
        return 0;
    }

    public void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        if (!bl && !blockState.is(blockState2.getBlock())) {
            if (((Boolean)blockState.getValue((Property)field_2252)).booleanValue()) {
                this.method_1892(blockState, level, blockPos);
            }
            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }
}

