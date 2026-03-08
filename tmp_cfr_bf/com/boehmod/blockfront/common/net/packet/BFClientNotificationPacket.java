/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.gui.BFNotificationType;
import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFClientNotificationPacket(@NotNull Component title, @NotNull Component subMessage, @NotNull BFNotificationType toastType) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFClientNotificationPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_client_notification"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFClientNotificationPacket> CODEC = CustomPacketPayload.codec(BFClientNotificationPacket::method_4044, BFClientNotificationPacket::new);

    public BFClientNotificationPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((Component)ComponentSerialization.STREAM_CODEC.decode((Object)registryFriendlyByteBuf), (Component)ComponentSerialization.STREAM_CODEC.decode((Object)registryFriendlyByteBuf), (BFNotificationType)registryFriendlyByteBuf.readEnum(BFNotificationType.class));
    }

    public void method_4044(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        ComponentSerialization.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, (Object)this.title);
        ComponentSerialization.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, (Object)this.subMessage);
        registryFriendlyByteBuf.writeEnum((Enum)this.toastType);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4043(BFClientNotificationPacket bFClientNotificationPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.clientNotification(bFClientNotificationPacket, iPayloadContext);
    }
}

