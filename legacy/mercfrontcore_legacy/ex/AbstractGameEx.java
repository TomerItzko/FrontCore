package red.vuis.mercfrontcore.ex;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;

public interface AbstractGameEx {
	boolean mercfrontcore$isForceClientConfig();
	
	AddonClientConfig.Data mercfrontcore$getClientConfig();
}
