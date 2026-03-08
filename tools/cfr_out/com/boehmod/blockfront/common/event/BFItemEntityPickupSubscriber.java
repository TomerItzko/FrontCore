/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.common.util.TriState
 *  net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent$Pre
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import java.util.UUID;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.jetbrains.annotations.NotNull;

public final class BFItemEntityPickupSubscriber {
    public static void onItemEntityPickupPre(@NotNull ItemEntityPickupEvent.Pre event) {
        Player player;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        Object d = ((PlayerDataHandler)obj).getPlayerData(player = event.getPlayer());
        if (((BFAbstractPlayerData)d).isOutOfGame()) {
            event.setCanPickup(TriState.FALSE);
            return;
        }
        UUID uUID = player.getUUID();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (!player.level().isClientSide && abstractGame != null) {
            ItemEntity itemEntity = event.getItemEntity();
            if (((BFAbstractPlayerData)d).isOutOfGame() || !((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2779(player, itemEntity, itemEntity.getItem())) {
                event.setCanPickup(TriState.FALSE);
            }
        }
    }
}

