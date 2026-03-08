/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFConsumableItem;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFUseItemPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFUseItemPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_use_item"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFUseItemPacket> CODEC = CustomPacketPayload.codec(BFUseItemPacket::method_4455, BFUseItemPacket::new);

    public BFUseItemPacket() {
    }

    public BFUseItemPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4455(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4454(BFUseItemPacket bFUseItemPacket, @NotNull IPayloadContext iPayloadContext) {
        Object object;
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack itemStack = serverPlayer.getMainHandItem();
        if (!itemStack.isEmpty() && (object = itemStack.getItem()) instanceof BFConsumableItem) {
            BFConsumableItem bFConsumableItem = (BFConsumableItem)object;
            object = BlockFront.getInstance().getManager();
            bFConsumableItem.method_4073((BFAbstractManager<?, ?, ?>)object, serverPlayer.serverLevel(), (Player)serverPlayer, bFConsumableItem, itemStack);
        }
    }
}

