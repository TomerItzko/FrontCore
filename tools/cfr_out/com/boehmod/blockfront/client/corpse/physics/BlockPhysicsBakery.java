/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.Util
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3d
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.client.corpse.physics;

import com.boehmod.blockfront.client.corpse.physics.RustCorpsePhysics;
import com.boehmod.blockfront.client.corpse.physics.a;
import com.boehmod.blockfront.common.block.base.IBarrierBlock;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class BlockPhysicsBakery {
    @NotNull
    private static final Logger field_6956 = LogManager.getLogger((String)"BlockPhysicsDataBakery");
    @NotNull
    private final ClientLevel level;
    @Nullable
    private final Function<BlockState, a> field_1093 = Util.memoize(this::method_786);

    public BlockPhysicsBakery(@NotNull ClientLevel level) {
        this.level = level;
    }

    @NotNull
    public Level getLevel() {
        return this.level;
    }

    @Nullable
    private a method_786(@NotNull BlockState blockState) {
        VoxelShape voxelShape = blockState.getCollisionShape((BlockGetter)this.level, BlockPos.ZERO);
        if (voxelShape.isEmpty() || blockState.getBlock() instanceof IBarrierBlock) {
            return null;
        }
        a a2 = RustCorpsePhysics.method_778();
        voxelShape.forAllBoxes((d, d2, d3, d4, d5, d6) -> a2.method_782((Vector3dc)new Vector3d(d, d2, d3), (Vector3dc)new Vector3d(d4, d5, d6)));
        return a2;
    }

    @Nullable
    public a method_787(@Nullable BlockState blockState) {
        if (blockState == null || this.field_1093 == null) {
            return null;
        }
        try {
            return this.field_1093.apply(blockState);
        }
        catch (Exception exception) {
            field_6956.error("Exception building physics data for block", (Throwable)exception);
            return null;
        }
    }
}

