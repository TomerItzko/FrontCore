package red.vuis.mercfrontcore.setup;

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

import red.vuis.mercfrontcore.AddonConstants;

public final class GunSkinIndex {
	public static final Map<Identifier, BiMap<String, Float>> SKINS = HashBiMap.create();
	
	private GunSkinIndex() {
	}
	
	public static void init(CloudRegistry registry) {
		for (CloudItem<?> item : registry.getItems()) {
			if (!(item instanceof CloudItemGun) || item.isDefault() || item.isDeprecated()) {
				continue;
			}
			
			BiMap<String, Float> skinMap = SKINS.computeIfAbsent(BFRes.fromCloud(item.getMinecraftItem()), key -> HashBiMap.create());
			
			float skinId = item.getSkin();
			if (skinMap.containsValue(skinId)) {
				String existingName = skinMap.inverse().get(skinId);
				String newName = item.getSuffixForDisplay();
				AddonConstants.LOGGER.warn("Item {} has duplicate skin IDs! (\"{}\" and \"{}\")", item.getMinecraftItem(), existingName, newName);
			} else {
				skinMap.put(item.getSuffixForDisplay(), item.getSkin());
			}
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
		return Optional.ofNullable(SKINS.get(itemId).get(name));
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
}
