/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.block.base;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface IBarrierPredicate {
    public boolean shouldBlockCollision(@Nullable Entity var1);
}

