/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.debug;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DebugLine {
    @NotNull
    private final Vec3 start;
    @NotNull
    private final Vec3 end;
    private final int maxTime;
    private final boolean fade;
    private final int color;
    private int timer;

    public DebugLine(@NotNull Vec3 start, @NotNull Vec3 end, int maxTime, boolean fade, int color) {
        this.start = start;
        this.end = end;
        this.maxTime = maxTime;
        this.fade = fade;
        this.timer = maxTime;
        this.color = color;
    }

    @NotNull
    public DebugLine withColor(int color) {
        return new DebugLine(this.start, this.end, this.maxTime, this.fade, color);
    }

    @NotNull
    public static DebugLine read(@NotNull FriendlyByteBuf buf) {
        return new DebugLine(buf.readVec3(), buf.readVec3(), buf.readInt(), buf.readBoolean(), buf.readInt());
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeVec3(this.start);
        buf.writeVec3(this.end);
        buf.writeInt(this.maxTime);
        buf.writeBoolean(this.fade);
        buf.writeInt(this.color);
    }

    public boolean update() {
        return this.timer-- <= 0;
    }

    public float getAlpha() {
        return this.fade ? (float)this.timer / (float)this.maxTime : 1.0f;
    }

    @NotNull
    public Vec3 getStart() {
        return this.start;
    }

    @NotNull
    public Vec3 getEnd() {
        return this.end;
    }

    public int getColor() {
        return this.color;
    }
}

