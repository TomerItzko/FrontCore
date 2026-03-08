package red.vuis.mercfrontcore.data.bf;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.render.effect.WeatherEffectType;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.MapEffectRegistry;
import com.boehmod.blockfront.util.RegistryUtils;
import net.minecraft.util.Identifier;

public final class AddonMapAssetData {
	private AddonMapAssetData() {
	}
	
	public static void readOldFDS(FDSTagCompound root, MapEnvironment env) {
		AddonFDS.ifInteger(root, "mapTime", env::setTime);
		if (root.getBoolean("hasCustomFog")) {
			env.setCustomFogDensity(
				root.getFloat("fogDensityNear", env.getNearFogDensity()),
				root.getFloat("fogDensityFar", env.getFarFogDensity())
			);
			AddonFDS.ifInteger(root, "fogColor", env::setCustomFogColor);
		}
		if (root.getBoolean("hasCustomSky")) {
			AddonFDS.ifInteger(root, "skyColor", env::setCustomSkyColor);
		}
		if (root.getBoolean("hasCustomWater")) {
			AddonFDS.ifInteger(root, "waterColor", env::setCustomWaterColor);
		}
		if (root.getBoolean("hasCustomLightColor")) {
			AddonFDS.ifInteger(root, "lightColor", env::setCustomLightColor);
		}
		AddonFDS.ifString(root, "soundOutdoors", id -> env.setExteriorSound(RegistryUtils.retrieveSoundEvent(id)));
		AddonFDS.ifString(root, "soundIndoors", id -> env.setInteriorSound(RegistryUtils.retrieveSoundEvent(id)));
		AddonFDS.ifBoolean(root, "disableClouds", env::setDisableClouds);
		AddonFDS.ifBoolean(root, "disableSky", env::setDisableSky);
		AddonFDS.ifString(root, "clientShader", id -> env.setShader(Identifier.tryParse(id)));
		
		int weatherEffectsSize = root.getInteger("weatherEffectsSize");
		for (int i = 0; i < weatherEffectsSize; i++) {
			try {
				env.addParticleEffect(WeatherEffectType.values()[root.getInteger("weatherEffect" + i)]);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
		
		int mapEffectSize = root.getInteger("mapEffectSize");
		for (int i = 0; i < mapEffectSize; i++) {
			Class<?> mapEffectClass = MapEffectRegistry.getEffect(root.getByte("mapEffectType" + i));
			if (mapEffectClass == null) continue;
			
			AbstractMapEffect mapEffect;
			try {
				mapEffect = (AbstractMapEffect) mapEffectClass.getDeclaredConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
			
			FDSTagCompound mapEffectRoot = root.getTagCompound("mapEffectTag" + i);
			if (mapEffectRoot == null) continue;
			mapEffect.readFromFDS(mapEffectRoot);
			
			env.addMapEffect(mapEffect);
		}
	}
}
