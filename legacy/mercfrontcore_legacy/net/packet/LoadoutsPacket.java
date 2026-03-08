package red.vuis.mercfrontcore.net.packet;

import java.util.List;
import java.util.Map;

import com.boehmod.blockfront.common.match.Loadout;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.client.data.AddonClientData;
import red.vuis.mercfrontcore.data.AddonPacketCodecs;
import red.vuis.mercfrontcore.setup.LoadoutIndex;
import red.vuis.mercfrontcore.util.AddonUtils;

public record LoadoutsPacket(Map<LoadoutIndex.Identifier, List<Loadout>> loadouts) implements CustomPayload {
	public static final CustomPayload.Id<LoadoutsPacket> ID = new CustomPayload.Id<>(AddonConstants.id("loadouts"));
	public static final PacketCodec<RegistryByteBuf, LoadoutsPacket> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.map(
			Object2ObjectOpenHashMap::new,
			AddonPacketCodecs.BF_COUNTRY,
			PacketCodecs.map(
				Object2ObjectOpenHashMap::new,
				PacketCodecs.STRING,
				PacketCodecs.map(
					Object2ObjectOpenHashMap::new,
					AddonPacketCodecs.MATCH_CLASS,
					PacketCodecs.collection(
						ObjectArrayList::new,
						AddonPacketCodecs.LOADOUT
					)
				)
			)
		),
		packet -> LoadoutIndex.flatToNested(packet.loadouts),
		nested -> new LoadoutsPacket(LoadoutIndex.nestedToFlat(nested))
	);
	
	@Override
	public @NotNull Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleClient(LoadoutsPacket packet, IPayloadContext context) {
		AddonConstants.LOGGER.info("Applying loadouts from server.");
		
		LoadoutIndex.apply(packet.loadouts);
		AddonClientData.getInstance().reloadLoadouts();
	}
	
	public static void handleServer(LoadoutsPacket packet, IPayloadContext context) {
		// Prevent the handler from running on the integrated server
		if (!FMLEnvironment.dist.isDedicatedServer()) {
			return;
		}
		
		if (!context.player().hasPermissionLevel(4)) {
			return;
		}
		
		if (AddonUtils.anyGamesActive()) {
			context.player().sendMessage(Text.translatable(
				"mercfrontcore.message.packet.loadouts.game",
				Text.literal("/mercfrontcore loadout sync").formatted(Formatting.GOLD)
			).formatted(Formatting.RED));
			return;
		}
		
		LoadoutIndex.apply(packet.loadouts);
		PacketDistributor.sendToAllPlayers(packet);
		
		context.player().sendMessage(Text.translatable(
			"mercfrontcore.message.packet.loadouts.success"
		));
	}
}
