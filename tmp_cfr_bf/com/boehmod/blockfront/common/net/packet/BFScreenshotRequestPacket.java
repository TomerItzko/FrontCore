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

public class BFScreenshotRequestPacket
implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BFScreenshotRequestPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_screenshot_request"));
    public static final StreamCodec<FriendlyByteBuf, BFScreenshotRequestPacket> CODEC = CustomPacketPayload.codec(BFScreenshotRequestPacket::method_1593, BFScreenshotRequestPacket::new);

    public BFScreenshotRequestPacket() {
    }

    public BFScreenshotRequestPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_1593(FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_1592(BFScreenshotRequestPacket bFScreenshotRequestPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.screenshotRequest(bFScreenshotRequestPacket, iPayloadContext);
    }
}

