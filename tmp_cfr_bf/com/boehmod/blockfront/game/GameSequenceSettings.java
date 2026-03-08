/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class GameSequenceSettings
implements IFDSObject<FDSTagCompound> {
    private boolean stopMusicOnFinish = false;

    public void setStopMusicOnFinish(boolean stopMusicOnFinish) {
        this.stopMusicOnFinish = stopMusicOnFinish;
    }

    public boolean getStopMusicOnFinish() {
        return this.stopMusicOnFinish;
    }

    @NotNull
    public static GameSequenceSettings readNewWithRegistry(@NotNull RegistryFriendlyByteBuf buf) {
        try {
            GameSequenceSettings gameSequenceSettings = new GameSequenceSettings();
            gameSequenceSettings.readWithRegistry(buf);
            return gameSequenceSettings;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        this.stopMusicOnFinish = root.getBoolean("stopMusicOnFinish");
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        root.setBoolean("stopMusicOnFinish", this.stopMusicOnFinish);
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        this.stopMusicOnFinish = IPacket.readBoolean((ByteBuf)buf);
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeBoolean((ByteBuf)buf, (boolean)this.stopMusicOnFinish);
    }

    public void readWithRegistry(@NotNull RegistryFriendlyByteBuf buf) throws IOException {
        this.stopMusicOnFinish = buf.readBoolean();
    }

    public void writeWithRegistry(@NotNull RegistryFriendlyByteBuf buf) throws IOException {
        buf.writeBoolean(this.stopMusicOnFinish);
    }
}

