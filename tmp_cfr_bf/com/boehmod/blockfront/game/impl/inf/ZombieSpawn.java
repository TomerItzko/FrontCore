/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public record ZombieSpawn(String zoneID, FDSPose position) {
    public ZombieSpawn(@NotNull ByteBuf buf) throws IOException {
        this(IPacket.readString((ByteBuf)buf), new FDSPose(buf));
    }

    public static ZombieSpawn readFromFDS(@NotNull FDSTagCompound root) {
        String string = root.getString("id");
        FDSPose fDSPose = FDSPose.readNamedFDS("location", root);
        return new ZombieSpawn(string, fDSPose);
    }

    public void writeToFDS(FDSTagCompound root) {
        root.setString("id", this.zoneID);
        this.position.writeNamedFDS("location", root);
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeString((ByteBuf)buf, (String)this.zoneID);
        this.position.write(buf);
    }
}

