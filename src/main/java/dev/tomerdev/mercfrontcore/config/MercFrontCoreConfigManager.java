package dev.tomerdev.mercfrontcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
            MercFrontCoreConfig loaded = GSON.fromJson(reader, MercFrontCoreConfig.class);
            config = loaded != null ? loaded : new MercFrontCoreConfig();
            boolean changed = normalizeConfig();
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
            MercFrontCoreConfig loaded = GSON.fromJson(reader, MercFrontCoreConfig.class);
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
