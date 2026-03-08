package dev.tomerdev.mercfrontcore.net.packet;

import java.io.IOException;
import java.util.Map;

import com.boehmod.blockfront.common.item.GunItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.data.GunModifier;
import dev.tomerdev.mercfrontcore.data.GunModifierFiles;
import dev.tomerdev.mercfrontcore.setup.GunModifierIndex;

public record GunModifiersPacket(Map<RegistryEntry<Item>, GunModifier> modifiers, boolean apply) implements CustomPayload {
	public static final Id<GunModifiersPacket> ID = new Id<>(AddonConstants.id("gun_modifiers"));
	public static final PacketCodec<RegistryByteBuf, GunModifiersPacket> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.map(
			Object2ObjectOpenHashMap::new,
			PacketCodecs.registryEntry(RegistryKeys.ITEM), GunModifier.PACKET_CODEC
		), GunModifiersPacket::modifiers,
		PacketCodecs.BOOL,
		GunModifiersPacket::apply,
		GunModifiersPacket::new
	);
	
	@Override
	public @NotNull Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleClient(GunModifiersPacket packet, IPayloadContext context) {
		if (packet.apply()) {
			GunModifierIndex.applyDefaults();
			
			for (Map.Entry<RegistryEntry<Item>, GunModifier> entry : packet.modifiers().entrySet()) {
				Item item = entry.getKey().value();
				if (item instanceof GunItem gunItem) {
					entry.getValue().apply(gunItem);
				}
			}
		}
		
		AddonClientData.getInstance().tempGunModifiers = packet.modifiers();
	}
	
	public static void handleServer(GunModifiersPacket packet, IPayloadContext context) {
		if (!context.player().hasPermissionLevel(4)) {
			return;
		}
		
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		assert server != null;
		try {
			GunModifier.ACTIVE.clear();
			GunModifier.ACTIVE.putAll(packet.modifiers());
			GunModifierIndex.applyDefaults();
			GunModifier.ACTIVE.forEach((entry, modifier) -> {
				Item item = entry.value();
				if (item instanceof GunItem gunItem) {
					modifier.apply(gunItem);
				}
			});
			GunModifierFiles.saveModifierMap(server.getPath("mercfrontcore").resolve(GunModifierFiles.GUN_MODIFIERS_PATH), packet.modifiers());
			PacketDistributor.sendToAllPlayers(new GunModifiersPacket(Map.copyOf(GunModifier.ACTIVE), true));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
