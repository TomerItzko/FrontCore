/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.block.base.IBarrierBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BF_905 {
    @NotNull
    public static HitResult method_3753(@NotNull Entity entity, double d, float f) {
        Vec3 vec3 = entity.getLookAngle();
        Vec3 vec32 = entity.getEyePosition(f);
        Vec3 vec33 = vec32.add(vec3.x * d, vec3.y * d, vec3.z * d);
        return BF_905.method_3755(entity.level(), new ClipContext(vec32, vec33, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, entity));
    }

    @NotNull
    public static BlockHitResult method_3755(Level level, ClipContext clipContext3) {
        return (BlockHitResult)BlockGetter.traverseBlocks((Vec3)clipContext3.getFrom(), (Vec3)clipContext3.getTo(), (Object)clipContext3, (clipContext2, blockPos) -> {
            IBarrierBlock iBarrierBlock;
            BlockState blockState = level.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block instanceof IBarrierBlock && !(iBarrierBlock = (IBarrierBlock)block).isSolid()) {
                return null;
            }
            iBarrierBlock = level.getFluidState(blockPos);
            Vec3 vec3 = clipContext3.getFrom();
            Vec3 vec32 = clipContext3.getTo();
            VoxelShape voxelShape = block instanceof IShootAction ? blockState.getShape((BlockGetter)level, blockPos) : clipContext3.getBlockShape(blockState, (BlockGetter)level, blockPos);
            BlockHitResult blockHitResult = level.clipWithInteractionOverride(vec3, vec32, blockPos, voxelShape, blockState);
            VoxelShape voxelShape2 = clipContext3.getFluidShape((FluidState)iBarrierBlock, (BlockGetter)level, blockPos);
            BlockHitResult blockHitResult2 = voxelShape2.clip(vec3, vec32, blockPos);
            Vec3 vec33 = clipContext3.getFrom();
            double d = blockHitResult == null ? Double.MAX_VALUE : vec33.distanceToSqr(blockHitResult.getLocation());
            double d2 = blockHitResult2 == null ? Double.MAX_VALUE : vec33.distanceToSqr(blockHitResult2.getLocation());
            return d <= d2 ? blockHitResult : blockHitResult2;
        }, clipContext -> {
            Vec3 vec3 = clipContext.getFrom().subtract(clipContext.getTo());
            BlockPos blockPos = BlockPos.containing((Position)clipContext.getTo());
            return BlockHitResult.miss((Vec3)clipContext.getTo(), (Direction)Direction.getNearest((double)vec3.x, (double)vec3.y, (double)vec3.z), (BlockPos)blockPos);
        });
    }
}

