package dev.tomerdev.mercfrontcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.neoforged.fml.loading.FMLPaths;
import dev.tomerdev.mercfrontcore.MercFrontCore;

public final class MercFrontCoreConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("mercfrontcore.json");
    private static MercFrontCoreConfig config = new MercFrontCoreConfig();

    private MercFrontCoreConfigManager() {
    }

    public static MercFrontCoreConfig get() {
        return config;
    }

    public static Path getConfigPath() {
        return CONFIG_PATH;
    }

    public static boolean load() {
        if (Files.notExists(CONFIG_PATH)) {
            return save();
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            boolean changed = mergeMissingDefaults(root, defaultConfigTree());
            MercFrontCoreConfig loaded = GSON.fromJson(root, MercFrontCoreConfig.class);
            config = loaded != null ? loaded : new MercFrontCoreConfig();
            changed |= normalizeConfig();
            if (changed) {
                save();
            }
            return true;
        } catch (IOException | JsonSyntaxException e) {
            MercFrontCore.LOGGER.error("Failed to load config at {}", CONFIG_PATH, e);
            return tryRecoverFromBackup();
        }
    }

    public static boolean save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            writeJsonAtomically(CONFIG_PATH, config);
            refreshBackup(CONFIG_PATH);
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save config at {}", CONFIG_PATH, e);
            return false;
        }
    }

    private static boolean tryRecoverFromBackup() {
        Path backup = backupPath(CONFIG_PATH);
        if (Files.notExists(backup)) {
            config = new MercFrontCoreConfig();
            return false;
        }
        try (Reader reader = Files.newBufferedReader(backup)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            mergeMissingDefaults(root, defaultConfigTree());
            MercFrontCoreConfig loaded = GSON.fromJson(root, MercFrontCoreConfig.class);
            config = loaded != null ? loaded : new MercFrontCoreConfig();
            normalizeConfig();
            writeJsonAtomically(CONFIG_PATH, config);
            MercFrontCore.LOGGER.warn("Recovered mercfrontcore config from backup {}", backup);
            return true;
        } catch (IOException | JsonSyntaxException ex) {
            MercFrontCore.LOGGER.error("Failed to recover config from backup {}", backup, ex);
            config = new MercFrontCoreConfig();
            return false;
        }
    }

    private static Path backupPath(Path path) {
        return path.resolveSibling(path.getFileName().toString() + ".bak");
    }

    private static void refreshBackup(Path path) {
        Path backup = backupPath(path);
        try {
            Files.copy(path, backup, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            MercFrontCore.LOGGER.warn("Failed to refresh config backup at {}", backup, e);
        }
    }

    private static boolean normalizeConfig() {
        boolean changed = false;
        if (config.proxy == null) {
            config.proxy = new MercFrontCoreConfig.ProxySettings();
            changed = true;
        }
        if (config.audio == null) {
            config.audio = new MercFrontCoreConfig.AudioSettings();
            changed = true;
        }
        if (config.rewards == null) {
            config.rewards = new MercFrontCoreConfig.RewardSettings();
            changed = true;
        }
        if (config.experience == null) {
            config.experience = new MercFrontCoreConfig.ExperienceSettings();
            changed = true;
        }
        if (Float.isNaN(config.rewards.winnerSkinDropChance)) {
            config.rewards.winnerSkinDropChance = 0.25f;
            changed = true;
        }
        if (config.rewards.winnerSkinDropChance < 0.0f) {
            config.rewards.winnerSkinDropChance = 0.0f;
            changed = true;
        } else if (config.rewards.winnerSkinDropChance > 1.0f) {
            config.rewards.winnerSkinDropChance = 1.0f;
            changed = true;
        }
        changed |= clampMin(() -> config.experience.deathXp, v -> config.experience.deathXp = v, 0);
        changed |= clampMin(() -> config.experience.assistXp, v -> config.experience.assistXp = v, 0);
        changed |= clampMin(() -> config.experience.botKillXp, v -> config.experience.botKillXp = v, 0);
        changed |= clampMin(() -> config.experience.playerKillXp, v -> config.experience.playerKillXp = v, 0);
        changed |= clampMin(() -> config.experience.fireKillXp, v -> config.experience.fireKillXp = v, 0);
        changed |= clampMin(() -> config.experience.vehicleKillXp, v -> config.experience.vehicleKillXp = v, 0);
        changed |= clampMin(() -> config.experience.headshotXp, v -> config.experience.headshotXp = v, 0);
        changed |= clampMin(() -> config.experience.noScopeXp, v -> config.experience.noScopeXp = v, 0);
        changed |= clampMin(() -> config.experience.backStabXp, v -> config.experience.backStabXp = v, 0);
        changed |= clampMin(() -> config.experience.firstBloodXp, v -> config.experience.firstBloodXp = v, 0);
        changed |= clampMin(() -> config.experience.participationXp, v -> config.experience.participationXp = v, 0);
        changed |= clampMin(() -> config.experience.victoryXp, v -> config.experience.victoryXp = v, 0);
        changed |= clampMin(() -> config.experience.infectedRoundWinXp, v -> config.experience.infectedRoundWinXp = v, 0);
        changed |= clampMin(() -> config.experience.infectedMatchWinXp, v -> config.experience.infectedMatchWinXp = v, 0);
        changed |= clampMin(() -> config.experience.classPlayerKillXp, v -> config.experience.classPlayerKillXp = v, 0);
        changed |= clampMin(() -> config.experience.classAssistXp, v -> config.experience.classAssistXp = v, 0);
        return changed;
    }

    private static boolean clampMin(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter, int min) {
        int value = getter.getAsInt();
        if (value >= min) {
            return false;
        }
        setter.accept(min);
        return true;
    }

    private static JsonObject defaultConfigTree() {
        return GSON.toJsonTree(new MercFrontCoreConfig()).getAsJsonObject();
    }

    private static boolean mergeMissingDefaults(JsonObject target, JsonObject defaults) {
        boolean changed = false;
        for (String key : defaults.keySet()) {
            JsonElement defaultValue = defaults.get(key);
            JsonElement existingValue = target.get(key);
            if (existingValue == null || existingValue.isJsonNull()) {
                target.add(key, defaultValue.deepCopy());
                changed = true;
                continue;
            }
            if (existingValue.isJsonObject() && defaultValue.isJsonObject()) {
                changed |= mergeMissingDefaults(existingValue.getAsJsonObject(), defaultValue.getAsJsonObject());
            }
        }
        return changed;
    }

    private static void writeJsonAtomically(Path path, Object jsonPayload) throws IOException {
        Path parent = path.getParent();
        String fileName = path.getFileName().toString();
        Path temp = Files.createTempFile(parent, fileName + ".", ".tmp");
        try {
            try (FileChannel channel = FileChannel.open(
                temp,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            ); Writer writer = Channels.newWriter(channel, java.nio.charset.StandardCharsets.UTF_8)) {
                GSON.toJson(jsonPayload, writer);
                writer.flush();
                channel.force(true);
            }

            try {
                Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException atomicMoveFailure) {
                Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
