package dev.tomerdev.mercfrontcore.net.packet;

import com.boehmod.blockfront.common.item.GunItem;
import dev.tomerdev.mercfrontcore.AddonConstants;
import net.minecraft.item.Item;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.data.WeaponExtraSettings;

public record GiveGunPacket(RegistryEntry<Item> item, WeaponExtraSettings settings) implements CustomPayload {
	private static final String GIVE_MENU_SKIN_MARKER = AddonConstants.MOD_ID + ":give_menu_skin";
	public static final Id<GiveGunPacket> ID = new Id<>(AddonConstants.id("give_gun"));
	public static final PacketCodec<RegistryByteBuf, GiveGunPacket> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.registryEntry(RegistryKeys.ITEM),
		GiveGunPacket::item,
		WeaponExtraSettings.PACKET_CODEC,
		GiveGunPacket::settings,
		GiveGunPacket::new
	);
	
	public GiveGunPacket {
		if (!(item.value() instanceof GunItem)) {
			throw new IllegalArgumentException("Item must be a GunItem!");
		}
	}
	
	public GiveGunPacket(Item item, WeaponExtraSettings settings) {
		this(Registries.ITEM.getEntry(item), settings);
	}
	
	@Override
	public @NotNull Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleServer(GiveGunPacket packet, IPayloadContext context) {
		if (!context.player().hasPermissionLevel(2)) {
			return;
		}
		ItemStack stack = packet.settings.getItemStack((GunItem) packet.item.value());
		if (packet.settings.skin != null && !packet.settings.skin.isBlank()) {
			NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, tag -> tag.putBoolean(GIVE_MENU_SKIN_MARKER, true));
		}
		context.player().giveItemStack(stack);
	}
}
