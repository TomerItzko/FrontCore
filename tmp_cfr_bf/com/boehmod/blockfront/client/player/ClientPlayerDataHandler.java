/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataCacheLoader;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataCacheLoader;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFAttachments;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientPlayerDataHandler
extends PlayerDataHandler<BFClientPlayerData> {
    @Nullable
    private static BFClientPlayerData cachedData;
    @Nullable
    private static PlayerCloudData cachedCloudData;

    public ClientPlayerDataHandler() {
        super(BFAttachments.CLIENT_PLAYER_DATA, BFAttachments.CLOUD_PLAYER_DATA);
    }

    private boolean isPlayerInGame(@NotNull Minecraft minecraft, @NotNull UUID uuid) {
        return minecraft.level != null && minecraft.level.getPlayerByUUID(uuid) != null;
    }

    @NotNull
    public BFClientPlayerData getPlayerData(@NotNull Minecraft minecraft) {
        return cachedData == null ? (cachedData = (BFClientPlayerData)this.getPlayerData(minecraft.getUser().getProfileId())) : cachedData;
    }

    @NotNull
    public PlayerCloudData getCloudData(@NotNull Minecraft minecraft) {
        return cachedCloudData == null ? (cachedCloudData = this.getCloudProfile(minecraft.getUser().getProfileId())) : cachedCloudData;
    }

    @Override
    protected boolean isPlayerInGame(@NotNull UUID uuid) {
        Minecraft minecraft = Minecraft.getInstance();
        return this.isPlayerInGame(minecraft, uuid) || uuid.equals(minecraft.getUser().getProfileId());
    }

    @Override
    @NotNull
    protected PlayerDataCacheLoader<BFClientPlayerData, ?> createPlayerDataCacheLoader() {
        return new ClientPlayerDataCacheLoader(this);
    }

    @Override
    protected BFClientPlayerData createPlayerCloudData(@NotNull UUID uUID) {
        return new BFClientPlayerData(uUID);
    }
}

