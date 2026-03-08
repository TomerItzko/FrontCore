/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.cloud.common.player.PlayerDataType
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 *  com.google.common.cache.RemovalCause
 *  com.google.common.cache.RemovalNotification
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.player;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataCacheLoader;
import com.boehmod.blockfront.common.player.PlayerDataCloudCacheLoader;
import com.boehmod.blockfront.common.player.clan.ClanCacheLoader;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalNotification;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerDataHandler<D extends BFAbstractPlayerData<?, ?, ?, ?>> {
    @NotNull
    private final AttachmentType<D> cloudAttachmentType;
    @NotNull
    private final AttachmentType<PlayerCloudData> profileAttachmentType;
    @NotNull
    private final LoadingCache<UUID, PlayerCloudData> cloudProfileCache = this.createCloudProfileCache();
    @NotNull
    private final LoadingCache<UUID, D> cloudDataCache = this.method_861();
    @NotNull
    private final LoadingCache<UUID, AbstractClanData> clanData = CacheBuilder.newBuilder().maximumSize(256L).expireAfterAccess(16L, TimeUnit.MINUTES).build((CacheLoader)new ClanCacheLoader());

    protected abstract boolean isPlayerInGame(@NotNull UUID var1);

    @NotNull
    protected abstract PlayerDataCacheLoader<D, ?> createPlayerDataCacheLoader();

    public PlayerDataHandler(@NotNull DeferredHolder<AttachmentType<?>, AttachmentType<D>> deferredHolder, @NotNull DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCloudData>> deferredHolder2) {
        this.cloudAttachmentType = (AttachmentType)deferredHolder.get();
        this.profileAttachmentType = (AttachmentType)deferredHolder2.get();
    }

    @NotNull
    protected LoadingCache<UUID, D> method_861() {
        return CacheBuilder.newBuilder().maximumSize(512L).expireAfterAccess(16L, TimeUnit.MINUTES).removalListener(this::method_876).build(this.createPlayerDataCacheLoader());
    }

    protected void method_870(@NotNull RemovalNotification<UUID, PlayerCloudData> removalNotification) {
        if (removalNotification.getCause() == RemovalCause.EXPLICIT || removalNotification.getCause() == RemovalCause.REPLACED) {
            return;
        }
        UUID uUID = (UUID)removalNotification.getKey();
        assert (uUID != null) : "Player UUID is null!";
        PlayerCloudData playerCloudData = (PlayerCloudData)((Object)removalNotification.getValue());
        if (playerCloudData == null) {
            return;
        }
        if (playerCloudData.getPlayerDataType() == PlayerDataType.DISPLAY || this.isPlayerInGame(uUID)) {
            BFLog.log("Refreshing expired player data for player '" + String.valueOf(uUID) + "' as they are still online.", new Object[0]);
            this.cloudProfileCache.put((Object)uUID, (Object)playerCloudData);
        }
    }

    protected void method_876(@NotNull RemovalNotification<UUID, D> removalNotification) {
        if (removalNotification.getCause() == RemovalCause.EXPLICIT || removalNotification.getCause() == RemovalCause.REPLACED) {
            return;
        }
        UUID uUID = (UUID)removalNotification.getKey();
        assert (uUID != null) : "Player UUID is null!";
        BFAbstractPlayerData bFAbstractPlayerData = (BFAbstractPlayerData)removalNotification.getValue();
        if (bFAbstractPlayerData == null) {
            return;
        }
        if (bFAbstractPlayerData.method_835() == PlayerDataType.DISPLAY || this.isPlayerInGame(uUID)) {
            BFLog.log("Refreshing expired player data for player '" + String.valueOf(uUID) + "' as they are still online.", new Object[0]);
            this.cloudDataCache.put((Object)uUID, (Object)bFAbstractPlayerData);
        }
    }

    public int dataCacheSize() {
        return this.cloudDataCache.asMap().size();
    }

    public int profileCacheSize() {
        return this.cloudProfileCache.asMap().size();
    }

    @NotNull
    private LoadingCache<UUID, PlayerCloudData> createCloudProfileCache() {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        if (EnvironmentUtils.isClient()) {
            cacheBuilder.expireAfterAccess(15L, TimeUnit.MINUTES);
        }
        cacheBuilder.removalListener(this::method_870);
        return cacheBuilder.build((CacheLoader)new PlayerDataCloudCacheLoader());
    }

    @NotNull
    public PlayerCloudData getCloudProfile(@NotNull UUID uuid) {
        try {
            return (PlayerCloudData)((Object)this.cloudProfileCache.get((Object)uuid));
        }
        catch (ExecutionException executionException) {
            BFLog.logThrowable("Error while fetching player cloud data for player '" + String.valueOf(uuid) + "'.", executionException, new Object[0]);
            return new PlayerCloudData(uuid);
        }
    }

    @NotNull
    public PlayerCloudData getCloudProfile(@NotNull Player player) {
        return (PlayerCloudData)((Object)player.getData(this.profileAttachmentType));
    }

    public void putClanData(@NotNull UUID uuid, @NotNull AbstractClanData clanData) {
        this.clanData.put((Object)uuid, (Object)clanData);
    }

    @NotNull
    public D getPlayerData(@NotNull UUID uuid) {
        try {
            return (D)((BFAbstractPlayerData)this.cloudDataCache.get((Object)uuid));
        }
        catch (ExecutionException executionException) {
            BFLog.logThrowable("Error while fetching player data for player '" + String.valueOf(uuid) + "'.", executionException, new Object[0]);
            return this.createPlayerCloudData(uuid);
        }
    }

    @NotNull
    public D getPlayerData(@NotNull Player player) {
        return (D)((BFAbstractPlayerData)player.getData(this.cloudAttachmentType));
    }

    protected abstract D createPlayerCloudData(@NotNull UUID var1);

    @NotNull
    public AbstractClanData getClanData(@NotNull UUID uuid) {
        try {
            return (AbstractClanData)this.clanData.get((Object)uuid);
        }
        catch (ExecutionException executionException) {
            BFLog.logThrowable("Error while fetching clan data for player '" + String.valueOf(uuid) + "'.", executionException, new Object[0]);
            return AbstractClanData.EMPTY_CLAN_DATA;
        }
    }

    public void method_862(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Level level) {
        for (BFAbstractPlayerData bFAbstractPlayerData : this.cloudDataCache.asMap().values()) {
            bFAbstractPlayerData.method_839(bFAbstractManager, level, true);
        }
    }

    public void removeData(@NotNull UUID playerUUID) {
        BFLog.log("Removing player data for player '%s'", playerUUID);
        this.cloudDataCache.asMap().remove(playerUUID);
    }

    public void removeCloudData(@NotNull UUID playerUUID) {
        BFLog.log("Removing player cloud data for player '%s'", playerUUID);
        this.cloudProfileCache.asMap().remove(playerUUID);
    }

    public void invalidateCache() {
        this.clanData.invalidateAll();
    }
}

