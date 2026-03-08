/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.common.block.BarrierBlock;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import org.jetbrains.annotations.NotNull;

public final class BFProjectileImpactSubscriber {
    public static void onProjectileImpact(@NotNull ProjectileImpactEvent event) {
        HitResult hitResult;
        Entity entity = event.getEntity();
        if (entity instanceof BFProjectileEntity && (hitResult = event.getRayTraceResult()) instanceof BlockHitResult) {
            BarrierBlock barrierBlock;
            BlockPos blockPos;
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            hitResult = entity.level();
            BlockState blockState = hitResult.getBlockState(blockPos = blockHitResult.getBlockPos());
            Block block = blockState.getBlock();
            if (block instanceof BarrierBlock && !(barrierBlock = (BarrierBlock)block).method_1810(entity)) {
                event.setCanceled(true);
            }
            if (blockState.getCollisionShape((BlockGetter)hitResult, blockPos) == Shapes.empty()) {
                event.setCanceled(true);
            }
        }
    }
}

