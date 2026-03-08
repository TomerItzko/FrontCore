/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.player;

import com.boehmod.blockfront.common.player.PlayerDataCacheLoader;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import org.jetbrains.annotations.NotNull;

public class ServerPlayerDataCacheLoader
extends PlayerDataCacheLoader<BFServerPlayerData, ServerPlayerDataHandler> {
    public ServerPlayerDataCacheLoader(@NotNull ServerPlayerDataHandler dataHandler) {
        super(dataHandler);
    }
}

