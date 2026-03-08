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

public record BFClearRagdollsPacket() implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFClearRagdollsPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_clear_ragdolls"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFClearRagdollsPacket> CODEC = CustomPacketPayload.codec(BFClearRagdollsPacket::method_4041, BFClearRagdollsPacket::new);

    public BFClearRagdollsPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this();
    }

    public void method_4041(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4040(BFClearRagdollsPacket bFClearRagdollsPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.clearRagdolls(bFClearRagdollsPacket, iPayloadContext);
    }
}

