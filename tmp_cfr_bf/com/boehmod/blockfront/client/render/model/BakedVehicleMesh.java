/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.model;

import com.mojang.blaze3d.vertex.VertexBuffer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.NativeResource;

public final class BakedVehicleMesh
implements NativeResource {
    @NotNull
    private final VertexBuffer buffer;
    private long lastUpdated;

    public BakedVehicleMesh(@NotNull VertexBuffer buffer, long lastUpdated) {
        this.buffer = buffer;
        this.lastUpdated = lastUpdated;
    }

    public void close() {
        this.buffer.close();
    }

    @NotNull
    public VertexBuffer getBuffer() {
        return this.buffer;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

