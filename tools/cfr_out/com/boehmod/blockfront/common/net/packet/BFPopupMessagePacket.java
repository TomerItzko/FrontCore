/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFPopupMessagePacket(@NotNull BFPopup gamePopups) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFPopupMessagePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_popup_message"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFPopupMessagePacket> CODEC = CustomPacketPayload.codec(BFPopupMessagePacket::method_4396, BFPopupMessagePacket::new);

    public BFPopupMessagePacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(new BFPopup(registryFriendlyByteBuf));
    }

    public void method_4396(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.gamePopups.method_3811(registryFriendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4395(BFPopupMessagePacket bFPopupMessagePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.popupMessage(bFPopupMessagePacket, iPayloadContext);
    }
}

