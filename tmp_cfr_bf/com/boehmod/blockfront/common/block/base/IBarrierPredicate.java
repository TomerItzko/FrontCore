/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block.base;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface IBarrierPredicate {
    public boolean shouldBlockCollision(@Nullable Entity var1);
}

