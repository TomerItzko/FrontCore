/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFHitMarkerPacket(boolean isKill) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFHitMarkerPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_hit_marker"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFHitMarkerPacket> CODEC = CustomPacketPayload.codec(BFHitMarkerPacket::method_4386, BFHitMarkerPacket::new);

    public BFHitMarkerPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readBoolean());
    }

    public void method_4386(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.isKill);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4384(BFHitMarkerPacket bFHitMarkerPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.hitMarker(bFHitMarkerPacket, iPayloadContext);
    }
}

