/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.fds.IFDSObject
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.IFDSObject;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.camera.CutscenePoint;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class GameSequence
implements IFDSObject<FDSTagCompound> {
    private final ObjectList<CutscenePoint> cutscenePoints = new ObjectArrayList();
    private boolean loop = false;
    private boolean showUI = false;
    private boolean allowSkip = false;
    private boolean fadeWhenFinished = false;

    public static Builder builder() {
        return new Builder();
    }

    public List<CutscenePoint> method_2185() {
        return this.cutscenePoints;
    }

    public boolean method_2177() {
        return this.showUI;
    }

    public boolean method_2178() {
        return this.loop;
    }

    public boolean method_2179() {
        return this.allowSkip;
    }

    public boolean method_2180() {
        return this.fadeWhenFinished;
    }

    @NotNull
    public static GameSequence method_2182(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        try {
            GameSequence gameSequence = new GameSequence();
            gameSequence.method_2183(registryFriendlyByteBuf);
            return gameSequence;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        int n = root.getInteger("shotsCount");
        for (int i = 0; i < n; ++i) {
            FDSTagCompound fDSTagCompound = root.getTagCompound("shot" + i);
            if (fDSTagCompound == null) continue;
            CutscenePoint cutscenePoint = new CutscenePoint();
            cutscenePoint.readFromFDS(fDSTagCompound);
            this.cutscenePoints.add((Object)cutscenePoint);
        }
        this.loop = root.getBoolean("loop");
        this.showUI = root.getBoolean("showUI");
        this.allowSkip = root.getBoolean("allowSkip");
        this.fadeWhenFinished = root.getBoolean("fadeWhenFinished");
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        int n = this.cutscenePoints.size();
        root.setInteger("shotsCount", n);
        for (int i = 0; i < n; ++i) {
            CutscenePoint cutscenePoint = (CutscenePoint)this.cutscenePoints.get(i);
            FDSTagCompound fDSTagCompound = new FDSTagCompound();
            cutscenePoint.writeToFDS(fDSTagCompound);
            root.setTagCompound("shot" + i, fDSTagCompound);
        }
        root.setBoolean("loop", this.loop);
        root.setBoolean("showUI", this.showUI);
        root.setBoolean("allowSkip", this.allowSkip);
        root.setBoolean("fadeWhenFinished", this.fadeWhenFinished);
    }

    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            CutscenePoint cutscenePoint = new CutscenePoint();
            cutscenePoint.read(byteBuf);
            this.cutscenePoints.add((Object)cutscenePoint);
        }
        this.loop = IPacket.readBoolean((ByteBuf)byteBuf);
        this.showUI = IPacket.readBoolean((ByteBuf)byteBuf);
        this.allowSkip = IPacket.readBoolean((ByteBuf)byteBuf);
        this.fadeWhenFinished = IPacket.readBoolean((ByteBuf)byteBuf);
    }

    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        byteBuf.writeInt(this.cutscenePoints.size());
        for (CutscenePoint cutscenePoint : this.cutscenePoints) {
            cutscenePoint.write(byteBuf);
        }
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.loop);
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.showUI);
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.allowSkip);
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.fadeWhenFinished);
    }

    public void method_2183(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) throws IOException {
        int n = registryFriendlyByteBuf.readVarInt();
        for (int i = 0; i < n; ++i) {
            CutscenePoint cutscenePoint = new CutscenePoint();
            cutscenePoint.readWithRegistry(registryFriendlyByteBuf);
            this.cutscenePoints.add((Object)cutscenePoint);
        }
        this.loop = registryFriendlyByteBuf.readBoolean();
        this.showUI = registryFriendlyByteBuf.readBoolean();
        this.allowSkip = registryFriendlyByteBuf.readBoolean();
        this.fadeWhenFinished = registryFriendlyByteBuf.readBoolean();
    }

    public void method_2184(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) throws IOException {
        registryFriendlyByteBuf.writeVarInt(this.cutscenePoints.size());
        for (CutscenePoint cutscenePoint : this.cutscenePoints) {
            cutscenePoint.writeWithRegistry(registryFriendlyByteBuf);
        }
        registryFriendlyByteBuf.writeBoolean(this.loop);
        registryFriendlyByteBuf.writeBoolean(this.showUI);
        registryFriendlyByteBuf.writeBoolean(this.allowSkip);
        registryFriendlyByteBuf.writeBoolean(this.fadeWhenFinished);
    }

    public static class Builder {
        private final GameSequence sequence = new GameSequence();

        public Builder cutscenePoint(CutscenePoint cutscenePoint) {
            this.sequence.cutscenePoints.add((Object)cutscenePoint);
            return this;
        }

        public Builder showUI(boolean showUI) {
            this.sequence.showUI = showUI;
            return this;
        }

        public Builder allowSkip(boolean allowSkip) {
            this.sequence.allowSkip = allowSkip;
            return this;
        }

        public Builder loop(boolean loop) {
            this.sequence.loop = loop;
            return this;
        }

        public Builder fadeWhenFinished(boolean fadeWhenFinished) {
            this.sequence.fadeWhenFinished = fadeWhenFinished;
            return this;
        }

        public GameSequence build() {
            return this.sequence;
        }
    }
}

