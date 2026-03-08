/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
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
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.CrateGunBlockEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.tag.IHasCrateGuns;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CrateGunBlock
extends BaseEntityBlock {
    @NotNull
    private static final MapCodec<CrateGunBlock> field_2233 = CrateGunBlock.simpleCodec(CrateGunBlock::new);
    @NotNull
    private static final DirectionProperty field_2235 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty field_2234 = BlockStateProperties.WATERLOGGED;
    @NotNull
    protected static final VoxelShape field_2237 = Block.box((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)13.0, (double)16.0);
    private String field_2236;

    public CrateGunBlock(@NotNull String string, @NotNull BlockBehaviour.Properties properties) {
        this(properties);
        this.field_2236 = string;
    }

    public CrateGunBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_2235, (Comparable)Direction.NORTH)).setValue((Property)field_2234, (Comparable)Boolean.FALSE));
    }

    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return field_2233;
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_2237;
    }

    @NotNull
    public BlockState updateShape(BlockState blockState, Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (direction.getOpposite() == blockState.getValue((Property)field_2235) && !blockState.canSurvive((LevelReader)levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (((Boolean)blockState.getValue((Property)field_2234)).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState;
        Level level = blockPlaceContext.getLevel();
        if (!blockPlaceContext.replacingClickedOnBlock() && (blockState = level.getBlockState(blockPlaceContext.getClickedPos().offset(blockPlaceContext.getClickedFace().getOpposite().getNormal()))).getBlock() == this && blockState.getValue((Property)field_2235) == blockPlaceContext.getClickedFace()) {
            return null;
        }
        blockState = this.defaultBlockState();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        FluidState fluidState = level.getFluidState(blockPlaceContext.getClickedPos());
        for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal() || !(blockState = (BlockState)blockState.setValue((Property)field_2235, (Comparable)direction.getOpposite())).canSurvive((LevelReader)level, blockPos)) continue;
            return (BlockState)blockState.setValue((Property)field_2234, (Comparable)Boolean.valueOf(fluidState.getType() == Fluids.WATER));
        }
        return null;
    }

    @NotNull
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue((Property)field_2235, (Comparable)rotation.rotate((Direction)blockState.getValue((Property)field_2235)));
    }

    @NotNull
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue((Property)field_2235)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{field_2235, field_2234});
    }

    @NotNull
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.getValue((Property)field_2234) != false ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return null;
    }

    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CrateGunBlockEntity(this.field_2236, blockPos, blockState);
    }

    @NotNull
    public InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult blockHitResult) {
        CrateGunBlockEntity crateGunBlockEntity;
        AbstractGame<?, ?, ?> abstractGame;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof CrateGunBlockEntity && (abstractGame = (crateGunBlockEntity = (CrateGunBlockEntity)blockEntity).getNearestActiveGame()) instanceof IHasCrateGuns) {
            ServerPlayer serverPlayer;
            IHasCrateGuns iHasCrateGuns = (IHasCrateGuns)((Object)abstractGame);
            if (player instanceof ServerPlayer && iHasCrateGuns.method_3406(bFAbstractManager, level, serverPlayer = (ServerPlayer)player, crateGunBlockEntity)) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}

