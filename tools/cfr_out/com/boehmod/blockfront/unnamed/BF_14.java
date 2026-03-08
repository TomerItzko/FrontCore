/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4d
 *  org.joml.Quaterniond
 *  org.joml.Quaterniondc
 *  org.joml.Vector3d
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_13;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public interface BF_14 {
    @NotNull
    public Vector3dc getPosition();

    @NotNull
    public Quaterniondc method_109();

    @NotNull
    public Vector3dc method_115();

    @NotNull
    public Vector3dc method_113();

    @NotNull
    default public Vector3dc method_112(@NotNull Vector3dc vector3dc, @NotNull Vector3d vector3d) {
        return this.method_109().transform(vector3dc.sub(this.method_115(), vector3d).mul(this.method_113())).add(this.getPosition());
    }

    @NotNull
    default public Vector3dc method_114(@NotNull Vector3dc vector3dc, @NotNull Vector3d vector3d) {
        Vector3dc vector3dc2 = this.method_113();
        return this.method_109().transformInverse(vector3dc.sub(this.getPosition(), vector3d)).mul(1.0 / vector3dc2.x(), 1.0 / vector3dc2.y(), 1.0 / vector3dc2.z()).add(this.method_115());
    }

    @NotNull
    default public Vector3dc method_116(@NotNull Vector3dc vector3dc, @NotNull Vector3d vector3d) {
        return this.method_109().transform(vector3dc.mul(this.method_113(), vector3d));
    }

    @NotNull
    default public Vector3dc method_118(@NotNull Vector3dc vector3dc, @NotNull Vector3d vector3d) {
        Vector3dc vector3dc2 = this.method_113();
        return this.method_109().transformInverse(vector3dc, vector3d).mul(1.0 / vector3dc2.x(), 1.0 / vector3dc2.y(), 1.0 / vector3dc2.z());
    }

    @NotNull
    default public Matrix4d method_111(@NotNull Matrix4d matrix4d) {
        return matrix4d.identity().translate(this.getPosition()).rotate(this.method_109()).scale(this.method_113()).translate(-this.method_115().x(), -this.method_115().y(), -this.method_115().z());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    default public boolean method_110(@NotNull BF_13 bF_13, double d, double d2) {
        if (!(this.getPosition().distanceSquared((Vector3dc)bF_13.getPosition()) <= d * d)) return false;
        Quaterniond quaterniond = new Quaterniond();
        if (!(this.method_109().div((Quaterniondc)bF_13.method_109(), quaterniond).angle() <= d2)) return false;
        return true;
    }
}

