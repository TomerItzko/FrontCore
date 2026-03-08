/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.corpse.physics;

import com.boehmod.blockfront.client.corpse.physics.RustCorpsePhysics;
import com.boehmod.blockfront.unnamed.BF_13;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

public record CorpsePhysicsBody(int handle) {
    public void removeObject() {
        RustCorpsePhysics.removeObject(this.handle);
    }

    @NotNull
    public BF_13 method_795(@NotNull BF_13 bF_13) {
        double[] dArray = RustCorpsePhysics.getPose(this.handle);
        bF_13.getPosition().set(dArray[0], dArray[1], dArray[2]);
        bF_13.method_109().set(dArray[3], dArray[4], dArray[5], dArray[6]);
        return bF_13;
    }

    public void addLinearAngularVelocities(@NotNull Vector3dc vector3dc, @NotNull Vector3dc vector3dc2) {
        RustCorpsePhysics.addLinearAngularVelocities(this.handle, vector3dc.x(), vector3dc.y(), vector3dc.z(), vector3dc2.x(), vector3dc2.y(), vector3dc2.z());
    }

    public void multiplyVelocities(double d) {
        RustCorpsePhysics.multiplyVelocities(this.handle, d);
    }

    public void addRagdollConstraint(@NotNull CorpsePhysicsBody corpsePhysicsBody, @NotNull Vector3dc vector3dc, @NotNull Vector3dc vector3dc2, @NotNull Quaterniondc quaterniondc, @NotNull Quaterniondc quaterniondc2, @NotNull Vector3dc vector3dc3, double d) {
        RustCorpsePhysics.addRagdollConstraint(this.handle, corpsePhysicsBody.handle, vector3dc.x(), vector3dc.y(), vector3dc.z(), quaterniondc.x(), quaterniondc.y(), quaterniondc.z(), quaterniondc.w(), vector3dc2.x(), vector3dc2.y(), vector3dc2.z(), quaterniondc2.x(), quaterniondc2.y(), quaterniondc2.z(), quaterniondc2.w(), vector3dc3.x(), vector3dc3.y(), vector3dc3.z(), d);
    }
}

