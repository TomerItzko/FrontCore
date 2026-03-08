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
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFWorldFlashPacket(int flashTime) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFWorldFlashPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_world_flash"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFWorldFlashPacket> CODEC = CustomPacketPayload.codec(BFWorldFlashPacket::method_4289, BFWorldFlashPacket::new);

    public BFWorldFlashPacket(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt());
    }

    public void method_4289(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.flashTime);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4287(BFWorldFlashPacket bFWorldFlashPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.worldFlash(bFWorldFlashPacket, iPayloadContext);
    }
}

