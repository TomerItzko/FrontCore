package red.vuis.mercfrontcore.registry;

import com.boehmod.blockfront.registry.BFItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import red.vuis.mercfrontcore.AddonConstants;

@SuppressWarnings("unused")
public final class AddonItemGroups {
	public static final DeferredRegister<ItemGroup> DR = DeferredRegister.create(Registries.ITEM_GROUP, AddonConstants.MOD_ID);
	
	public static final Object FRONT_UTILITIES = DR.register(
		"front_utilities",
		() -> ItemGroup.builder()
			.displayName(Text.translatable("itemGroup.mercfrontcore"))
			.icon(() -> new ItemStack(BFItems.MELEE_ITEM_WRENCH.get()))
			.entries((displayContext, entries) -> {
				entries.add(AddonBlocks.BLOOD_NO_COLLISION.get());
				entries.add(AddonBlocks.DEAD_FISH_NO_COLLISION.get());
			})
			.build()
	);
	
	private AddonItemGroups() {
	}
	
	public static void init(IEventBus eventBus) {
		DR.register(eventBus);
	}
}
