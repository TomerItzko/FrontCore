package dev.tomerdev.mercfrontcore.data;

import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;
import dev.tomerdev.mercfrontcore.util.LoadoutCompat;

public final class LoadoutEditorStore {
    private static final LoadoutEditorStore INSTANCE = new LoadoutEditorStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "mercfrontcore";
    private static final String FILE_NAME = "bf_loadout_editor.json";

    private LoadoutEditorStore() {
    }

    public static LoadoutEditorStore getInstance() {
        return INSTANCE;
    }

    private Path getPath(MinecraftServer server) {
        return server.getPath(DATA_DIR).resolve(FILE_NAME);
    }

    public int loadAndApply(MinecraftServer server) {
        Path path = getPath(server);
        if (Files.notExists(path)) {
            return 0;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray entries = root.has("entries") ? root.getAsJsonArray("entries") : new JsonArray();

            Map<LoadoutIndex.Identifier, List<Loadout>> parsed = new Object2ObjectOpenHashMap<>();
            for (JsonElement element : entries) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject obj = element.getAsJsonObject();

                BFCountry country = parseCountry(obj.get("country"));
                MatchClass matchClass = parseMatchClass(obj.get("matchClass"));
                String skin = getString(obj, "skin", "");
                if (country == null || matchClass == null || skin.isBlank()) {
                    continue;
                }

                List<Loadout> levels = parseLevels(obj.get("levels"));
                parsed.put(new LoadoutIndex.Identifier(country, skin, matchClass), levels);
            }

            if (!parsed.isEmpty()) {
                LoadoutIndex.apply(parsed);
            }
            return parsed.size();
        } catch (Exception e) {
            MercFrontCore.LOGGER.error("Failed to load BF loadout editor data from {}", path, e);
            return 0;
        }
    }

    public boolean saveCurrent(MinecraftServer server) {
        Path path = getPath(server);
        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            JsonArray entries = new JsonArray();

            LoadoutIndex.currentFlat().entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<LoadoutIndex.Identifier, List<Loadout>> e) ->
                    e.getKey().country().name())
                    .thenComparing(e -> e.getKey().skin())
                    .thenComparing(e -> e.getKey().matchClass().name()))
                .forEach(entry -> entries.add(toJson(entry.getKey(), entry.getValue())));

            root.add("entries", entries);
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save BF loadout editor data to {}", path, e);
            return false;
        }
    }

    private static JsonObject toJson(LoadoutIndex.Identifier id, List<Loadout> loadouts) {
        JsonObject obj = new JsonObject();
        obj.addProperty("country", id.country().name());
        obj.addProperty("skin", id.skin());
        obj.addProperty("matchClass", id.matchClass().name());

        JsonArray levels = new JsonArray();
        for (Loadout loadout : loadouts) {
            JsonObject level = new JsonObject();
            level.addProperty("primary", itemId(LoadoutCompat.getPrimary(loadout)));
            level.addProperty("secondary", itemId(LoadoutCompat.getSecondary(loadout)));
            level.addProperty("melee", itemId(LoadoutCompat.getMelee(loadout)));
            level.addProperty("offHand", itemId(LoadoutCompat.getOffHand(loadout)));
            level.addProperty("head", itemId(LoadoutCompat.getHead(loadout)));
            level.addProperty("chest", itemId(LoadoutCompat.getChest(loadout)));
            level.addProperty("legs", itemId(LoadoutCompat.getLegs(loadout)));
            level.addProperty("feet", itemId(LoadoutCompat.getFeet(loadout)));
            level.addProperty("minimumXp", LoadoutCompat.getMinimumXp(loadout));

            JsonArray extra = new JsonArray();
            for (ItemStack extraStack : LoadoutCompat.getExtra(loadout)) {
                extra.add(itemId(extraStack));
            }
            level.add("extra", extra);
            levels.add(level);
        }
        obj.add("levels", levels);
        return obj;
    }

    private static List<Loadout> parseLevels(JsonElement levelsElement) {
        List<Loadout> levels = new ObjectArrayList<>();
        if (!(levelsElement instanceof JsonArray levelsArray)) {
            return levels;
        }

        for (JsonElement levelElement : levelsArray) {
            if (!levelElement.isJsonObject()) {
                continue;
            }
            JsonObject level = levelElement.getAsJsonObject();

            Loadout loadout = new Loadout(
                stack(getString(level, "primary", "minecraft:air")),
                stack(getString(level, "secondary", "minecraft:air")),
                stack(getString(level, "melee", "minecraft:air")),
                stack(getString(level, "offHand", "minecraft:air")),
                stack(getString(level, "head", "minecraft:air")),
                stack(getString(level, "chest", "minecraft:air")),
                stack(getString(level, "legs", "minecraft:air")),
                stack(getString(level, "feet", "minecraft:air"))
            );

            if (level.has("extra") && level.get("extra").isJsonArray()) {
                for (JsonElement extraElement : level.getAsJsonArray("extra")) {
                    if (extraElement.isJsonPrimitive()) {
                        loadout.addExtra(stack(extraElement.getAsString()));
                    }
                }
            }

            int minimumXp = level.has("minimumXp") ? level.get("minimumXp").getAsInt() : 0;
            levels.add(LoadoutCompat.setMinimumXp(loadout, minimumXp));
        }
        return levels;
    }

    private static BFCountry parseCountry(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        try {
            return BFCountry.valueOf(element.getAsString());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static MatchClass parseMatchClass(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return null;
        }
        try {
            return MatchClass.valueOf(element.getAsString());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static String getString(JsonObject obj, String key, String fallback) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsString() : fallback;
    }

    private static String itemId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return "minecraft:air";
        }
        return Registries.ITEM.getId(stack.getItem()).toString();
    }

    private static ItemStack stack(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        Item item = id == null ? Items.AIR : Registries.ITEM.get(id);
        if (item == null) {
            item = Items.AIR;
        }
        return new ItemStack(item);
    }
}
