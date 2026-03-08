/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFExplosionPacket(@NotNull ExplosionType explosionType, @NotNull Vec3 position) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFExplosionPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_explosion"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFExplosionPacket> CODEC = CustomPacketPayload.codec(BFExplosionPacket::method_4375, BFExplosionPacket::new);

    public BFExplosionPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((ExplosionType)friendlyByteBuf.readEnum(ExplosionType.class), friendlyByteBuf.readVec3());
    }

    public void method_4375(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum((Enum)this.explosionType);
        friendlyByteBuf.writeVec3(this.position);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4374(BFExplosionPacket bFExplosionPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.explosion(bFExplosionPacket, iPayloadContext);
    }
}

