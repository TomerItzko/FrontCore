/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.util.math.HorizontalShapeRotator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TarpSideBlock
extends PropBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty field_6696 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    public static final BooleanProperty field_6697 = BooleanProperty.create((String)"full");
    @NotNull
    private static final HorizontalShapeRotator field_6695 = new HorizontalShapeRotator(0.0f, 0.0f, 15.0f, 16.0f, 16.0f, 16.0f);

    public TarpSideBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue((Property)field_6697, (Comparable)Boolean.valueOf(false)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{field_6697});
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = super.getStateForPlacement(blockPlaceContext);
        if (blockState != null) {
            Level level = blockPlaceContext.getLevel();
            BlockPos blockPos = blockPlaceContext.getClickedPos();
            boolean bl = this.method_5641((BlockGetter)level, blockPos, blockState);
            blockState = (BlockState)blockState.setValue((Property)field_6697, (Comparable)Boolean.valueOf(bl));
            this.method_5642(level, blockPos, blockState);
        }
        return blockState;
    }

    public void onPlace(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
        if (!level.isClientSide() && !blockState2.is((Block)this)) {
            this.method_5642(level, blockPos, blockState);
        }
    }

    public void onRemove(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
        BlockPos blockPos2;
        BlockState blockState3;
        super.onRemove(blockState, level, blockPos, blockState2, bl);
        if (!level.isClientSide() && !blockState2.is((Block)this) && (blockState3 = level.getBlockState(blockPos2 = blockPos.above())).getBlock() instanceof TarpSideBlock && ((Boolean)blockState3.getValue((Property)field_6697)).booleanValue()) {
            level.setBlock(blockPos2, (BlockState)blockState3.setValue((Property)field_6697, (Comparable)Boolean.valueOf(false)), 3);
        }
    }

    @Override
    @NotNull
    public BlockState updateShape(BlockState blockState, Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        BlockState blockState3 = super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        if (direction == Direction.DOWN) {
            boolean bl = this.method_5641((BlockGetter)levelAccessor, blockPos, blockState);
            if ((Boolean)blockState.getValue((Property)field_6697) != bl) {
                blockState3 = (BlockState)blockState3.setValue((Property)field_6697, (Comparable)Boolean.valueOf(bl));
            }
        }
        return blockState3;
    }

    private boolean method_5641(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        BlockPos blockPos2 = blockPos.below();
        BlockState blockState2 = blockGetter.getBlockState(blockPos2);
        if (blockState2.getBlock() instanceof TarpSideBlock) {
            Direction direction;
            Direction direction2 = (Direction)blockState.getValue((Property)field_6696);
            return direction2 == (direction = (Direction)blockState2.getValue((Property)field_6696));
        }
        return false;
    }

    private void method_5642(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        Direction direction;
        Direction direction2;
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState2 = level.getBlockState(blockPos2);
        if (blockState2.getBlock() instanceof TarpSideBlock && (direction2 = (Direction)blockState.getValue((Property)field_6696)) == (direction = (Direction)blockState2.getValue((Property)field_6696)) && !((Boolean)blockState2.getValue((Property)field_6697)).booleanValue()) {
            level.setBlock(blockPos2, (BlockState)blockState2.setValue((Property)field_6697, (Comparable)Boolean.valueOf(true)), 3);
        }
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_6695.getByDirection((Direction)blockState.getValue((Property)field_6696));
    }
}

