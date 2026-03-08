/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.tag.IHasItemShop;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGameBuyItemRequestPacket(int index) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGameBuyItemRequestPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_buy_item_request"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGameBuyItemRequestPacket> CODEC = CustomPacketPayload.codec(BFGameBuyItemRequestPacket::method_4338, BFGameBuyItemRequestPacket::new);

    public BFGameBuyItemRequestPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt());
    }

    public void method_4338(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.index);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4336(BFGameBuyItemRequestPacket bFGameBuyItemRequestPacket, @NotNull IPayloadContext iPayloadContext) {
        Object object = iPayloadContext.player();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        Object h = ((BFAbstractManager)object).getPlayerDataHandler();
        UUID uUID = serverPlayer.getUUID();
        Object d = ((PlayerDataHandler)h).getPlayerData((Player)serverPlayer);
        if (BFUtils.isPlayerUnavailable((Player)serverPlayer, d)) {
            return;
        }
        if (((BFAbstractPlayerData)d).isOutOfGame()) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return;
        }
        if (!(abstractGame instanceof IHasItemShop)) {
            return;
        }
        IHasItemShop iHasItemShop = (IHasItemShop)((Object)abstractGame);
        if (abstractGame.getStatus() != GameStatus.GAME) {
            return;
        }
        List<GameShopItem> list = iHasItemShop.getShopItems(uUID);
        GameShopItem gameShopItem = list.get(bFGameBuyItemRequestPacket.index);
        if (gameShopItem == null) {
            return;
        }
        int n = gameShopItem.getPrice();
        BFStat bFStat = iHasItemShop.getPointsStat();
        int n2 = BFUtils.getPlayerStat(abstractGame, uUID, bFStat);
        if (n2 >= n) {
            serverPlayer.addItem(gameShopItem.getItemStack().copy());
            BFUtils.setPlayerStat(abstractGame, uUID, bFStat, n2 - n);
            BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.ENTITY_GUNDEALER_RESUPPLY.get(), SoundSource.NEUTRAL);
            Component component = gameShopItem.getItemStack().getDisplayName();
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ttt.loadout", (Object[])new Object[]{component, mutableComponent}).withStyle(ChatFormatting.GRAY);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent2);
        } else {
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.ttt.loadout.fail", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent3);
        }
    }
}

