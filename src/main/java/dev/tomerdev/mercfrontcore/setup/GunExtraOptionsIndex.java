package dev.tomerdev.mercfrontcore.setup;

import com.boehmod.blockfront.common.gun.GunBarrelType;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.boehmod.blockfront.util.BFRes;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.net.packet.GunExtraOptionsPacket;

public final class GunExtraOptionsIndex {
    private static final Map<Identifier, GunExtraOptionsPacket.GunOptions> CURRENT = new Object2ObjectOpenHashMap<>();

    private GunExtraOptionsIndex() {
    }

    public static void rebuild() {
        CURRENT.clear();
        GunSkinIndex.ensureInitialized();
        Map<Identifier, CloudGunData> cloudByItem = loadCloudGunData();
        Map<Identifier, FallbackGunData> fallbackByItem = loadFallbackGunData();

        for (var item : Registries.ITEM) {
            if (!(item instanceof GunItem gunItem)) {
                continue;
            }
            Identifier id = Registries.ITEM.getId(gunItem);
            GunExtraOptionsPacket.GunOptions options = discoverFor(gunItem, id, cloudByItem.get(id), fallbackByItem.get(id));
            CURRENT.put(id, options);
        }

        int guns = CURRENT.size();
        int withMagVariants = 0;
        int withBarrelVariants = 0;
        int withSkins = 0;
        for (var options : CURRENT.values()) {
            if (options.magTypes().size() > 1) {
                withMagVariants++;
            }
            if (options.barrelTypes().size() > 1) {
                withBarrelVariants++;
            }
            if (!options.skins().isEmpty()) {
                withSkins++;
            }
        }
        AddonConstants.LOGGER.info(
            "Built gun option index: guns={}, magVariants={}, barrelVariants={}, skins={}, cloudItems={}, fallbackItems={}",
            guns,
            withMagVariants,
            withBarrelVariants,
            withSkins,
            cloudByItem.size(),
            fallbackByItem.size()
        );
        if (withMagVariants == 0 && withBarrelVariants == 0 && withSkins == 0) {
            AddonConstants.LOGGER.warn("No non-default gun variants were discovered. Verify BF option discovery source data.");
        }
    }

    public static Map<Identifier, GunExtraOptionsPacket.GunOptions> snapshot() {
        return Map.copyOf(CURRENT);
    }

    public static List<String> getDeclaredFallbackSkins(Identifier itemId) {
        FallbackGunData fallback = loadFallbackGunData().get(itemId);
        if (fallback == null || fallback.skins.isEmpty()) {
            return List.of();
        }
        return List.copyOf(dedupPreserveOrder(fallback.skins));
    }

    private static GunExtraOptionsPacket.GunOptions discoverFor(
        GunItem gunItem,
        Identifier itemId,
        CloudGunData cloudData,
        FallbackGunData fallbackData
    ) {
        List<String> magTypes = new ArrayList<>();
        List<String> barrelTypes = new ArrayList<>();
        magTypes.add("default");
        barrelTypes.add("default");

        if (fallbackData != null) {
            for (String key : fallbackData.magTypes) {
                if (key != null && !key.isBlank()) {
                    magTypes.add(key);
                }
            }
            for (String key : fallbackData.barrelTypes) {
                if (key != null && !key.isBlank()) {
                    barrelTypes.add(key);
                }
            }
        }

        if (cloudData != null) {
            for (String key : cloudData.magTypes) {
                if (isValidMagType(gunItem, key)) {
                    magTypes.add(key);
                }
            }
            for (String key : cloudData.barrelTypes) {
                if (isValidBarrelType(gunItem, key)) {
                    barrelTypes.add(key);
                }
            }
        }

        collectTypedMapKeys(gunItem, "com.boehmod.blockfront.common.gun.GunMagType", magTypes);
        collectTypedMapKeys(gunItem, "com.boehmod.blockfront.common.gun.GunBarrelType", barrelTypes);
        collectStringMapKeysHeuristic(gunItem, "mag", magTypes);
        collectStringMapKeysHeuristic(gunItem, "barrel", barrelTypes);

        // Obfuscation-safe fallback: collect all string map keys and validate via GunItem set/get behavior.
        List<String> allStringMapKeys = new ArrayList<>();
        collectAllStringMapKeys(gunItem, allStringMapKeys);
        for (String candidate : allStringMapKeys) {
            if (isValidMagType(gunItem, candidate) && !magTypes.contains(candidate)) {
                magTypes.add(candidate);
            }
            if (isValidBarrelType(gunItem, candidate) && !barrelTypes.contains(candidate)) {
                barrelTypes.add(candidate);
            }
        }

        List<String> skins = new ArrayList<>();
        if (fallbackData != null && !fallbackData.skins.isEmpty()) {
            skins.addAll(fallbackData.skins);
        }
        if (cloudData != null && !cloudData.skins.isEmpty()) {
            skins.addAll(cloudData.skins);
        }
        skins.addAll(GunSkinIndex.getStrictSkinNames(gunItem));

        List<String> validatedMagTypes = keepValidMagTypes(gunItem, dedupPreserveOrder(magTypes));
        List<String> validatedBarrelTypes = keepValidBarrelTypes(gunItem, dedupPreserveOrder(barrelTypes));

        List<String> validatedSkins = keepValidSkins(gunItem, dedupPreserveOrder(skins));

        return new GunExtraOptionsPacket.GunOptions(
            List.copyOf(validatedMagTypes),
            List.copyOf(validatedBarrelTypes),
            List.copyOf(validatedSkins)
        );
    }

    private static Map<Identifier, CloudGunData> loadCloudGunData() {
        Map<Identifier, CloudGunData> result = new Object2ObjectOpenHashMap<>();
        CloudRegistry registry = resolveCloudRegistry();
        if (registry == null) {
            return result;
        }

        for (CloudItem<?> item : registry.getItems()) {
            if (!(item instanceof CloudItemGun gun)) {
                continue;
            }
            Identifier itemId = BFRes.fromCloud(gun.getMinecraftItem());
            CloudGunData data = result.computeIfAbsent(itemId, k -> new CloudGunData());

            String skin = safeString(gun.getSuffixForDisplay());
            if (!skin.isBlank()) {
                data.skins.add(skin);
            }

            String mag = readStringByNames(gun, "getMagType", "getMagazineType", "magType");
            if (!mag.isBlank()) {
                data.magTypes.add(mag);
            }

            String barrel = readStringByNames(gun, "getBarrelType", "barrelType");
            if (!barrel.isBlank()) {
                data.barrelTypes.add(barrel);
            }
        }
        return result;
    }

    private static Map<Identifier, FallbackGunData> loadFallbackGunData() {
        Map<Identifier, FallbackGunData> result = new Object2ObjectOpenHashMap<>();
        String resourcePath = "mercfrontcore/gun_options_fallback.json";
        try (InputStream inputStream = GunExtraOptionsIndex.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return result;
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    Identifier itemId = Identifier.tryParse(entry.getKey());
                    if (itemId == null || !entry.getValue().isJsonObject()) {
                        continue;
                    }
                    JsonObject obj = entry.getValue().getAsJsonObject();
                    FallbackGunData data = new FallbackGunData();
                    addJsonArray(obj, "magTypes", data.magTypes);
                    addJsonArray(obj, "barrelTypes", data.barrelTypes);
                    addJsonArray(obj, "skins", data.skins);
                    result.put(itemId, data);
                }
            }
        } catch (Throwable throwable) {
            AddonConstants.LOGGER.warn("Failed to read {}.", resourcePath, throwable);
        }
        return result;
    }

    private static void addJsonArray(JsonObject obj, String key, List<String> out) {
        if (!obj.has(key) || !obj.get(key).isJsonArray()) {
            return;
        }
        for (JsonElement element : obj.getAsJsonArray(key)) {
            if (!element.isJsonPrimitive()) {
                continue;
            }
            String value = element.getAsString();
            if (!value.isBlank()) {
                out.add(value);
            }
        }
    }

    private static CloudRegistry resolveCloudRegistry() {
        return CloudRegistryAccess.resolveRegistryObject();
    }

    private static List<String> dedupPreserveOrder(List<String> values) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String value : values) {
            if (value == null || value.isBlank()) {
                continue;
            }
            set.add(value);
        }
        return new ArrayList<>(set);
    }

    private static boolean isValidMagType(GunItem gunItem, String key) {
        if ("default".equalsIgnoreCase(key)) {
            return true;
        }
        try {
            ItemStack stack = new ItemStack(gunItem);
            GunItem.setMagType(stack, key);
            GunMagType resolved = gunItem.getMagTypeOrDefault(stack);
            return resolved != null && !resolved.isDefault();
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static boolean isValidBarrelType(GunItem gunItem, String key) {
        if ("default".equalsIgnoreCase(key)) {
            return true;
        }
        try {
            ItemStack stack = new ItemStack(gunItem);
            GunItem.setBarrelType(stack, key);
            GunBarrelType resolved = gunItem.getBarrelTypeOrDefault(stack);
            return resolved != null && resolved != GunBarrelType.DEFAULT;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static String readStringByNames(Object target, String... names) {
        for (String name : names) {
            try {
                var method = target.getClass().getMethod(name);
                Object value = method.invoke(target);
                if (value != null) {
                    return value.toString();
                }
            } catch (Throwable ignored) {
            }
        }
        return "";
    }

    private static String safeString(String value) {
        return value == null ? "" : value.trim();
    }

    private static void collectTypedMapKeys(Object target, String valueTypeClassName, List<String> out) {
        Class<?> valueType;
        try {
            valueType = Class.forName(valueTypeClassName);
        } catch (Throwable ignored) {
            return;
        }

        Class<?> current = target.getClass();
        while (current != null && current != Object.class) {
            for (var field : current.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object raw = field.get(target);
                    if (!(raw instanceof Map<?, ?> map) || map.isEmpty()) {
                        continue;
                    }
                    Object firstValue = map.values().iterator().next();
                    if (firstValue == null || !valueType.isInstance(firstValue)) {
                        continue;
                    }
                    for (Object key : map.keySet()) {
                        if (key instanceof String s && !s.isBlank() && !out.contains(s)) {
                            out.add(s);
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
            current = current.getSuperclass();
        }
    }

    private static void collectStringMapKeysHeuristic(Object target, String fieldKeyword, List<String> out) {
        Class<?> current = target.getClass();
        while (current != null && current != Object.class) {
            for (var field : current.getDeclaredFields()) {
                String lowerFieldName = field.getName().toLowerCase();
                if (!lowerFieldName.contains(fieldKeyword)) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object raw = field.get(target);
                    if (!(raw instanceof Map<?, ?> map) || map.isEmpty()) {
                        continue;
                    }
                    for (Object key : map.keySet()) {
                        if (key instanceof String s && !s.isBlank() && !out.contains(s)) {
                            out.add(s);
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
            current = current.getSuperclass();
        }
    }

    private static void collectAllStringMapKeys(Object target, List<String> out) {
        Class<?> current = target.getClass();
        while (current != null && current != Object.class) {
            for (var field : current.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object raw = field.get(target);
                    if (!(raw instanceof Map<?, ?> map) || map.isEmpty()) {
                        continue;
                    }
                    for (Object key : map.keySet()) {
                        if (key instanceof String s && !s.isBlank() && !out.contains(s)) {
                            out.add(s);
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
            current = current.getSuperclass();
        }
    }

    private static List<String> keepValidMagTypes(GunItem gunItem, List<String> values) {
        List<String> out = new ArrayList<>();
        if (!out.contains("default")) {
            out.add("default");
        }
        for (String value : values) {
            if ("default".equalsIgnoreCase(value)) {
                continue;
            }
            if (isValidMagType(gunItem, value) && !out.contains(value)) {
                out.add(value);
            }
        }
        return out;
    }

    private static List<String> keepValidBarrelTypes(GunItem gunItem, List<String> values) {
        List<String> out = new ArrayList<>();
        if (!out.contains("default")) {
            out.add("default");
        }
        for (String value : values) {
            if ("default".equalsIgnoreCase(value)) {
                continue;
            }
            if (isValidBarrelType(gunItem, value) && !out.contains(value)) {
                out.add(value);
            }
        }
        return out;
    }

    private static List<String> keepValidSkins(GunItem gunItem, List<String> values) {
        List<String> out = new ArrayList<>();
        for (String value : values) {
            if (value == null || value.isBlank()) {
                continue;
            }
            if (GunSkinIndex.getSkinId(gunItem, value).isPresent() && !out.contains(value)) {
                out.add(value);
            }
        }
        return out;
    }

    private static final class CloudGunData {
        private final List<String> skins = new ArrayList<>();
        private final List<String> magTypes = new ArrayList<>();
        private final List<String> barrelTypes = new ArrayList<>();
    }

    private static final class FallbackGunData {
        private final List<String> skins = new ArrayList<>();
        private final List<String> magTypes = new ArrayList<>();
        private final List<String> barrelTypes = new ArrayList<>();
    }
}
