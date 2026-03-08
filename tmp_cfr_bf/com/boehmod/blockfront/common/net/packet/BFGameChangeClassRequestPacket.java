/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGameChangeClassRequestPacket(@NotNull MatchClass classType, int classIndex) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGameChangeClassRequestPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_change_class_request"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGameChangeClassRequestPacket> CODEC = CustomPacketPayload.codec(BFGameChangeClassRequestPacket::method_4342, BFGameChangeClassRequestPacket::new);

    public BFGameChangeClassRequestPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((MatchClass)friendlyByteBuf.readEnum(MatchClass.class), friendlyByteBuf.readInt());
    }

    public void method_4342(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum((Enum)this.classType);
        friendlyByteBuf.writeInt(this.classIndex);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4339(BFGameChangeClassRequestPacket bFGameChangeClassRequestPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ServerLevel serverLevel = serverPlayer.serverLevel();
        UUID uUID = serverPlayer.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame != null) {
            abstractGame.handleClassChangeRequest(bFAbstractManager, serverLevel, serverPlayer, uUID, bFGameChangeClassRequestPacket.classType, bFGameChangeClassRequestPacket.classIndex);
        }
    }
}

