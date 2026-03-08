/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGameVoteMapPacket(@NotNull String mapName, @NotNull String mapGame) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGameVoteMapPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_vote_map"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGameVoteMapPacket> CODEC = CustomPacketPayload.codec(BFGameVoteMapPacket::method_4349, BFGameVoteMapPacket::new);

    public BFGameVoteMapPacket(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readUtf(Short.MAX_VALUE), friendlyByteBuf.readUtf(Short.MAX_VALUE));
    }

    public void method_4349(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(this.mapName);
        friendlyByteBuf.writeUtf(this.mapGame);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4348(BFGameVoteMapPacket bFGameVoteMapPacket, @NotNull IPayloadContext iPayloadContext) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Player player = iPayloadContext.player();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(player.getUUID());
        if (abstractGame == null) {
            return;
        }
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            abstractGame.getMapVoteManager().method_3717(serverPlayer, bFGameVoteMapPacket.mapName, bFGameVoteMapPacket.mapGame);
        }
    }
}

