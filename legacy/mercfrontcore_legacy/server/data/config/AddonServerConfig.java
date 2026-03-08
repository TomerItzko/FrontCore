package red.vuis.mercfrontcore.server.data.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class AddonServerConfig {
	public static final AddonServerConfig INSTANCE;
	public static final ModConfigSpec SPEC;
	
	private final ModConfigSpec.ConfigValue<String> profileFetchHost;
	
	private AddonServerConfig(ModConfigSpec.Builder builder) {
		profileFetchHost = builder.define("profile_fetch_host", "https://blockfrontapi.vuis.dev");
	}
	
	public static String getProfileFetchHost() {
		return INSTANCE.profileFetchHost.get();
	}
	
	static {
		Pair<AddonServerConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(AddonServerConfig::new);
		INSTANCE = pair.getLeft();
		SPEC = pair.getRight();
	}
}
