/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.fds.IFDSObject
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameMatchStreakTracker
implements IFDSObject<FDSTagCompound> {
    private static final int DEFAULT_STREAK = 1;
    private int streak = 1;
    @NotNull
    private final Object2IntMap<UUID> playerStreaks = new Object2IntOpenHashMap();
    @Nullable
    private UUID streakLeader = null;

    public void incrementStreak() {
        ++this.streak;
        this.playerStreaks.replaceAll((uuid, streak) -> streak + 1);
        this.updateStreakLeader();
    }

    private void updateStreakLeader() {
        this.streakLeader = null;
        int n = -1;
        for (Object2IntMap.Entry entry : this.playerStreaks.object2IntEntrySet()) {
            UUID uUID = (UUID)entry.getKey();
            int n2 = entry.getIntValue();
            if (n2 <= n) continue;
            n = n2;
            this.streakLeader = uUID;
        }
    }

    public int getPlayerStreak(@NotNull UUID uuid) {
        return this.playerStreaks.getOrDefault((Object)uuid, 1);
    }

    @Nullable
    public UUID getStreakLeader() {
        return this.streakLeader;
    }

    public int getStreak() {
        return this.streak;
    }

    public void reset() {
        this.streak = 1;
        this.playerStreaks.clear();
        this.updateStreakLeader();
    }

    public void removePlayerStreak(@NotNull UUID uuid) {
        this.playerStreaks.removeInt((Object)uuid);
        if (this.streakLeader != null && this.streakLeader.equals(uuid)) {
            this.streakLeader = null;
        }
        this.updateStreakLeader();
    }

    public void addPlayerStreak(@NotNull UUID uuid) {
        this.playerStreaks.putIfAbsent((Object)uuid, 1);
        if (this.streakLeader == null) {
            this.streakLeader = uuid;
        }
    }

    public void merge(@NotNull GameMatchStreakTracker otherTracker) {
        this.streak = otherTracker.streak;
        this.streakLeader = otherTracker.streakLeader;
        for (Object2IntMap.Entry entry : otherTracker.playerStreaks.object2IntEntrySet()) {
            this.playerStreaks.put((Object)((UUID)entry.getKey()), entry.getIntValue());
        }
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        this.streak = root.getInteger("streak", 1);
        boolean bl = root.getBoolean("hasStreakLeader", false);
        this.streakLeader = !bl ? null : root.getUUID("streakLeader");
        this.playerStreaks.clear();
        int n = root.getInteger("playerStreaksSize", 0);
        for (int i = 0; i < n; ++i) {
            UUID uUID = root.getUUID("playerStreaksKey" + i);
            int n2 = root.getInteger("playerStreaksValue" + i);
            this.playerStreaks.put((Object)uUID, n2);
        }
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        root.setInteger("streak", this.streak);
        boolean bl = this.streakLeader != null;
        root.setBoolean("hasStreakLeader", bl);
        if (bl) {
            root.setUUID("streakLeader", this.streakLeader);
        }
        root.setInteger("playerStreaksSize", this.playerStreaks.size());
        int n = 0;
        for (UUID uUID : this.playerStreaks.keySet()) {
            root.setUUID("playerStreaksKey" + n, uUID);
            root.setInteger("playerStreaksValue" + n, this.playerStreaks.getInt((Object)uUID));
            ++n;
        }
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        this.streak = buf.readInt();
        boolean bl = IPacket.readBoolean((ByteBuf)buf);
        this.streakLeader = !bl ? null : IPacket.readUUID((ByteBuf)buf);
        this.playerStreaks.clear();
        int n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            UUID uUID = IPacket.readUUID((ByteBuf)buf);
            int n2 = buf.readInt();
            this.playerStreaks.put((Object)uUID, n2);
        }
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        buf.writeInt(this.streak);
        boolean bl = this.streakLeader != null;
        IPacket.writeBoolean((ByteBuf)buf, (boolean)bl);
        if (bl) {
            IPacket.writeUUID((ByteBuf)buf, (UUID)this.streakLeader);
        }
        buf.writeInt(this.playerStreaks.size());
        for (UUID uUID : this.playerStreaks.keySet()) {
            IPacket.writeUUID((ByteBuf)buf, (UUID)uUID);
            buf.writeInt(this.playerStreaks.getInt((Object)uUID));
        }
    }
}

