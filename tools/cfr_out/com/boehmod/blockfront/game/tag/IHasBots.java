/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.game.GameTeam;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface IHasBots {
    public void method_3394(@NotNull ServerLevel var1);

    public void method_3392(@NotNull BotEntity var1, @NotNull Level var2);

    public void method_3387(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull ServerLevel var2, @NotNull BotEntity var3, @NotNull DamageSource var4);

    public void method_3391(@NotNull BotEntity var1, @NotNull Player var2);

    public void method_3389(@NotNull BotEntity var1);

    @Nullable
    public BlockPos method_3388(@NotNull BotEntity var1);

    public boolean method_3390(@NotNull BotEntity var1, @NotNull DamageSource var2);

    public int method_3393(@NotNull GameTeam var1);
}

