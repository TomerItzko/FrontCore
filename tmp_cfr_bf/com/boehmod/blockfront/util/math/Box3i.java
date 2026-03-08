/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util.math;

import com.boehmod.blockfront.util.math.IBox3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class Box3i
implements IBox3i {
    public static final IBox3i field_74 = new Box3i().method_78(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public Box3i(int n, int n2, int n3, int n4, int n5, int n6) {
        this.method_76(n, n2, n3, n4, n5, n6);
    }

    public Box3i(@NotNull IBox3i iBox3i) {
        this.method_77(iBox3i);
    }

    public Box3i(@NotNull BoundingBox boundingBox) {
        this.method_76(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ());
    }

    public Box3i() {
        this(0, 0, 0, 0, 0, 0);
    }

    @NotNull
    public Box3i method_77(@NotNull IBox3i iBox3i) {
        this.method_76(iBox3i.minX(), iBox3i.minY(), iBox3i.minZ(), iBox3i.maxX(), iBox3i.maxY(), iBox3i.maxZ());
        return this;
    }

    @NotNull
    public Box3i method_76(int n, int n2, int n3, int n4, int n5, int n6) {
        this.minX = Math.min(n, n4);
        this.minY = Math.min(n2, n5);
        this.minZ = Math.min(n3, n6);
        this.maxX = Math.max(n, n4);
        this.maxY = Math.max(n2, n5);
        this.maxZ = Math.max(n3, n6);
        return this;
    }

    @NotNull
    public Box3i method_78(int n, int n2, int n3, int n4, int n5, int n6) {
        this.minX = n;
        this.minY = n2;
        this.minZ = n3;
        this.maxX = n4;
        this.maxY = n5;
        this.maxZ = n6;
        return this;
    }

    @NotNull
    public Box3i method_79(@NotNull IBox3i iBox3i) {
        this.minX = iBox3i.minX();
        this.minY = iBox3i.minY();
        this.minZ = iBox3i.minZ();
        this.maxX = iBox3i.maxX();
        this.maxY = iBox3i.maxY();
        this.maxZ = iBox3i.maxZ();
        return this;
    }

    @Override
    public int minX() {
        return this.minX;
    }

    @Override
    public int minY() {
        return this.minY;
    }

    @Override
    public int minZ() {
        return this.minZ;
    }

    @Override
    public int maxX() {
        return this.maxX;
    }

    @Override
    public int maxY() {
        return this.maxY;
    }

    @Override
    public int maxZ() {
        return this.maxZ;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof IBox3i)) {
            return false;
        }
        IBox3i iBox3i = (IBox3i)object;
        return this.minX() == iBox3i.minX() && this.minY() == iBox3i.minY() && this.minZ() == iBox3i.minZ() && this.maxX() == iBox3i.maxX() && this.maxY() == iBox3i.maxY() && this.maxZ() == iBox3i.maxZ();
    }
}

