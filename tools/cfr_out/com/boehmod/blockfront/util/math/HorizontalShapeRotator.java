/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.math;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class HorizontalShapeRotator {
    @NotNull
    private final Map<Direction, VoxelShape> DIRECTION_MAP = Maps.newEnumMap(Direction.class);

    public HorizontalShapeRotator(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.DIRECTION_MAP.put(Direction.NORTH, Block.box((double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2));
        this.DIRECTION_MAP.put(Direction.SOUTH, Block.box((double)(16.0f - x2), (double)y1, (double)(16.0f - z2), (double)(16.0f - x1), (double)y2, (double)(16.0f - z1)));
        this.DIRECTION_MAP.put(Direction.EAST, Block.box((double)(16.0f - z2), (double)y1, (double)x1, (double)(16.0f - z1), (double)y2, (double)x2));
        this.DIRECTION_MAP.put(Direction.WEST, Block.box((double)z1, (double)y1, (double)(16.0f - x2), (double)z2, (double)y2, (double)(16.0f - x1)));
    }

    @NotNull
    public VoxelShape getByDirection(@NotNull Direction direction) {
        return this.DIRECTION_MAP.get(direction);
    }
}

