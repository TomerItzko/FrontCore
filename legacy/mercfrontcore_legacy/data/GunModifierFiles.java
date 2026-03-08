package red.vuis.mercfrontcore.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.file.PathUtils;

import red.vuis.mercfrontcore.AddonConstants;

public final class GunModifierFiles {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path BACKUP_PATH_SUFFIX = Path.of("backup");
	public static final Path GUN_MODIFIERS_PATH = Path.of("gunModifiers");
	
	private GunModifierFiles() {
	}
	
	public static void loadModifierMap(Path basePath, Map<RegistryEntry<Item>, GunModifier> target) throws IOException {
		if (!Files.isDirectory(basePath)) {
			return;
		}
		
		try (Stream<Path> pathStream = Files.walk(basePath)) {
			Iterator<Path> fileIterator = pathStream.filter(Files::isRegularFile).iterator();
			while (fileIterator.hasNext()) {
				Path file = fileIterator.next();
				String filename = FilenameUtils.removeExtension(file.getFileName().toString());
				Identifier id = Identifier.tryParse(filename.replace('+', ':'));
				if (id == null) {
					AddonConstants.LOGGER.error("Invalid gun modifier filename \"{}\"!", filename);
					continue;
				}
				
				Optional<RegistryEntry.Reference<Item>> itemEntryResult = Registries.ITEM.getEntry(id);
				if (itemEntryResult.isEmpty()) {
					AddonConstants.LOGGER.error("Unknown item {}! (filename \"{}\")", id, filename);
					continue;
				}
				RegistryEntry<Item> itemEntry = itemEntryResult.orElseThrow();
				
				JsonElement element;
				try (BufferedReader reader = Files.newBufferedReader(file)) {
					element = GSON.fromJson(reader, JsonElement.class);
				}
				DataResult<GunModifier> parseResult = GunModifier.CODEC.parse(JsonOps.INSTANCE, element);
				if (parseResult.isError()) {
					AddonConstants.LOGGER.error(parseResult.error().orElseThrow());
					continue;
				}
				
				target.put(itemEntry, parseResult.getOrThrow());
			}
		}
	}
	
	public static void saveModifierMap(Path basePath, Map<RegistryEntry<Item>, GunModifier> modifiers) throws IOException {
		Files.createDirectories(basePath);
		
		Path backupPath = basePath.resolve(BACKUP_PATH_SUFFIX);
		
		if (Files.exists(backupPath)) {
			PathUtils.deleteDirectory(backupPath);
		}
		
		if (!PathUtils.isEmptyDirectory(basePath)) {
			Files.createDirectories(backupPath);
			
			try (Stream<Path> pathStream = Files.walk(basePath)) {
				Iterator<Path> fileIterator = pathStream.filter(Files::isRegularFile).iterator();
				while (fileIterator.hasNext()) {
					Path oldModifierPath = fileIterator.next();
					Files.move(oldModifierPath, backupPath.resolve(oldModifierPath.getFileName()));
				}
			}
		}
		
		for (Map.Entry<RegistryEntry<Item>, GunModifier> modifierEntry : modifiers.entrySet()) {
			if (!modifierEntry.getValue().hasData()) {
				continue;
			}
			
			JsonElement serialized = GunModifier.CODEC.encodeStart(JsonOps.INSTANCE, modifierEntry.getValue())
				.resultOrPartial(AddonConstants.LOGGER::error)
				.orElseThrow();
			
			Path modifierPath = basePath.resolve(Path.of(modifierEntry.getKey().getIdAsString().replace(':', '+') + ".json"));
			try (BufferedWriter writer = Files.newBufferedWriter(modifierPath)) {
				GSON.toJson(serialized, writer);
			}
		}
	}
}
