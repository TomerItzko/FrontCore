package red.vuis.mercfrontcore.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import red.vuis.mercfrontcore.AddonConstants;

@SuppressWarnings("unused")
public final class AddonItems {
	public static final DeferredRegister.Items DR = DeferredRegister.createItems(AddonConstants.MOD_ID);
	
	private AddonItems() {
	}
	
	public static void init(IEventBus eventBus) {
		DR.register(eventBus);
	}
}
