package red.vuis.mercfrontcore.command.bf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.map.MapEnvironment;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

public final class AssetsCommandEx {
	private AssetsCommandEx() {
	}
	
	public static CompletableFuture<Suggestions> suggestAssetArgs(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
		AssetRegistry<?> registry = AssetStore.getInstance().method_1635(StringArgumentType.getString(context, "assetType"));
		if (registry == null) {
			return suggestions.buildFuture();
		}
		IAsset asset = registry.getByName(StringArgumentType.getString(context, "assetName"));
		if (asset == null) {
			return suggestions.buildFuture();
		}
		
		String[] args;
		try {
			args = StringArgumentType.getString(context, "args").split(" ");
		} catch (IllegalArgumentException e) {
			args = new String[0];
		}
		
		AssetCommandBuilder currentCommand = asset.getCommand();
		List<String> parents = new ArrayList<>();
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			// workaround for environment commands
			if (i == 2 && parents.get(0).equals("environment") && parents.get(1).equals("edit") && asset instanceof MapAsset mapAsset) {
				MapEnvironment environment = mapAsset.getEnvironments().get(arg);
				if (environment == null) {
					break;
				}
				currentCommand = environment.getCommand();
			} else {
				if (!currentCommand.subCommands.containsKey(arg)) {
					break;
				}
				currentCommand = currentCommand.subCommands.get(arg);
			}
			
			parents.add(arg);
		}
		
		Stream<String> resultStream = currentCommand.subCommands.keySet().stream();
		if (!parents.isEmpty()) {
			resultStream = resultStream.map(child -> String.join(" ", parents) + " " + child);
		}
		return CommandSource.suggestMatching(resultStream, suggestions);
	}
}
