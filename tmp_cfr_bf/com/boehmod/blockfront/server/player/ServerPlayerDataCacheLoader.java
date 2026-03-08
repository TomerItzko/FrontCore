/*
 * Decompiled with CFR.
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

