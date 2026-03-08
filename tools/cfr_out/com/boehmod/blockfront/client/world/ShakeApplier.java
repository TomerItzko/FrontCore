/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.world;

import com.boehmod.blockfront.util.math.ShakeNodeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ShakeApplier {
    @NotNull
    private final ShakeNodeData data;
    private float pitchSine;
    private float yawSine;
    private float rollSine;
    private float currentLerp;
    private float idleTimer = 0.0f;
    private float amplitude = 1.0f;

    public ShakeApplier(@NotNull ShakeNodeData data) {
        this.data = data;
    }

    @NotNull
    public ShakeApplier setAmplitude(float amplitude) {
        this.amplitude = amplitude;
        return this;
    }

    public boolean update(float t) {
        this.updateLerps();
        this.setSineValues(t);
        this.idleTimer += 1.0f;
        return this.idleTimer >= (float)this.data.idleTime() && this.currentLerp <= 0.01f;
    }

    private void updateLerps() {
        if (this.idleTimer < (float)this.data.idleTime()) {
            this.currentLerp = Mth.lerp((float)this.data.easeInLerp(), (float)this.currentLerp, (float)1.0f);
        } else {
            if (this.data.idleTime() == 0 && this.idleTimer == 0.0f) {
                this.currentLerp = 1.0f;
            }
            this.currentLerp = Mth.lerp((float)this.data.easeOutLerp(), (float)this.currentLerp, (float)0.0f);
        }
        this.currentLerp = Mth.clamp((float)this.currentLerp, (float)0.0f, (float)1.0f);
    }

    private void setSineValues(float t) {
        this.pitchSine = (float)Math.sin(t * this.data.pitchFrequency());
        this.yawSine = (float)Math.sin(t * this.data.yawFrequency());
        this.rollSine = (float)Math.sin(t * this.data.rollFrequency());
    }

    @NotNull
    public Vector3f getCurrentShake() {
        return new Vector3f(this.pitchSine, this.yawSine, this.rollSine).mul(this.data.pitchAmplitude(), this.data.yawAmplitude(), this.data.rollAmplitude()).mul(this.currentLerp * this.amplitude);
    }

    @NotNull
    public static ShakeApplier readBuf(@NotNull FriendlyByteBuf buf) {
        ShakeNodeData shakeNodeData = ShakeNodeData.readBuf(buf);
        ShakeApplier shakeApplier = new ShakeApplier(shakeNodeData);
        shakeApplier.setAmplitude(buf.readFloat());
        return shakeApplier;
    }

    public void writeBuf(@NotNull FriendlyByteBuf buf) {
        this.data.writeBuf(buf);
        buf.writeFloat(this.amplitude);
    }
}

