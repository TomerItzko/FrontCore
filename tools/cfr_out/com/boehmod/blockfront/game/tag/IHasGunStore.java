/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.game.GameShopItem;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public interface IHasGunStore {
    public boolean method_3416(@NotNull Player var1, @NotNull GameShopItem var2);

    public void method_3415(@NotNull ServerPlayer var1, @NotNull GameShopItem var2);

    @Nullable
    public GameShopItem method_3417(@NotNull Player var1, @NotNull Item var2);
}

