/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.blockfront.util.math.IBox3d;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Box3d
implements IBox3d {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public Box3d(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.fixAndSetPoints(x1, y1, z1, x2, y2, z2);
    }

    public Box3d(@NotNull IBox3d other) {
        this.fixAndSetOther(other);
    }

    public Box3d(@NotNull AABB aabb) {
        this.fixAndSetPoints(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public Box3d(@NotNull Vec3 v1, @NotNull Vec3 v2) {
        this.fixAndSetPoints(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    public void fixAndSetOther(@NotNull IBox3d other) {
        this.fixAndSetPoints(other.minX(), other.minY(), other.minZ(), other.maxX(), other.maxY(), other.maxZ());
    }

    public void fixAndSetPoints(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public void setPoints(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void setOther(@NotNull IBox3d other) {
        this.minX = other.minX();
        this.minY = other.minY();
        this.minZ = other.minZ();
        this.maxX = other.maxX();
        this.maxY = other.maxY();
        this.maxZ = other.maxZ();
    }

    @Override
    public double minX() {
        return this.minX;
    }

    @Override
    public double minY() {
        return this.minY;
    }

    @Override
    public double minZ() {
        return this.minZ;
    }

    @Override
    public double maxX() {
        return this.maxX;
    }

    @Override
    public double maxY() {
        return this.maxY;
    }

    @Override
    public double maxZ() {
        return this.maxZ;
    }
}

