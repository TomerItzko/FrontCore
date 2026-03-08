package red.vuis.mercfrontcore.command.bf;

import java.util.Map;

import com.boehmod.blockfront.map.effect.BulletTracerSpawnerMapEffect;
import com.boehmod.blockfront.map.effect.FallingArtilleryMapEffect;
import com.boehmod.blockfront.map.effect.LoopingSoundPointMapEffect;
import com.boehmod.blockfront.map.effect.ParticleEmitterMapEffect;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.util.RegistryUtils;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import red.vuis.mercfrontcore.util.AddonRegUtils;
import red.vuis.mercfrontcore.util.AddonUtils;
import red.vuis.mercfrontcore.util.property.PropertyEntry;
import red.vuis.mercfrontcore.util.property.PropertyHandler;
import red.vuis.mercfrontcore.util.property.PropertyRegistry;

import static red.vuis.mercfrontcore.util.AddonAccessors.accessBulletTracerSpawner;
import static red.vuis.mercfrontcore.util.AddonAccessors.accessFallingArtillery;
import static red.vuis.mercfrontcore.util.AddonAccessors.accessParticleEmitter;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public final class MapCommands {
	public static final PropertyRegistry MAP_EFFECT_PROPERTIES = new PropertyRegistry(
		new PropertyHandler<>(BulletTracerSpawnerMapEffect.class, Map.of(
			"chance", new PropertyEntry<>(Float::parseFloat, (m, v) -> accessBulletTracerSpawner(m, a -> a.setChance(v))),
			"endPosX", new PropertyEntry<>(Double::parseDouble, (m, v) -> accessBulletTracerSpawner(m, a -> a.setEndPos(AddonUtils.vec3WithX(a.getEndPos(), v)))),
			"endPosY", new PropertyEntry<>(Double::parseDouble, (m, v) -> accessBulletTracerSpawner(m, a -> a.setEndPos(AddonUtils.vec3WithY(a.getEndPos(), v)))),
			"endPosZ", new PropertyEntry<>(Double::parseDouble, (m, v) -> accessBulletTracerSpawner(m, a -> a.setEndPos(AddonUtils.vec3WithZ(a.getEndPos(), v)))),
			"playSound", new PropertyEntry<>(Boolean::parseBoolean, (m, v) -> accessBulletTracerSpawner(m, a -> a.setPlaySound(v))),
			"spreadX", new PropertyEntry<>(Double::parseDouble, (m, v) -> accessBulletTracerSpawner(m, a -> a.setSpread(AddonUtils.vec3WithX(a.getSpread(), v)))),
			"spreadY", new PropertyEntry<>(Double::parseDouble, (m, v) -> accessBulletTracerSpawner(m, a -> a.setSpread(AddonUtils.vec3WithY(a.getSpread(), v)))),
			"spreadZ", new PropertyEntry<>(Double::parseDouble, (m, v) -> accessBulletTracerSpawner(m, a -> a.setSpread(AddonUtils.vec3WithZ(a.getSpread(), v))))
		)),
		new PropertyHandler<>(FallingArtilleryMapEffect.class, Map.of(
			"startX", new PropertyEntry<>(Float::parseFloat, (m, v) -> accessFallingArtillery(m, a -> a.setStart(AddonUtils.vec2WithX(a.getStart(), v)))),
			"startY", new PropertyEntry<>(Float::parseFloat, (m, v) -> accessFallingArtillery(m, a -> a.setStart(AddonUtils.vec2WithY(a.getStart(), v)))),
			"endX", new PropertyEntry<>(Float::parseFloat, (m, v) -> accessFallingArtillery(m, a -> a.setEnd(AddonUtils.vec2WithX(a.getEnd(), v)))),
			"endY", new PropertyEntry<>(Float::parseFloat, (m, v) -> accessFallingArtillery(m, a -> a.setEnd(AddonUtils.vec2WithY(a.getEnd(), v))))
		)),
		new PropertyHandler<>(LoopingSoundPointMapEffect.class, Map.of(
			"activationDistance", new PropertyEntry<>(Float::parseFloat, LoopingSoundPointMapEffect::setActivationDistance),
			"maxTime", new PropertyEntry<>(Integer::parseUnsignedInt, (m, v) -> m.maxTime = v),
			"pitch", new PropertyEntry<>(Float::parseFloat, LoopingSoundPointMapEffect::setPitch),
			"sound", new PropertyEntry<>(RegistryUtils::retrieveSoundEvent, (m, v) -> m.sound = v),
			"volume", new PropertyEntry<>(Float::parseFloat, LoopingSoundPointMapEffect::setVolume)
		)),
		new PropertyHandler<>(ParticleEmitterMapEffect.class, Map.of(
			"maxTick", new PropertyEntry<>(Integer::parseUnsignedInt, (m, v) -> accessParticleEmitter(m, a -> a.setMaxTick(v))),
			"particle", new PropertyEntry<>(AddonRegUtils::getSimpleParticle, (m, v) -> accessParticleEmitter(m, a -> a.setParticle(v))),
			"sound", new PropertyEntry<>(RegistryUtils::retrieveSoundEvent, (m, v) -> accessParticleEmitter(m, a -> a.setSound(v))),
			"soundVolume", new PropertyEntry<>(Float::parseFloat, (m, v) -> accessParticleEmitter(m, a -> a.setSoundVolume(v)))
		)),
		new PropertyHandler<>(PositionedMapEffect.class, Map.of(
			"x", new PropertyEntry<>(Double::parseDouble, (m, v) -> m.position = AddonUtils.vec3WithX(m.position, v)),
			"y", new PropertyEntry<>(Double::parseDouble, (m, v) -> m.position = AddonUtils.vec3WithY(m.position, v)),
			"z", new PropertyEntry<>(Double::parseDouble, (m, v) -> m.position = AddonUtils.vec3WithZ(m.position, v))
		))
	);
	
	private MapCommands() {
	}
	
	public static @Nullable BulletTracerSpawnerMapEffect parseBulletTracerSpawner(String[] args) {
		if (!(args.length == 6 || args.length == 11)) {
			return null;
		}
		
		var x = AddonUtils.parse(Double::parseDouble, args[0]);
		var y = AddonUtils.parse(Double::parseDouble, args[1]);
		var z = AddonUtils.parse(Double::parseDouble, args[2]);
		var endPosX = AddonUtils.parse(Double::parseDouble, args[3]);
		var endPosY = AddonUtils.parse(Double::parseDouble, args[4]);
		var endPosZ = AddonUtils.parse(Double::parseDouble, args[5]);
		
		if (AddonUtils.anyEmpty(x, y, z, endPosX, endPosY, endPosZ)) {
			return null;
		}
		
		var mapEffect = new BulletTracerSpawnerMapEffect(new Vec3d(x.get(), y.get(), z.get()), new Vec3d(endPosX.get(), endPosY.get(), endPosZ.get()));
		
		if (args.length == 11) {
			var chance = AddonUtils.parse(Float::parseFloat, args[6]);
			var playSound = AddonUtils.parse(Boolean::parseBoolean, args[7]);
			var spreadX = AddonUtils.parse(Double::parseDouble, args[8]);
			var spreadY = AddonUtils.parse(Double::parseDouble, args[9]);
			var spreadZ = AddonUtils.parse(Double::parseDouble, args[10]);
			
			if (AddonUtils.anyEmpty(x, y, z, endPosX, endPosY, endPosZ)) {
				return null;
			}
			
			accessBulletTracerSpawner(mapEffect, accessor -> {
				accessor.setChance(chance.get());
				accessor.setPlaySound(playSound.get());
			});
			mapEffect.method_3105(new Vec3d(spreadX.get(), spreadY.get(), spreadZ.get()));
		}
		
		return mapEffect;
	}
	
	public static @Nullable LoopingSoundPointMapEffect parseLoopingSoundPoint(String[] args) {
		if (args.length != 5) {
			return null;
		}
		
		var sound = AddonUtils.parse(RegistryUtils::retrieveSoundEvent, args[0]);
		var maxTime = AddonUtils.parse(Integer::parseInt, args[1]);
		var x = AddonUtils.parse(Double::parseDouble, args[2]);
		var y = AddonUtils.parse(Double::parseDouble, args[3]);
		var z = AddonUtils.parse(Double::parseDouble, args[4]);
		
		if (AddonUtils.anyEmpty(sound, maxTime, x, y, z)) {
			return null;
		}
		
		return new LoopingSoundPointMapEffect(sound.get(), new Vec3d(x.get(), y.get(), z.get()), maxTime.get());
	}
	
	public static @Nullable ParticleEmitterMapEffect parseParticleEmitter(String[] args) {
		if (!(args.length == 5 || args.length == 7)) {
			return null;
		}
		
		var particle = AddonUtils.parse(AddonRegUtils::getSimpleParticle, args[0]);
		var maxTick = AddonUtils.parse(Integer::parseInt, args[1]);
		var x = AddonUtils.parse(Double::parseDouble, args[2]);
		var y = AddonUtils.parse(Double::parseDouble, args[3]);
		var z = AddonUtils.parse(Double::parseDouble, args[4]);
		
		if (AddonUtils.anyEmpty(particle, maxTick, x, y, z)) {
			return null;
		}
		
		var mapEffect = new ParticleEmitterMapEffect(particle.get(), new Vec3d(x.get(), y.get(), z.get()), maxTick.get());
		
		if (args.length == 7) {
			var sound = AddonUtils.parse(RegistryUtils::retrieveSoundEvent, args[5]);
			var soundVolume = AddonUtils.parse(Float::parseFloat, args[6]);
			
			if (AddonUtils.anyEmpty(sound, soundVolume)) {
				return null;
			}
			
			mapEffect.method_3111(sound.get(), soundVolume.get());
		}
		
		return mapEffect;
	}
}
