/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  net.minecraft.client.Minecraft
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.env;

import com.boehmod.blockfront.client.env.FakeLevel;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public final class FakeEnvironment {
    private final Object2ObjectMap<UUID, FakePlayer> playerCache = new Object2ObjectOpenHashMap();
    @NotNull
    private final GameProfile profile;
    private FakeLevel level;
    private FakePlayer player;

    public FakeEnvironment(@NotNull GameProfile profile) {
        this.profile = profile;
    }

    public void update(@NotNull Minecraft minecraft) {
        this.playerCache.values().forEach(FakePlayer::tick);
        this.checkEnvironment(minecraft);
        this.player.tick();
    }

    private void checkEnvironment(@NotNull Minecraft minecraft) {
        if (this.level == null || this.player == null) {
            this.init(minecraft);
        }
    }

    private void init(@NotNull Minecraft minecraft) {
        BFLog.log("Initializing fake player environment...", new Object[0]);
        this.level = new FakeLevel(minecraft);
        this.player = new FakePlayer(this.level, this.profile);
        BFLog.log("Finished initializing fake player environment.", new Object[0]);
    }

    public FakePlayer getPlayer(@NotNull Minecraft minecraft) {
        this.checkEnvironment(minecraft);
        return this.player;
    }

    public FakePlayer getPlayerCached(@NotNull Minecraft minecraft, @NotNull GameProfile profile) {
        this.checkEnvironment(minecraft);
        return (FakePlayer)((Object)this.playerCache.computeIfAbsent((Object)profile.getId(), id -> new FakePlayer(this.level, profile)));
    }
}

