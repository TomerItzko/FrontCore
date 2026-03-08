/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.PowerLineBlock;
import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.util.math.HorizontalShapeRotator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class PowerLineWallBlock
extends PropBlock
implements IShootAction,
SimpleWaterloggedBlock {
    @NotNull
    private static final DirectionProperty field_6529 = BlockStateProperties.HORIZONTAL_FACING;
    @NotNull
    private static final BooleanProperty field_6530 = BlockStateProperties.WATERLOGGED;
    @NotNull
    private static final HorizontalShapeRotator field_2265 = new HorizontalShapeRotator(6.5f, 0.0f, 15.0f, 9.5f, 16.0f, 16.0f);

    public PowerLineWallBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_6529, (Comparable)Direction.NORTH)).setValue((Property)field_6530, (Comparable)Boolean.FALSE)).setValue((Property)BFBlockProperties.SPARKING, (Comparable)Boolean.FALSE));
    }

    @OnlyIn(value=Dist.CLIENT)
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (!(level instanceof ClientLevel)) {
            return;
        }
        ClientLevel clientLevel = (ClientLevel)level;
        if (((Boolean)blockState.getValue((Property)BFBlockProperties.SPARKING)).booleanValue()) {
            Minecraft minecraft = Minecraft.getInstance();
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            PowerLineBlock.method_1898(minecraft, bFClientManager, clientLevel, blockPos, randomSource);
        }
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_2265.getByDirection((Direction)blockState.getValue((Property)field_6529));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        if (!(level instanceof ClientLevel)) {
            return;
        }
        ClientLevel clientLevel = (ClientLevel)level;
        if (Math.random() < (double)0.2f) {
            PowerLineBlock.method_1898(minecraft, manager, clientLevel, blockPos, level.getRandom());
        }
    }

    @Override
    public boolean showParticle() {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{BFBlockProperties.SPARKING});
    }
}

