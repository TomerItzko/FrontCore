package dev.tomerdev.mercfrontcore.server.command;

import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.client.sound.BFMusicSets;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.map.MapEnvironment;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class AssetCommandHelper {
    private AssetCommandHelper() {
    }

    public static CompletableFuture<Suggestions> suggestAssetTypes(
        CommandContext<ServerCommandSource> context,
        SuggestionsBuilder suggestions
    ) {
        AssetStore store = AssetStore.getInstance();
        if (store == null) {
            return suggestions.buildFuture();
        }
        return CommandSource.suggestMatching(store.getTypes(), suggestions);
    }

    public static CompletableFuture<Suggestions> suggestAssetNames(
        CommandContext<ServerCommandSource> context,
        SuggestionsBuilder suggestions
    ) {
        AssetRegistry<?> registry = getRegistry(context);
        if (registry == null) {
            return suggestions.buildFuture();
        }
        return CommandSource.suggestMatching(registry.getEntries().keySet(), suggestions);
    }

    public static CompletableFuture<Suggestions> suggestAssetArgs(
        CommandContext<ServerCommandSource> context,
        SuggestionsBuilder suggestions
    ) {
        AssetRegistry<?> registry = getRegistry(context);
        if (registry == null) {
            return suggestions.buildFuture();
        }

        IAsset asset = registry.getByName(StringArgumentType.getString(context, "assetName"));
        if (asset == null) {
            return suggestions.buildFuture();
        }

        ParsedArgs parsedArgs = parseArgs(context);
        CompletableFuture<Suggestions> mapSuggestions = suggestMapAssetArgs(asset, parsedArgs, suggestions);
        if (mapSuggestions != null) {
            return mapSuggestions;
        }

        AssetCommandBuilder currentCommand = asset.getCommand();
        List<String> parents = new ArrayList<>();
        for (int i = 0; i < parsedArgs.consumedArgs().size(); i++) {
            String arg = parsedArgs.consumedArgs().get(i);

            // BlockFront environment edit commands switch to the selected environment command tree.
            if (i == 2
                && parents.size() >= 2
                && "environment".equals(parents.get(0))
                && "edit".equals(parents.get(1))
                && asset instanceof MapAsset mapAsset) {
                MapEnvironment environment = mapAsset.getEnvironments().get(arg);
                if (environment == null) {
                    return suggestions.buildFuture();
                }
                currentCommand = environment.getCommand();
            } else {
                AssetCommandBuilder next = currentCommand.subCommands.get(arg);
                if (next == null) {
                    return suggestCommands(parents, currentCommand.subCommands.keySet(), suggestions);
                }
                currentCommand = next;
            }
            parents.add(arg);
        }

        return suggestCommands(parents, currentCommand.subCommands.keySet(), suggestions);
    }

    private static ParsedArgs parseArgs(CommandContext<ServerCommandSource> context) {
        String rawArgs;
        try {
            rawArgs = StringArgumentType.getString(context, "args");
        } catch (IllegalArgumentException ignored) {
            rawArgs = "";
        }

        if (rawArgs.isBlank()) {
            return new ParsedArgs(List.of(), "");
        }

        List<String> parts = new ArrayList<>(Arrays.asList(rawArgs.split(" ", -1)));
        String currentToken;
        if (rawArgs.endsWith(" ")) {
            currentToken = "";
            while (!parts.isEmpty() && parts.get(parts.size() - 1).isEmpty()) {
                parts.remove(parts.size() - 1);
            }
        } else {
            currentToken = parts.remove(parts.size() - 1);
        }
        return new ParsedArgs(List.copyOf(parts), currentToken);
    }

    private static CompletableFuture<Suggestions> suggestMapAssetArgs(
        IAsset asset,
        ParsedArgs parsedArgs,
        SuggestionsBuilder suggestions
    ) {
        if (!(asset instanceof MapAsset mapAsset)) {
            return null;
        }

        List<String> consumed = parsedArgs.consumedArgs();
        if (consumed.isEmpty()) {
            return null;
        }

        if ("musicSet".equals(consumed.get(0)) && consumed.size() == 1) {
            return suggestCommands(consumed, Arrays.asList(BFMusicSets.getIds()), suggestions);
        }

        if (consumed.size() >= 2
            && "environment".equals(consumed.get(0))
            && ("edit".equals(consumed.get(1)) || "remove".equals(consumed.get(1)))
            && consumed.size() == 2) {
            return suggestCommands(consumed, mapAsset.getEnvironments().keySet(), suggestions);
        }

        if (consumed.size() >= 3
            && "environment".equals(consumed.get(0))
            && "edit".equals(consumed.get(1))) {
            MapEnvironment environment = mapAsset.getEnvironments().get(consumed.get(2));
            if (environment == null) {
                return suggestCommands(consumed.subList(0, 2), mapAsset.getEnvironments().keySet(), suggestions);
            }

            List<String> nestedParents = consumed.subList(3, consumed.size());
            AssetCommandBuilder currentCommand = environment.getCommand();
            for (String parent : nestedParents) {
                AssetCommandBuilder next = currentCommand.subCommands.get(parent);
                if (next == null) {
                    return suggestCommands(buildEnvironmentPrefix(consumed.get(2), List.of()), currentCommand.subCommands.keySet(), suggestions);
                }
                currentCommand = next;
            }
            return suggestCommands(buildEnvironmentPrefix(consumed.get(2), nestedParents), currentCommand.subCommands.keySet(), suggestions);
        }

        if (consumed.size() >= 2 && "teams".equals(consumed.get(0)) && "set".equals(consumed.get(1))) {
            if (consumed.size() == 2) {
                return suggestCommands(consumed, List.of("Allies", "Axis"), suggestions);
            }

            if (consumed.size() == 3) {
                return suggestCommands(consumed, Arrays.stream(BFCountry.values()).map(BFCountry::getTag).toList(), suggestions);
            }

            if (consumed.size() == 4) {
                BFCountry country = BFCountry.fromTag(consumed.get(3));
                if (country == null) {
                    return suggestCommands(consumed.subList(0, 3), Arrays.stream(BFCountry.values()).map(BFCountry::getTag).toList(), suggestions);
                }
                return suggestCommands(consumed, getSkinsForCountry(country), suggestions);
            }
        }

        return null;
    }

    private static List<String> buildEnvironmentPrefix(String environmentName, List<String> nestedParents) {
        List<String> prefix = new ArrayList<>();
        prefix.add("environment");
        prefix.add("edit");
        prefix.add(environmentName);
        prefix.addAll(nestedParents);
        return prefix;
    }

    private static List<String> getSkinsForCountry(BFCountry country) {
        Set<String> skins = new LinkedHashSet<>();
        for (DivisionData divisionData : DivisionData.INSTANCES) {
            if (divisionData.getCountry() == country) {
                skins.add(divisionData.getSkin());
            }
        }
        return List.copyOf(skins);
    }

    private static CompletableFuture<Suggestions> suggestCommands(
        List<String> parents,
        Iterable<String> children,
        SuggestionsBuilder suggestions
    ) {
        Stream<String> resultStream = stream(children);
        if (!parents.isEmpty()) {
            String prefix = String.join(" ", parents);
            resultStream = resultStream.map(child -> prefix + " " + child);
        }
        return CommandSource.suggestMatching(resultStream, suggestions);
    }

    private static Stream<String> stream(Iterable<String> values) {
        if (values instanceof Collection<?> collection) {
            return collection.stream().map(String.class::cast);
        }
        List<String> copy = new ArrayList<>();
        for (String value : values) {
            copy.add(value);
        }
        return copy.stream();
    }

    public static int executeEdit(CommandContext<ServerCommandSource> context) {
        String assetType = StringArgumentType.getString(context, "assetType");
        String assetName = StringArgumentType.getString(context, "assetName");

        AssetRegistry<?> registry = getRegistry(assetType);
        if (registry == null) {
            context.getSource().sendError(Text.literal("Asset registry '" + assetType + "' was not found."));
            return 0;
        }

        IAsset asset = registry.getByName(assetName);
        if (asset == null) {
            context.getSource().sendError(
                Text.translatable("mercfrontcore.message.argument.asset.error.not_found", assetName, registry.getType())
            );
            return 0;
        }

        String[] args;
        try {
            String rawArgs = StringArgumentType.getString(context, "args");
            args = rawArgs.isBlank() ? new String[0] : rawArgs.split(" ");
        } catch (IllegalArgumentException ignored) {
            args = new String[0];
        }

        asset.getCommand().run(castContext(context), args);
        return 1;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static CommandContext castContext(CommandContext<ServerCommandSource> context) {
        return (CommandContext) context;
    }

    private static AssetRegistry<?> getRegistry(CommandContext<ServerCommandSource> context) {
        try {
            return getRegistry(StringArgumentType.getString(context, "assetType"));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private static AssetRegistry<?> getRegistry(String assetType) {
        AssetStore store = AssetStore.getInstance();
        if (store == null || assetType == null || assetType.isBlank()) {
            return null;
        }

        AssetRegistry<?> reflected = invokeRegistryLookup(store, assetType);
        if (reflected != null) {
            return reflected;
        }

        String target = assetType.toLowerCase(Locale.ROOT);
        for (Method method : store.getClass().getMethods()) {
            if (method.getParameterCount() != 0 || !AssetRegistry.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            try {
                Object value = method.invoke(store);
                if (value instanceof AssetRegistry<?> registry
                    && registry.getType().toLowerCase(Locale.ROOT).equals(target)) {
                    return registry;
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return null;
    }

    private static AssetRegistry<?> invokeRegistryLookup(AssetStore store, String assetType) {
        for (Method method : store.getClass().getMethods()) {
            if (method.getParameterCount() != 1
                || method.getParameterTypes()[0] != String.class
                || method.getReturnType() == Void.TYPE) {
                continue;
            }
            try {
                Object value = method.invoke(store, assetType);
                if (value instanceof AssetRegistry<?> registry) {
                    return registry;
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
        return null;
    }

    private record ParsedArgs(List<String> consumedArgs, String currentToken) {
    }
}
