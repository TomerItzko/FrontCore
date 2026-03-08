/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public class MMScheduleCache {
    @NotNull
    private final Cache<UUID, UUID> cache = CacheBuilder.newBuilder().maximumSize(256L).expireAfterAccess(1L, TimeUnit.MINUTES).build();

    public void put(@NotNull UUID player, @NotNull UUID match) {
        this.cache.put((Object)player, (Object)match);
    }

    @Nullable
    public UUID getAssignedMatch(@NotNull UUID player) {
        UUID uUID = (UUID)this.cache.getIfPresent((Object)player);
        if (uUID != null) {
            this.cache.invalidate((Object)player);
        }
        return uUID;
    }

    public void changeMatch(@NotNull UUID from, @NotNull UUID to) {
        this.cache.asMap().forEach((player, match) -> {
            if (match.equals(from)) {
                this.cache.put(player, (Object)to);
            }
        });
    }
}

