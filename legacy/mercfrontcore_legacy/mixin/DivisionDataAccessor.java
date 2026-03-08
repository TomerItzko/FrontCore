package red.vuis.mercfrontcore.mixin;

import java.util.EnumMap;
import java.util.List;

import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DivisionData.class)
public interface DivisionDataAccessor {
	@Accessor("loadouts")
	EnumMap<MatchClass, List<Loadout>> getRawLoadouts();
}
