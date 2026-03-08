/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud.connection;

import com.boehmod.bflib.cloud.common.ConnectionType;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface IConnectionDetails {
    @NotNull
    public UUID getUUID();

    @NotNull
    public String getUsername();

    public byte[] getHardwareId();

    @NotNull
    public String getVersion();

    @NotNull
    public String getVersionHash();

    @NotNull
    public ConnectionType getType();

    public int getMinecraftPort();

    @NotNull
    public String getLocation();

    @NotNull
    public String getServerId();

    public int getCloudPort();

    public void disconnect(@NotNull String var1);

    public boolean updateMatchMaking();
}

