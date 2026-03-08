/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_694;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFHornPacket() implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFHornPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_horn"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFHornPacket> CODEC = CustomPacketPayload.codec(BFHornPacket::method_4390, BFHornPacket::new);

    public BFHornPacket(FriendlyByteBuf friendlyByteBuf) {
        this();
    }

    public void method_4390(FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4389(BFHornPacket bFHornPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        Entity entity = player.getVehicle();
        if (!(entity instanceof AbstractVehicleEntity)) {
            return;
        }
        AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
        entity = abstractVehicleEntity.method_2319();
        if (entity == null || !entity.equals((Object)player)) {
            return;
        }
        BF_694<?> bF_694 = abstractVehicleEntity.method_2318();
        if (bF_694.field_2998 != null) {
            abstractVehicleEntity.playSound((SoundEvent)bF_694.field_2998.get(), 2.5f, 1.0f);
        }
        if (bF_694.field_2999 != null) {
            abstractVehicleEntity.playSound((SoundEvent)bF_694.field_2999.get(), 5.0f, 1.0f);
        }
    }
}

