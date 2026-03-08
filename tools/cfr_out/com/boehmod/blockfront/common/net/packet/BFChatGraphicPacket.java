/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.ChatGraphic
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

import com.boehmod.bflib.cloud.common.ChatGraphic;
import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFChatGraphicPacket(@NotNull ChatGraphic graphic, @NotNull Component component) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFChatGraphicPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_chat_graphic"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFChatGraphicPacket> CODEC = CustomPacketPayload.codec(BFChatGraphicPacket::method_4038, BFChatGraphicPacket::new);

    public BFChatGraphicPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((ChatGraphic)registryFriendlyByteBuf.readEnum(ChatGraphic.class), (Component)ComponentSerialization.STREAM_CODEC.decode((Object)registryFriendlyByteBuf));
    }

    public void method_4038(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeEnum((Enum)this.graphic);
        ComponentSerialization.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, (Object)this.component);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4037(BFChatGraphicPacket bFChatGraphicPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.chatGraphic(bFChatGraphicPacket, iPayloadContext);
    }
}

