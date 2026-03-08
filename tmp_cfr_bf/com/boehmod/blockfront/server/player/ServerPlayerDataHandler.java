/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.player;

import com.boehmod.blockfront.common.player.PlayerDataCacheLoader;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFAttachments;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataCacheLoader;
import com.boehmod.blockfront.util.EnvironmentUtils;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class ServerPlayerDataHandler
extends PlayerDataHandler<BFServerPlayerData> {
    public ServerPlayerDataHandler() {
        super(BFAttachments.SERVER_PLAYER_DATA, BFAttachments.CLOUD_PLAYER_DATA);
    }

    @Override
    protected boolean isPlayerInGame(@NotNull UUID uuid) {
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        assert (minecraftServer != null) : "Minecraft Server instance is null!";
        return minecraftServer.getPlayerList().getPlayer(uuid) != null;
    }

    @Override
    @NotNull
    protected PlayerDataCacheLoader<BFServerPlayerData, ?> createPlayerDataCacheLoader() {
        return new ServerPlayerDataCacheLoader(this);
    }

    @Override
    protected BFServerPlayerData createPlayerCloudData(@NotNull UUID uUID) {
        return new BFServerPlayerData(uUID);
    }
}

