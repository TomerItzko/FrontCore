/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.GermanRadarBlock;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SwingingLogBlock
extends GermanRadarBlock {
    public SwingingLogBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties, BFBlockEntityTypes.SWINGING_LOG);
    }

    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (randomSource.nextInt(24) == 0) {
            level.playLocalSound(blockPos, (SoundEvent)BFSounds.BLOCK_SWINGING_LOG_CREAK.get(), SoundSource.BLOCKS, 0.5f, 0.8f + 0.2f * randomSource.nextFloat(), false);
        }
    }
}

