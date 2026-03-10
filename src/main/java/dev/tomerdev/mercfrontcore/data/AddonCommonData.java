package dev.tomerdev.mercfrontcore.data;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
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
import net.minecraft.server.network.ServerPlayerEntity;
import dev.tomerdev.mercfrontcore.MercFrontCore;

public final class AddonCommonData {
    private static final AddonCommonData INSTANCE = new AddonCommonData();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "mercfrontcore";
    private static final String PROFILE_OVERRIDES_FILE = "profile_overrides.json";

    private final Map<UUID, ProfileOverrideData> profileOverrides = new ConcurrentHashMap<>();
    private final Map<UUID, LiveProfileState> liveProfileSnapshots = new ConcurrentHashMap<>();

    private AddonCommonData() {
    }

    public static AddonCommonData getInstance() {
        return INSTANCE;
    }

    public Map<UUID, ProfileOverrideData> getProfileOverrides() {
        return profileOverrides;
    }

    public void applyProfileOverrides(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            refreshLiveProfile(player);
        }
    }

    public void refreshLiveProfile(ServerPlayerEntity player) {
        ServerPlayerDataHandler dataHandler = getServerPlayerDataHandler();
        if (dataHandler == null) {
            return;
        }

        UUID uuid = player.getUuid();
        ProfileOverrideData override = profileOverrides.get(uuid);
        PlayerCloudData attachedProfile = dataHandler.getCloudProfile(player);
        PlayerCloudData cachedProfile = dataHandler.getCloudProfile(uuid);

        if (override == null) {
            LiveProfileState snapshot = liveProfileSnapshots.remove(uuid);
            if (snapshot != null) {
                restoreProfile(attachedProfile, snapshot, player);
                if (cachedProfile != attachedProfile) {
                    restoreProfile(cachedProfile, snapshot, player);
                }
            } else {
                attachedProfile.setUsername(player.getGameProfile().getName());
                if (cachedProfile != attachedProfile) {
                    cachedProfile.setUsername(player.getGameProfile().getName());
                }
                requestCloudRefresh(uuid);
            }
            return;
        }

        liveProfileSnapshots.putIfAbsent(uuid, LiveProfileState.capture(attachedProfile));
        applyOverride(attachedProfile, override);
        if (cachedProfile != attachedProfile) {
            applyOverride(cachedProfile, override);
        }
    }

    public void forgetLiveProfileSnapshot(UUID uuid) {
        liveProfileSnapshots.remove(uuid);
    }

    public void reapplyProfileOverride(UUID uuid) {
        ProfileOverrideData override = profileOverrides.get(uuid);
        if (override == null) {
            return;
        }

        BFAbstractManager<?, ?, ?> manager = getManager();
        if (manager == null) {
            return;
        }
        if (!(manager.getPlayerDataHandler() instanceof PlayerDataHandler<?> dataHandler)) {
            return;
        }

        applyOverride(dataHandler.getCloudProfile(uuid), override);
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

    private static void applyOverride(PlayerCloudData profile, ProfileOverrideData override) {
        profile.setUsername(override.displayName());
        profile.setExp(override.level());
        profile.setPrestigeLevel(override.prestige());
    }

    private static void restoreProfile(PlayerCloudData profile, LiveProfileState snapshot, ServerPlayerEntity player) {
        profile.setUsername(snapshot.displayName().isBlank() ? player.getGameProfile().getName() : snapshot.displayName());
        profile.setExp(snapshot.exp());
        profile.setPrestigeLevel(snapshot.prestige());
    }

    private static ServerPlayerDataHandler getServerPlayerDataHandler() {
        BFAbstractManager<?, ?, ?> manager = getManager();
        if (manager == null) {
            return null;
        }
        if (manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler dataHandler) {
            return dataHandler;
        }
        return null;
    }

    private static void requestCloudRefresh(UUID uuid) {
        BFAbstractManager<?, ?, ?> manager = getManager();
        if (manager == null) {
            return;
        }
        if (manager.getConnectionManager() instanceof AbstractConnectionManager connectionManager) {
            connectionManager.getRequester().push(RequestType.PLAYER_DATA, uuid);
            connectionManager.getRequester().push(RequestType.PLAYER_INVENTORY, uuid);
            connectionManager.getRequester().push(RequestType.PLAYER_INVENTORY_DEFAULTS, uuid);
            connectionManager.getRequester().push(RequestType.PLAYER_INVENTORY_SHOWCASE, uuid);
        }
    }

    private static BFAbstractManager<?, ?, ?> getManager() {
        BlockFront blockFront = BlockFront.getInstance();
        if (blockFront == null) {
            return null;
        }
        BFAbstractManager<?, ?, ?> manager = blockFront.getManager();
        if (manager instanceof BFServerManager) {
            return manager;
        }
        return manager;
    }

    private record LiveProfileState(String displayName, int exp, int prestige) {
        private static LiveProfileState capture(PlayerCloudData profile) {
            return new LiveProfileState(profile.getUsername(), profile.getExp(), profile.getPrestigeLevel());
        }
    }
}
