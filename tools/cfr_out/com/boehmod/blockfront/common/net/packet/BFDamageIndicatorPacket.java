/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFDamageIndicatorPacket(@NotNull Vec3 position, float alpha) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFDamageIndicatorPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_damage_indicator"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFDamageIndicatorPacket> CODEC = CustomPacketPayload.codec(BFDamageIndicatorPacket::method_4053, BFDamageIndicatorPacket::new);

    public BFDamageIndicatorPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readVec3(), friendlyByteBuf.readFloat());
    }

    public void method_4053(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeVec3(this.position);
        friendlyByteBuf.writeFloat(this.alpha);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4052(BFDamageIndicatorPacket bFDamageIndicatorPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.damageIndicator(bFDamageIndicatorPacket, iPayloadContext);
    }
}

