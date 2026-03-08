/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util.debug;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DebugBox {
    @NotNull
    private final AABB box;
    private final int maxTime;
    private final boolean fade;
    private final int color;
    private int timer;

    public DebugBox(@NotNull AABB box, int maxTime, boolean fade, int color) {
        this.box = box;
        this.maxTime = maxTime;
        this.fade = fade;
        this.timer = maxTime;
        this.color = color;
    }

    @NotNull
    public DebugBox withColor(int color) {
        return new DebugBox(this.box, this.maxTime, this.fade, color);
    }

    public DebugBox(@NotNull Vec3 start, @NotNull Vec3 end, int maxTime, boolean fade, int color) {
        this(new AABB(start.subtract(end), start.add(end)), maxTime, fade, color);
    }

    public static DebugBox read(@NotNull FriendlyByteBuf buf) {
        Vec3 vec3 = buf.readVec3();
        Vec3 vec32 = buf.readVec3();
        AABB aABB = new AABB(vec3, vec32);
        return new DebugBox(aABB, buf.readInt(), buf.readBoolean(), buf.readInt());
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeVec3(new Vec3(this.box.minX, this.box.minY, this.box.minZ));
        buf.writeVec3(new Vec3(this.box.maxX, this.box.maxY, this.box.maxZ));
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
    public AABB getBox() {
        return this.box;
    }

    public int getColor() {
        return this.color;
    }
}

