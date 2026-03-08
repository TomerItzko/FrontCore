package red.vuis.mercfrontcore.command.bf;

import com.boehmod.blockfront.assets.AssetCommandValidator;
import com.boehmod.blockfront.assets.AssetCommandValidators;

public final class AssetCommandValidatorsEx {
	private AssetCommandValidatorsEx() {
	}
	
	public static AssetCommandValidator count(String... args) {
		return AssetCommandValidators.count(args);
	}
}
