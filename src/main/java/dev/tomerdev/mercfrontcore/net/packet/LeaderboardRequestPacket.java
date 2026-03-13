package dev.tomerdev.mercfrontcore.net.packet;

import io.netty.buffer.ByteBuf;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.data.LeaderboardManager;
import dev.tomerdev.mercfrontcore.data.LeaderboardPeriod;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeaderboardRequestPacket(String periodId) implements CustomPayload {
    public static final Id<LeaderboardRequestPacket> ID = new Id<>(AddonConstants.id("leaderboard_request"));
    public static final PacketCodec<ByteBuf, LeaderboardRequestPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING,
        LeaderboardRequestPacket::periodId,
        LeaderboardRequestPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleServer(LeaderboardRequestPacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayerEntity player)) {
            return;
        }
        LeaderboardPeriod period = LeaderboardPeriod.fromId(packet.periodId());
        PacketDistributor.sendToPlayer(
            player,
            new LeaderboardResponsePacket(period.id(), LeaderboardManager.build(player.getServer(), period))
        );
    }
}
