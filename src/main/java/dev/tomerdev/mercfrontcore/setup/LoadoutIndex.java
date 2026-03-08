package dev.tomerdev.mercfrontcore.setup;

import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.util.LoadoutCompat;

public final class LoadoutIndex {
    public static final Map<String, BFCountry> COUNTRIES = new Object2ObjectOpenHashMap<>();
    public static final Map<BFCountry, List<String>> SKINS = new EnumMap<>(BFCountry.class);
    public static final Map<String, MatchClass> MATCH_CLASSES = new Object2ObjectOpenHashMap<>();
    public static final Map<Identifier, @UnmodifiableView List<Loadout>> DEFAULT = new Object2ObjectOpenHashMap<>();

    public static final List<Function<Loadout, ItemStack>> SLOT_FUNCS = List.of(
        LoadoutCompat::getPrimary, LoadoutCompat::getSecondary, LoadoutCompat::getMelee, LoadoutCompat::getOffHand,
        LoadoutCompat::getHead, LoadoutCompat::getChest, LoadoutCompat::getLegs, LoadoutCompat::getFeet
    );

    private static boolean initialized = false;
    private static Field divisionLoadoutsField;

    private LoadoutIndex() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        for (BFCountry country : BFCountry.values()) {
            COUNTRIES.put(country.getTag(), country);
            SKINS.computeIfAbsent(country, k -> new ObjectArrayList<>());
        }
        for (MatchClass matchClass : MatchClass.values()) {
            MATCH_CLASSES.put(matchClass.getKey(), matchClass);
        }

        for (DivisionData division : DivisionData.INSTANCES) {
            BFCountry country = division.getCountry();
            String skin = division.getSkin();
            SKINS.computeIfAbsent(country, k -> new ObjectArrayList<>()).add(skin);

            for (Map.Entry<MatchClass, ObjectList<Loadout>> entry : division.getLoadouts().entrySet()) {
                List<Loadout> cloned = new ObjectArrayList<>(entry.getValue().size());
                entry.getValue().forEach(loadout -> cloned.add(cloneLoadout(loadout)));
                DEFAULT.put(new Identifier(country, skin, entry.getKey()), Collections.unmodifiableList(cloned));
            }
        }
        initialized = true;
    }

    public static Loadout cloneLoadout(Loadout original) {
        Loadout loadout = new Loadout(
            LoadoutCompat.getPrimary(original).copy(),
            LoadoutCompat.getSecondary(original).copy(),
            LoadoutCompat.getMelee(original).copy(),
            LoadoutCompat.getOffHand(original).copy(),
            LoadoutCompat.getHead(original).copy(),
            LoadoutCompat.getChest(original).copy(),
            LoadoutCompat.getLegs(original).copy(),
            LoadoutCompat.getFeet(original).copy()
        );
        loadout.addExtra(LoadoutCompat.getExtra(original).stream().map(ItemStack::copy).toList());
        return LoadoutCompat.setMinimumXp(loadout, LoadoutCompat.getMinimumXp(original));
    }

    public static Map<Identifier, List<Loadout>> copyFlat(Map<Identifier, List<Loadout>> original) {
        Map<Identifier, List<Loadout>> result = new Object2ObjectOpenHashMap<>();
        original.forEach((id, loadouts) -> {
            List<Loadout> cloned = new ObjectArrayList<>(loadouts.size());
            loadouts.forEach(loadout -> cloned.add(cloneLoadout(loadout)));
            result.put(id, cloned);
        });
        return result;
    }

    public static void apply(Map<Identifier, List<Loadout>> loadouts) {
        Map<DivisionData, Map<MatchClass, List<Loadout>>> mutableMaps = new Object2ObjectOpenHashMap<>();
        for (DivisionData divisionData : DivisionData.INSTANCES) {
            Map<MatchClass, List<Loadout>> mutable = rawLoadouts(divisionData);
            if (mutable == null) {
                MercFrontCore.LOGGER.warn(
                    "Could not locate mutable loadouts map for division {}:{}; skipping loadout apply.",
                    divisionData.getCountry(),
                    divisionData.getSkin()
                );
                continue;
            }
            mutable.clear();
            mutableMaps.put(divisionData, mutable);
        }

        loadouts.forEach((id, value) -> {
            if (value.isEmpty()) {
                return;
            }
            DivisionData divisionData = DivisionData.getByCountryAndSkin(id.country(), id.skin());
            if (divisionData == null) {
                return;
            }
            Map<MatchClass, List<Loadout>> mutable = mutableMaps.get(divisionData);
            if (mutable == null) {
                return;
            }
            ObjectList<Loadout> cloned = new ObjectArrayList<>(value.size());
            value.forEach(loadout -> cloned.add(cloneLoadout(loadout)));
            mutable.put(id.matchClass(), cloned);
        });
    }

    @SuppressWarnings("unchecked")
    private static Map<MatchClass, List<Loadout>> rawLoadouts(DivisionData divisionData) {
        Field loadoutsField = resolveDivisionLoadoutsField(divisionData);
        if (loadoutsField != null) {
            try {
                Object value = loadoutsField.get(divisionData);
                if (value instanceof Map<?, ?> map && isLikelyMutableMap(map)) {
                    return (Map<MatchClass, List<Loadout>>) (Map<?, ?>) map;
                }
            } catch (Throwable ignored) {
                // Fall through to exposed map handling.
            }
        }

        Map<?, ?> exposed = divisionData.getLoadouts();
        if (isLikelyMutableMap(exposed)) {
            return (Map<MatchClass, List<Loadout>>) (Map<?, ?>) exposed;
        }
        return null;
    }

    private static Field resolveDivisionLoadoutsField(DivisionData divisionData) {
        if (divisionLoadoutsField != null) {
            return divisionLoadoutsField;
        }
        Class<?> type = divisionData.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                if (!Map.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(divisionData);
                    if (value instanceof Map<?, ?> map && !map.isEmpty()) {
                        Object key = map.keySet().iterator().next();
                        Object entryValue = map.values().iterator().next();
                        if (key instanceof MatchClass && entryValue instanceof List<?>) {
                            divisionLoadoutsField = field;
                            return divisionLoadoutsField;
                        }
                    }
                } catch (Throwable ignored) {
                    // Continue scanning.
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static boolean isLikelyMutableMap(Map<?, ?> map) {
        String name = map.getClass().getName();
        if (name.contains("Unmodifiable") || name.contains("Immutable")) {
            return false;
        }
        try {
            if (!map.isEmpty()) {
                Map.Entry<?, ?> first = map.entrySet().iterator().next();
                ((Map) map).put(first.getKey(), first.getValue());
                return true;
            }
            ((Map) map).put(MatchClass.CLASS_RIFLEMAN, new ObjectArrayList<Loadout>());
            ((Map) map).remove(MatchClass.CLASS_RIFLEMAN);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static Map<Identifier, List<Loadout>> currentFlat() {
        init();
        Map<Identifier, List<Loadout>> result = new Object2ObjectOpenHashMap<>();
        for (DivisionData divisionData : DivisionData.INSTANCES) {
            BFCountry country = divisionData.getCountry();
            String skin = divisionData.getSkin();
            for (Map.Entry<MatchClass, ObjectList<Loadout>> loadoutData : divisionData.getLoadouts().entrySet()) {
                List<Loadout> cloned = new ObjectArrayList<>(loadoutData.getValue().size());
                loadoutData.getValue().forEach(loadout -> cloned.add(cloneLoadout(loadout)));
                result.put(new Identifier(country, skin, loadoutData.getKey()), cloned);
            }
        }
        return result;
    }

    public static Map<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> flatToNested(Map<Identifier, List<Loadout>> flat) {
        Map<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> nested = new EnumMap<>(BFCountry.class);
        flat.forEach((id, value) -> nested
            .computeIfAbsent(id.country(), k -> new Object2ObjectOpenHashMap<>())
            .computeIfAbsent(id.skin(), k -> new EnumMap<>(MatchClass.class))
            .put(id.matchClass(), value)
        );
        return nested;
    }

    public static Map<Identifier, List<Loadout>> nestedToFlat(Map<BFCountry, Map<String, Map<MatchClass, List<Loadout>>>> nested) {
        Map<Identifier, List<Loadout>> flat = new Object2ObjectOpenHashMap<>();
        nested.forEach((country, skinMap) -> skinMap.forEach((skin, classMap) -> classMap.forEach((matchClass, loadouts) ->
            flat.put(new Identifier(country, skin, matchClass), loadouts)
        )));
        return flat;
    }

    public record Identifier(BFCountry country, String skin, MatchClass matchClass) {
    }
}
