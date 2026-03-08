/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import com.boehmod.blockfront.util.BFRes;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFCapturePointSpawnPacket(int capturePoint) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFCapturePointSpawnPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_capture_point_spawn"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFCapturePointSpawnPacket> CODEC = CustomPacketPayload.codec(BFCapturePointSpawnPacket::method_4345, BFCapturePointSpawnPacket::new);

    public BFCapturePointSpawnPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt());
    }

    public void method_4345(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.capturePoint);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4343(BFCapturePointSpawnPacket bFCapturePointSpawnPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        UUID uUID = serverPlayer.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame instanceof ConquestGame) {
            ConquestGame conquestGame = (ConquestGame)abstractGame;
            ((ConquestPlayerManager)conquestGame.getPlayerManager()).method_3304(serverPlayer, bFCapturePointSpawnPacket.capturePoint);
        }
    }
}

