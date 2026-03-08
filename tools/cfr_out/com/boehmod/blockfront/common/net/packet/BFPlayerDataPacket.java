/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFPlayerDataPacket(@NotNull BFAbstractPlayerData<?, ?, ?, ?> playerData) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFPlayerDataPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_player_data"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFPlayerDataPacket> CODEC = CustomPacketPayload.codec(BFPlayerDataPacket::method_4393, BFPlayerDataPacket::new);

    public BFPlayerDataPacket(FriendlyByteBuf friendlyByteBuf) {
        this(SerializationUtils.readPlayerGameData(friendlyByteBuf));
    }

    public void method_4393(FriendlyByteBuf friendlyByteBuf) {
        SerializationUtils.writePlayerData(friendlyByteBuf, this.playerData);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4392(BFPlayerDataPacket bFPlayerDataPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.playerData(bFPlayerDataPacket, iPayloadContext);
    }
}

