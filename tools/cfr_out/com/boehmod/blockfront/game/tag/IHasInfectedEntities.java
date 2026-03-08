/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface IHasInfectedEntities {
    public void method_3424(@NotNull InfectedEntity var1, @NotNull Level var2);

    public void method_3422(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull ServerLevel var2, @NotNull InfectedEntity var3, @NotNull DamageSource var4);

    public void method_3423(@NotNull InfectedEntity var1, @NotNull Player var2);

    public void method_3425(@NotNull ServerLevel var1, @NotNull InfectedEntity var2);

    public boolean method_3421(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull InfectedEntity var2, @NotNull DamageSource var3, float var4);

    public int method_3426();
}

