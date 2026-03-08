/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BasicVehicleEntity
extends AbstractVehicleEntity {
    public BasicVehicleEntity(EntityType<? extends BasicVehicleEntity> entityType, Level level, BlockEntityType<? extends BFBlockEntity> blockEntityType, BlockState blockState, BlockEntityType<? extends BFBlockEntity> blockEntityType2, BlockState blockState2) {
        super(entityType, level, blockEntityType, blockState, blockEntityType2, blockState2);
    }

    @Override
    public float method_2311() {
        return 0.0f;
    }
}

