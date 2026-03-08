package red.vuis.mercfrontcore.net.packet;

import java.util.Set;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.AddonCommonData;

public record ClearProfileOverridesPacket(Set<UUID> uuids) implements CustomPayload {
	public static final Id<ClearProfileOverridesPacket> ID = new Id<>(AddonConstants.id("clear_profile_overrides"));
	public static final PacketCodec<ByteBuf, ClearProfileOverridesPacket> PACKET_CODEC = PacketCodec.tuple(
		Uuids.PACKET_CODEC.collect(PacketCodecs.toCollection(ObjectOpenHashSet::new)), ClearProfileOverridesPacket::uuids,
		ClearProfileOverridesPacket::new
	);
	
	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleClient(ClearProfileOverridesPacket packet, IPayloadContext context) {
		packet.uuids().forEach(AddonCommonData.getInstance().profileOverrides::remove);
	}
}
