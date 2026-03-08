package red.vuis.mercfrontcore.net.packet;

import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.AddonPacketCodecs;

public record MapEffectPositionPacket(String map, String environment, int index, Vec3d position) implements CustomPayload {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final Id<MapEffectPositionPacket> ID = new Id<>(AddonConstants.id("map_effect_position"));
	public static final PacketCodec<ByteBuf, MapEffectPositionPacket> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.STRING, MapEffectPositionPacket::map,
		PacketCodecs.STRING, MapEffectPositionPacket::environment,
		PacketCodecs.VAR_INT, MapEffectPositionPacket::index,
		AddonPacketCodecs.VEC3D, MapEffectPositionPacket::position,
		MapEffectPositionPacket::new
	);
	
	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleServer(MapEffectPositionPacket packet, IPayloadContext context) {
		if (!FMLEnvironment.dist.isDedicatedServer()) {
			return;
		}
		
		if (!context.player().hasPermissionLevel(3)) {
			return;
		}
		
		AssetRegistry<MapAsset> mapRegistry = AssetStore.getInstance().getRegistry(MapAsset.class);
		
		MapAsset map = mapRegistry.getByName(packet.map);
		if (map == null) {
			LOGGER.error("Map \"{}\" does not exist!", packet.map);
			return;
		}
		
		MapEnvironment environment = map.environments.get(packet.environment);
		if (environment == null) {
			LOGGER.error("Map environment \"{}\" does not exist (\"{}\")!", packet.environment, packet.map);
			return;
		}
		
		if (packet.index < 0 || packet.index >= environment.getMapEffects().size()) {
			LOGGER.error("Map effect index {} is out of bounds (\"{}\" - \"{}\")!", packet.index, packet.map, packet.environment);
			return;
		}
		AbstractMapEffect absMapEffect = environment.getMapEffects().get(packet.index);
		if (!(absMapEffect instanceof PositionedMapEffect posMapEffect)) {
			LOGGER.error("Map effect at index {} does not have a valid position (\"{}\" - \"{}\")!", packet.index, packet.map, packet.environment);
			return;
		}
		
		posMapEffect.position = packet.position;
	}
}
