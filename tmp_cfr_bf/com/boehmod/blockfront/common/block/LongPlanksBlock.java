/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.common.block.WaterloggableFloorPropBlock;
import com.boehmod.blockfront.common.block.base.IBlockStepSoundable;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class LongPlanksBlock
extends WaterloggableFloorPropBlock
implements IBlockStepSoundable {
    public LongPlanksBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public SoundEvent getStepSound() {
        return (SoundEvent)BFSounds.BLOCK_PLANK_STEP.get();
    }
}

