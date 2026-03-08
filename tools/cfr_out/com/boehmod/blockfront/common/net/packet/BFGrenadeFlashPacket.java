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

public record BFGrenadeFlashPacket(float flashTime) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGrenadeFlashPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_grenade_flash"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGrenadeFlashPacket> CODEC = CustomPacketPayload.codec(BFGrenadeFlashPacket::method_4352, BFGrenadeFlashPacket::new);

    public BFGrenadeFlashPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readFloat());
    }

    public void method_4352(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFloat(this.flashTime);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4351(BFGrenadeFlashPacket bFGrenadeFlashPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.grenadeFlash(bFGrenadeFlashPacket, iPayloadContext);
    }
}

