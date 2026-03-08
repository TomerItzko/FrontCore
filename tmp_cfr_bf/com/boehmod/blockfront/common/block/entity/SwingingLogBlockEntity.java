/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block.entity;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SwingingLogBlockEntity
extends BFBlockEntity {
    public SwingingLogBlockEntity(@NotNull BlockEntityType<?> blockEntityType, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public boolean method_1882() {
        return true;
    }
}

