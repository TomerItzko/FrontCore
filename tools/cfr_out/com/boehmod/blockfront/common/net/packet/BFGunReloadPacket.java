/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFGunReloadPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFGunReloadPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_gun_reload"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGunReloadPacket> CODEC = CustomPacketPayload.codec(BFGunReloadPacket::method_4357, BFGunReloadPacket::new);

    public BFGunReloadPacket() {
    }

    public BFGunReloadPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4357(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4356(BFGunReloadPacket bFGunReloadPacket, @NotNull IPayloadContext iPayloadContext) {
        GunItem gunItem;
        Player player = iPayloadContext.player();
        ItemStack itemStack = player.getMainHandItem();
        ItemStack itemStack2 = player.getOffhandItem();
        Item item = itemStack.getItem();
        if (item instanceof GunItem && (gunItem = (GunItem)item).method_4145((LivingEntity)player, player.level(), itemStack, true)) {
            gunItem.method_4145((LivingEntity)player, player.level(), itemStack, true);
        } else {
            GunItem gunItem2;
            item = itemStack2.getItem();
            if (item instanceof GunItem && (gunItem2 = (GunItem)item).method_4145((LivingEntity)player, player.level(), itemStack2, false)) {
                gunItem2.method_4145((LivingEntity)player, player.level(), itemStack2, false);
            }
        }
    }
}

