/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.util.BFRes;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFWarCryPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFWarCryPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_war_cry"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFWarCryPacket> CODEC = CustomPacketPayload.codec(BFWarCryPacket::method_4383, BFWarCryPacket::new);

    public BFWarCryPacket() {
    }

    public BFWarCryPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4383(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4382(BFWarCryPacket bFWarCryPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        UUID uUID = serverPlayer.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame instanceof IAllowsWarCry) {
            GameTeam gameTeam;
            IAllowsWarCry iAllowsWarCry = (IAllowsWarCry)((Object)abstractGame);
            Object obj = bFAbstractManager.getPlayerDataHandler();
            Level level = serverPlayer.level();
            if (IAllowsWarCry.method_3439(obj, serverPlayer) && iAllowsWarCry.method_3442(level, serverPlayer, uUID) && (gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID)) != null) {
                iAllowsWarCry.method_3441(level, serverPlayer, uUID);
                IAllowsWarCry.method_3440(abstractGame, gameTeam.getDivisionData(abstractGame), (LivingEntity)serverPlayer, uUID);
            }
        }
    }
}

