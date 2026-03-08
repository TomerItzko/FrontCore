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

public record BFVictoryPacket(@NotNull Component subtitle, boolean victory) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFVictoryPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_victory"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFVictoryPacket> CODEC = CustomPacketPayload.codec(BFVictoryPacket::method_4285, BFVictoryPacket::new);

    public BFVictoryPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((Component)ComponentSerialization.STREAM_CODEC.decode((Object)registryFriendlyByteBuf), registryFriendlyByteBuf.readBoolean());
    }

    public void method_4285(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        ComponentSerialization.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, (Object)this.subtitle);
        registryFriendlyByteBuf.writeBoolean(this.victory);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4283(BFVictoryPacket bFVictoryPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.victory(bFVictoryPacket, iPayloadContext);
    }
}

