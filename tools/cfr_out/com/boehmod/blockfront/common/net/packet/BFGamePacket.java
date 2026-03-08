/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  javax.annotation.Nullable
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class BFGamePacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFGamePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFGamePacket> CODEC = CustomPacketPayload.codec(BFGamePacket::method_4320, BFGamePacket::new);
    @Nullable
    private ByteBuf field_4154;
    private final boolean field_4156;
    @Nullable
    private String field_4155 = null;

    public BFGamePacket(@Nullable AbstractGame<?, ?, ?> abstractGame, boolean bl) {
        this.field_4156 = abstractGame != null;
        this.field_4154 = null;
        if (abstractGame != null) {
            this.field_4155 = abstractGame.getType();
            try {
                ByteBuf byteBuf = Unpooled.buffer((int)1024);
                abstractGame.writeAll(byteBuf, bl);
                abstractGame.writeForGamePacket(byteBuf);
                this.field_4154 = byteBuf;
            }
            catch (Exception exception) {
                BFLog.logError("Failed to send game data update to player.", exception);
                this.field_4154 = Unpooled.EMPTY_BUFFER;
            }
        }
    }

    public BFGamePacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.field_4156 = friendlyByteBuf.readBoolean();
        if (this.field_4156) {
            this.field_4155 = friendlyByteBuf.readUtf(Short.MAX_VALUE);
            byte[] byArray = friendlyByteBuf.readByteArray();
            this.field_4154 = Unpooled.wrappedBuffer((byte[])byArray);
        } else {
            this.field_4154 = null;
        }
    }

    public void method_4320(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.field_4156);
        if (this.field_4156) {
            assert (this.field_4155 != null) : "Game type is null!";
            friendlyByteBuf.writeUtf(this.field_4155);
            if (this.field_4154 != null) {
                int n = this.field_4154.readableBytes();
                byte[] byArray = new byte[n];
                this.field_4154.getBytes(this.field_4154.readerIndex(), byArray);
                friendlyByteBuf.writeByteArray(byArray);
            } else {
                friendlyByteBuf.writeByteArray(new byte[0]);
            }
        }
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4317(BFGamePacket bFGamePacket, @NotNull IPayloadContext iPayloadContext) {
        try {
            BFClientPacketHandlers.game(bFGamePacket, iPayloadContext);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    public ByteBuf method_4316() {
        return this.field_4154 != null ? this.field_4154 : Unpooled.EMPTY_BUFFER;
    }

    public boolean method_4319() {
        return this.field_4156;
    }

    @Nullable
    public String method_4315() {
        return this.field_4155;
    }

    public void freeBuf() {
        if (this.field_4154 != null && this.field_4154.refCnt() > 0) {
            this.field_4154.release();
        }
    }
}

