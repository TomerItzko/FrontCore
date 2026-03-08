/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.debug.DebugBox;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFDebugBoxPacket(@NotNull DebugBox debugBox) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFDebugBoxPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_debug_box"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFDebugBoxPacket> CODEC = CustomPacketPayload.codec(BFDebugBoxPacket::method_5787, BFDebugBoxPacket::new);

    public BFDebugBoxPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(DebugBox.read(friendlyByteBuf));
    }

    public void method_5787(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.debugBox.write(friendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_5786(BFDebugBoxPacket bFDebugBoxPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.debugBox(bFDebugBoxPacket, iPayloadContext);
    }
}

