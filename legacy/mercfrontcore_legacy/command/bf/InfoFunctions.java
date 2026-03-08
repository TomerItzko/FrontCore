package red.vuis.mercfrontcore.command.bf;

import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.BulletTracerSpawnerMapEffect;
import com.boehmod.blockfront.map.effect.FallingArtilleryMapEffect;
import com.boehmod.blockfront.map.effect.LoopingSoundPointMapEffect;
import com.boehmod.blockfront.map.effect.ParticleEmitterMapEffect;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.FDSPose;

import static red.vuis.mercfrontcore.util.AddonAccessors.applyBulletTracerSpawner;
import static red.vuis.mercfrontcore.util.AddonAccessors.applyFallingArtillery;
import static red.vuis.mercfrontcore.util.AddonAccessors.applyParticleEmitter;

public final class InfoFunctions {
	private InfoFunctions() {
	}
	
	public static String pose(FDSPose pose) {
		return String.format(
			"(x: %.2f, y: %.2f, z: %.2f)",
			pose.position.x,
			pose.position.y,
			pose.position.z
		);
	}
	
	public static String capturePoint(AbstractCapturePoint<?> capturePoint) {
		return String.format("%s %s", capturePoint.name, pose(capturePoint));
	}
	
	public static String mapEffectType(AbstractMapEffect absEffect) {
		return switch (absEffect) {
			case BulletTracerSpawnerMapEffect ignored -> "bulletTracerSpawner";
			case FallingArtilleryMapEffect ignored -> "fallingArtillery";
			case LoopingSoundPointMapEffect ignored -> "loopingSoundPoint";
			case ParticleEmitterMapEffect ignored -> "particleEmitter";
			default -> "unknown";
		};
	}
	
	public static String mapEffect(AbstractMapEffect absEffect) {
		return mapEffectType(absEffect) + switch (absEffect) {
			case BulletTracerSpawnerMapEffect effect -> applyBulletTracerSpawner(effect, accessor -> String.format(
				" (x: %.2f, y: %.2f, z: %.2f, endPosX: %.2f, endPosY: %.2f, endPosZ: %.2f, chance: %.2f, playSound: %b, spreadX: %.2f, spreadY: %.2f, spreadZ: %.2f",
				effect.position.x,
				effect.position.y,
				effect.position.z,
				accessor.getEndPos().x,
				accessor.getEndPos().y,
				accessor.getEndPos().z,
				accessor.getChance(),
				accessor.getPlaySound(),
				accessor.getSpread().x,
				accessor.getSpread().y,
				accessor.getSpread().z
			));
			case FallingArtilleryMapEffect effect -> applyFallingArtillery(effect, accessor -> String.format(
				" (startX: %.2f, startY: %.2f, endX: %.2f, endY: %.2f)",
				accessor.getStart().x,
				accessor.getStart().y,
				accessor.getEnd().x,
				accessor.getEnd().y
			));
			case LoopingSoundPointMapEffect effect -> String.format(
				" (sound: %s, maxTime: %d, x: %.2f, y: %.2f, z: %.2f, volume: %.2f, pitch: %.2f, activationDistance: %.2f)",
				effect.sound != null ? RegistryUtils.getSoundEventId(effect.sound.get()) : "null",
				effect.maxTime,
				effect.position.x,
				effect.position.y,
				effect.position.z,
				effect.volume,
				effect.pitch,
				effect.activationDistance
			);
			case ParticleEmitterMapEffect effect -> applyParticleEmitter(effect, accessor -> String.format(
				" (particle: %s, maxTick: %d, x: %.2f, y: %.2f, z: %.2f, sound: %s, soundVolume: %.2f)",
				accessor.getParticle() != null ? RegistryUtils.getParticleTypeId(accessor.getParticle()) : "null",
				accessor.getMaxTick(),
				effect.getPosition().x,
				effect.getPosition().y,
				effect.getPosition().z,
				accessor.getSound() != null ? RegistryUtils.getSoundEventId(accessor.getSound().get()) : "null",
				accessor.getSoundVolume()
			));
			default -> "";
		};
	}
}
