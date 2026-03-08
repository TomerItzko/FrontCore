package red.vuis.mercfrontcore.event;

import java.io.IOException;

import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.command.MercFrontCoreCommand;
import red.vuis.mercfrontcore.data.GunModifier;
import red.vuis.mercfrontcore.data.GunModifierFiles;
import red.vuis.mercfrontcore.net.packet.ClearProfileOverridesPacket;
import red.vuis.mercfrontcore.net.packet.GiveGunPacket;
import red.vuis.mercfrontcore.net.packet.GunModifiersPacket;
import red.vuis.mercfrontcore.net.packet.LoadoutsPacket;
import red.vuis.mercfrontcore.net.packet.MapEffectPositionPacket;
import red.vuis.mercfrontcore.net.packet.NewProfileOverridesPacket;
import red.vuis.mercfrontcore.net.packet.SetProfileOverridesPacket;
import red.vuis.mercfrontcore.net.packet.SetProfileOverridesPropertyPacket;
import red.vuis.mercfrontcore.net.packet.ViewSpawnsPacket;
import red.vuis.mercfrontcore.setup.GunItemIndex;
import red.vuis.mercfrontcore.setup.GunModifierIndex;
import red.vuis.mercfrontcore.setup.GunSkinIndex;
import red.vuis.mercfrontcore.setup.LoadoutIndex;
import red.vuis.mercfrontcore.util.AddonUtils;

@EventBusSubscriber(
	modid = AddonConstants.MOD_ID
)
public final class AddonCommonEvents {
	private AddonCommonEvents() {
	}
	
	@SubscribeEvent
	public static void onLoadComplete(FMLLoadCompleteEvent event) {
		AddonConstants.LOGGER.info("Indexing default gun properties...");
		GunModifierIndex.init();
		
		AddonConstants.LOGGER.info("Indexing default loadouts...");
		LoadoutIndex.init();
		
		AddonConstants.LOGGER.info("Indexing gun items...");
		GunItemIndex.init();
		
		AddonConstants.LOGGER.info("Indexing skins...");
		GunSkinIndex.init(AddonUtils.getBfManager().getCloudRegistry());
	}
	
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayerEntity player)) {
			return;
		}
		
		AddonConstants.LOGGER.info("Syncing custom common data with player '{}'.", player.getNameForScoreboard());
		
		PacketDistributor.sendToPlayer(player, new GunModifiersPacket(GunModifier.ACTIVE, true));
	}
	
	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		AddonConstants.LOGGER.info("Registering commands...");
		
		var dispatcher = event.getDispatcher();
		MercFrontCoreCommand.register(dispatcher);
	}
	
	@SubscribeEvent
	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(ClearProfileOverridesPacket.ID, ClearProfileOverridesPacket.PACKET_CODEC, ClearProfileOverridesPacket::handleClient);
		registrar.playToServer(GiveGunPacket.ID, GiveGunPacket.PACKET_CODEC, GiveGunPacket::handleServer);
		registrar.playBidirectional(GunModifiersPacket.ID, GunModifiersPacket.PACKET_CODEC, new DirectionalPayloadHandler<>(
			GunModifiersPacket::handleClient, GunModifiersPacket::handleServer
		));
		registrar.playBidirectional(LoadoutsPacket.ID, LoadoutsPacket.PACKET_CODEC, new DirectionalPayloadHandler<>(
			LoadoutsPacket::handleClient, LoadoutsPacket::handleServer
		));
		registrar.playToServer(MapEffectPositionPacket.ID, MapEffectPositionPacket.PACKET_CODEC, MapEffectPositionPacket::handleServer);
		registrar.playToClient(NewProfileOverridesPacket.ID, NewProfileOverridesPacket.PACKET_CODEC, NewProfileOverridesPacket::handleClient);
		registrar.playToClient(SetProfileOverridesPacket.ID, SetProfileOverridesPacket.PACKET_CODEC, SetProfileOverridesPacket::handleClient);
		registrar.playToClient(SetProfileOverridesPropertyPacket.ID, SetProfileOverridesPropertyPacket.PACKET_CODEC, SetProfileOverridesPropertyPacket::handleClient);
		registrar.playToClient(ViewSpawnsPacket.ID, ViewSpawnsPacket.PACKET_CODEC, ViewSpawnsPacket::handleClient);
	}
	
	@SubscribeEvent
	public static void onServerStarting(ServerStartingEvent event) {
		AddonConstants.LOGGER.info("Preparing gun modifiers...");
		
		GunModifierIndex.applyDefaults();
		
		AddonConstants.LOGGER.info("Loading gun modifiers...");
		
		try {
			GunModifierFiles.loadModifierMap(AddonUtils.getServerDataPath(event.getServer()).resolve(GunModifierFiles.GUN_MODIFIERS_PATH), GunModifier.ACTIVE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		AddonConstants.LOGGER.info("Applying gun modifier targets...");
		
		GunModifier.ACTIVE.forEach((itemEntry, modifier) -> {
			Item item = itemEntry.value();
			if (item instanceof GunItem gunItem) {
				modifier.apply(gunItem);
			} else {
				AddonConstants.LOGGER.error("{} is not modifiable!", itemEntry.getIdAsString());
			}
		});
	}
}
