/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.CacheLoader
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.player;

import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.google.common.cache.CacheLoader;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerDataCacheLoader<D extends BFAbstractPlayerData<?, ?, ?, ?>, H extends PlayerDataHandler<D>>
extends CacheLoader<UUID, D> {
    @NotNull
    private final H handler;

    public PlayerDataCacheLoader(@NotNull H handler) {
        this.handler = handler;
    }

    @NotNull
    public D load(@NotNull UUID uUID) throws Exception {
        return ((PlayerDataHandler)this.handler).createPlayerCloudData(uUID);
    }
}

