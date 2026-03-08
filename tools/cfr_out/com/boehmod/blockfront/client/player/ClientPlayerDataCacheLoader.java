/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.player.PlayerDataCacheLoader;
import org.jetbrains.annotations.NotNull;

public class ClientPlayerDataCacheLoader
extends PlayerDataCacheLoader<BFClientPlayerData, ClientPlayerDataHandler> {
    public ClientPlayerDataCacheLoader(@NotNull ClientPlayerDataHandler dataHandler) {
        super(dataHandler);
    }
}

