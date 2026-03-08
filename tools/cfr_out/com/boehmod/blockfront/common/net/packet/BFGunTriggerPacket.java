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

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
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

public final class BFGunTriggerPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFGunTriggerPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_gun_trigger"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGunTriggerPacket> CODEC = CustomPacketPayload.codec(BFGunTriggerPacket::method_4372, BFGunTriggerPacket::new);

    public BFGunTriggerPacket() {
    }

    public BFGunTriggerPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4372(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4371(BFGunTriggerPacket bFGunTriggerPacket, @NotNull IPayloadContext iPayloadContext) {
        Item item;
        Player player;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        Object d = ((PlayerDataHandler)obj).getPlayerData(player = iPayloadContext.player());
        if (((BFAbstractPlayerData)d).method_849()) {
            return;
        }
        ItemStack itemStack = player.getInventory().getSelected();
        if (!itemStack.isEmpty() && (item = itemStack.getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            gunItem.method_4156(itemStack, player.level(), (LivingEntity)player);
        }
    }
}

