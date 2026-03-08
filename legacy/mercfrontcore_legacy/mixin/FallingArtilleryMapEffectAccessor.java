package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.map.effect.FallingArtilleryMapEffect;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallingArtilleryMapEffect.class)
public interface FallingArtilleryMapEffectAccessor {
	@Accessor("field_3050")
	Vec2f getStart();
	
	@Accessor("field_3050")
	void setStart(Vec2f start);
	
	@Accessor("field_3051")
	Vec2f getEnd();
	
	@Accessor("field_3051")
	void setEnd(Vec2f start);
}
