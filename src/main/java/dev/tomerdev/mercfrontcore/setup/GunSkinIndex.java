package dev.tomerdev.mercfrontcore.setup;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.util.BFRes;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.tomerdev.mercfrontcore.AddonConstants;

public final class GunSkinIndex {
	public static final Map<Identifier, BiMap<String, Float>> SKINS = HashBiMap.create();
    private static volatile boolean initialized = false;
    private static volatile long lastInitAttemptMs = 0L;
	
	private GunSkinIndex() {
	}
	
	public static void init(CloudRegistry registry) {
        SKINS.clear();
		for (CloudItem<?> item : registry.getItems()) {
			if (!(item instanceof CloudItemGun)) {
				continue;
			}
            if (item.getSkin() <= 0.0f) {
                continue;
            }
            String suffix = item.getSuffixForDisplay();
            if (suffix == null || suffix.isBlank()) {
                continue;
            }

			BiMap<String, Float> skinMap = SKINS.computeIfAbsent(BFRes.fromCloud(item.getMinecraftItem()), key -> HashBiMap.create());

			float skinId = item.getSkin();
			if (skinMap.containsValue(skinId)) {
				String existingName = skinMap.inverse().get(skinId);
				String newName = suffix;
				AddonConstants.LOGGER.warn("Item {} has duplicate skin IDs! (\"{}\" and \"{}\")", item.getMinecraftItem(), existingName, newName);
			} else {
				skinMap.put(suffix, item.getSkin());
			}
		}
        initialized = !SKINS.isEmpty();
        AddonConstants.LOGGER.info("GunSkinIndex init complete: gunsWithSkins={}", SKINS.size());
	}

    public static void ensureInitialized() {
        if (initialized && !SKINS.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastInitAttemptMs < 750L) {
            return;
        }
        lastInitAttemptMs = now;
        try {
            CloudRegistry registry = CloudRegistryAccess.resolveRegistryObject();
            if (registry != null) {
                init(registry);
                if (!initialized) {
                    AddonConstants.LOGGER.warn("GunSkinIndex attempted init but cloud registry had no gun skins yet; will retry.");
                }
                return;
            }
            initialized = false;
        } catch (Throwable ignored) {
            initialized = false;
        }
    }
	
	public static Optional<Float> getSkinId(@NotNull Item item, @Nullable String name) {
		if (name == null) {
			return Optional.empty();
		}
		Identifier itemId = Registries.ITEM.getId(item);
		if (!SKINS.containsKey(itemId)) {
			return Optional.empty();
		}
		BiMap<String, Float> skins = SKINS.get(itemId);
		Float direct = skins.get(name);
		if (direct != null) {
			return Optional.of(direct);
		}

		String normalizedRequested = normalize(name);
		for (Map.Entry<String, Float> entry : skins.entrySet()) {
			String key = entry.getKey();
			if (key == null) {
				continue;
			}
			if (key.equalsIgnoreCase(name) || normalize(key).equals(normalizedRequested)) {
				return Optional.ofNullable(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	public static Optional<String> getSkinName(@NotNull ItemStack itemStack) {
		Identifier itemId = Registries.ITEM.getId(itemStack.getItem());
		if (!SKINS.containsKey(itemId)) {
			return Optional.empty();
		}
		return Optional.ofNullable(SKINS.get(itemId).inverse().get(itemStack.get(BFDataComponents.SKIN_ID)));
	}
	
	public static Optional<Set<String>> getSkinNames(@NotNull Item item) {
		Identifier itemId = Registries.ITEM.getId(item);
		if (!SKINS.containsKey(itemId)) {
			return Optional.empty();
		}
		return Optional.of(SKINS.get(itemId).keySet());
	}

	private static String normalize(String value) {
		return value.toLowerCase(java.util.Locale.ROOT).replace(".", "").replace("-", "").replace("_", "").replace(" ", "");
	}
}
