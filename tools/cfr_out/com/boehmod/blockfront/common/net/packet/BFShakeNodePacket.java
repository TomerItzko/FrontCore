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
import com.boehmod.blockfront.util.math.ShakeNodeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFShakeNodePacket(@NotNull ShakeNodeData node) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFShakeNodePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_shake_node"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFShakeNodePacket> CODEC = CustomPacketPayload.codec(BFShakeNodePacket::write, BFShakeNodePacket::new);

    public BFShakeNodePacket(@NotNull FriendlyByteBuf buf) {
        this(ShakeNodeData.readBuf(buf));
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        this.node.writeBuf(buf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4430(BFShakeNodePacket bFShakeNodePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.shakeNode(bFShakeNodePacket, iPayloadContext);
    }
}

