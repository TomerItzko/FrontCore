/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.HorizontalShapeRotator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public final class BF_1224
extends PropBlock
implements IShootAction,
SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty field_7106 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty field_7105 = BlockStateProperties.WATERLOGGED;
    @NotNull
    private static final HorizontalShapeRotator field_7104 = new HorizontalShapeRotator(4.0f, 0.0f, 13.0f, 12.0f, 9.0f, 16.0f);

    public BF_1224(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_7106, (Comparable)Direction.NORTH)).setValue((Property)field_7105, (Comparable)Boolean.FALSE));
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_7104.getByDirection((Direction)blockState.getValue((Property)field_7106));
    }

    @Override
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        level.playLocalSound((double)((float)blockPos.getX() + 0.5f), (double)((float)blockPos.getY() + 0.5f), (double)((float)blockPos.getZ() + 0.5f), (SoundEvent)BFSounds.BLOCK_TELEPHONE_IMPACT.get(), SoundSource.BLOCKS, 5.0f, 0.8f + 0.1f * (float)level.random.nextInt(4), false);
    }

    @Override
    public boolean showParticle() {
        return true;
    }
}

