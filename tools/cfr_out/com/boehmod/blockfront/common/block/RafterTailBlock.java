/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
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

public final class RafterTailBlock
extends PropBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty field_6682 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty field_6683 = BlockStateProperties.WATERLOGGED;
    @NotNull
    private final HorizontalShapeRotator field_6681 = new HorizontalShapeRotator(5.0f, 5.0f, 12.0f, 11.0f, 11.0f, 16.0f);

    public RafterTailBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_6682, (Comparable)Direction.NORTH)).setValue((Property)field_6683, (Comparable)Boolean.FALSE));
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.field_6681.getByDirection((Direction)blockState.getValue((Property)field_6682));
    }
}

