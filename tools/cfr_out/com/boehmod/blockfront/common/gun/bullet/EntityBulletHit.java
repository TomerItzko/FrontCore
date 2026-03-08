/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.gun.bullet;

import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record EntityBulletHit(@NotNull EntityCollisionEntry entitySnapshot, @NotNull Vec3 intersection, double distanceSqr) {
}

