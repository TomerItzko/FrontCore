package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.map.effect.BulletTracerSpawnerMapEffect;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BulletTracerSpawnerMapEffect.class)
public interface BulletTracerSpawnerMapEffectAccessor {
	@Accessor("field_3152")
	Vec3d getEndPos();
	
	@Accessor("field_3152")
	void setEndPos(Vec3d endPos);
	
	@Accessor("field_3154")
	float getChance();
	
	@Accessor("field_3154")
	void setChance(float value);
	
	@Accessor("field_3153")
	boolean getPlaySound();
	
	@Accessor("field_3153")
	void setPlaySound(boolean value);
	
	@Accessor("field_3151")
	Vec3d getSpread();
	
	@Accessor("field_3151")
	void setSpread(Vec3d spread);
}
