/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.PacketClientRequest
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  it.unimi.dsi.fastutil.objects.ObjectListIterator
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.cloud.common;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.PacketClientRequest;
import com.boehmod.blockfront.cloud.BFFeatureFlags;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CloudRequestManager<M extends BFAbstractManager<?, ?, ?>> {
    private static final int field_2107 = 120;
    private static final int field_2108 = 10;
    private static final int field_2109 = 6;
    @NotNull
    private final Deque<Entry> queue = new ArrayDeque<Entry>();
    @NotNull
    private final BFFeatureFlags featureFlagManager = new BFFeatureFlags();
    private int updateRequestTimer = 0;
    @NotNull
    private final M manager;

    public CloudRequestManager(@NotNull M manager) {
        this.manager = manager;
    }

    public void removeUuid(@NotNull UUID uuid) {
        this.queue.removeIf(entry -> uuid.equals(entry.playerUUID()));
    }

    public void push(@NotNull RequestType requestType) {
        this.queue.add(new Entry(requestType, null));
    }

    public void push(@NotNull RequestType requestType, @NotNull UUID playerUUID) {
        this.queue.add(new Entry(requestType, playerUUID));
    }

    public void onUpdate() {
        if (this.updateRequestTimer-- <= 0) {
            this.updateRequestTimer = 120;
            this.updateRequests();
        }
    }

    private void updateRequests() {
        int n = 0;
        while (!this.queue.isEmpty() && n++ < 6) {
            this.pollQueue(n);
        }
    }

    private void pollQueue(int n) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (int i = 0; i < 10 && !this.queue.isEmpty(); ++i) {
            objectArrayList.add((Object)this.queue.poll());
        }
        EnumSet<RequestType> enumSet = EnumSet.noneOf(RequestType.class);
        ObjectArrayList objectArrayList2 = new ObjectArrayList();
        for (Entry entry : objectArrayList) {
            UUID uUID = entry.playerUUID();
            RequestType requestType = entry.requestType();
            if (uUID == null) {
                enumSet.add(requestType);
                continue;
            }
            objectArrayList2.add(Map.entry(uUID, EnumSet.of(requestType)));
        }
        ObjectListIterator objectListIterator = new PacketClientRequest(enumSet, (ObjectList)objectArrayList2);
        ((AbstractConnectionManager)((BFAbstractManager)this.manager).getConnectionManager()).sendPacket((IPacket)objectListIterator);
    }

    @NotNull
    public BFFeatureFlags getFeatureFlagManager() {
        return this.featureFlagManager;
    }

    public void updateRequestsNow() {
        this.updateRequestTimer = 0;
    }

    private record Entry(@NotNull RequestType requestType, @Nullable UUID playerUUID) {
    }
}

