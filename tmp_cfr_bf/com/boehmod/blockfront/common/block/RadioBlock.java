/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.HorizontalShapeRotator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RadioBlock
extends PropBlock
implements SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    @NotNull
    private final HorizontalShapeRotator shape;
    @Nullable
    private final DeferredHolder<SoundEvent, SoundEvent> chatterSound;

    public RadioBlock(float shapeX1, float shapeY1, float shapeZ1, float shapeX2, float shapeY2, float shapeZ2, @NotNull BlockBehaviour.Properties properties, @Nullable DeferredHolder<SoundEvent, SoundEvent> chatterSound) {
        super(properties);
        this.shape = new HorizontalShapeRotator(shapeX1, shapeY1, shapeZ1, shapeX2, shapeY2, shapeZ2);
        this.chatterSound = chatterSound;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)HORIZONTAL_FACING, (Comparable)Direction.NORTH)).setValue((Property)WATERLOGGED, (Comparable)Boolean.FALSE));
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.shape.getByDirection((Direction)blockState.getValue((Property)HORIZONTAL_FACING));
    }

    protected boolean isPathfindable(@NotNull BlockState blockState, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        if (!level.isClientSide) {
            return;
        }
        Vec3 vec3 = blockPos.getCenter();
        if (random.nextInt(3) == 0) {
            level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.BLOCK_RADIO_STATIC.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        if (this.chatterSound != null) {
            if (random.nextInt(64) == 0) {
                level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.BLOCK_RADIO_MORSE.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }
            if (random.nextInt(64) == 0) {
                level.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)this.chatterSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }
        }
    }
}

