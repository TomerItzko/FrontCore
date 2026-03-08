/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.BombEntity;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface BF_809 {
    public int method_3386();

    public void method_3382(@NotNull BombEntity var1, @NotNull Level var2);

    public void method_3381(@NotNull BombEntity var1, @Nullable ServerPlayer var2, @NotNull UUID var3);

    public boolean method_3384(@NotNull Level var1, @NotNull Player var2, @NotNull BombEntity var3);

    public boolean method_3383(@NotNull Level var1, @NotNull Player var2);

    public void method_3385(@NotNull Level var1, @NotNull Player var2, @NotNull ItemStack var3);
}

