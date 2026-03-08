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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.MinecraftServer;
import dev.tomerdev.mercfrontcore.MercFrontCore;

public final class AddonCommonData {
    private static final AddonCommonData INSTANCE = new AddonCommonData();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "mercfrontcore";
    private static final String PROFILE_OVERRIDES_FILE = "profile_overrides.json";

    private final Map<UUID, ProfileOverrideData> profileOverrides = new ConcurrentHashMap<>();

    private AddonCommonData() {
    }

    public static AddonCommonData getInstance() {
        return INSTANCE;
    }

    public Map<UUID, ProfileOverrideData> getProfileOverrides() {
        return profileOverrides;
    }

    public Path getProfileOverridesPath(MinecraftServer server) {
        return server.getPath(DATA_DIR).resolve(PROFILE_OVERRIDES_FILE);
    }

    public int loadProfileOverrides(MinecraftServer server) {
        Path path = getProfileOverridesPath(server);
        if (Files.notExists(path)) {
            profileOverrides.clear();
            return 0;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            profileOverrides.clear();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(entry.getKey());
                } catch (IllegalArgumentException ignored) {
                    continue;
                }

                JsonObject value = entry.getValue().getAsJsonObject();
                String displayName = value.has("displayName") ? value.get("displayName").getAsString() : "unknown";
                int level = value.has("level") ? value.get("level").getAsInt() : 0;
                int prestige = value.has("prestige") ? value.get("prestige").getAsInt() : 0;
                profileOverrides.put(uuid, new ProfileOverrideData(displayName, level, prestige));
            }
            return profileOverrides.size();
        } catch (Exception e) {
            MercFrontCore.LOGGER.error("Failed to load profile overrides from {}", path, e);
            return 0;
        }
    }

    public boolean saveProfileOverrides(MinecraftServer server) {
        Path path = getProfileOverridesPath(server);
        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            profileOverrides.forEach((uuid, data) -> {
                JsonObject value = new JsonObject();
                value.addProperty("displayName", data.displayName());
                value.addProperty("level", data.level());
                value.addProperty("prestige", data.prestige());
                root.add(uuid.toString(), value);
            });
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save profile overrides to {}", path, e);
            return false;
        }
    }
}
