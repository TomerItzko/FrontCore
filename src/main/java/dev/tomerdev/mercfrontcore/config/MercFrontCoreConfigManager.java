package dev.tomerdev.mercfrontcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
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
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to load config at {}", CONFIG_PATH, e);
            config = new MercFrontCoreConfig();
            return false;
        }
    }

    public static boolean save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save config at {}", CONFIG_PATH, e);
            return false;
        }
    }
}
