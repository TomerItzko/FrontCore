/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.math;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public record ShakeNodeData(float pitchAmplitude, float yawAmplitude, float rollAmplitude, float pitchFrequency, float yawFrequency, float rollFrequency, float easeInLerp, float easeOutLerp, int idleTime) {
    public void writeBuf(@NotNull FriendlyByteBuf buf) {
        buf.writeFloat(this.pitchAmplitude);
        buf.writeFloat(this.yawAmplitude);
        buf.writeFloat(this.rollAmplitude);
        buf.writeFloat(this.pitchFrequency);
        buf.writeFloat(this.yawFrequency);
        buf.writeFloat(this.rollFrequency);
        buf.writeFloat(this.easeInLerp);
        buf.writeFloat(this.easeOutLerp);
        buf.writeVarInt(this.idleTime);
    }

    public static ShakeNodeData readBuf(@NotNull FriendlyByteBuf buf) {
        return new ShakeNodeData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readVarInt());
    }
}

