/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class BFGameMapVoteInfoPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFGameMapVoteInfoPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_map_vote_info"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFGameMapVoteInfoPacket> CODEC = CustomPacketPayload.codec(BFGameMapVoteInfoPacket::method_5541, BFGameMapVoteInfoPacket::new);
    private ByteBuf field_6411;

    public BFGameMapVoteInfoPacket(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        ByteBuf byteBuf = Unpooled.buffer();
        try {
            abstractGame.getMapVoteManager().write(byteBuf);
            this.field_6411 = byteBuf;
        }
        catch (IOException iOException) {
            BFLog.logError("Failed to write map vote manager data to packet", iOException);
            this.field_6411 = Unpooled.EMPTY_BUFFER;
        }
    }

    public BFGameMapVoteInfoPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        byte[] byArray = friendlyByteBuf.readByteArray();
        this.field_6411 = Unpooled.wrappedBuffer((byte[])byArray);
    }

    public void method_5541(@NotNull FriendlyByteBuf friendlyByteBuf) {
        int n = this.field_6411.readableBytes();
        byte[] byArray = new byte[n];
        this.field_6411.getBytes(this.field_6411.readerIndex(), byArray);
        friendlyByteBuf.writeByteArray(byArray);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_5539(BFGameMapVoteInfoPacket bFGameMapVoteInfoPacket, @NotNull IPayloadContext iPayloadContext) {
        try {
            BFClientPacketHandlers.gameMapVoteInfo(bFGameMapVoteInfoPacket, iPayloadContext);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    public ByteBuf method_5538() {
        return this.field_6411;
    }

    public void method_5540() {
        if (this.field_6411 != null && this.field_6411.refCnt() > 0) {
            this.field_6411.release();
        }
    }
}

