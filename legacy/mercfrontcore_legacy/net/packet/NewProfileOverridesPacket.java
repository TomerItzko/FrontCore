package red.vuis.mercfrontcore.net.packet;

import java.util.Collection;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.AddonCommonData;
import red.vuis.mercfrontcore.data.AddonPacketCodecs;

public record NewProfileOverridesPacket(Collection<Pair<UUID, String>> idPairs) implements CustomPayload {
	public static final Id<NewProfileOverridesPacket> ID = new Id<>(AddonConstants.id("new_profile_overrides"));
	public static final PacketCodec<ByteBuf, NewProfileOverridesPacket> PACKET_CODEC = PacketCodec.tuple(
		AddonPacketCodecs.pair(Uuids.PACKET_CODEC, PacketCodecs.STRING).collect(PacketCodecs.toCollection(ObjectArrayList::new)), NewProfileOverridesPacket::idPairs,
		NewProfileOverridesPacket::new
	);
	
	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleClient(NewProfileOverridesPacket packet, IPayloadContext context) {
		AddonCommonData.getInstance().putNewProfileOverrides(packet.idPairs());
	}
}
