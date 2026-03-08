/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt.randomat;

import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MelonCannonRandomatEvent
extends RandomatEvent {
    @Override
    public void update(@NotNull TroubleTownGame game, @NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Set<UUID> players) {
    }

    @Override
    public void activate(@NotNull TroubleTownGame game, @NotNull Player activator, @NotNull Level level, @NotNull Set<UUID> players) {
        for (UUID uUID : players) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            serverPlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack((ItemLike)BFItems.GUN_MELON_CANNON.get()));
        }
    }

    @Override
    public void finish(@NotNull TroubleTownGame game, @NotNull Level level, @NotNull Set<UUID> players) {
    }

    @Override
    @NotNull
    public String getName() {
        return "bf.message.gamemode.ttt.randomat.melon.cannon";
    }
}

