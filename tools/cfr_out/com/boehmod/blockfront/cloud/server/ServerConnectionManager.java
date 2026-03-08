/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.ConnectionType
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  net.minecraft.server.MinecraftServer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.cloud.server;

import com.boehmod.bflib.cloud.common.ConnectionType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.ac.HardwareIdentifier;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.EnvironmentUtils;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public final class ServerConnectionManager
extends AbstractConnectionManager<BFServerManager> {
    public ServerConnectionManager(@NotNull BFServerManager bFServerManager) {
        super(bFServerManager);
    }

    @Override
    @NotNull
    protected CloudRequestManager<BFServerManager> createRequester(BFServerManager bFServerManager) {
        return new CloudRequestManager<BFServerManager>(bFServerManager);
    }

    @Override
    public void sendPacket(@NotNull IPacket iPacket) {
        ((ServerConnectionManager)((BFServerManager)this.manager).getConnectionManager()).getConnection().sendPacket(iPacket);
    }

    @Override
    @NotNull
    public UUID getUUID() {
        return ((BFServerManager)this.manager).getInstanceUUID();
    }

    @Override
    @NotNull
    public String getUsername() {
        return "MM_Server";
    }

    @Override
    public byte[] getHardwareId() {
        return HardwareIdentifier.get();
    }

    @Override
    @NotNull
    public String getLocation() {
        return ((BFServerManager)this.manager).getCloudUrl();
    }

    @Override
    @NotNull
    public String getServerId() {
        return ((BFServerManager)this.manager).getMatchMakingPassword();
    }

    @Override
    public int getCloudPort() {
        return 1924;
    }

    @Override
    public boolean updateMatchMaking() {
        return ((BFServerManager)this.manager).isMatchMakingEnabled();
    }

    @Override
    @NotNull
    public ConnectionType getType() {
        return ConnectionType.SERVER;
    }

    @Override
    public int getMinecraftPort() {
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer == null) {
            return -1;
        }
        return minecraftServer.getPort();
    }

    @Override
    @NotNull
    public String getVersion() {
        return BlockFront.VERSION;
    }

    @Override
    @NotNull
    public String getVersionHash() {
        return "";
    }
}

