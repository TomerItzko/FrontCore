/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFVehicleControlPacket(boolean forward, boolean back, boolean left, boolean right, boolean sprint, boolean space, float throttle) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFVehicleControlPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_vehicle_control"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFVehicleControlPacket> CODEC = CustomPacketPayload.codec(BFVehicleControlPacket::method_4464, BFVehicleControlPacket::new);

    public BFVehicleControlPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readFloat());
    }

    public void method_4464(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.forward);
        friendlyByteBuf.writeBoolean(this.back);
        friendlyByteBuf.writeBoolean(this.left);
        friendlyByteBuf.writeBoolean(this.right);
        friendlyByteBuf.writeBoolean(this.sprint);
        friendlyByteBuf.writeBoolean(this.space);
        friendlyByteBuf.writeFloat(this.throttle);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4456(BFVehicleControlPacket bFVehicleControlPacket, @NotNull IPayloadContext iPayloadContext) {
        AbstractVehicleEntity abstractVehicleEntity;
        Player player = iPayloadContext.player();
        Entity entity = player.getVehicle();
        if (entity instanceof AbstractVehicleEntity && player.equals((Object)(abstractVehicleEntity = (AbstractVehicleEntity)entity).method_2319())) {
            abstractVehicleEntity.method_2306(bFVehicleControlPacket.forward);
            abstractVehicleEntity.method_2307(bFVehicleControlPacket.back);
            abstractVehicleEntity.method_2308(bFVehicleControlPacket.left);
            abstractVehicleEntity.method_2309(bFVehicleControlPacket.right);
            abstractVehicleEntity.method_2310(bFVehicleControlPacket.sprint);
            abstractVehicleEntity.method_2312(bFVehicleControlPacket.space);
            abstractVehicleEntity.method_2350(bFVehicleControlPacket.throttle);
        }
    }
}

