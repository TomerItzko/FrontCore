package dev.tomerdev.mercfrontcore.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;
import dev.tomerdev.mercfrontcore.MercFrontCore;

public final class LoadoutStore {
    private static final LoadoutStore INSTANCE = new LoadoutStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "mercfrontcore";
    private static final String LOADOUTS_FILE = "loadouts.json";

    private final Map<String, LoadoutData> loadouts = new ConcurrentHashMap<>();

    private LoadoutStore() {
    }

    public static LoadoutStore getInstance() {
        return INSTANCE;
    }

    public Map<String, LoadoutData> getLoadouts() {
        return loadouts;
    }

    public Path getLoadoutsPath(MinecraftServer server) {
        return server.getPath(DATA_DIR).resolve(LOADOUTS_FILE);
    }

    public int load(MinecraftServer server) {
        Path path = getLoadoutsPath(server);
        if (Files.notExists(path)) {
            loadouts.clear();
            return 0;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            loadouts.clear();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                JsonObject obj = entry.getValue().getAsJsonObject();
                String primary = obj.has("primaryItem") ? obj.get("primaryItem").getAsString() : "minecraft:air";
                String secondary = obj.has("secondaryItem") ? obj.get("secondaryItem").getAsString() : "minecraft:air";
                loadouts.put(entry.getKey(), new LoadoutData(primary, secondary));
            }
            return loadouts.size();
        } catch (Exception e) {
            MercFrontCore.LOGGER.error("Failed to load loadouts from {}", path, e);
            return 0;
        }
    }

    public boolean save(MinecraftServer server) {
        Path path = getLoadoutsPath(server);
        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            loadouts.forEach((name, loadout) -> {
                JsonObject obj = new JsonObject();
                obj.addProperty("primaryItem", loadout.primaryItem());
                obj.addProperty("secondaryItem", loadout.secondaryItem());
                root.add(name, obj);
            });
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save loadouts to {}", path, e);
            return false;
        }
    }
}
