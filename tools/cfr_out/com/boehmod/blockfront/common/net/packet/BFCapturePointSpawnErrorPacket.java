/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentSerialization
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFCapturePointSpawnErrorPacket(Component message) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFCapturePointSpawnErrorPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_capture_point_spawn_error"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFCapturePointSpawnErrorPacket> CODEC = CustomPacketPayload.codec(BFCapturePointSpawnErrorPacket::method_4313, BFCapturePointSpawnErrorPacket::new);

    public BFCapturePointSpawnErrorPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((Component)ComponentSerialization.STREAM_CODEC.decode((Object)registryFriendlyByteBuf));
    }

    public void method_4313(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        ComponentSerialization.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, (Object)this.message);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4312(BFCapturePointSpawnErrorPacket bFCapturePointSpawnErrorPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.capturePointSpawnError(bFCapturePointSpawnErrorPacket, iPayloadContext);
    }
}

