package red.vuis.mercfrontcore.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.gun.GunCameraConfig;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunFireMode;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import com.boehmod.blockfront.common.item.GunItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.util.AddonEntityUtils;

public record GunModifier(
	Optional<Ammo> ammo,
	Optional<Camera> camera,
	Optional<List<Damage>> damage,
	Optional<List<FireMode>> fireModes,
	Optional<Spread> spread,
	Optional<Float> weight
) {
	public static final GunModifier EMPTY = new GunModifier(
		Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
	);
	public static final Map<RegistryEntry<Item>, GunModifier> ACTIVE = new Object2ObjectOpenHashMap<>();
	
	public static final Codec<GunModifier> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Ammo.CODEC.optionalFieldOf("ammo").forGetter(GunModifier::ammo),
			Camera.CODEC.optionalFieldOf("camera").forGetter(GunModifier::camera),
			Damage.CODEC.listOf(1, Integer.MAX_VALUE).optionalFieldOf("damage").forGetter(GunModifier::damage),
			FireMode.CODEC.listOf(1, Integer.MAX_VALUE).optionalFieldOf("fire_modes").forGetter(GunModifier::fireModes),
			Spread.CODEC.optionalFieldOf("spread").forGetter(GunModifier::spread),
			Codec.FLOAT.optionalFieldOf("weight").forGetter(GunModifier::weight)
		).apply(instance, GunModifier::new)
	);
	public static final PacketCodec<RegistryByteBuf, GunModifier> PACKET_CODEC = PacketCodec.tuple(
		Ammo.PACKET_CODEC.collect(PacketCodecs::optional), GunModifier::ammo,
		Camera.PACKET_CODEC.collect(PacketCodecs::optional), GunModifier::camera,
		Damage.PACKET_CODEC.collect(PacketCodecs.toList()).collect(PacketCodecs::optional), GunModifier::damage,
		FireMode.PACKET_CODEC.collect(PacketCodecs.toList()).collect(PacketCodecs::optional), GunModifier::fireModes,
		Spread.PACKET_CODEC.collect(PacketCodecs::optional), GunModifier::spread,
		PacketCodecs.FLOAT.collect(PacketCodecs::optional), GunModifier::weight,
		GunModifier::new
	);
	
	public GunModifier(Ammo ammo, Camera camera, List<Damage> damage, List<FireMode> fireModes, Spread spread, float weight) {
		this(
			Optional.of(ammo),
			Optional.of(camera),
			Optional.of(damage),
			Optional.of(fireModes),
			Optional.of(spread),
			Optional.of(weight)
		);
	}
	
	public GunModifier withAmmo(Optional<Ammo> ammo) {
		return new GunModifier(ammo, camera, damage, fireModes, spread, weight);
	}
	
	public GunModifier withCamera(Optional<Camera> camera) {
		return new GunModifier(ammo, camera, damage, fireModes, spread, weight);
	}
	
	public GunModifier withDamage(Optional<List<Damage>> damage) {
		return new GunModifier(ammo, camera, damage, fireModes, spread, weight);
	}
	
	public GunModifier withFireModes(Optional<List<FireMode>> fireModes) {
		return new GunModifier(ammo, camera, damage, fireModes, spread, weight);
	}
	
	public GunModifier withSpread(Optional<Spread> spread) {
		return new GunModifier(ammo, camera, damage, fireModes, spread, weight);
	}
	
	public GunModifier withWeight(Optional<Float> weight) {
		return new GunModifier(ammo, camera, damage, fireModes, spread, weight);
	}
	
	public void apply(@NotNull GunItem item) {
		ammo.ifPresent(ammo -> Ammo.apply(ammo, item));
		camera.ifPresent(camera -> Camera.apply(camera, item));
		damage.ifPresent(damage -> Damage.apply(damage, item));
		fireModes.ifPresent(fireModes -> FireMode.apply(fireModes, item));
		spread.ifPresent(spread -> Spread.apply(spread, item));
		weight.ifPresent(item::weight);
	}
	
	public boolean hasData() {
		return ammo.isPresent() || camera.isPresent() || damage.isPresent() || fireModes.isPresent() || spread.isPresent() || weight.isPresent();
	}
	
	public record Ammo(
		int magazine,
		int reserve
	) {
		public static final Codec<Ammo> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				Codec.intRange(0, Integer.MAX_VALUE).fieldOf("magazine").forGetter(Ammo::magazine),
				Codec.intRange(0, Integer.MAX_VALUE).fieldOf("reserve").forGetter(Ammo::reserve)
			).apply(instance, Ammo::new)
		);
		public static final PacketCodec<ByteBuf, Ammo> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_INT, Ammo::magazine,
			PacketCodecs.VAR_INT, Ammo::reserve,
			Ammo::new
		);
		
		public static Ammo of(GunMagType magType) {
			return new Ammo(magType.capacity(), magType.maxAmmo());
		}
		
		private static void apply(Ammo ammo, @NotNull GunItem item) {
			item.defaultMag(ammo.magazine, ammo.reserve);
		}
	}
	
	public record Camera(
		float jumpingRecoil,
		float sprintingRecoil,
		float walkingRecoil,
		float idleRecoil,
		float crawlingRecoil,
		float recoverSpeed
	) {
		public static final Codec<Camera> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("jumping_recoil").forGetter(Camera::jumpingRecoil),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("sprinting_recoil").forGetter(Camera::sprintingRecoil),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("walking_recoil").forGetter(Camera::walkingRecoil),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("idle_recoil").forGetter(Camera::idleRecoil),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("crawling_recoil").forGetter(Camera::crawlingRecoil),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("recover_speed").forGetter(Camera::recoverSpeed)
			).apply(instance, Camera::new)
		);
		public static final PacketCodec<ByteBuf, Camera> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.FLOAT, Camera::jumpingRecoil,
			PacketCodecs.FLOAT, Camera::sprintingRecoil,
			PacketCodecs.FLOAT, Camera::walkingRecoil,
			PacketCodecs.FLOAT, Camera::idleRecoil,
			PacketCodecs.FLOAT, Camera::crawlingRecoil,
			PacketCodecs.FLOAT, Camera::recoverSpeed,
			Camera::new
		);
		
		public static Camera of(GunCameraConfig config) {
			return new Camera(
				config.jumpingAmount(),
				config.sprintingAmount(),
				config.walkingAmount(),
				config.idleAmount(),
				config.crawlingAmount(),
				config.recoverSpeed()
			);
		}
		
		private static void apply(Camera camera, @NotNull GunItem item) {
			GunCameraConfig prevConfig = item.getCameraConfig();
			item.camera(new GunCameraConfig(
				prevConfig.maxModelDistance(),
				prevConfig.maxModelPitch(),
				prevConfig.maxCameraPitch(),
				camera.recoverSpeed,
				camera.idleRecoil,
				camera.walkingRecoil,
				camera.sprintingRecoil,
				camera.crawlingRecoil,
				camera.jumpingRecoil
			));
		}
	}
	
	public record Damage(
		float body,
		float head,
		float minDist
	) {
		public static final Codec<Damage> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("body").forGetter(Damage::body),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("head").forGetter(Damage::head),
				Codec.floatRange(0f, Float.MAX_VALUE).optionalFieldOf("min_dist", 0f).forGetter(Damage::minDist)
			).apply(instance, Damage::new)
		);
		public static final PacketCodec<ByteBuf, Damage> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.FLOAT, Damage::body,
			PacketCodecs.FLOAT, Damage::head,
			PacketCodecs.FLOAT, Damage::minDist,
			Damage::new
		);
		
		public static List<Damage> of(GunDamageConfig config) {
			return config.getEntries().entrySet().stream().map(
				entry -> new Damage(entry.getValue().firstFloat(), entry.getValue().secondFloat(), entry.getKey())
			).toList();
		}
		
		private static void apply(List<Damage> damage, @NotNull GunItem item) {
			GunDamageConfig config = new GunDamageConfig(damage.getFirst().body, damage.getFirst().head);
			
			for (int i = 1; i < damage.size(); i++) {
				Damage additionalDamage = damage.get(i);
				config.add(additionalDamage.minDist, additionalDamage.body, additionalDamage.head);
			}
			
			item.damage(config);
		}
	}
	
	public record FireMode(
		GunTriggerSpawnType type,
		GunFireMode mode,
		int ticks,
		int numInstances,
		Optional<RegistryEntry<EntityType<?>>> entity
	) {
		public static final Codec<FireMode> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				AddonCodecs.GUN_TRIGGER_SPAWN_TYPE.fieldOf("type").forGetter(FireMode::type),
				AddonCodecs.GUN_FIRE_MODE.fieldOf("mode").forGetter(FireMode::mode),
				Codec.intRange(1, Integer.MAX_VALUE).fieldOf("ticks").forGetter(FireMode::ticks),
				Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("num_instances", 1).forGetter(FireMode::numInstances),
				Registries.ENTITY_TYPE.getEntryCodec().optionalFieldOf("entity").forGetter(FireMode::entity)
			).apply(instance, FireMode::new)
		);
		public static final PacketCodec<RegistryByteBuf, FireMode> PACKET_CODEC = PacketCodec.tuple(
			AddonPacketCodecs.GUN_TRIGGER_SPAWN_TYPE, FireMode::type,
			AddonPacketCodecs.GUN_FIRE_MODE, FireMode::mode,
			PacketCodecs.VAR_INT, FireMode::ticks,
			PacketCodecs.VAR_INT, FireMode::numInstances,
			PacketCodecs.registryEntry(RegistryKeys.ENTITY_TYPE).collect(PacketCodecs::optional), FireMode::entity,
			FireMode::new
		);
		
		public static List<FireMode> of(GunFireConfig[] fireConfigs) {
			return Arrays.stream(fireConfigs)
				.map(config -> new FireMode(
					config.method_4023(),
					config.getMode(),
					config.getFireRate(),
					config.method_4026(),
					Optional.ofNullable(config.method_4024())
						.map(Supplier::get)
						.map(Registries.ENTITY_TYPE::getEntry)
				))
				.toList();
		}
		
		@SuppressWarnings("unchecked")
		private static void apply(List<FireMode> fireModes, @NotNull GunItem item) {
			List<GunFireConfig> fireConfigs = new ArrayList<>();
			
			for (FireMode fireMode : fireModes) {
				if (fireMode.type == GunTriggerSpawnType.ENTITY && fireMode.entity.isPresent()) {
					EntityType<?> entityType = fireMode.entity.orElseThrow().value();
					
					if (!AddonEntityUtils.PRODUCED_PROJECTILES.contains(entityType)) {
						AddonConstants.LOGGER.error(
							"Entity type {} is not a valid projectile! Discarding fire mode.",
							Registries.ENTITY_TYPE.getId(entityType)
						);
						continue;
					}
				}
				
				switch (fireMode.type) {
					case BULLET -> fireConfigs.add(new GunFireConfig(
						fireMode.mode, fireMode.ticks, fireMode.numInstances
					));
					case ENTITY -> {
						if (fireMode.entity.isEmpty()) {
							AddonConstants.LOGGER.error("Entity is not specified for fire mode of type entity! Discarding fire mode.");
							break;
						}
						
						fireConfigs.add(new GunFireConfig(
							fireMode.mode, fireMode.ticks, () -> (EntityType<? extends IProducedProjectileEntity>) fireMode.entity.orElseThrow().value()
						));
					}
				}
			}
			
			if (fireConfigs.isEmpty()) {
				AddonConstants.LOGGER.error(
					"No valid fire modes for item {}! Not applying.",
					Registries.ITEM.getId(item)
				);
			}
			
			item.fireModes(fireConfigs.toArray(GunFireConfig[]::new));
		}
	}
	
	public record Spread(
		float jumping,
		float walking,
		float walkingAds,
		float idle,
		float idleAds,
		float crawling,
		float crawlingAds
	) {
		public static final Codec<Spread> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("jumping").forGetter(Spread::jumping),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("walking").forGetter(Spread::walking),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("walking_ads").forGetter(Spread::walkingAds),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("idle").forGetter(Spread::idle),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("idle_ads").forGetter(Spread::idleAds),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("crawling").forGetter(Spread::crawling),
				Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("crawling_ads").forGetter(Spread::crawlingAds)
			).apply(instance, Spread::new)
		);
		public static PacketCodec<ByteBuf, Spread> PACKET_CODEC = AddonPacketCodecs.tuple(
			PacketCodecs.FLOAT, Spread::jumping,
			PacketCodecs.FLOAT, Spread::walking,
			PacketCodecs.FLOAT, Spread::walkingAds,
			PacketCodecs.FLOAT, Spread::idle,
			PacketCodecs.FLOAT, Spread::idleAds,
			PacketCodecs.FLOAT, Spread::crawling,
			PacketCodecs.FLOAT, Spread::crawlingAds,
			Spread::new
		);
		
		public static Spread of(GunSpreadConfig config) {
			return new Spread(
				config.jumpingSpread(),
				config.walkingSpread(),
				config.walkingSpreadAiming(),
				config.idleSpread(),
				config.idleSpreadAiming(),
				config.crawlingSpread(),
				config.crawlingSpreadAiming()
			);
		}
		
		private static void apply(Spread spread, @NotNull GunItem item) {
			item.spread(new GunSpreadConfig(
				spread.jumping,
				spread.walking,
				spread.walkingAds,
				spread.idle,
				spread.idleAds,
				spread.crawling,
				spread.crawlingAds
			));
		}
	}
}
