/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.RenderShape
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
 *  net.minecraft.world.phys.shapes.EntityCollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.base.IBarrierBlock;
import com.boehmod.blockfront.common.block.base.IBarrierPredicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
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
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class BarrierBlock
extends Block
implements IBarrierBlock {
    @NotNull
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final boolean isSolid;
    private final boolean field_2175;
    @NotNull
    private final IBarrierPredicate predicate;

    public BarrierBlock(boolean isSolid, boolean bl, @NotNull IBarrierPredicate predicate) {
        super(BlockBehaviour.Properties.of().strength(-1.0f, 3600000.0f).noOcclusion().noLootTable().noTerrainParticles().isSuffocating((blockState, blockGetter, blockPos) -> false).isRedstoneConductor((blockState, blockGetter, blockPos) -> false).isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false));
        this.isSolid = isSolid;
        this.field_2175 = bl;
        this.predicate = predicate;
        this.registerDefaultState((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)WATERLOGGED, (Comparable)Boolean.FALSE));
    }

    public boolean method_1810(@Nullable Entity entity) {
        return this.predicate.shouldBlockCollision(entity);
    }

    protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
        return this.field_2175;
    }

    @NotNull
    public BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (((Boolean)blockState.getValue((Property)WATERLOGGED)).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        Player player;
        EntityCollisionContext entityCollisionContext;
        Entity entity;
        if (collisionContext instanceof EntityCollisionContext && (entity = (entityCollisionContext = (EntityCollisionContext)collisionContext).getEntity()) instanceof Player && !(player = (Player)entity).isCreative()) {
            return Shapes.empty();
        }
        return super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        EntityCollisionContext entityCollisionContext;
        if (collisionContext instanceof EntityCollisionContext && !this.method_1810((entityCollisionContext = (EntityCollisionContext)collisionContext).getEntity())) {
            return Shapes.empty();
        }
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    public float getShadeBrightness(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return 1.0f;
    }

    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return true;
    }

    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        return localPlayer != null && localPlayer.isCreative() ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{WATERLOGGED});
    }

    @NotNull
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.getValue((Property)WATERLOGGED) != false ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = this.defaultBlockState();
        Level level = blockPlaceContext.getLevel();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        if (blockState.canSurvive((LevelReader)level, blockPos)) {
            return (BlockState)blockState.setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidState.getType() == Fluids.WATER));
        }
        return null;
    }

    @Override
    public boolean isSolid() {
        return this.isSolid;
    }
}

