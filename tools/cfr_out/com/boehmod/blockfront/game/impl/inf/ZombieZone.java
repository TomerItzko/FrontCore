/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.fds.IFDSObject
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.game.impl.inf.ZombieSpawn;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class ZombieZone
implements IFDSObject<FDSTagCompound> {
    private final List<ZombieSpawn> spawns = new ObjectArrayList();
    private String id;
    private boolean active = false;

    public ZombieZone() {
        this("unknown");
    }

    public ZombieZone(String id) {
        this.id = id;
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        this.id = root.getString("id", "unknown");
        this.active = root.getBoolean("active");
        int n = root.getInteger("spawnCount");
        for (int i = 0; i < n; ++i) {
            FDSTagCompound fDSTagCompound = root.getTagCompound("spawn" + i);
            if (fDSTagCompound == null) continue;
            this.spawns.add(ZombieSpawn.readFromFDS(fDSTagCompound));
        }
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        root.setString("id", this.id);
        root.setBoolean("active", this.active);
        int n = this.spawns.size();
        root.setInteger("spawnCount", n);
        for (int i = 0; i < n; ++i) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("spawn" + i);
            this.spawns.get(i).writeToFDS(fDSTagCompound);
            root.setTagCompound("spawn" + i, fDSTagCompound);
        }
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        this.id = IPacket.readString((ByteBuf)buf);
        this.active = IPacket.readBoolean((ByteBuf)buf);
        int n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            this.spawns.add(new ZombieSpawn(buf));
        }
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeString((ByteBuf)buf, (String)this.id);
        IPacket.writeBoolean((ByteBuf)buf, (boolean)this.active);
        buf.writeInt(this.spawns.size());
        for (ZombieSpawn zombieSpawn : this.spawns) {
            zombieSpawn.write(buf);
        }
    }

    public void setInactive() {
        this.active = false;
    }

    public ZombieSpawn getRandomSpawn() {
        return this.spawns.get(ThreadLocalRandom.current().nextInt(this.spawns.size()));
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return this.id;
    }

    public List<ZombieSpawn> getSpawns() {
        return this.spawns;
    }
}

