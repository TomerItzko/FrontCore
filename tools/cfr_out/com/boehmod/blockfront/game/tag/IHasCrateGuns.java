/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.AbstractCrateGunBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface IHasCrateGuns {
    public boolean method_3406(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull Level var2, @NotNull ServerPlayer var3, @NotNull AbstractCrateGunBlockEntity var4);

    public void putCrateItem(@NotNull AbstractCrateGunBlockEntity var1);

    public int getCrateRefreshInterval(@NotNull AbstractCrateGunBlockEntity var1);
}

