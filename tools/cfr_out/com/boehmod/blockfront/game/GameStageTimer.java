/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class GameStageTimer {
    private final int startSeconds;
    private final int startMaxSeconds;
    private int secondsRemaining;
    private int maxSeconds;
    private int warningThreshold = 0;
    private boolean playWarningSounds = false;
    private boolean nextSoundIsTock = false;

    public GameStageTimer() {
        this.secondsRemaining = 0;
        this.maxSeconds = 0;
        this.startSeconds = 0;
        this.startMaxSeconds = 0;
    }

    public GameStageTimer(int minutes, int seconds) {
        this.maxSeconds = 60 * minutes;
        this.maxSeconds += seconds;
        this.secondsRemaining = this.maxSeconds;
        this.startSeconds = this.maxSeconds;
        this.startMaxSeconds = this.maxSeconds;
    }

    public GameStageTimer warningTime(int seconds) {
        this.warningThreshold = seconds;
        this.playWarningSounds = true;
        return this;
    }

    public void restart() {
        this.secondsRemaining = this.startSeconds;
        this.maxSeconds = this.startMaxSeconds;
    }

    public void update(@NotNull Set<UUID> players) {
        if (this.secondsRemaining >= 0) {
            --this.secondsRemaining;
        }
        if (this.playWarningSounds && this.secondsRemaining <= this.warningThreshold) {
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder = this.nextSoundIsTock ? BFSounds.MATCH_TOCK : BFSounds.MATCH_TICK;
            float f = 0.9f + ThreadLocalRandom.current().nextFloat() * 0.2f;
            BFUtils.playSound(players, (SoundEvent)deferredHolder.get(), SoundSource.MASTER, 1.0f, f);
            this.nextSoundIsTock = !this.nextSoundIsTock;
        }
    }

    public void writeToFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound("timer");
        fDSTagCompound.setInteger("tc", this.secondsRemaining);
        fDSTagCompound.setInteger("tm", this.maxSeconds);
        fDSTagCompound.setBoolean("pt", this.playWarningSounds);
        fDSTagCompound.setInteger("stt", this.warningThreshold);
        root.setTagCompound("timer", fDSTagCompound);
    }

    public void readFromFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = root.getTagCompound("timer");
        if (fDSTagCompound != null) {
            this.secondsRemaining = fDSTagCompound.getInteger("tc");
            this.maxSeconds = fDSTagCompound.getInteger("tm");
            this.playWarningSounds = fDSTagCompound.getBoolean("pt");
            this.warningThreshold = fDSTagCompound.getInteger("stt");
        }
    }

    public MutableComponent getComponent() {
        return Component.literal((String)BFRendering.formatTime(this.getSecondsRemaining())).withColor(this.secondsRemaining <= this.warningThreshold ? 16765813 : 0xFFFFFF);
    }

    public void setSecondsRemaining(int seconds) {
        this.secondsRemaining = seconds;
    }

    public boolean isRunning() {
        return this.secondsRemaining > 0;
    }

    public boolean isDone() {
        return this.secondsRemaining <= 0;
    }

    public int secondsPassed() {
        return this.maxSeconds - this.secondsRemaining;
    }

    public int getSecondsRemaining() {
        return this.secondsRemaining;
    }

    public double normalizedTime() {
        return (double)this.secondsRemaining / (double)this.maxSeconds;
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        this.secondsRemaining = buf.readInt();
        this.maxSeconds = buf.readInt();
        this.playWarningSounds = IPacket.readBoolean((ByteBuf)buf);
        this.warningThreshold = buf.readInt();
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        buf.writeInt(this.secondsRemaining);
        buf.writeInt(this.maxSeconds);
        IPacket.writeBoolean((ByteBuf)buf, (boolean)this.playWarningSounds);
        buf.writeInt(this.warningThreshold);
    }
}

