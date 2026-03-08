package red.vuis.mercfrontcore.setup;

import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFRes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import red.vuis.mercfrontcore.AddonConstants;

public final class AddonRegistryMigration {
	private AddonRegistryMigration() {
	}
	
	public static void init() {
		try {
			Field deferredRegisterField = BFItems.class.getDeclaredField("DR");
			Object deferredRegister = deferredRegisterField.get(null);
			Method addAlias = deferredRegister.getClass().getMethod("addAlias", net.minecraft.util.Identifier.class, net.minecraft.util.Identifier.class);
			addAlias.invoke(deferredRegister, BFRes.loc("ammo_crate"), BFRes.loc("ammo_box"));
		} catch (Throwable throwable) {
			AddonConstants.LOGGER.warn("Skipping BlockFront alias migration (BFItems.DR/addAlias not available in this runtime).");
		}
	}
}
