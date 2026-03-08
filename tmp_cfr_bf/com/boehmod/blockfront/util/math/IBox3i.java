/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.blockfront.util.math.Box3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public interface IBox3i {
    default public boolean method_83(@NotNull IBox3i iBox3i) {
        return this.method_81(iBox3i.minX(), iBox3i.minY(), iBox3i.minZ(), iBox3i.maxX(), iBox3i.maxY(), iBox3i.maxZ());
    }

    default public boolean method_85(@NotNull BoundingBox boundingBox) {
        return this.method_81(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
    }

    default public boolean method_81(int n, int n2, int n3, int n4, int n5, int n6) {
        return this.maxX() >= n && this.maxY() >= n2 && this.maxZ() >= n3 && this.minX() <= n4 && this.minY() <= n5 && this.minZ() <= n6;
    }

    default public boolean method_87(@NotNull Vector3ic vector3ic) {
        return this.method_80(vector3ic.x(), vector3ic.y(), vector3ic.z());
    }

    default public boolean method_80(int n, int n2, int n3) {
        return n >= this.minX() && n <= this.maxX() && n2 >= this.minY() && n2 <= this.maxY() && n3 >= this.minZ() && n3 <= this.maxZ();
    }

    public int minX();

    public int minY();

    public int minZ();

    public int maxX();

    public int maxY();

    public int maxZ();

    default public Box3i method_88(@NotNull Vector3ic vector3ic, @NotNull Box3i box3i) {
        return this.method_82(vector3ic.x(), vector3ic.y(), vector3ic.z(), box3i);
    }

    default public Box3i method_82(int n, int n2, int n3, @NotNull Box3i box3i) {
        box3i.method_77(this);
        box3i.maxX = Math.max(box3i.maxX, n);
        box3i.maxY = Math.max(box3i.maxY, n2);
        box3i.maxZ = Math.max(box3i.maxZ, n3);
        box3i.minX = Math.min(box3i.minX, n);
        box3i.minY = Math.min(box3i.minY, n2);
        box3i.minZ = Math.min(box3i.minZ, n3);
        return box3i;
    }

    default public Box3i method_84(@NotNull IBox3i iBox3i, @NotNull Box3i box3i) {
        box3i.method_77(this);
        box3i.maxX = Math.max(box3i.maxX, iBox3i.maxX());
        box3i.maxY = Math.max(box3i.maxY, iBox3i.maxY());
        box3i.maxZ = Math.max(box3i.maxZ, iBox3i.maxZ());
        box3i.minX = Math.min(box3i.minX, iBox3i.minX());
        box3i.minY = Math.min(box3i.minY, iBox3i.minY());
        box3i.minZ = Math.min(box3i.minZ, iBox3i.minZ());
        return box3i;
    }

    default public Box3i method_91(@NotNull Vector3ic vector3ic, @NotNull Box3i box3i) {
        return this.method_89(vector3ic.x(), vector3ic.y(), vector3ic.z(), box3i);
    }

    default public Box3i method_89(int n, int n2, int n3, @NotNull Box3i box3i) {
        box3i.method_77(this);
        box3i.minX += n;
        box3i.minY += n2;
        box3i.minZ += n3;
        box3i.maxX += n;
        box3i.maxY += n2;
        box3i.maxZ += n3;
        return box3i;
    }

    default public Vector3ic method_86(@NotNull Vector3i vector3i) {
        return vector3i.set((this.minX() + this.maxX()) / 2, (this.minY() + this.maxY()) / 2, (this.minZ() + this.maxZ()) / 2);
    }

    default public Vector3ic method_90(@NotNull Vector3i vector3i) {
        return vector3i.set(this.maxX() - this.minX(), this.maxY() - this.minY(), this.maxZ() - this.minZ());
    }

    default public int method_98() {
        return (this.maxX() - this.minX()) * (this.maxY() - this.minY()) * (this.maxZ() - this.minZ());
    }
}

