package red.vuis.mercfrontcore.setup;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Util;
import org.jetbrains.annotations.UnmodifiableView;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.data.AddonCodecs;
import red.vuis.mercfrontcore.mixin.DivisionDataAccessor;
import red.vuis.mercfrontcore.util.AddonGunUtils;
import red.vuis.mercfrontcore.util.LoadoutCompat;

import static red.vuis.mercfrontcore.util.AddonAccessors.accessDivisionData;
import static red.vuis.mercfrontcore.util.AddonAccessors.applyDivisionData;

public final class LoadoutIndex {
	public static final Map<String, BFCountry> COUNTRIES = new Object2ObjectOpenHashMap<>();
	public static final Map<BFCountry, List<String>> SKINS = new EnumMap<>(BFCountry.class);
	public static final Map<String, MatchClass> MATCH_CLASSES = new Object2ObjectOpenHashMap<>();
	public static final Map<Identifier, @UnmodifiableView List<Loadout>> DEFAULT = new Object2ObjectOpenHashMap<>();
	
	public static final List<Function<Loadout, ItemStack>> SLOT_FUNCS = List.of(
		LoadoutCompat::getPrimary, LoadoutCompat::getSecondary, LoadoutCompat::getMelee, LoadoutCompat::getOffHand,
		LoadoutCompat::getHead, LoadoutCompat::getChest, LoadoutCompat::getLegs, LoadoutCompat::getFeet
	);
	
	private LoadoutIndex() {
	}
	
	public static void init() {
		for (DivisionData division : DivisionData.INSTANCES) {
			BFCountry country = division.getCountry();
			String skin = division.getSkin();
			
			SKINS.get(country).add(skin);
			
			for (Map.Entry<MatchClass, ObjectList<Loadout>> entry : division.getLoadouts().entrySet()) {
				MatchClass matchClass = entry.getKey();
				List<Loadout> loadouts = entry.getValue();
				Identifier id = new Identifier(country, skin, matchClass);
				
				List<Loadout> clonedLoadouts = new ObjectArrayList<>(loadouts.size());
				for (Loadout loadout : loadouts) {
					clonedLoadouts.add(cloneLoadout(loadout));
				}
				DEFAULT.put(id, Collections.unmodifiableList(clonedLoadouts));
			}
		}
	}
	
	public static Loadout cloneLoadout(Loadout original) {
		Loadout loadout = new Loadout(
			AddonGunUtils.optimizeComponents(LoadoutCompat.getPrimary(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getSecondary(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getMelee(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getOffHand(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getHead(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getChest(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getLegs(original).copy()),
			AddonGunUtils.optimizeComponents(LoadoutCompat.getFeet(original).copy())
		)
			.addExtra(LoadoutCompat.getExtra(original).stream().map(ItemStack::copy).map(AddonGunUtils::optimizeComponents).toList())
			.method_3154(original.method_3160());
		return LoadoutCompat.setMinimumXp(loadout, LoadoutCompat.getMinimumXp(original));
	}
	
	public static Map<Identifier, List<Loadout>> copyFlat(Map<Identifier, List<Loadout>> original) {
		Map<Identifier, List<Loadout>> result = new Object2ObjectOpenHashMap<>();
		
		for (Map.Entry<Identifier, List<Loadout>> entry : original.entrySet()) {
			List<Loadout> clonedLoadouts = new ObjectArrayList<>();
			entry.getValue().forEach(loudout -> clonedLoadouts.add(cloneLoadout(loudout)));
			result.put(entry.getKey(), clonedLoadouts);
		}
		
		return result;
	}
	
	public static void apply(Map<Identifier, List<Loadout>> loadouts) {
		for (DivisionData divisionData : DivisionData.INSTANCES) {
			accessDivisionData(divisionData, accessor -> accessor.getRawLoadouts().clear());
		}
		
		for (Map.Entry<Identifier, List<Loadout>> entry : loadouts.entrySet()) {
			Identifier id = entry.getKey();
			List<Loadout> value = entry.getValue();
			
			if (value.isEmpty()) {
				continue;
			}
			
			DivisionData divisionData = DivisionData.getByCountryAndSkin(id.country(), id.skin());
			if (divisionData == null) {
				continue;
			}
			Map<MatchClass, List<Loadout>> rawLoadouts = applyDivisionData(divisionData, DivisionDataAccessor::getRawLoadouts);
			
			List<Loadout> clonedLoadouts = new ObjectArrayList<>();
			value.forEach(loudout -> clonedLoadouts.add(cloneLoadout(loudout)));
			
			rawLoadouts.put(id.matchClass(), clonedLoadouts);
		}
	}
	
	public static Map<Identifier, List<Loadout>> currentFlat() {
		Map<Identifier, List<Loadout>> result = new Object2ObjectOpenHashMap<>();
		
		for (DivisionData divisionData : DivisionData.INSTANCES) {
			BFCountry country = divisionData.getCountry();
			String skin = divisionData.getSkin();
			
			for (Map.Entry<MatchClass, ObjectList<Loadout>> loadoutData : divisionData.getLoadouts().entrySet()) {
				List<Loadout> clonedLoadouts = new ObjectArrayList<>();
				loadoutData.getValue().forEach(loudout -> clonedLoadouts.add(cloneLoadout(loudout)));
				result.put(new Identifier(country, skin, loadoutData.getKey()), clonedLoadouts);
			}
		}
		
		return result;
	}
	
	public static Map<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> flatToNested(Map<Identifier, List<Loadout>> flat) {
		Map<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> nested = new EnumMap<>(BFCountry.class);
		
		for (Map.Entry<LoadoutIndex.Identifier, List<Loadout>> entry : flat.entrySet()) {
			LoadoutIndex.Identifier id = entry.getKey();
			nested.computeIfAbsent(id.country(), k -> new Object2ObjectOpenHashMap<>())
				.computeIfAbsent(id.skin(), k -> new EnumMap<>(MatchClass.class))
				.put(id.matchClass(), entry.getValue());
		}
		
		return nested;
	}
	
	public static Map<Identifier, List<Loadout>> nestedToFlat(Map<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> nested) {
		Map<LoadoutIndex.Identifier, List<Loadout>> flat = new Object2ObjectOpenHashMap<>();
		
		for (Map.Entry<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> countryEntry : nested.entrySet()) {
			BFCountry country = countryEntry.getKey();
			for (Map.Entry<String, Map<MatchClass, List<Loadout>>> skinEntry : countryEntry.getValue().entrySet()) {
				String skin = skinEntry.getKey();
				for (Map.Entry<MatchClass, List<Loadout>> matchClassEntry : skinEntry.getValue().entrySet()) {
					MatchClass matchClass = matchClassEntry.getKey();
					flat.put(new LoadoutIndex.Identifier(country, skin, matchClass), matchClassEntry.getValue());
				}
			}
		}
		
		return flat;
	}
	
	public static boolean parseAndApply(Path indexPath) {
		AddonConstants.LOGGER.info("Parsing and applying loadout data from disk...");
		long startNs = Util.getMeasuringTimeNano();
		
		NbtCompound indexTag;
		try (InputStream input = Files.newInputStream(indexPath)) {
			indexTag = NbtIo.readCompound(new DataInputStream(input));
		} catch (NoSuchFileException e) {
			AddonConstants.LOGGER.error("Loadout file does not exist!");
			return false;
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Error while reading loadout data from disk!", e);
			return false;
		}
		
		var result = AddonCodecs.LOADOUT_INDEX.parse(NbtOps.INSTANCE, indexTag);
		if (result.isError()) {
			AddonConstants.LOGGER.error(result.error().orElseThrow());
			return false;
		}
		apply(result.getOrThrow());
		
		long endNs = Util.getMeasuringTimeNano();
		AddonConstants.LOGGER.info("Loadout data loaded in {} ms.", String.format("%.3f", (endNs - startNs) / 1.0E6));
		return true;
	}
	
	public static boolean saveCurrent(Path indexPath) {
		AddonConstants.LOGGER.info("Saving loadout data to disk...");
		long startNs = Util.getMeasuringTimeNano();
		
		NbtCompound encodeResult = (NbtCompound) AddonCodecs.LOADOUT_INDEX.encodeStart(NbtOps.INSTANCE, currentFlat())
			.resultOrPartial(AddonConstants.LOGGER::error)
			.orElseThrow();
		
		try (OutputStream output = Files.newOutputStream(indexPath)) {
			NbtIo.writeCompound(encodeResult, new DataOutputStream(output));
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Error while writing loadout data to disk!", e);
			return false;
		}
		
		long endNs = Util.getMeasuringTimeNano();
		AddonConstants.LOGGER.info("Loadout data saved in {} ms.", String.format("%.3f", (endNs - startNs) / 1.0E6));
		return true;
	}
	
	static {
		for (BFCountry country : BFCountry.values()) {
			COUNTRIES.put(country.getTag(), country);
			SKINS.put(country, new ObjectArrayList<>());
		}
		for (MatchClass matchClass : MatchClass.values()) {
			MATCH_CLASSES.put(matchClass.getKey(), matchClass);
		}
	}
	
	public record Identifier(BFCountry country, String skin, MatchClass matchClass) {
	}
}
