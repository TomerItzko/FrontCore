/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFSequencePositionUpdatedPacket() implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFSequencePositionUpdatedPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_sequence_position_updated"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFSequencePositionUpdatedPacket> CODEC = CustomPacketPayload.codec(BFSequencePositionUpdatedPacket::method_4428, BFSequencePositionUpdatedPacket::new);

    public BFSequencePositionUpdatedPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this();
    }

    public void method_4428(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4427(BFSequencePositionUpdatedPacket bFSequencePositionUpdatedPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.sequencePositionUpdated(bFSequencePositionUpdatedPacket, iPayloadContext);
    }
}

