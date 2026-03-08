/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun.bullet;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record LivingBulletCollision(int entityId, float damageReduction, boolean headShot, boolean collateral, boolean noScope, boolean backpack, @NotNull Direction direction, @NotNull Vec3 hitVec) {
    public static LivingBulletCollision readBuf(@NotNull FriendlyByteBuf buf) {
        return new LivingBulletCollision(buf.readInt(), buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), (Direction)buf.readEnum(Direction.class), buf.readVec3());
    }

    public void writeBuf(@NotNull FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeFloat(this.damageReduction);
        buf.writeBoolean(this.headShot);
        buf.writeBoolean(this.collateral);
        buf.writeBoolean(this.noScope);
        buf.writeBoolean(this.backpack);
        buf.writeEnum((Enum)this.direction);
        buf.writeVec3(this.hitVec);
    }
}

