/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.neoforged.neoforge.event.entity.living.MobDespawnEvent
 *  net.neoforged.neoforge.event.entity.living.MobDespawnEvent$Result
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.event.entity.living.MobDespawnEvent;
import org.jetbrains.annotations.NotNull;

public class BFMobDespawnSubscriber {
    public static void onMobDespawn(@NotNull MobDespawnEvent event) {
        CompoundTag compoundTag = event.getEntity().getPersistentData();
        if (compoundTag.hasUUID("matchId")) {
            event.setResult(MobDespawnEvent.Result.DENY);
        }
    }
}

