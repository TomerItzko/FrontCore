/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.CrossCollisionBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.ClientUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class PowerLineBlock
extends CrossCollisionBlock
implements IShootAction {
    @NotNull
    private static final BooleanProperty field_2263 = BlockStateProperties.UP;
    @NotNull
    private static final MapCodec<PowerLineBlock> field_2262 = PowerLineBlock.simpleCodec(PowerLineBlock::new);
    @NotNull
    private final VoxelShape[] field_2260;

    public PowerLineBlock(@NotNull BlockBehaviour.Properties properties) {
        super(0.0f, 3.0f, 0.0f, 16.0f, 24.0f, properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getStateDefinition().any()).setValue((Property)field_2263, (Comparable)Boolean.TRUE)).setValue((Property)NORTH, (Comparable)Boolean.FALSE)).setValue((Property)EAST, (Comparable)Boolean.FALSE)).setValue((Property)SOUTH, (Comparable)Boolean.FALSE)).setValue((Property)WEST, (Comparable)Boolean.FALSE)).setValue((Property)WATERLOGGED, (Comparable)Boolean.FALSE)).setValue((Property)BFBlockProperties.SPARKING, (Comparable)Boolean.FALSE));
        this.field_2260 = this.makeShapes(3.0f, 3.0f, 16.0f, 0.0f, 16.0f);
    }

    @OnlyIn(value=Dist.CLIENT)
    public static void method_1898(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        float f;
        float f2;
        float f3;
        int n;
        SimpleParticleType simpleParticleType;
        Vec3 vec3 = new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()).add(0.5, 0.5, 0.5);
        if (randomSource.nextInt(6) == 0) {
            simpleParticleType = (SimpleParticleType)BFParticleTypes.ELECTRIC_SPARK_PARTICLE.get();
            for (n = 0; n < 6; ++n) {
                f3 = 0.5f * randomSource.nextFloat();
                f2 = 0.5f * randomSource.nextFloat();
                f = 0.5f * randomSource.nextFloat();
                if (randomSource.nextBoolean()) {
                    f3 = -f3;
                }
                if (randomSource.nextBoolean()) {
                    f2 = -f2;
                }
                if (randomSource.nextBoolean()) {
                    f = -f;
                }
                clientLevel.addParticle((ParticleOptions)simpleParticleType, vec3.x + (double)f3, vec3.y + (double)f2, vec3.z + (double)f, 0.0, 0.0, 0.0);
            }
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.BLOCK_POWERLINE_ZAP.get(), SoundSource.BLOCKS, 0.2f, 0.8f + 0.4f * randomSource.nextFloat(), false);
        }
        if (randomSource.nextInt(14) == 0) {
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.BLOCK_POWERLINE_EXPLODE.get(), SoundSource.BLOCKS, 0.5f, 0.8f + 0.4f * randomSource.nextFloat(), false);
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.BLOCK_POWERLINE_ELECTRIC.get(), SoundSource.BLOCKS, 1.0f, 0.8f + 0.4f * randomSource.nextFloat(), false);
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)BFSounds.BLOCK_POWERLINE_ZAP.get(), SoundSource.BLOCKS, 1.0f, 0.8f + 0.4f * randomSource.nextFloat(), false);
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.GRENADE_FLASH.get(), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            simpleParticleType = (SimpleParticleType)BFParticleTypes.BULLET_SPARK_PARTICLE.get();
            for (n = 0; n < 16; ++n) {
                f3 = 0.5f * randomSource.nextFloat();
                f2 = 0.5f * randomSource.nextFloat();
                f = 0.5f * randomSource.nextFloat();
                if (randomSource.nextBoolean()) {
                    f3 = -f3;
                }
                if (randomSource.nextBoolean()) {
                    f2 = -f2;
                }
                if (randomSource.nextBoolean()) {
                    f = -f;
                }
                clientLevel.addParticle((ParticleOptions)simpleParticleType, vec3.x + (double)f3, vec3.y + (double)f2, vec3.z + (double)f, 0.0, 0.0, 0.0);
            }
            SimpleParticleType simpleParticleType2 = (SimpleParticleType)BFParticleTypes.ELECTRIC_SPARK_PARTICLE.get();
            for (int i = 0; i < 6; ++i) {
                f2 = 0.5f * randomSource.nextFloat();
                f = 0.5f * randomSource.nextFloat();
                float f4 = 0.5f * randomSource.nextFloat();
                if (randomSource.nextBoolean()) {
                    f2 = -f2;
                }
                if (randomSource.nextBoolean()) {
                    f = -f;
                }
                if (randomSource.nextBoolean()) {
                    f4 = -f4;
                }
                clientLevel.addParticle((ParticleOptions)simpleParticleType2, vec3.x + (double)f2, vec3.y + (double)f, vec3.z + (double)f4, 0.0, 0.0, 0.0);
            }
        }
    }

    private boolean method_1899(@NotNull BlockState blockState) {
        return blockState.getBlock() instanceof PowerLineBlock;
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
    protected MapCodec<? extends CrossCollisionBlock> codec() {
        return field_2262;
    }

    @NotNull
    public VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return (Boolean)blockState.getValue((Property)field_2263) != false ? this.field_2260[this.getAABBIndex(blockState)] : super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @NotNull
    public VoxelShape getCollisionShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (!((Boolean)blockState.getValue((Property)field_2263)).booleanValue()) {
            return Shapes.empty();
        }
        return this.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        BlockPos blockPos2 = blockPos.north();
        BlockPos blockPos3 = blockPos.east();
        BlockPos blockPos4 = blockPos.south();
        BlockPos blockPos5 = blockPos.west();
        BlockState blockState = level.getBlockState(blockPos2);
        BlockState blockState2 = level.getBlockState(blockPos3);
        BlockState blockState3 = level.getBlockState(blockPos4);
        BlockState blockState4 = level.getBlockState(blockPos5);
        boolean bl = this.method_1899(blockState);
        boolean bl2 = this.method_1899(blockState2);
        boolean bl3 = this.method_1899(blockState3);
        boolean bl4 = this.method_1899(blockState4);
        boolean bl5 = !(bl && !bl2 && bl3 && !bl4 || !bl && bl2 && !bl3 && bl4);
        return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue((Property)field_2263, (Comparable)Boolean.valueOf(bl5 || !level.isEmptyBlock(blockPos.above())))).setValue((Property)NORTH, (Comparable)Boolean.valueOf(bl))).setValue((Property)EAST, (Comparable)Boolean.valueOf(bl2))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(bl3))).setValue((Property)WEST, (Comparable)Boolean.valueOf(bl4))).setValue((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidState.getType() == Fluids.WATER));
    }

    @NotNull
    public BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (((Boolean)blockState.getValue((Property)WATERLOGGED)).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)levelAccessor));
        }
        if (direction == Direction.DOWN) {
            return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }
        boolean bl = direction == Direction.NORTH ? this.method_1899(blockState2) : ((Boolean)blockState.getValue((Property)NORTH)).booleanValue();
        boolean bl2 = direction == Direction.EAST ? this.method_1899(blockState2) : ((Boolean)blockState.getValue((Property)EAST)).booleanValue();
        boolean bl3 = direction == Direction.SOUTH ? this.method_1899(blockState2) : ((Boolean)blockState.getValue((Property)SOUTH)).booleanValue();
        boolean bl4 = direction == Direction.WEST ? this.method_1899(blockState2) : ((Boolean)blockState.getValue((Property)WEST)).booleanValue();
        boolean bl5 = !(bl && !bl2 && bl3 && !bl4 || !bl && bl2 && !bl3 && bl4);
        return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)blockState.setValue((Property)field_2263, (Comparable)Boolean.valueOf(bl5))).setValue((Property)NORTH, (Comparable)Boolean.valueOf(bl))).setValue((Property)EAST, (Comparable)Boolean.valueOf(bl2))).setValue((Property)SOUTH, (Comparable)Boolean.valueOf(bl3))).setValue((Property)WEST, (Comparable)Boolean.valueOf(bl4));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{field_2263, NORTH, EAST, WEST, SOUTH, WATERLOGGED, BFBlockProperties.SPARKING});
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        if (!(level instanceof ClientLevel)) {
            return;
        }
        ClientLevel clientLevel = (ClientLevel)level;
        if (Math.random() < (double)0.2f && (((Boolean)blockState.getValue((Property)NORTH)).booleanValue() || ((Boolean)blockState.getValue((Property)EAST)).booleanValue() || ((Boolean)blockState.getValue((Property)SOUTH)).booleanValue() || ((Boolean)blockState.getValue((Property)WEST)).booleanValue())) {
            PowerLineBlock.method_1898(minecraft, manager, clientLevel, blockPos, level.getRandom());
        }
    }

    @Override
    public boolean showParticle() {
        return false;
    }
}

