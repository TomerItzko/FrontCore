/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.player.clan;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.google.common.cache.CacheLoader;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ClanCacheLoader
extends CacheLoader<UUID, AbstractClanData> {
    @NotNull
    public AbstractClanData load(@NotNull UUID uUID) throws Exception {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "The mod manager is null!";
        ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).getRequester().push(RequestType.CLAN_DATA, uUID);
        return new AbstractClanData(uUID, UUID.randomUUID(), "Unknown");
    }

    @NotNull
    public /* synthetic */ Object load(@NotNull Object uuid) throws Exception {
        return this.load((UUID)uuid);
    }
}

