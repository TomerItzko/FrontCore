/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BF_1194 {
    @NotNull
    public List<EntityCollisionEntry> method_5833(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull AABB var2, @Nullable LivingEntity var3);

    @Nullable
    public EntityCollisionEntry method_5832(@NotNull BFAbstractManager<?, ?, ?> var1, int var2, @Nullable LivingEntity var3);
}

