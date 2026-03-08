/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.WaterloggableFloorPropBlock;
import com.boehmod.blockfront.common.block.base.IBlockStepSoundable;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class RubbleBlock
extends WaterloggableFloorPropBlock
implements IBlockStepSoundable {
    public RubbleBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public SoundEvent getStepSound() {
        return (SoundEvent)BFSounds.BLOCK_RUBBLE_STEP.get();
    }
}

