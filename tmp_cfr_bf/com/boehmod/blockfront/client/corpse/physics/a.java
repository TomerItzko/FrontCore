/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.corpse.physics;

import com.boehmod.blockfront.client.corpse.physics.RustCorpsePhysics;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3dc;

public record a(int handle) {
    public void method_782(@NotNull Vector3dc vector3dc, @NotNull Vector3dc vector3dc2) {
        RustCorpsePhysics.addBox(this.handle, new double[]{vector3dc.x(), vector3dc.y(), vector3dc.z(), vector3dc2.x(), vector3dc2.y(), vector3dc2.z()});
    }
}

