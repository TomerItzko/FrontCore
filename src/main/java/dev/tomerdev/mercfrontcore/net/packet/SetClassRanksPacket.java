package dev.tomerdev.mercfrontcore.net.packet;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.data.AddonPacketCodecs;
import dev.tomerdev.mercfrontcore.util.ClassRankCompat;
import com.boehmod.blockfront.common.match.MatchClass;

public record SetClassRanksPacket(Map<MatchClass, String> rankNames) implements CustomPayload {
    public static final Id<SetClassRanksPacket> ID = new Id<>(AddonConstants.id("set_class_ranks"));
    public static final PacketCodec<ByteBuf, SetClassRanksPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.map(Object2ObjectOpenHashMap::new, AddonPacketCodecs.MATCH_CLASS, PacketCodecs.STRING),
        SetClassRanksPacket::rankNames,
        SetClassRanksPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(SetClassRanksPacket packet, IPayloadContext context) {
        ClassRankCompat.applyRankNames(packet.rankNames());
    }

    public static SetClassRanksPacket fromCurrentConfig() {
        return new SetClassRanksPacket(ClassRankCompat.toRankNameMap(MercFrontCoreConfigManager.get()));
    }
}
