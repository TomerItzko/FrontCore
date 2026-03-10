package dev.tomerdev.mercfrontcore.data;

import com.boehmod.blockfront.registry.BFDataComponents;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class PlayerGunSkinStore {
    private static final PlayerGunSkinStore INSTANCE = new PlayerGunSkinStore();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "mercfrontcore";
    private static final String FILE_NAME = "player_gun_skins.json";

    private final Map<UUID, Map<String, OwnedGunSkins>> playerGunSkins = new ConcurrentHashMap<>();

    private PlayerGunSkinStore() {
    }

    public static PlayerGunSkinStore getInstance() {
        return INSTANCE;
    }

    public Path getPath(MinecraftServer server) {
        return server.getPath(DATA_DIR).resolve(FILE_NAME);
    }

    public int load(MinecraftServer server) {
        Path path = getPath(server);
        if (Files.notExists(path)) {
            playerGunSkins.clear();
            return 0;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            playerGunSkins.clear();
            int count = 0;
            for (Map.Entry<String, JsonElement> playerEntry : root.entrySet()) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(playerEntry.getKey());
                } catch (IllegalArgumentException ignored) {
                    continue;
                }
                if (!playerEntry.getValue().isJsonObject()) {
                    continue;
                }

                Map<String, OwnedGunSkins> skinsByGun = new ConcurrentHashMap<>();
                JsonObject playerObject = playerEntry.getValue().getAsJsonObject();
                for (Map.Entry<String, JsonElement> gunEntry : playerObject.entrySet()) {
                    OwnedGunSkins owned = parseOwnedGunSkins(gunEntry.getValue());
                    if (owned != null && !owned.ownedSkins().isEmpty()) {
                        skinsByGun.put(gunEntry.getKey(), owned);
                        count += owned.ownedSkins().size();
                    }
                }
                if (!skinsByGun.isEmpty()) {
                    playerGunSkins.put(uuid, skinsByGun);
                }
            }
            return count;
        } catch (Exception e) {
            MercFrontCore.LOGGER.error("Failed to load player gun skins from {}", path, e);
            return 0;
        }
    }

    public boolean save(MinecraftServer server) {
        Path path = getPath(server);
        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            playerGunSkins.forEach((uuid, skinsByGun) -> {
                JsonObject playerObject = new JsonObject();
                skinsByGun.forEach((gunId, owned) -> {
                    JsonObject gunObject = new JsonObject();
                    gunObject.addProperty("selected", owned.selectedSkin());
                    JsonArray ownedArray = new JsonArray();
                    owned.ownedSkins().forEach(ownedArray::add);
                    gunObject.add("owned", ownedArray);
                    playerObject.add(gunId, gunObject);
                });
                root.add(uuid.toString(), playerObject);
            });
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(root, writer);
            }
            return true;
        } catch (IOException e) {
            MercFrontCore.LOGGER.error("Failed to save player gun skins to {}", path, e);
            return false;
        }
    }

    public String grantPlayerSkin(UUID uuid, String gunId, String skin) {
        OwnedGunSkins updated = playerGunSkins
            .computeIfAbsent(uuid, ignored -> new ConcurrentHashMap<>())
            .compute(gunId, (ignored, current) -> {
                LinkedHashSet<String> owned = new LinkedHashSet<>();
                if (current != null) {
                    owned.addAll(current.ownedSkins());
                }
                owned.add(skin);
                return new OwnedGunSkins(skin, List.copyOf(owned));
            });
        return updated.selectedSkin();
    }

    public boolean selectPlayerSkin(UUID uuid, String gunId, String skin) {
        Map<String, OwnedGunSkins> skinsByGun = playerGunSkins.get(uuid);
        if (skinsByGun == null) {
            return false;
        }
        OwnedGunSkins current = skinsByGun.get(gunId);
        if (current == null || !current.ownedSkins().contains(skin)) {
            return false;
        }
        skinsByGun.put(gunId, new OwnedGunSkins(skin, current.ownedSkins()));
        return true;
    }

    public RevokeResult revokePlayerSkin(UUID uuid, String gunId, String skin) {
        Map<String, OwnedGunSkins> skinsByGun = playerGunSkins.get(uuid);
        if (skinsByGun == null) {
            return RevokeResult.NONE;
        }

        OwnedGunSkins previous = skinsByGun.get(gunId);
        if (previous == null) {
            return RevokeResult.NONE;
        }

        if (skin == null) {
            skinsByGun.remove(gunId);
            if (skinsByGun.isEmpty()) {
                playerGunSkins.remove(uuid);
            }
            return new RevokeResult(previous.ownedSkins().size(), previous, null);
        }

        if (!previous.ownedSkins().contains(skin)) {
            return RevokeResult.NONE;
        }

        LinkedHashSet<String> owned = new LinkedHashSet<>(previous.ownedSkins());
        owned.remove(skin);
        if (owned.isEmpty()) {
            skinsByGun.remove(gunId);
            if (skinsByGun.isEmpty()) {
                playerGunSkins.remove(uuid);
            }
            return new RevokeResult(1, previous, null);
        }

        String selected = previous.selectedSkin();
        if (selected.equalsIgnoreCase(skin)) {
            selected = owned.iterator().next();
        }
        OwnedGunSkins current = new OwnedGunSkins(selected, List.copyOf(owned));
        skinsByGun.put(gunId, current);
        return new RevokeResult(1, previous, current);
    }

    public Map<String, OwnedGunSkins> getPlayerSkins(UUID uuid) {
        return playerGunSkins.getOrDefault(uuid, Map.of());
    }

    public PlayerGunSkinStatePacket toPacket(UUID uuid) {
        Map<Identifier, PlayerGunSkinStatePacket.GunSkinState> states = new ConcurrentHashMap<>();
        getPlayerSkins(uuid).forEach((gunId, owned) -> {
            Identifier id = Identifier.tryParse(gunId);
            if (id != null) {
                states.put(id, new PlayerGunSkinStatePacket.GunSkinState(owned.selectedSkin(), owned.ownedSkins()));
            }
        });
        return new PlayerGunSkinStatePacket(states);
    }

    public int applyToPlayer(ServerPlayerEntity player) {
        Map<String, OwnedGunSkins> skinsByGun = getPlayerSkins(player.getUuid());
        if (skinsByGun.isEmpty()) {
            return 0;
        }

        GunSkinIndex.ensureInitialized();
        int changed = 0;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (applyToStack(stack, skinsByGun)) {
                changed++;
            }
        }
        if (changed > 0) {
            player.getInventory().markDirty();
            player.currentScreenHandler.sendContentUpdates();
            player.playerScreenHandler.sendContentUpdates();
        }
        return changed;
    }

    public int reconcilePlayerGun(ServerPlayerEntity player, String gunId, OwnedGunSkins previous, OwnedGunSkins current) {
        if (previous == null) {
            return current == null ? 0 : applyToPlayer(player);
        }

        GunSkinIndex.ensureInitialized();
        int changed = 0;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (stack.isEmpty()) {
                continue;
            }
            Identifier itemId = Registries.ITEM.getId(stack.getItem());
            if (!gunId.equals(itemId.toString())) {
                continue;
            }
            String explicitPattern = stack.get(BFDataComponents.PATTERN_NAME);
            if (explicitPattern != null && !explicitPattern.isBlank()) {
                continue;
            }
            if (current != null && !current.selectedSkin().isBlank()) {
                if (applyToStack(stack, Map.of(gunId, current))) {
                    changed++;
                }
                continue;
            }

            Float currentSkin = stack.get(BFDataComponents.SKIN_ID);
            if (currentSkin == null) {
                continue;
            }
            boolean matchesRemoved = false;
            for (String removedSkin : previous.ownedSkins()) {
                Optional<Float> removedSkinId = GunSkinIndex.getSkinId(stack.getItem(), removedSkin);
                if (removedSkinId.isPresent() && Float.compare(currentSkin, removedSkinId.get()) == 0) {
                    matchesRemoved = true;
                    break;
                }
            }
            if (matchesRemoved) {
                stack.remove(BFDataComponents.SKIN_ID);
                changed++;
            }
        }
        if (changed > 0) {
            player.getInventory().markDirty();
            player.currentScreenHandler.sendContentUpdates();
            player.playerScreenHandler.sendContentUpdates();
        }
        return changed;
    }

    private static boolean applyToStack(ItemStack stack, Map<String, OwnedGunSkins> skinsByGun) {
        if (stack.isEmpty()) {
            return false;
        }

        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        OwnedGunSkins owned = skinsByGun.get(itemId.toString());
        if (owned == null || owned.selectedSkin().isBlank()) {
            return false;
        }
        String explicitPattern = stack.get(BFDataComponents.PATTERN_NAME);
        if (explicitPattern != null && !explicitPattern.isBlank()) {
            return false;
        }

        Optional<Float> skinId = GunSkinIndex.getSkinId(stack.getItem(), owned.selectedSkin());
        if (skinId.isEmpty()) {
            return false;
        }

        Float currentSkin = stack.get(BFDataComponents.SKIN_ID);
        if (currentSkin != null && Float.compare(currentSkin, skinId.get()) == 0) {
            return false;
        }

        stack.set(BFDataComponents.SKIN_ID, skinId.get());
        return true;
    }

    private static OwnedGunSkins parseOwnedGunSkins(JsonElement element) {
        if (element == null) {
            return null;
        }
        if (element.isJsonPrimitive()) {
            String skin = element.getAsString();
            return new OwnedGunSkins(skin, List.of(skin));
        }
        if (!element.isJsonObject()) {
            return null;
        }

        JsonObject obj = element.getAsJsonObject();
        LinkedHashSet<String> owned = new LinkedHashSet<>();
        if (obj.has("owned") && obj.get("owned").isJsonArray()) {
            for (JsonElement skinElement : obj.getAsJsonArray("owned")) {
                if (skinElement.isJsonPrimitive()) {
                    owned.add(skinElement.getAsString());
                }
            }
        }
        String selected = obj.has("selected") && obj.get("selected").isJsonPrimitive()
            ? obj.get("selected").getAsString()
            : "";
        if (owned.isEmpty() && !selected.isBlank()) {
            owned.add(selected);
        }
        if (!selected.isBlank()) {
            owned.add(selected);
        }
        if (owned.isEmpty()) {
            return null;
        }
        if (selected.isBlank()) {
            selected = owned.iterator().next();
        }
        return new OwnedGunSkins(selected, new ArrayList<>(owned));
    }

    public record OwnedGunSkins(String selectedSkin, List<String> ownedSkins) {
    }

    public record RevokeResult(int removedCount, OwnedGunSkins previousState, OwnedGunSkins currentState) {
        private static final RevokeResult NONE = new RevokeResult(0, null, null);
    }
}
