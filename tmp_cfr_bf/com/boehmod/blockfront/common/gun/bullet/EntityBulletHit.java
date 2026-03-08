/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun.bullet;

import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record EntityBulletHit(@NotNull EntityCollisionEntry entitySnapshot, @NotNull Vec3 intersection, double distanceSqr) {
}

