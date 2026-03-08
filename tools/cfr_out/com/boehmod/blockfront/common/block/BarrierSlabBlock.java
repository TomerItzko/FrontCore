/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.EntityCollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.base.IBarrierBlock;
import com.boehmod.blockfront.common.block.base.IBarrierPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BarrierSlabBlock
extends SlabBlock
implements IBarrierBlock {
    private final boolean field_2183;
    private final boolean field_2184;
    @NotNull
    private final IBarrierPredicate predicate;

    public BarrierSlabBlock(boolean bl, boolean bl2, @NotNull IBarrierPredicate predicate) {
        super(BlockBehaviour.Properties.of().strength(-1.0f, 3600000.0f).noOcclusion().noLootTable().noTerrainParticles().isSuffocating((blockState, blockGetter, blockPos) -> false).isRedstoneConductor((blockState, blockGetter, blockPos) -> false).isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false));
        this.field_2183 = bl;
        this.field_2184 = bl2;
        this.predicate = predicate;
    }

    public boolean method_1818(@Nullable Entity entity) {
        return this.predicate.shouldBlockCollision(entity);
    }

    protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
        return this.field_2184;
    }

    public float getShadeBrightness(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return 1.0f;
    }

    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return true;
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        EntityCollisionContext entityCollisionContext;
        if (collisionContext instanceof EntityCollisionContext && this.method_1818((entityCollisionContext = (EntityCollisionContext)collisionContext).getEntity())) {
            return Shapes.empty();
        }
        return super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        EntityCollisionContext entityCollisionContext;
        if (collisionContext instanceof EntityCollisionContext && !this.method_1818((entityCollisionContext = (EntityCollisionContext)collisionContext).getEntity())) {
            return Shapes.empty();
        }
        return super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player != null && minecraft.player.isCreative() ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public boolean isSolid() {
        return this.field_2183;
    }
}

