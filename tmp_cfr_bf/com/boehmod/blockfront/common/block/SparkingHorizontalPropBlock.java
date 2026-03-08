/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.HorizontalPropBlock;
import com.boehmod.blockfront.common.block.PowerLineBlock;
import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SparkingHorizontalPropBlock
extends HorizontalPropBlock {
    public SparkingHorizontalPropBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)BFBlockProperties.SPARKING, (Comparable)Boolean.FALSE));
    }

    @OnlyIn(value=Dist.CLIENT)
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (!(level instanceof ClientLevel)) {
            return;
        }
        ClientLevel clientLevel = (ClientLevel)level;
        super.animateTick(blockState, level, blockPos, randomSource);
        if (((Boolean)blockState.getValue((Property)BFBlockProperties.SPARKING)).booleanValue()) {
            Minecraft minecraft = Minecraft.getInstance();
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            PowerLineBlock.method_1898(minecraft, bFClientManager, clientLevel, blockPos, randomSource);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(new Property[]{BFBlockProperties.SPARKING});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return (BlockState)super.getStateForPlacement(blockPlaceContext).setValue((Property)BFBlockProperties.SPARKING, (Comparable)Boolean.FALSE);
    }
}

