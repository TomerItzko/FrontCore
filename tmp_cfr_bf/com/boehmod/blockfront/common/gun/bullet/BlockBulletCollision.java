/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun.bullet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record BlockBulletCollision(@NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull Vec3 hitVec) {
    @NotNull
    public static BlockBulletCollision readBuf(@NotNull FriendlyByteBuf buf) {
        return new BlockBulletCollision(buf.readBlockPos(), (Direction)buf.readEnum(Direction.class), buf.readVec3());
    }

    public void writeBuf(@NotNull FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeEnum((Enum)this.direction);
        buf.writeVec3(this.hitVec);
    }
}

