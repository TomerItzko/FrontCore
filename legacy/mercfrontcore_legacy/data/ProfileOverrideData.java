package red.vuis.mercfrontcore.data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.player.AbstractCloudInventory;
import com.boehmod.bflib.cloud.common.player.AbstractPlayerCloudData;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import red.vuis.mercfrontcore.util.AddonUtils;

public record ProfileOverrideData(
	String username,
	int exp,
	int prestige,
	List<CloudItemStack> equipped
) {
	public static final PacketCodec<ByteBuf, ProfileOverrideData> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.STRING, ProfileOverrideData::username,
		PacketCodecs.VAR_INT, ProfileOverrideData::exp,
		PacketCodecs.VAR_INT, ProfileOverrideData::prestige,
		AddonPacketCodecs.CLOUD_ITEM_STACK.collect(PacketCodecs.toCollection(ObjectArrayList::new)), ProfileOverrideData::equipped,
		ProfileOverrideData::new
	);
	
	public static ProfileOverrideData of(AbstractPlayerCloudData<?> cloudData) {
		AbstractCloudInventory<?> inventory = cloudData.getInventory();
		return new ProfileOverrideData(
			cloudData.getUsername(),
			cloudData.getExp(),
			cloudData.getPrestigeLevel(),
			inventory.getEquippedItemStacks().stream().map(inventory::getStackFromUUID).filter(Optional::isPresent).map(Optional::get).toList()
		);
	}
	
	public void apply(AbstractPlayerCloudData<?> cloudData) {
		AbstractCloudInventory<?> inventory = cloudData.getInventory();
		cloudData.setUsername(username());
		cloudData.setExp(exp());
		cloudData.setPrestigeLevel(prestige());
		inventory.onReceiveSection(equipped(), 0);
		inventory.populateEquippedItems(
			AddonUtils.getBfManager().getCloudRegistry(),
			equipped().stream().map(CloudItemStack::getUUID).collect(Collectors.toSet())
		);
	}
}
