package red.vuis.mercfrontcore.server;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.server.data.config.AddonServerConfig;

@Mod(
	value = AddonConstants.MOD_ID,
	dist = Dist.DEDICATED_SERVER
)
public final class MercFrontCoreServer {
	public MercFrontCoreServer(ModContainer container) {
		container.registerConfig(ModConfig.Type.SERVER, AddonServerConfig.SPEC);
		
		AddonConstants.LOGGER.info("Initialized MERCFront-core for server!");
	}
}
