/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.blockfront.unnamed.BF_14;
import com.boehmod.blockfront.util.math.Box3d;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public interface IBox3d {
    default public boolean intersects(@NotNull IBox3d other) {
        return this.intersects(other.minX(), other.minY(), other.minZ(), other.maxX(), other.maxY(), other.maxZ());
    }

    default public boolean intersects(@NotNull AABB aabb) {
        return this.intersects(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    default public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.maxX() >= minX && this.maxY() >= minY && this.maxZ() >= minZ && this.minX() <= maxX && this.minY() <= maxY && this.minZ() <= maxZ;
    }

    default public boolean within(@NotNull Vector3dc vec) {
        return this.within(vec.x(), vec.y(), vec.z());
    }

    default public boolean within(double x, double y, double z) {
        return x >= this.minX() && x <= this.maxX() && y >= this.minY() && y <= this.maxY() && z >= this.minZ() && z <= this.maxZ();
    }

    public double minX();

    public double minY();

    public double minZ();

    public double maxX();

    public double maxY();

    public double maxZ();

    @NotNull
    default public Box3d setAndExtend(@NotNull Vector3dc vec, @NotNull Box3d other) {
        return this.setAndExtend(vec.x(), vec.y(), vec.z(), other);
    }

    @NotNull
    default public Box3d setAndExtend(double x, double y, double z, @NotNull Box3d other) {
        other.setOther(this);
        other.maxX = Math.max(other.maxX, x);
        other.maxY = Math.max(other.maxY, y);
        other.maxZ = Math.max(other.maxZ, z);
        other.minX = Math.min(other.minX, x);
        other.minY = Math.min(other.minY, y);
        other.minZ = Math.min(other.minZ, z);
        return other;
    }

    @NotNull
    default public Box3d setAndEncase(@NotNull IBox3d box1, @NotNull Box3d box2) {
        box2.setOther(this);
        box2.maxX = Math.max(box2.maxX, box1.maxX());
        box2.maxY = Math.max(box2.maxY, box1.maxY());
        box2.maxZ = Math.max(box2.maxZ, box1.maxZ());
        box2.minX = Math.min(box2.minX, box1.minX());
        box2.minY = Math.min(box2.minY, box1.minY());
        box2.minZ = Math.min(box2.minZ, box1.minZ());
        return box2;
    }

    @NotNull
    default public Box3d expand(double amount, @NotNull Box3d other) {
        other.setPoints(this.minX() - amount, this.minY() - amount, this.minZ() - amount, this.maxX() + amount, this.maxY() + amount, this.maxZ() + amount);
        return other;
    }

    @NotNull
    default public Box3d method_63(@NotNull BF_14 bF_14, @NotNull Box3d box3d) {
        Matrix4d matrix4d = bF_14.method_111(new Matrix4d());
        Vector3d[] vector3dArray = new Vector3d[]{new Vector3d(this.minX(), this.minY(), this.minZ()), new Vector3d(this.minX(), this.minY(), this.maxZ()), new Vector3d(this.minX(), this.maxY(), this.minZ()), new Vector3d(this.minX(), this.maxY(), this.maxZ()), new Vector3d(this.maxX(), this.minY(), this.minZ()), new Vector3d(this.maxX(), this.minY(), this.maxZ()), new Vector3d(this.maxX(), this.maxY(), this.minZ()), new Vector3d(this.maxX(), this.maxY(), this.maxZ())};
        box3d.setPoints(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        Vector3d vector3d = new Vector3d();
        for (Vector3d vector3d2 : vector3dArray) {
            matrix4d.transformPosition((Vector3dc)vector3d2, vector3d);
            box3d.setAndExtend((Vector3dc)vector3d, box3d);
        }
        return box3d;
    }

    @NotNull
    default public Vector3d center(@NotNull Vector3d vec) {
        return vec.set((this.minX() + this.maxX()) / 2.0, (this.minY() + this.maxY()) / 2.0, (this.minZ() + this.maxZ()) / 2.0);
    }

    @NotNull
    default public Vector3d size(@NotNull Vector3d vec) {
        return vec.set(this.maxX() - this.minX(), this.maxY() - this.minY(), this.maxZ() - this.minZ());
    }

    default public double diagonalLen() {
        return (this.maxX() - this.minX()) * (this.maxY() - this.minY()) * (this.maxZ() - this.minZ());
    }

    @Contract(value=" -> new")
    default public AABB asAABB() {
        return new AABB(this.minX(), this.minY(), this.minZ(), this.maxX(), this.maxY(), this.maxZ());
    }
}

