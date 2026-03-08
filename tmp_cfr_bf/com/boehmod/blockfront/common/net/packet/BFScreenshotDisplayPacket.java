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

public record BFScreenshotDisplayPacket(byte[] bytes) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<BFScreenshotDisplayPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_screenshot_display"));
    public static final StreamCodec<FriendlyByteBuf, BFScreenshotDisplayPacket> CODEC = CustomPacketPayload.codec(BFScreenshotDisplayPacket::method_1591, BFScreenshotDisplayPacket::new);

    public BFScreenshotDisplayPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readByteArray());
    }

    public void method_1591(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeByteArray(this.bytes);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_1589(BFScreenshotDisplayPacket bFScreenshotDisplayPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.screenshotDisplay(bFScreenshotDisplayPacket, iPayloadContext);
    }
}

