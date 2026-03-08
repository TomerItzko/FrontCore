package red.vuis.mercfrontcore.util;

import java.util.function.Consumer;
import java.util.function.Function;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.map.effect.BulletTracerSpawnerMapEffect;
import com.boehmod.blockfront.map.effect.FallingArtilleryMapEffect;
import com.boehmod.blockfront.map.effect.ParticleEmitterMapEffect;

import red.vuis.mercfrontcore.mixin.BulletTracerSpawnerMapEffectAccessor;
import red.vuis.mercfrontcore.mixin.DivisionDataAccessor;
import red.vuis.mercfrontcore.mixin.FallingArtilleryMapEffectAccessor;
import red.vuis.mercfrontcore.mixin.GameShopItemAccessor;
import red.vuis.mercfrontcore.mixin.GameStageTimerAccessor;
import red.vuis.mercfrontcore.mixin.GunItemAccessor;
import red.vuis.mercfrontcore.mixin.ParticleEmitterMapEffectAccessor;

/**
 * Accessor helper methods which are meant to be statically imported.
 */
public final class AddonAccessors {
	private AddonAccessors() {
	}
	
	public static BulletTracerSpawnerMapEffect accessBulletTracerSpawner(BulletTracerSpawnerMapEffect mapEffect, Consumer<BulletTracerSpawnerMapEffectAccessor> consumer) {
		consumer.accept((BulletTracerSpawnerMapEffectAccessor) mapEffect);
		return mapEffect;
	}
	
	public static <R> R applyBulletTracerSpawner(BulletTracerSpawnerMapEffect mapEffect, Function<BulletTracerSpawnerMapEffectAccessor, R> function) {
		return function.apply((BulletTracerSpawnerMapEffectAccessor) mapEffect);
	}
	
	public static DivisionData accessDivisionData(DivisionData divisionData, Consumer<DivisionDataAccessor> consumer) {
		consumer.accept((DivisionDataAccessor) (Object) divisionData);
		return divisionData;
	}
	
	public static <R> R applyDivisionData(DivisionData divisionData, Function<DivisionDataAccessor, R> function) {
		return function.apply((DivisionDataAccessor) (Object) divisionData);
	}
	
	public static FallingArtilleryMapEffect accessFallingArtillery(FallingArtilleryMapEffect mapEffect, Consumer<FallingArtilleryMapEffectAccessor> consumer) {
		consumer.accept((FallingArtilleryMapEffectAccessor) mapEffect);
		return mapEffect;
	}
	
	public static <R> R applyFallingArtillery(FallingArtilleryMapEffect mapEffect, Function<FallingArtilleryMapEffectAccessor, R> function) {
		return function.apply((FallingArtilleryMapEffectAccessor) mapEffect);
	}
	
	public static GameShopItem accessGameShopItem(GameShopItem item, Consumer<GameShopItemAccessor> consumer) {
		consumer.accept((GameShopItemAccessor) item);
		return item;
	}
	
	public static <R> R applyGameShopItem(GameShopItem item, Function<GameShopItemAccessor, R> function) {
		return function.apply((GameShopItemAccessor) item);
	}
	
	public static GameStageTimer accessGameStageTimer(GameStageTimer timer, Consumer<GameStageTimerAccessor> consumer) {
		consumer.accept((GameStageTimerAccessor) (Object) timer);
		return timer;
	}
	
	public static <R> R applyGameStageTimer(GameStageTimer timer, Function<GameStageTimerAccessor, R> function) {
		return function.apply((GameStageTimerAccessor) (Object) timer);
	}
	
	public static GunItem accessGunItem(GunItem mapEffect, Consumer<GunItemAccessor> consumer) {
		consumer.accept((GunItemAccessor) (Object) mapEffect);
		return mapEffect;
	}
	
	public static <R> R applyGunItem(GunItem mapEffect, Function<GunItemAccessor, R> function) {
		return function.apply((GunItemAccessor) (Object) mapEffect);
	}
	
	public static ParticleEmitterMapEffect accessParticleEmitter(ParticleEmitterMapEffect mapEffect, Consumer<ParticleEmitterMapEffectAccessor> consumer) {
		consumer.accept((ParticleEmitterMapEffectAccessor) mapEffect);
		return mapEffect;
	}
	
	public static <R> R applyParticleEmitter(ParticleEmitterMapEffect mapEffect, Function<ParticleEmitterMapEffectAccessor, R> function) {
		return function.apply((ParticleEmitterMapEffectAccessor) mapEffect);
	}
}
