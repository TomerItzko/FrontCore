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

public record BFVehicleFireEffectPacket(int entityId, String partName) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFVehicleFireEffectPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_vehicle_fire_effect"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFVehicleFireEffectPacket> CODEC = CustomPacketPayload.codec(BFVehicleFireEffectPacket::method_4470, BFVehicleFireEffectPacket::new);

    public BFVehicleFireEffectPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt(), friendlyByteBuf.readUtf(Short.MAX_VALUE));
    }

    public void method_4470(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.entityId);
        friendlyByteBuf.writeUtf(this.partName);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4468(BFVehicleFireEffectPacket bFVehicleFireEffectPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.vehicleFireEffect(bFVehicleFireEffectPacket, iPayloadContext);
    }
}

