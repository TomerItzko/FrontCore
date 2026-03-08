/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.connection.ConnectionStatus
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.cloud.common;

import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.cloud.connection.IConnectionDetails;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.util.BFLog;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractConnectionManager<M extends BFAbstractManager<?, ?, ?>>
implements IConnectionDetails {
    @NotNull
    private final BFConnection connection;
    @NotNull
    private final CloudRequestManager<M> requester;
    @NotNull
    protected final M manager;

    public AbstractConnectionManager(@NotNull M m) {
        this.manager = m;
        this.requester = this.createRequester(m);
        this.connection = new BFConnection(this, (BFAbstractManager<?, ?, ?>)m);
    }

    @OverridingMethodsMustInvokeSuper
    public void onUpdate() {
        this.connection.onUpdateCore();
        if (this.connection.getStatus().isVerified()) {
            this.requester.onUpdate();
        }
    }

    @NotNull
    protected abstract CloudRequestManager<M> createRequester(M var1);

    public abstract void sendPacket(@NotNull IPacket var1);

    @NotNull
    public BFConnection getConnection() {
        return this.connection;
    }

    @NotNull
    public CloudRequestManager<M> getRequester() {
        return this.requester;
    }

    public void shutdown() {
        this.disconnect("Shutting down.", true);
        this.connection.shutdown();
    }

    public void disconnect(@NotNull String string, boolean bl) {
        this.connection.disconnect(string, bl);
    }

    @NotNull
    public ConnectionStatus getStatus() {
        return this.connection.getStatus();
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void disconnect(@NotNull String reason) {
        BFLog.log("Disconnected from Cloud. (%s)", reason);
    }
}

