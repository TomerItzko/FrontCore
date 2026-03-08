/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_632;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFVehicleFireShellPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFVehicleFireShellPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_vehicle_fire_shell"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFVehicleFireShellPacket> CODEC = CustomPacketPayload.codec(BFVehicleFireShellPacket::method_4472, BFVehicleFireShellPacket::new);

    public BFVehicleFireShellPacket() {
    }

    public BFVehicleFireShellPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4472(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4471(BFVehicleFireShellPacket bFVehicleFireShellPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        Entity entity = serverPlayer.getVehicle();
        if (entity instanceof AbstractVehicleEntity) {
            AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
            BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
            for (BF_633 bF_633 : bF_624.field_2679) {
                BF_631 bF_631;
                if (!(bF_633 instanceof BF_631) || !serverPlayer.equals((Object)(bF_631 = (BF_631)bF_633).method_2465()) || bF_631.method_2442() != BF_632.SHELL) continue;
                bF_631.method_2460(serverPlayer);
                return;
            }
        }
    }
}

