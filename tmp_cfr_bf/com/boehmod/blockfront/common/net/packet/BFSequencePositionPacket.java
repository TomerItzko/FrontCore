/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.FDSPose;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFSequencePositionPacket(FDSPose blockLocation) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFSequencePositionPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_sequence_position"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFSequencePositionPacket> CODEC = CustomPacketPayload.codec(BFSequencePositionPacket::method_4425, BFSequencePositionPacket::new);

    public BFSequencePositionPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(FDSPose.readBuf(friendlyByteBuf));
    }

    public void method_4425(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.blockLocation.writeBuf(friendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4424(BFSequencePositionPacket bFSequencePositionPacket, @NotNull IPayloadContext iPayloadContext) {
        BFServerPlayerData bFServerPlayerData;
        ServerPlayer serverPlayer;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        Object d = ((PlayerDataHandler)obj).getPlayerData((Player)(serverPlayer = (ServerPlayer)iPayloadContext.player()));
        if (d instanceof BFServerPlayerData && (bFServerPlayerData = (BFServerPlayerData)d).getSequenceManager().method_5452(bFSequencePositionPacket.blockLocation())) {
            bFServerPlayerData.getSequenceManager().getChunkLoader().loadAroundPosition((ServerLevel)serverPlayer.level(), serverPlayer, bFSequencePositionPacket.blockLocation());
        }
    }
}

