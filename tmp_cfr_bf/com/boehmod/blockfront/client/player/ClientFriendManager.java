/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class ClientFriendManager {
    private static final int field_3323 = 160;
    @NotNull
    private final Set<UUID> friends = new ObjectOpenHashSet();
    @NotNull
    private final Set<UUID> friendRequests = new ObjectOpenHashSet();
    @NotNull
    private final Set<UUID> clanRequests = new ObjectOpenHashSet();
    private int requestTimer = 0;

    public void update(@NotNull ClientConnectionManager connectionManager, @NotNull PlayerCloudData cloudData) {
        if (!connectionManager.getStatus().isConnected()) {
            this.friends.clear();
            this.friendRequests.clear();
            return;
        }
        if (this.requestTimer++ >= 160) {
            this.requestTimer = 0;
            connectionManager.getRequester().push(RequestType.PLAYER_FRIENDS, cloudData.getUUID());
        }
    }

    public boolean hasFriend(@NotNull UUID uuid) {
        return this.friends.contains(uuid);
    }

    @NotNull
    public Set<UUID> getFriends() {
        return Collections.unmodifiableSet(this.friends);
    }

    @NotNull
    public Set<UUID> getFriendRequests() {
        return Collections.unmodifiableSet(this.friendRequests);
    }

    @NotNull
    public Set<UUID> getClanRequests() {
        return Collections.unmodifiableSet(this.clanRequests);
    }

    public void replaceAll(@NotNull Set<UUID> friends, @NotNull Set<UUID> friendRequests, @NotNull Set<UUID> clanRequests) {
        this.friends.clear();
        this.friends.addAll(friends);
        this.friendRequests.clear();
        this.friendRequests.addAll(friendRequests);
        this.clanRequests.clear();
        this.clanRequests.addAll(clanRequests);
    }
}

