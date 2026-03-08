/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.debug.DebugLine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFDebugLinePacket(@NotNull DebugLine debugLine) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFDebugLinePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_debug_line"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFDebugLinePacket> CODEC = CustomPacketPayload.codec(BFDebugLinePacket::method_4057, BFDebugLinePacket::new);

    public BFDebugLinePacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(DebugLine.read(friendlyByteBuf));
    }

    public void method_4057(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.debugLine.write(friendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4056(BFDebugLinePacket bFDebugLinePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.debugLine(bFDebugLinePacket, iPayloadContext);
    }
}

