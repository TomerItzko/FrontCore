/*
 * Decompiled with CFR.
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

