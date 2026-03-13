package dev.tomerdev.mercfrontcore.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.server.MinecraftServer;
import com.boehmod.blockfront.common.match.MatchClass;

public final class PlayerXpStore {
    private static final PlayerXpStore INSTANCE = new PlayerXpStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "mercfrontcore";
    private static final String PLAYERS_DIR = "players";
    private static final long HISTORY_RETENTION_SECONDS = 400L * 24L * 60L * 60L;

    private final ConcurrentMap<UUID, PlayerXpData> cache = new ConcurrentHashMap<>();

    private PlayerXpStore() {
    }

    public static PlayerXpStore getInstance() {
        return INSTANCE;
    }

    public Path getPlayerPath(MinecraftServer server, UUID playerUuid) {
        return getLegacyPlayerPath(server, playerUuid);
    }

    public Path getPlayerPath(MinecraftServer server, UUID playerUuid, String username) {
        String safeName = sanitizeUsername(username);
        return server.getPath(DATA_DIR).resolve(PLAYERS_DIR).resolve(safeName + "--" + playerUuid + ".json");
    }

    private Path getLegacyPlayerPath(MinecraftServer server, UUID playerUuid) {
        return server.getPath(DATA_DIR).resolve(PLAYERS_DIR).resolve(playerUuid + ".json");
    }

    public PlayerXpData loadOrCreate(MinecraftServer server, UUID playerUuid, int fallbackExp, int fallbackPrestige) {
        return loadOrCreate(server, playerUuid, playerUuid.toString(), fallbackExp, fallbackPrestige, List.of());
    }

    public PlayerXpData loadOrCreate(
        MinecraftServer server,
        UUID playerUuid,
        String username,
        int fallbackExp,
        int fallbackPrestige,
        List<Integer> fallbackClassExp
    ) {
        return cache.computeIfAbsent(playerUuid, ignored -> {
            Path path = resolveExistingPath(server, playerUuid, username);
            if (Files.exists(path)) {
                PlayerXpData loaded = readPlayerData(path, username, fallbackExp, fallbackPrestige, fallbackClassExp);
                if (loaded != null) {
                    return loaded;
                }
            }
            PlayerXpData seeded = new PlayerXpData(
                Math.max(0, fallbackExp),
                Math.max(0, fallbackPrestige),
                normalizeClassExp(fallbackClassExp),
                username,
                List.of()
            );
            writeFile(server, playerUuid, username, seeded);
            return seeded;
        });
    }

    public PlayerXpData get(UUID playerUuid) {
        return cache.get(playerUuid);
    }

    public PlayerXpData save(MinecraftServer server, UUID playerUuid, String username, PlayerXpData data) {
        PlayerXpData previous = cache.get(playerUuid);
        if (previous == null) {
            Path path = resolveExistingPath(server, playerUuid, username);
            if (Files.exists(path)) {
                previous = readPlayerData(path, username, data.exp(), data.prestige(), data.classExp());
            }
        }
        List<XpGainEntry> history = new ArrayList<>(previous == null ? List.<XpGainEntry>of() : previous.xpHistory());
        int gainedXp = data.exp() - (previous == null ? 0 : previous.exp());
        if (gainedXp > 0) {
            history.add(new XpGainEntry(Instant.now().getEpochSecond(), gainedXp));
        }
        PlayerXpData normalized = new PlayerXpData(
            Math.max(0, data.exp()),
            Math.max(0, data.prestige()),
            normalizeClassExp(data.classExp()),
            username,
            pruneHistory(history)
        );
        writeFile(server, playerUuid, username, normalized);
        cache.put(playerUuid, normalized);
        return normalized;
    }

    public List<StoredPlayerXp> readAllPlayers(MinecraftServer server) {
        Path playersDir = server.getPath(DATA_DIR).resolve(PLAYERS_DIR);
        if (!Files.isDirectory(playersDir)) {
            return List.of();
        }

        List<StoredPlayerXp> players = new ArrayList<>();
        try (var stream = Files.list(playersDir)) {
            stream.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json"))
                .forEach(path -> {
                    UUID uuid = parseUuid(path);
                    if (uuid == null) {
                        return;
                    }
                    PlayerXpData data = readPlayerData(path, "unknown", 0, 0, List.of());
                    if (data != null) {
                        players.add(new StoredPlayerXp(uuid, data));
                    }
                });
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to scan player XP directory {}", playersDir, e);
        }
        return List.copyOf(players);
    }

    public void clearSession() {
        cache.clear();
    }

    private void writeFile(MinecraftServer server, UUID playerUuid, String username, PlayerXpData data) {
        Path path = getPlayerPath(server, playerUuid, username);
        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            root.addProperty("username", username);
            root.addProperty("uuid", playerUuid.toString());
            root.addProperty("exp", Math.max(0, data.exp()));
            root.addProperty("prestige", Math.max(0, data.prestige()));
            JsonArray classExp = new JsonArray();
            for (int value : normalizeClassExp(data.classExp())) {
                classExp.add(Math.max(0, value));
            }
            root.add("classExp", classExp);
            JsonArray history = new JsonArray();
            for (XpGainEntry entry : pruneHistory(data.xpHistory())) {
                JsonObject historyEntry = new JsonObject();
                historyEntry.addProperty("timestamp", entry.timestamp());
                historyEntry.addProperty("xp", Math.max(0, entry.xp()));
                history.add(historyEntry);
            }
            root.add("xpHistory", history);
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }
            Path legacyPath = getLegacyPlayerPath(server, playerUuid);
            if (!legacyPath.equals(path)) {
                Files.deleteIfExists(legacyPath);
            }
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save player XP to {}", path, e);
        }
    }

    private Path resolveExistingPath(MinecraftServer server, UUID playerUuid, String username) {
        Path preferred = getPlayerPath(server, playerUuid, username);
        if (Files.exists(preferred)) {
            return preferred;
        }
        Path legacy = getLegacyPlayerPath(server, playerUuid);
        if (Files.exists(legacy)) {
            return legacy;
        }
        return preferred;
    }

    private static String sanitizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return "unknown";
        }
        return username.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private static List<Integer> parseClassExp(JsonObject root, List<Integer> fallbackClassExp) {
        if (!root.has("classExp") || !root.get("classExp").isJsonArray()) {
            return normalizeClassExp(fallbackClassExp);
        }
        List<Integer> values = new ArrayList<>();
        for (var element : root.getAsJsonArray("classExp")) {
            values.add(element.isJsonPrimitive() ? Math.max(0, element.getAsInt()) : 0);
        }
        return normalizeClassExp(values);
    }

    private static List<XpGainEntry> parseHistory(JsonObject root) {
        if (!root.has("xpHistory") || !root.get("xpHistory").isJsonArray()) {
            return List.of();
        }
        List<XpGainEntry> history = new ArrayList<>();
        for (var element : root.getAsJsonArray("xpHistory")) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject entry = element.getAsJsonObject();
            long timestamp = entry.has("timestamp") ? entry.get("timestamp").getAsLong() : 0L;
            int xp = entry.has("xp") ? entry.get("xp").getAsInt() : 0;
            if (timestamp > 0 && xp > 0) {
                history.add(new XpGainEntry(timestamp, xp));
            }
        }
        return pruneHistory(history);
    }

    private static List<XpGainEntry> pruneHistory(List<XpGainEntry> history) {
        long cutoff = Instant.now().getEpochSecond() - HISTORY_RETENTION_SECONDS;
        List<XpGainEntry> pruned = new ArrayList<>();
        for (XpGainEntry entry : history) {
            if (entry != null && entry.timestamp() >= cutoff && entry.xp() > 0) {
                pruned.add(entry);
            }
        }
        return List.copyOf(pruned);
    }

    private static UUID parseUuid(Path path) {
        String filename = path.getFileName().toString();
        String base = filename.endsWith(".json") ? filename.substring(0, filename.length() - 5) : filename;
        int separator = base.lastIndexOf("--");
        String candidate = separator >= 0 ? base.substring(separator + 2) : base;
        try {
            return UUID.fromString(candidate);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static PlayerXpData readPlayerData(
        Path path,
        String username,
        int fallbackExp,
        int fallbackPrestige,
        List<Integer> fallbackClassExp
    ) {
        try (Reader reader = Files.newBufferedReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            int exp = root.has("exp") ? root.get("exp").getAsInt() : fallbackExp;
            int prestige = root.has("prestige") ? root.get("prestige").getAsInt() : fallbackPrestige;
            List<Integer> classExp = parseClassExp(root, fallbackClassExp);
            String storedUsername = root.has("username") ? root.get("username").getAsString() : username;
            return new PlayerXpData(
                Math.max(0, exp),
                Math.max(0, prestige),
                classExp,
                storedUsername,
                parseHistory(root)
            );
        } catch (Exception e) {
            MercFrontCore.LOGGER.error("Failed to load player XP from {}", path, e);
            return null;
        }
    }

    private static List<Integer> normalizeClassExp(List<Integer> values) {
        int length = MatchClass.values().length;
        List<Integer> normalized = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            int value = i < values.size() ? values.get(i) : 0;
            normalized.add(Math.max(0, value));
        }
        return List.copyOf(normalized);
    }

    public record PlayerXpData(int exp, int prestige, List<Integer> classExp, String username, List<XpGainEntry> xpHistory) {
    }

    public record XpGainEntry(long timestamp, int xp) {
    }

    public record StoredPlayerXp(UUID uuid, PlayerXpData data) {
    }
}
