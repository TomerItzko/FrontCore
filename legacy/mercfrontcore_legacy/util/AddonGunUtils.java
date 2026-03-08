package red.vuis.mercfrontcore.util;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.BFDataComponents;
import net.minecraft.item.ItemStack;

public final class AddonGunUtils {
	private AddonGunUtils() {
	}
	
	public static ItemStack optimizeComponents(ItemStack itemStack) {
		if (itemStack.has(BFDataComponents.BARREL_TYPE) && GunItem.getBarrelType(itemStack).equals("default")) {
			itemStack.remove(BFDataComponents.BARREL_TYPE);
		}
		if (itemStack.has(BFDataComponents.MAG_TYPE) && GunItem.getMagType(itemStack).equals("default")) {
			itemStack.remove(BFDataComponents.MAG_TYPE);
		}
		if (itemStack.has(BFDataComponents.SCOPE) && !GunItem.getScope(itemStack)) {
			itemStack.remove(BFDataComponents.SCOPE);
		}
		return itemStack;
	}
}
