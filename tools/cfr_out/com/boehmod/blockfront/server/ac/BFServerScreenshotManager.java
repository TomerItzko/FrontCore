/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.ac;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.ac.BFAbstractScreenshotManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.BFScreenshotRequester;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class BFServerScreenshotManager
extends BFAbstractScreenshotManager<BFServerManager> {
    @NotNull
    private final Object2ObjectMap<UUID, BFScreenshotRequester> REQUESTERS = new Object2ObjectOpenHashMap();

    @Nullable
    public BFScreenshotRequester enqueue(@NotNull ServerPlayer player) {
        UUID uUID = player.getUUID();
        if (!this.REQUESTERS.containsKey((Object)uUID)) {
            this.REQUESTERS.put((Object)uUID, (Object)new BFScreenshotRequester(player));
        }
        return (BFScreenshotRequester)this.REQUESTERS.get((Object)uUID);
    }

    public void unqueue(@NotNull UUID playerUuid) {
        this.REQUESTERS.remove((Object)playerUuid);
    }

    @Override
    protected void onUpdate(@NotNull BFServerManager bFServerManager) {
        this.REQUESTERS.values().forEach(BFScreenshotRequester::onUpdate);
    }

    @Override
    protected /* synthetic */ void onUpdate(@NotNull BFAbstractManager manager) {
        this.onUpdate((BFServerManager)manager);
    }
}

