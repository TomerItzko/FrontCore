package red.vuis.mercfrontcore;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import red.vuis.mercfrontcore.registry.AddonBlocks;
import red.vuis.mercfrontcore.registry.AddonItemGroups;
import red.vuis.mercfrontcore.registry.AddonItems;
import red.vuis.mercfrontcore.registry.AddonSounds;
import red.vuis.mercfrontcore.setup.AddonRegistryMigration;

@Mod(AddonConstants.MOD_ID)
public final class MercFrontCore {
	public MercFrontCore(IEventBus eventBus) {
		AddonBlocks.init(eventBus);
		AddonItemGroups.init(eventBus);
		AddonItems.init(eventBus);
		AddonSounds.init(eventBus);
		
		AddonRegistryMigration.init();
		
		AddonConstants.LOGGER.info("Initialized MERCFront-core for common!");
	}
}
