package red.vuis.mercfrontcore.mixin;

import java.util.Map;

import com.boehmod.blockfront.common.gun.GunBarrelType;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.GunItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GunItem.class)
public interface GunItemAccessor {
	@Accessor("magIdMap")
	@NotNull Map<String, GunMagType> getMagIdMap();
	
	@Accessor("barrelIdMap")
	@NotNull Map<String, GunBarrelType> getBarrelIdMap();
}
