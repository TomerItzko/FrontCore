package red.vuis.mercfrontcore.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.io.IOUtils;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.net.packet.SetProfileOverridesPacket;
import red.vuis.mercfrontcore.net.packet.SetProfileOverridesPropertyPacket;
import red.vuis.mercfrontcore.server.data.config.AddonServerConfig;
import red.vuis.mercfrontcore.util.AddonHttpUtils;
import red.vuis.mercfrontcore.util.AddonUtils;

public final class AddonCommonData {
	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
	private static AddonCommonData instance = null;
	
	public Map<UUID, PlayerCloudData> profileOverrides = new ConcurrentHashMap<>();
	
	private AddonCommonData() {
	}
	
	public static AddonCommonData getInstance() {
		if (instance == null) {
			instance = new AddonCommonData();
		}
		return instance;
	}
	
	public void syncProfileOverrides(Set<UUID> uuids) {
		PacketDistributor.sendToAllPlayers(new SetProfileOverridesPacket(
			profileOverrides.entrySet().stream().filter(e -> uuids.contains(e.getKey())).collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> ProfileOverrideData.of(e.getValue())
			))
		));
	}
	
	public void putNewProfileOverride(Pair<UUID, String> idPair) {
		profileOverrides.put(idPair.left(), AddonUtils.createPlayerCloudData(idPair));
	}
	
	public void putNewProfileOverrides(Collection<Pair<UUID, String>> idPairs) {
		idPairs.forEach(this::putNewProfileOverride);
	}
	
	public Map<UUID, ProfileOverrideData> getProfileOverrideData() {
		Map<UUID, ProfileOverrideData> data = new Object2ObjectOpenHashMap<>();
		for (Map.Entry<UUID, PlayerCloudData> entry : profileOverrides.entrySet()) {
			data.put(entry.getKey(), ProfileOverrideData.of(entry.getValue()));
		}
		return data;
	}
	
	public void fetchCloudProfiles(Set<Pair<UUID, String>> idPairs) {
		if (idPairs.isEmpty()) {
			throw new IllegalArgumentException("Empty ID pair set");
		}
		
		new Thread(() -> fetchCloudProfilesThread(idPairs), "Profile fetch").start();
	}
	
	private void fetchCloudProfilesThread(Set<Pair<UUID, String>> idPairs) {
		if (idPairs.size() == 1) {
			fetchSingleProfile(idPairs.iterator().next());
		} else {
			fetchMultipleProfiles(idPairs);
		}
		
		if (FMLEnvironment.dist.isDedicatedServer()) {
			syncProfileOverrides(idPairs.stream().map(Pair::left).collect(Collectors.toSet()));
		}
		
		idPairs.forEach(this::fetchProfileInventory);
	}
	
	private void fetchSingleProfile(Pair<UUID, String> idPair) {
		UUID uuid = idPair.left();
		
		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(AddonServerConfig.getProfileFetchHost() + "/api/v1/player_data?uuid=" + uuid.toString()))
			.build();
		
		HttpResponse<InputStream> response;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
		} catch (InterruptedException | IOException e) {
			AddonConstants.LOGGER.error("Exception while fetching profile for {}", uuid);
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		if (!AddonHttpUtils.isStatusOk(response.statusCode())) {
			try {
				AddonConstants.LOGGER.error("Failed to fetch profile for {} ({})\n{}", uuid, response.statusCode(), IOUtils.toString(response.body(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return;
		}
		
		JsonObject json;
		try {
			json = JsonParser.parseReader(AddonUtils.toBufferedReader(response.body())).getAsJsonObject();
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Failed to parse json for profile {}", uuid);
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		handleProfileJson(idPair, json);
		
		AddonConstants.LOGGER.info("Successfully fetched profile for {}", uuid);
	}
	
	private void fetchMultipleProfiles(Set<Pair<UUID, String>> idPairs) {
		HttpRequest request = HttpRequest.newBuilder()
			.POST(HttpRequest.BodyPublishers.ofString(idPairs.stream().map(p -> p.left().toString()).collect(Collectors.joining(","))))
			.uri(URI.create(AddonServerConfig.getProfileFetchHost() + "/api/v1/player_data/bulk"))
			.build();
		
		HttpResponse<InputStream> response;
		try {
			response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
		} catch (InterruptedException | IOException e) {
			AddonConstants.LOGGER.error("Exception while bulk fetching profile for {} players", idPairs.size());
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		if (!AddonHttpUtils.isStatusOk(response.statusCode())) {
			try {
				AddonConstants.LOGGER.error("Failed to bulk fetch profile for {} players ({})\n{}", idPairs.size(), response.statusCode(), IOUtils.toString(response.body(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return;
		}
		
		Map<UUID, JsonObject> profileJsonMap = new Object2ObjectOpenHashMap<>(idPairs.size());
		try {
			JsonArray json = JsonParser.parseReader(AddonUtils.toBufferedReader(response.body())).getAsJsonArray();
			for (JsonElement profileElement : json) {
				JsonObject profileObject = profileElement.getAsJsonObject();
				profileJsonMap.put(UUID.fromString(profileObject.get("uuid").getAsString()), profileObject);
			}
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Failed to parse json for bulk profile fetch", e);
			return;
		}
		
		profileJsonMap.forEach((id, json) -> handleProfileJson(Pair.of(id, null), json));
		
		AddonConstants.LOGGER.info("Successfully fetched profiles for {} players", idPairs.size());
	}
	
	private void handleProfileJson(Pair<UUID, String> idPair, JsonObject json) {
		UUID uuid = idPair.left();
		String username = idPair.right() == null ? json.get("username").getAsString() : idPair.right();
		
		PlayerCloudData cloudData = new PlayerCloudData(uuid);
		
		try {
			cloudData.setUsername(username);
			cloudData.setExp(json.get("exp").getAsInt());
			cloudData.setPrestigeLevel(json.get("prestige").getAsInt());
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Failed to handle json for profile {}", idPair);
			AddonConstants.LOGGER.throwing(e);
			profileOverrides.put(uuid, AddonUtils.createPlayerCloudData(uuid, username));
			return;
		}

		profileOverrides.put(uuid, cloudData);
	}
	
	private void fetchProfileInventory(Pair<UUID, String> idPair) {
		UUID uuid = idPair.left();
		
		HttpRequest inventoryRequest = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(AddonServerConfig.getProfileFetchHost() + "/api/v1/player_inventory?include_uuid=true&uuid=" + uuid))
			.build();
		
		HttpResponse<InputStream> inventoryResponse;
		try {
			inventoryResponse = HTTP_CLIENT.send(inventoryRequest, HttpResponse.BodyHandlers.ofInputStream());
		} catch (InterruptedException | IOException e) {
			AddonConstants.LOGGER.error("Exception while fetching inventory for player {}", uuid);
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		if (!AddonHttpUtils.isStatusOk(inventoryResponse.statusCode())) {
			try {
				AddonConstants.LOGGER.error("Failed to fetch equipped items for player {} ({})\n{}", uuid, inventoryResponse.statusCode(), IOUtils.toString(inventoryResponse.body(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return;
		}
		
		HttpRequest equippedRequest = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(AddonServerConfig.getProfileFetchHost() + "/api/v1/player_inventory/equipped?uuid=" + uuid))
			.build();
		
		HttpResponse<InputStream> equippedResponse;
		try {
			equippedResponse = HTTP_CLIENT.send(equippedRequest, HttpResponse.BodyHandlers.ofInputStream());
		} catch (InterruptedException | IOException e) {
			AddonConstants.LOGGER.error("Exception while fetching equipped items for player {}", uuid);
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		Set<UUID> equippedUuids;
		try {
			JsonObject equippedJson = JsonParser.parseReader(AddonUtils.toBufferedReader(equippedResponse.body())).getAsJsonObject();
			equippedUuids = equippedJson.get("equipped").getAsJsonArray().asList().stream().map(v -> AddonUtils.decodeBase64Uuid(v.getAsString())).collect(Collectors.toSet());
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Failed to parse equipped items json for profile {}", uuid);
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		JsonObject inventoryJson;
		try {
			inventoryJson = JsonParser.parseReader(AddonUtils.toBufferedReader(inventoryResponse.body())).getAsJsonObject();
		} catch (Exception e) {
			AddonConstants.LOGGER.error("Failed to parse inventory json for profile {}", uuid);
			AddonConstants.LOGGER.throwing(e);
			return;
		}
		
		List<CloudItemStack> itemStacks = inventoryJson.get("inventory").getAsJsonArray().asList().stream().map(json -> {
			JsonObject o = json.getAsJsonObject();
			
			UUID stackUuid = AddonUtils.decodeBase64Uuid(o.get("uuid").getAsString());
			if (!equippedUuids.contains(stackUuid)) {
				return null;
			}
			
			CloudItemStack stack = new CloudItemStack(
				AddonUtils.decodeBase64Uuid(o.get("uuid").getAsString()),
				o.get("id").getAsInt(),
				o.get("mint").getAsDouble()
			);
			if (o.has("name_tag")) {
				stack.setNameTag(o.get("name_tag").getAsString());
			}
			return stack;
		}).filter(Objects::nonNull).toList();
		
		AddonConstants.LOGGER.info("Profile {} has {} equipped items", uuid, itemStacks.size());
		
		PlayerCloudInventory inventory = profileOverrides.get(uuid).getInventory();
		inventory.onReceiveSection(itemStacks, 0);
		inventory.populateEquippedItems(AddonUtils.getBfManager().getCloudRegistry(), equippedUuids);
		
		AddonConstants.LOGGER.info("Successfully fetched inventory for profile {}", uuid);
		
		PacketDistributor.sendToAllPlayers(new SetProfileOverridesPropertyPacket(
			Set.of(idPair),
			IntObjectPair.of(3, itemStacks)
		));
	}
}
