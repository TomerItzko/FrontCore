package red.vuis.mercfrontcore.setup;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.boehmod.blockfront.common.item.GunItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.GunModifier;

public final class GunModifierIndex {
	public static final Map<RegistryEntry<Item>, GunModifier> DEFAULT = new Object2ObjectOpenHashMap<>();
	
	private GunModifierIndex() {
	}
	
	public static void init() {
		for (Item item : Registries.ITEM) {
			if (!(item instanceof GunItem gunItem)) {
				continue;
			}
			Optional<GunModifier.Ammo> ammo = getAmmo(gunItem);
			Optional<GunModifier.Camera> camera = safe(() -> GunModifier.Camera.of(gunItem.getCameraConfig()), gunItem, "camera config");
			Optional<java.util.List<GunModifier.Damage>> damage = safe(() -> GunModifier.Damage.of(gunItem.getDamageConfig()), gunItem, "damage config");
			Optional<java.util.List<GunModifier.FireMode>> fireModes = safe(() -> GunModifier.FireMode.of(gunItem.getFireConfigs()), gunItem, "fire config");
			Optional<GunModifier.Spread> spread = safe(() -> GunModifier.Spread.of(gunItem.getAimConfig()), gunItem, "spread config");
			Optional<Float> weight = safe(() -> gunItem.getWeight(null), gunItem, "weight");

			DEFAULT.put(
				Registries.ITEM.getEntry(gunItem),
				new GunModifier(
					ammo,
					camera,
					damage,
					fireModes,
					spread,
					weight
				)
			);
		}
	}
	
	public static void applyDefaults() {
		for (Map.Entry<RegistryEntry<Item>, GunModifier> entry : DEFAULT.entrySet()) {
			if (entry.getKey().value() instanceof GunItem gunItem) {
				entry.getValue().apply(gunItem);
			}
		}
	}

	private static Optional<GunModifier.Ammo> getAmmo(GunItem gunItem) {
		ItemStack stack = new ItemStack(gunItem);
		try {
			return Optional.of(GunModifier.Ammo.of(gunItem.getMagTypeOrDefault(stack)));
		} catch (Throwable ignored) {
		}

		try {
			Method named = gunItem.getClass().getMethod("getMagTypeOrDefault", ItemStack.class);
			named.setAccessible(true);
			Object result = named.invoke(gunItem, stack);
			if (result instanceof com.boehmod.blockfront.common.gun.GunMagType magType) {
				return Optional.of(GunModifier.Ammo.of(magType));
			}
		} catch (Throwable ignored) {
		}

		for (Method method : gunItem.getClass().getMethods()) {
			if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != ItemStack.class) {
				continue;
			}
			if (!"com.boehmod.blockfront.common.gun.GunMagType".equals(method.getReturnType().getName())) {
				continue;
			}
			try {
				method.setAccessible(true);
				Object result = method.invoke(gunItem, stack);
				if (result instanceof com.boehmod.blockfront.common.gun.GunMagType magType) {
					AddonConstants.LOGGER.warn(
						"Using fallback method {} for {} ammo defaults.",
						method.getName(),
						Registries.ITEM.getId(gunItem)
					);
					return Optional.of(GunModifier.Ammo.of(magType));
				}
			} catch (Throwable ignored) {
			}
		}

		AddonConstants.LOGGER.warn("Skipping ammo defaults for {} (GunItem API mismatch).", Registries.ITEM.getId(gunItem));
		return Optional.empty();
	}

	private static <T> Optional<T> safe(Supplier<T> getter, GunItem gunItem, String partName) {
		try {
			return Optional.ofNullable(getter.get());
		} catch (Throwable throwable) {
			AddonConstants.LOGGER.warn(
				"Skipping {} defaults for {} ({}).",
				partName,
				Registries.ITEM.getId(gunItem),
				throwable.getClass().getSimpleName()
			);
			return Optional.empty();
		}
	}
}
