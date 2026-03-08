/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.BFScreenshotRequester;
import com.boehmod.blockfront.server.ac.BFServerAntiCheat;
import com.boehmod.blockfront.server.ac.BFServerScreenshotManager;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFScreenshotResponsePacket(byte[] bytes) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<BFScreenshotResponsePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_screenshot_response"));
    public static final StreamCodec<FriendlyByteBuf, BFScreenshotResponsePacket> CODEC = CustomPacketPayload.codec(BFScreenshotResponsePacket::method_1596, BFScreenshotResponsePacket::new);

    public BFScreenshotResponsePacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readByteArray());
    }

    public void method_1596(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeByteArray(this.bytes);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_1594(BFScreenshotResponsePacket bFScreenshotResponsePacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        String string = serverPlayer.getScoreboardName();
        BFLog.log("[Screenshot] Received screenshot data from player '%s'.", string);
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (!(bFAbstractManager instanceof BFServerManager)) {
            BFLog.logError("[Screenshot] Failed to receive screenshot data from player '%s' - Server Manager is null.", string);
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
        BFServerAntiCheat bFServerAntiCheat = (BFServerAntiCheat)bFServerManager.getAntiCheat();
        BFServerScreenshotManager bFServerScreenshotManager = (BFServerScreenshotManager)bFServerAntiCheat.getScreenshotManager();
        BFScreenshotRequester bFScreenshotRequester = bFServerScreenshotManager.enqueue(serverPlayer);
        if (bFScreenshotRequester != null) {
            bFScreenshotRequester.method_4592(bFScreenshotResponsePacket.bytes());
        }
    }
}

