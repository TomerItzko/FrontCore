/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;

public class ConfigurableDoorBlock
extends DoorBlock {
    private final boolean isStrong;

    public ConfigurableDoorBlock(@NotNull BlockSetType blockSetType, @NotNull BlockBehaviour.Properties properties, boolean isStrong) {
        super(blockSetType, properties);
        this.isStrong = isStrong;
    }

    public boolean isStrong() {
        return this.isStrong;
    }
}

