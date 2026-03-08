package red.vuis.mercfrontcore.setup;

import java.util.List;

import com.boehmod.blockfront.common.item.GunItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class GunItemIndex {
	public static final List<Identifier> GUN_ITEMS = new ObjectArrayList<>();
	
	private GunItemIndex() {
	}
	
	public static void init() {
		for (Item item : Registries.ITEM) {
			if (item instanceof GunItem) {
				GUN_ITEMS.add(Registries.ITEM.getId(item));
			}
		}
	}
}
