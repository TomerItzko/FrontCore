package red.vuis.mercfrontcore.net.packet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.encoding.StringEncoding;
import net.minecraft.network.encoding.VarInts;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.AddonCommonData;
import red.vuis.mercfrontcore.data.AddonPacketCodecs;
import red.vuis.mercfrontcore.util.AddonUtils;

// property ids:
// 	0 - username
// 	1 - exp
// 	2 - prestige
// 	3 - equipped item stacks
public record SetProfileOverridesPropertyPacket(Set<Pair<UUID, String>> idPairs, IntObjectPair<Object> property) implements CustomPayload {
	public static final Id<SetProfileOverridesPropertyPacket> ID = new Id<>(AddonConstants.id("set_profile_overrides_property"));
	public static final PacketCodec<ByteBuf, SetProfileOverridesPropertyPacket> PACKET_CODEC = PacketCodec.tuple(
		AddonPacketCodecs.pair(Uuids.PACKET_CODEC, PacketCodecs.STRING).collect(PacketCodecs.toCollection(ObjectOpenHashSet::new)), SetProfileOverridesPropertyPacket::idPairs,
		PropertyCodec.INSTANCE, SetProfileOverridesPropertyPacket::property,
		SetProfileOverridesPropertyPacket::new
	);
	
	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	@SuppressWarnings("unchecked")
	public static void handleClient(SetProfileOverridesPropertyPacket packet, IPayloadContext context) {
		Map<UUID, PlayerCloudData> profileOverrides = AddonCommonData.getInstance().profileOverrides;
		
		for (Pair<UUID, String> idPair : packet.idPairs()) {
			PlayerCloudData cloudData = profileOverrides.computeIfAbsent(idPair.left(), uuid -> AddonUtils.createPlayerCloudData(idPair));
			IntObjectPair<Object> property = packet.property();
			switch (property.leftInt()) {
				case 0 -> cloudData.setUsername((String) property.right());
				case 1 -> cloudData.setExp((Integer) property.right());
				case 2 -> cloudData.setPrestigeLevel((Integer) property.right());
				case 3 -> {
					PlayerCloudInventory inventory = cloudData.getInventory();
					List<CloudItemStack> stacks = (List<CloudItemStack>) property.right();
					inventory.onReceiveSection(stacks, 0);
					inventory.populateEquippedItems(
						AddonUtils.getBfManager().getCloudRegistry(),
						stacks.stream().map(CloudItemStack::getUUID).collect(Collectors.toSet())
					);
				}
				default -> throw new AssertionError();
			}
		}
	}
	
	private static class PropertyCodec implements PacketCodec<ByteBuf, IntObjectPair<Object>> {
		public static final PropertyCodec INSTANCE = new PropertyCodec();
		
		private PropertyCodec() {
		}
		
		@Override
		public IntObjectPair<Object> decode(ByteBuf buf) {
			int propertyId = VarInts.read(buf);
			Object obj = switch (propertyId) {
				case 0 -> StringEncoding.decode(buf, 16);
				case 1, 2 -> VarInts.read(buf);
				case 3 -> {
					try {
						yield IPacket.readCloudItemStackList(buf);
					} catch (IOException e) {
						throw new DecoderException(e);
					}
				}
				default -> throw new DecoderException("Received unknown property id");
			};
			return IntObjectPair.of(propertyId, obj);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public void encode(ByteBuf buf, IntObjectPair<Object> value) {
			int propertyId = value.leftInt();
			VarInts.write(buf, propertyId);
			switch (propertyId) {
				case 0 -> StringEncoding.encode(buf, (String) value.right(), 16);
				case 1, 2 -> VarInts.write(buf, (Integer) value.right());
				case 3 -> {
					try {
						IPacket.writeCloudItemStackList(buf, (List<CloudItemStack>) value.right());
					} catch (IOException e) {
						throw new EncoderException(e);
					}
				}
			}
		}
	}
}
