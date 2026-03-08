/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.common.net.packet.BFShakeNodePacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGrenadeThrowPacket(float force) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGrenadeThrowPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_grenade_throw"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGrenadeThrowPacket> CODEC = CustomPacketPayload.codec(BFGrenadeThrowPacket::method_4355, BFGrenadeThrowPacket::new);

    public BFGrenadeThrowPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readFloat());
    }

    public void method_4355(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeFloat(this.force);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4353(BFGrenadeThrowPacket bFGrenadeThrowPacket, @NotNull IPayloadContext iPayloadContext) {
        Object object;
        AbstractVehicleEntity abstractVehicleEntity;
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        Object object2 = serverPlayer.getVehicle();
        if (object2 instanceof AbstractVehicleEntity && (object2 = (abstractVehicleEntity = (AbstractVehicleEntity)object2).method_2324((Entity)serverPlayer)) != null && ((BF_623)object2).field_2663) {
            return;
        }
        abstractVehicleEntity = serverPlayer.getMainHandItem();
        if (!abstractVehicleEntity.isEmpty() && (object = abstractVehicleEntity.getItem()) instanceof GrenadeFragItem) {
            object2 = (GrenadeFragItem)object;
            object = BlockFront.getInstance().getManager().getPlayerDataHandler();
            ((GrenadeFragItem)object2).method_4089((PlayerDataHandler<?>)object, (ItemStack)abstractVehicleEntity, serverPlayer.level(), serverPlayer, Math.min(bFGrenadeThrowPacket.force, 2.0f));
            PacketUtils.sendToPlayer(new BFShakeNodePacket(ShakeNodePresets.field_1926), serverPlayer);
        }
    }
}

