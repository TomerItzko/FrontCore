/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.tag.ICanSwitchTeams;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFSwitchTeamPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFSwitchTeamPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_switch_team"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFSwitchTeamPacket> CODEC = CustomPacketPayload.codec(BFSwitchTeamPacket::method_4381, BFSwitchTeamPacket::new);
    public static final Component ERROR_DISABLED = Component.translatable((String)"bf.message.gamemode.switchteams.error.disabled").withStyle(ChatFormatting.RED);
    public static final Component ERROR_TIME = Component.translatable((String)"bf.message.gamemode.switchteams.error.time").withStyle(ChatFormatting.RED);

    public BFSwitchTeamPacket() {
    }

    public BFSwitchTeamPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4381(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4380(BFSwitchTeamPacket bFSwitchTeamPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        UUID uUID = serverPlayer.getUUID();
        ServerLevel serverLevel = serverPlayer.serverLevel();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (!(abstractGame instanceof ICanSwitchTeams)) {
            BFUtils.sendNoticeMessage(serverPlayer, ERROR_DISABLED);
            return;
        }
        ICanSwitchTeams iCanSwitchTeams = (ICanSwitchTeams)((Object)abstractGame);
        Object obj = abstractGame.getPlayerManager();
        int n = BFUtils.getPlayerStat(abstractGame, uUID, BFStats.TEAM_SWITCH);
        if (n > 0 && !player.isCreative()) {
            BFUtils.sendNoticeMessage(serverPlayer, ERROR_TIME);
            return;
        }
        Component component = iCanSwitchTeams.getSwitchTeamMessage(serverPlayer);
        if (component != null && !player.isCreative()) {
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.switchteams.error.condition", (Object[])new Object[]{component}).withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent);
            return;
        }
        iCanSwitchTeams.playerSwitchTeam(bFAbstractManager, serverLevel, serverPlayer, uUID);
        BFUtils.setPlayerStat(abstractGame, uUID, BFStats.TEAM_SWITCH, iCanSwitchTeams.getTeamSwitchCooldown());
        BFUtils.discardPlayerGrenades(serverLevel, abstractGame, obj, serverPlayer);
    }
}

