/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaterniondc
 *  org.joml.Vector3dc
 */
package com.boehmod.blockfront.client.corpse.physics;

import com.boehmod.blockfront.client.corpse.physics.CorpsePartPhysics;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;

public record c(CorpsePartPhysics other, Quaterniondc rotationA, Quaterniondc rotationB, Vector3dc pivotA, Vector3dc pivotB, Vector3dc rotationLimits) {
}

