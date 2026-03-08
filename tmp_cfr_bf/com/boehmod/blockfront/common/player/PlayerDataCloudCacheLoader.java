/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.player;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.google.common.cache.CacheLoader;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class PlayerDataCloudCacheLoader
extends CacheLoader<UUID, PlayerCloudData> {
    @NotNull
    public PlayerCloudData load(@NotNull UUID uUID) throws Exception {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager != null) {
            Object obj = bFAbstractManager.getConnectionManager();
            CloudRequestManager cloudRequestManager = ((AbstractConnectionManager)obj).getRequester();
            cloudRequestManager.push(RequestType.PLAYER_DATA, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
        }
        return new PlayerCloudData(uUID);
    }
}

