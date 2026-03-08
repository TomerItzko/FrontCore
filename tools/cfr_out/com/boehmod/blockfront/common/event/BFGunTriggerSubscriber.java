/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.event.impl.GunEvent;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BFGunTriggerSubscriber {
    public static void onEntityTriggerGun(@NotNull GunEvent.EntityTrigger event) {
        Object object = event.getEntity();
        if (!(object instanceof Player)) {
            return;
        }
        Player player = (Player)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        UUID uUID = player.getUUID();
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame != null) {
            ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2789(uUID);
        }
    }
}

