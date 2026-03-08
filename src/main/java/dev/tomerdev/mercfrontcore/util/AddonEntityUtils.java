package dev.tomerdev.mercfrontcore.util;

import java.util.Set;

import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.registry.BFEntityTypes;
import net.minecraft.entity.EntityType;

public final class AddonEntityUtils {
	public static Set<EntityType<? extends IProducedProjectileEntity>> PRODUCED_PROJECTILES = Set.of(
		BFEntityTypes.ANTI_AIR_ROCKET.get(),
		BFEntityTypes.ROCKET.get(),
		BFEntityTypes.AIRSTRIKE_ROCKET.get(),
		BFEntityTypes.PRECISION_AIRSTRIKE_ROCKET.get(),
		BFEntityTypes.TANK_ROCKET.get(),
		BFEntityTypes.FLAME_THROWER_FIRE.get(),
		BFEntityTypes.MELON_ROCKET.get()
	);
	
	private AddonEntityUtils() {
	}
}
