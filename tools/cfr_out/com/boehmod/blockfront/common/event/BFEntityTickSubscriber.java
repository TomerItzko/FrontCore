/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Leashable
 *  net.neoforged.neoforge.event.tick.EntityTickEvent$Post
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.util.BFLog;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

public final class BFEntityTickSubscriber {
    public static void onEntityTickPost(@NotNull EntityTickEvent.Post event) {
        UUID uUID;
        AbstractGame<?, ?, ?> abstractGame;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Entity entity = event.getEntity();
        CompoundTag compoundTag = entity.getPersistentData();
        if (compoundTag.hasUUID("matchId") && ((abstractGame = bFAbstractManager.retrieveGame(uUID = compoundTag.getUUID("matchId"))) == null || abstractGame.getStatus() == GameStatus.POST_GAME || abstractGame.getStatus() == GameStatus.IDLE)) {
            if (entity instanceof Leashable) {
                Leashable leashable = (Leashable)entity;
                leashable.setLeashData(null);
            }
            BFLog.logError("Failed to load match for entity '" + entity.getId() + "'. Discarding.", new Object[0]);
            entity.discard();
        }
    }
}

