/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_14;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class BF_13
implements BF_14 {
    @NotNull
    private final Vector3d position;
    @NotNull
    private final Quaterniond field_75;
    @NotNull
    private final Vector3d field_77;
    @NotNull
    private final Vector3d field_78;

    public BF_13(@NotNull Vector3d vector3d, @NotNull Quaterniond quaterniond, @NotNull Vector3d vector3d2, @NotNull Vector3d vector3d3) {
        this.position = vector3d;
        this.field_75 = quaterniond;
        this.field_77 = vector3d2;
        this.field_78 = vector3d3;
    }

    public BF_13() {
        this.position = new Vector3d();
        this.field_75 = new Quaterniond();
        this.field_77 = new Vector3d();
        this.field_78 = new Vector3d(1.0);
    }

    public BF_13(@NotNull BF_14 bF_14) {
        this.position = new Vector3d(bF_14.getPosition());
        this.field_75 = new Quaterniond(bF_14.method_109());
        this.field_77 = new Vector3d(bF_14.method_115());
        this.field_78 = new Vector3d(bF_14.method_113());
    }

    @NotNull
    public BF_13 method_101(@NotNull BF_14 bF_14) {
        this.position.set(bF_14.getPosition());
        this.field_75.set(bF_14.method_109());
        this.field_77.set(bF_14.method_115());
        this.field_78.set(bF_14.method_113());
        return this;
    }

    @NotNull
    public BF_13 method_102(@NotNull BF_14 bF_14, double d) {
        this.position.lerp(bF_14.getPosition(), d);
        this.field_75.slerp(bF_14.method_109(), d);
        this.field_77.lerp(bF_14.method_115(), d);
        this.field_78.lerp(bF_14.method_113(), d);
        return this;
    }

    @NotNull
    public Vector3dc method_103(@NotNull Vector3d vector3d) {
        return this.method_112((Vector3dc)vector3d, vector3d);
    }

    @NotNull
    public Vector3dc method_105(@NotNull Vector3d vector3d) {
        return this.method_114((Vector3dc)vector3d, vector3d);
    }

    @NotNull
    public Vector3dc method_107(@NotNull Vector3d vector3d) {
        return this.method_116((Vector3dc)vector3d, vector3d);
    }

    @NotNull
    public Vector3dc method_108(@NotNull Vector3d vector3d) {
        return this.method_118((Vector3dc)vector3d, vector3d);
    }

    @NotNull
    public Vector3d getPosition() {
        return this.position;
    }

    @NotNull
    public Quaterniond method_109() {
        return this.field_75;
    }

    @NotNull
    public Vector3d method_115() {
        return this.field_77;
    }

    @NotNull
    public Vector3d method_113() {
        return this.field_78;
    }

    @Override
    @NotNull
    public /* synthetic */ Vector3dc method_113() {
        return this.method_113();
    }

    @Override
    @NotNull
    public /* synthetic */ Vector3dc method_115() {
        return this.method_115();
    }

    @Override
    @NotNull
    public /* synthetic */ Vector3dc getPosition() {
        return this.getPosition();
    }
}

