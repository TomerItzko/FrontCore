package red.vuis.mercfrontcore.net.packet;

import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.WeaponExtraSettings;

public record GiveGunPacket(RegistryEntry<Item> item, WeaponExtraSettings settings) implements CustomPayload {
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
		context.player().giveItemStack(packet.settings.getItemStack((GunItem) packet.item.value()));
	}
}
