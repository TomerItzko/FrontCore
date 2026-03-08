/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.tag.IHasGunStore;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.RegistryUtils;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGunStoreBuyPacket(@NotNull Item item) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGunStoreBuyPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_gun_store_buy"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFGunStoreBuyPacket> CODEC = CustomPacketPayload.codec(BFGunStoreBuyPacket::method_4370, BFGunStoreBuyPacket::new);

    public BFGunStoreBuyPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(RegistryUtils.retrieveItem(registryFriendlyByteBuf.readUtf()).get());
    }

    public void method_4370(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeUtf(RegistryUtils.getItemId(this.item));
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4368(BFGunStoreBuyPacket bFGunStoreBuyPacket, @NotNull IPayloadContext iPayloadContext) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Manager is null";
        Object object = iPayloadContext.player();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer((UUID)object);
        if (!(abstractGame instanceof IHasGunStore)) {
            return;
        }
        IHasGunStore iHasGunStore = (IHasGunStore)((Object)abstractGame);
        GameShopItem gameShopItem = iHasGunStore.method_3417((Player)serverPlayer, bFGunStoreBuyPacket.item);
        if (gameShopItem != null) {
            iHasGunStore.method_3415(serverPlayer, gameShopItem);
        }
    }
}

