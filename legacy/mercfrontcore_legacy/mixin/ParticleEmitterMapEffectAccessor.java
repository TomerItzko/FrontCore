package red.vuis.mercfrontcore.mixin;

import java.util.function.Supplier;

import com.boehmod.blockfront.map.effect.ParticleEmitterMapEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleEmitterMapEffect.class)
public interface ParticleEmitterMapEffectAccessor {
	@Accessor("field_3165")
	SimpleParticleType getParticle();
	
	@Accessor("field_3165")
	void setParticle(SimpleParticleType particle);
	
	@Accessor("field_3168")
	int getMaxTick();
	
	@Accessor("field_3168")
	void setMaxTick(int maxTick);
	
	@Accessor("field_3167")
	Supplier<SoundEvent> getSound();
	
	@Accessor("field_3167")
	void setSound(Supplier<SoundEvent> sound);
	
	@Accessor("field_3166")
	float getSoundVolume();
	
	@Accessor("field_3166")
	void setSoundVolume(float soundVolume);
}
