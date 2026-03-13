package dev.tomerdev.mercfrontcore.net.packet;

import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeaderboardResponsePacket(String periodId, List<Entry> entries) implements CustomPayload {
    public static final Id<LeaderboardResponsePacket> ID = new Id<>(AddonConstants.id("leaderboard_response"));
    private static final PacketCodec<ByteBuf, Entry> ENTRY_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING,
        Entry::username,
        Uuids.PACKET_CODEC,
        Entry::uuid,
        PacketCodecs.VAR_INT,
        Entry::xp,
        Entry::new
    );
    public static final PacketCodec<ByteBuf, LeaderboardResponsePacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING,
        LeaderboardResponsePacket::periodId,
        PacketCodecs.collection(ObjectArrayList::new, ENTRY_CODEC),
        LeaderboardResponsePacket::entries,
        LeaderboardResponsePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(LeaderboardResponsePacket packet, IPayloadContext context) {
        AddonClientData.getInstance().setLeaderboard(packet.periodId(), packet.entries());
    }

    public record Entry(String username, UUID uuid, int xp) {
    }
}
