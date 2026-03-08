package red.vuis.mercfrontcore.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.boehmod.blockfront.common.match.Loadout;
import net.minecraft.item.ItemStack;

public final class LoadoutCompat {
	private static final Method PRIMARY = find("getPrimary", "c");
	private static final Method SECONDARY = find("getSecondary", "d");
	private static final Method MELEE = find("getMelee", "e");
	private static final Method OFF_HAND = find("getOffHand", "f");
	private static final Method HEAD = find("getHead", "g");
	private static final Method CHEST = find("getChest", "h");
	private static final Method LEGS = find("getLegs", "i");
	private static final Method FEET = find("getFeet", "j");
	private static final Method EXTRA = find("getExtra", "t", "u");
	private static final Method GET_MINIMUM_XP = find("getMinimumXp", "aJ", "aI");
	private static final Method SET_MINIMUM_XP = findIntArg("setMinimumXp", "b", "a");

	private LoadoutCompat() {
	}

	public static ItemStack getPrimary(Loadout loadout) {
		return invokeItemStack(PRIMARY, loadout, "primary");
	}

	public static ItemStack getSecondary(Loadout loadout) {
		return invokeItemStack(SECONDARY, loadout, "secondary");
	}

	public static ItemStack getMelee(Loadout loadout) {
		return invokeItemStack(MELEE, loadout, "melee");
	}

	public static ItemStack getOffHand(Loadout loadout) {
		return invokeItemStack(OFF_HAND, loadout, "offhand");
	}

	public static ItemStack getHead(Loadout loadout) {
		return invokeItemStack(HEAD, loadout, "head");
	}

	public static ItemStack getChest(Loadout loadout) {
		return invokeItemStack(CHEST, loadout, "chest");
	}

	public static ItemStack getLegs(Loadout loadout) {
		return invokeItemStack(LEGS, loadout, "legs");
	}

	public static ItemStack getFeet(Loadout loadout) {
		return invokeItemStack(FEET, loadout, "feet");
	}

	@SuppressWarnings("unchecked")
	public static List<ItemStack> getExtra(Loadout loadout) {
		try {
			Object result = EXTRA.invoke(loadout);
			if (result instanceof List<?> list) {
				return (List<ItemStack>) list;
			}
		} catch (Throwable throwable) {
			throw new IllegalStateException("Failed to read loadout extra items.", throwable);
		}
		return Collections.emptyList();
	}

	public static int getMinimumXp(Loadout loadout) {
		try {
			Object result = GET_MINIMUM_XP.invoke(loadout);
			if (result instanceof Integer value) {
				return value;
			}
		} catch (Throwable throwable) {
			throw new IllegalStateException("Failed to read loadout minimum xp.", throwable);
		}
		return 0;
	}

	public static Loadout setMinimumXp(Loadout loadout, int value) {
		try {
			Object result = SET_MINIMUM_XP.invoke(loadout, value);
			if (result instanceof Loadout mapped) {
				return mapped;
			}
			return loadout;
		} catch (Throwable throwable) {
			throw new IllegalStateException("Failed to set loadout minimum xp.", throwable);
		}
	}

	private static Method find(String... names) {
		for (String name : names) {
			try {
				Method method = Loadout.class.getMethod(name);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException ignored) {
			}
		}
		throw new IllegalStateException("Failed to locate compatible Loadout getter method.");
	}

	private static Method findIntArg(String... names) {
		for (String name : names) {
			try {
				Method method = Loadout.class.getMethod(name, int.class);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException ignored) {
			}
		}
		throw new IllegalStateException("Failed to locate compatible Loadout integer method.");
	}

	private static ItemStack invokeItemStack(Method method, Loadout loadout, String slotName) {
		try {
			Object result = method.invoke(loadout);
			if (result instanceof ItemStack stack) {
				return stack;
			}
		} catch (Throwable throwable) {
			throw new IllegalStateException("Failed to read loadout " + slotName + " slot.", throwable);
		}
		return ItemStack.EMPTY;
	}
}
