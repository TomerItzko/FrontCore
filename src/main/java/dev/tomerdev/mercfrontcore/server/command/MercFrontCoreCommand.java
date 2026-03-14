package dev.tomerdev.mercfrontcore.server.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.math.FDSPose;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.reflect.Field;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import dev.tomerdev.mercfrontcore.data.GunModifier;
import dev.tomerdev.mercfrontcore.data.LoadoutStore;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import dev.tomerdev.mercfrontcore.data.ProfileOverrideData;
import dev.tomerdev.mercfrontcore.data.SkinRewardSelector;
import dev.tomerdev.mercfrontcore.net.packet.ClearProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.NewProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPropertyPacket;
import dev.tomerdev.mercfrontcore.server.ProxyCompatibility;
import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;

public final class MercFrontCoreCommand {
    private static final List<String> LOBBY_MODE_ORDER = List.of("ffa", "tdm", "dom", "conq", "inf", "gg", "ttt", "boot");
    private static final Map<String, Integer> LOBBY_MODE_INDEX = new HashMap<>();
    private static final Map<String, Integer> LOBBY_MAP_INDEX = new HashMap<>();
    private static final String LOBBY_ANY_KEY = "__any__";

    private MercFrontCoreCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(rootCommand("frontcore"));
        dispatcher.register(rootCommand("fc"));
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<ServerCommandSource> rootCommand(String name) {
        return literal(name)
            .then(literal("status").requires(MercFrontCoreCommand::isAdminCommandSource).executes(ctx -> showStatus(ctx.getSource())))
            .then(
                literal("profile").requires(MercFrontCoreCommand::isAdminCommandSource)
                    .then(literal("show").executes(ctx -> showProfileOverrides(ctx.getSource())))
                    .then(literal("save").executes(ctx -> saveProfileOverrides(ctx.getSource())))
                    .then(literal("reload").executes(ctx -> reloadProfileOverrides(ctx.getSource())))
                    .then(
                        literal("clear")
                            .then(argument("target", EntityArgumentType.players()).executes(ctx ->
                                clearProfileOverride(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "target"))
                            ))
                    )
                    .then(
                        literal("set")
                            .then(argument("target", EntityArgumentType.players())
                                .then(argument("displayName", StringArgumentType.word())
                                    .then(argument("level", IntegerArgumentType.integer(0))
                                        .then(argument("prestige", IntegerArgumentType.integer(0)).executes(ctx ->
                                            setProfileOverride(
                                                ctx.getSource(),
                                                EntityArgumentType.getPlayers(ctx, "target"),
                                                StringArgumentType.getString(ctx, "displayName"),
                                                IntegerArgumentType.getInteger(ctx, "level"),
                                                IntegerArgumentType.getInteger(ctx, "prestige")
                                            )
                                        ))
                                    )
                                )
                            )
                    )
            )
            .then(
                literal("gun")
                    .then(
                        literal("giveWithSkin").requires(MercFrontCoreCommand::isAdminCommandSource)
                            .then(argument("id", IdentifierArgumentType.identifier())
                                .suggests(MercFrontCoreCommand::suggestGunIds)
                                .then(argument("skin", StringArgumentType.word())
                                    .suggests(MercFrontCoreCommand::suggestGunSkins)
                                    .executes(ctx -> giveWithSkin(
                                        ctx.getSource(),
                                        IdentifierArgumentType.getIdentifier(ctx, "id"),
                                        StringArgumentType.getString(ctx, "skin")
                                    ))
                                )
                            )
                    )
                    .then(
                        literal("skinPlayer").requires(MercFrontCoreCommand::isAdminCommandSource)
                            .then(argument("target", EntityArgumentType.player())
                                .then(argument("id", IdentifierArgumentType.identifier())
                                    .suggests(MercFrontCoreCommand::suggestGunIds)
                                    .then(argument("skin", StringArgumentType.word())
                                        .suggests(MercFrontCoreCommand::suggestGunSkins)
                                        .executes(ctx -> setPermanentPlayerSkin(
                                            ctx.getSource(),
                                            EntityArgumentType.getPlayer(ctx, "target"),
                                            IdentifierArgumentType.getIdentifier(ctx, "id"),
                                            StringArgumentType.getString(ctx, "skin")
                                        ))
                                    )
                                )
                            )
                    )
                    .then(
                        literal("removeSkinPlayer").requires(MercFrontCoreCommand::isAdminCommandSource)
                            .then(argument("target", EntityArgumentType.player())
                                .then(literal("all").executes(ctx ->
                                    revokeAllPermanentPlayerSkins(
                                        ctx.getSource(),
                                        EntityArgumentType.getPlayer(ctx, "target")
                                    )
                                ))
                                .then(argument("id", IdentifierArgumentType.identifier())
                                    .suggests(MercFrontCoreCommand::suggestPlayerOwnedGunIds)
                                    .executes(ctx -> revokePermanentPlayerSkin(
                                        ctx.getSource(),
                                        EntityArgumentType.getPlayer(ctx, "target"),
                                        IdentifierArgumentType.getIdentifier(ctx, "id"),
                                        null
                                    ))
                                    .then(argument("skin", StringArgumentType.word())
                                        .suggests(MercFrontCoreCommand::suggestPlayerOwnedGunSkins)
                                        .executes(ctx -> revokePermanentPlayerSkin(
                                            ctx.getSource(),
                                            EntityArgumentType.getPlayer(ctx, "target"),
                                            IdentifierArgumentType.getIdentifier(ctx, "id"),
                                            StringArgumentType.getString(ctx, "skin")
                                        ))
                                    )
                                )
                            )
                    )
                    .then(
                        literal("modifier").requires(MercFrontCoreCommand::isAdminCommandSource)
                            .then(literal("list").executes(ctx -> listGunModifiers(ctx.getSource())))
                    )
            )
            .then(
                literal("lobby")
                    .then(literal("random").executes(ctx -> joinLobbyGame(ctx.getSource(), null)))
                    .then(literal("ffa").executes(ctx -> joinLobbyGame(ctx.getSource(), "ffa")))
                    .then(literal("tdm").executes(ctx -> joinLobbyGame(ctx.getSource(), "tdm")))
                    .then(literal("dom").executes(ctx -> joinLobbyGame(ctx.getSource(), "dom")))
                    .then(literal("conq").executes(ctx -> joinLobbyGame(ctx.getSource(), "conq")))
                    .then(literal("inf").executes(ctx -> joinLobbyGame(ctx.getSource(), "inf")))
                    .then(literal("gg").executes(ctx -> joinLobbyGame(ctx.getSource(), "gg")))
                    .then(literal("ttt").executes(ctx -> joinLobbyGame(ctx.getSource(), "ttt")))
                    .then(literal("boot").executes(ctx -> joinLobbyGame(ctx.getSource(), "boot")))
            )
            .then(
                literal("loadout").requires(MercFrontCoreCommand::isAdminCommandSource)
                    .then(literal("reload").executes(ctx -> reloadLoadouts(ctx.getSource())))
            )
            .then(
                literal("admin").requires(MercFrontCoreCommand::isAdminCommandSource)
                    .then(
                        literal("lobby")
                            .then(literal("debug").executes(ctx -> debugLobbyGames(ctx.getSource(), null)))
                            .then(literal("random").executes(ctx -> debugLobbyGames(ctx.getSource(), null)))
                            .then(literal("ffa").executes(ctx -> debugLobbyGames(ctx.getSource(), "ffa")))
                            .then(literal("tdm").executes(ctx -> debugLobbyGames(ctx.getSource(), "tdm")))
                            .then(literal("dom").executes(ctx -> debugLobbyGames(ctx.getSource(), "dom")))
                            .then(literal("conq").executes(ctx -> debugLobbyGames(ctx.getSource(), "conq")))
                            .then(literal("inf").executes(ctx -> debugLobbyGames(ctx.getSource(), "inf")))
                            .then(literal("gg").executes(ctx -> debugLobbyGames(ctx.getSource(), "gg")))
                            .then(literal("ttt").executes(ctx -> debugLobbyGames(ctx.getSource(), "ttt")))
                            .then(literal("boot").executes(ctx -> debugLobbyGames(ctx.getSource(), "boot")))
                    )
                    .then(
                        literal("proxy")
                            .then(literal("show").executes(ctx -> showStatus(ctx.getSource())))
                            .then(literal("reload").executes(ctx -> reloadConfig(ctx.getSource())))
                            .then(literal("save").executes(ctx -> saveConfig(ctx.getSource())))
                            .then(
                                literal("set")
                                    .then(
                                        literal("compatibility")
                                            .then(argument("value", BoolArgumentType.bool()).executes(ctx ->
                                                setCompatibility(ctx.getSource(), BoolArgumentType.getBool(ctx, "value"))
                                            ))
                                    )
                                    .then(
                                        literal("directOnly")
                                            .then(argument("value", BoolArgumentType.bool()).executes(ctx ->
                                                setDirectOnly(ctx.getSource(), BoolArgumentType.getBool(ctx, "value"))
                                            ))
                                    )
                                    .then(
                                        literal("trustForwardedIdentity")
                                            .then(argument("value", BoolArgumentType.bool()).executes(ctx ->
                                                setTrustForwardedIdentity(ctx.getSource(), BoolArgumentType.getBool(ctx, "value"))
                                            ))
                                    )
                            )
                    )
                    .then(
                        literal("randomDrop")
                            .then(argument("players", EntityArgumentType.players()).executes(ctx ->
                                randomDrop(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "players"), 1, null)
                            ))
                            .then(argument("players", EntityArgumentType.players())
                                .then(argument("count", IntegerArgumentType.integer(1, 100))
                                    .executes(ctx ->
                                        randomDrop(
                                            ctx.getSource(),
                                            EntityArgumentType.getPlayers(ctx, "players"),
                                            IntegerArgumentType.getInteger(ctx, "count"),
                                            null
                                        )
                                    )
                                    .then(argument("rarity", StringArgumentType.word())
                                        .suggests(MercFrontCoreCommand::suggestSkinRarities)
                                        .executes(ctx ->
                                            randomDrop(
                                                ctx.getSource(),
                                                EntityArgumentType.getPlayers(ctx, "players"),
                                                IntegerArgumentType.getInteger(ctx, "count"),
                                                StringArgumentType.getString(ctx, "rarity")
                                            )
                                        )
                                    )
                                )
                            )
                    )
                    .then(
                        literal("infected")
                            .then(
                                literal("vendor")
                                    .then(literal("relocate").executes(ctx -> forceInfectedVendorRelocate(ctx.getSource(), false)))
                                    .then(literal("here").executes(ctx -> forceInfectedVendorRelocate(ctx.getSource(), true)))
                            )
                    )
            );
    }

    private static boolean isAdminCommandSource(ServerCommandSource source) {
        return source.hasPermissionLevel(2);
    }

    private static CompletableFuture<Suggestions> suggestGunIds(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        GunSkinIndex.ensureInitialized();
        return CommandSource.suggestMatching(
            GunSkinIndex.SKINS.keySet().stream().map(Identifier::toString),
            suggestions
        );
    }

    private static CompletableFuture<Suggestions> suggestPlayerOwnedGunIds(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        try {
            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
            return CommandSource.suggestMatching(
                PlayerGunSkinStore.getInstance().getPlayerSkins(target.getUuid()).keySet(),
                suggestions
            );
        } catch (Exception ignored) {
            return suggestions.buildFuture();
        }
    }

    private static CompletableFuture<Suggestions> suggestPlayerOwnedGunSkins(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        try {
            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
            Identifier id = IdentifierArgumentType.getIdentifier(context, "id");
            var owned = PlayerGunSkinStore.getInstance().getPlayerSkins(target.getUuid()).get(id.toString());
            if (owned == null) {
                return suggestions.buildFuture();
            }
            return CommandSource.suggestMatching(owned.ownedSkins(), suggestions);
        } catch (Exception ignored) {
            return suggestions.buildFuture();
        }
    }

    private static CompletableFuture<Suggestions> suggestGunSkins(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        GunSkinIndex.ensureInitialized();
        Identifier id = extractGunIdForSkinSuggestion(context.getInput());
        if (id == null) {
            return suggestions.buildFuture();
        }
        var selectableSkins = SkinRewardSelector.getSelectableGunSkins(id);
        if (!selectableSkins.isEmpty()) {
            return CommandSource.suggestMatching(selectableSkins, suggestions);
        }
        return suggestions.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestSkinRarities(
        CommandContext<ServerCommandSource> context,
        SuggestionsBuilder suggestions
    ) {
        return CommandSource.suggestMatching(SkinRewardSelector.RARITY_NAMES, suggestions);
    }

    private static Identifier extractGunIdForSkinSuggestion(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        String[] tokens = input.trim().split("\\s+");
        if (tokens.length < 4) {
            return null;
        }
        if (!"gun".equals(tokens[1])) {
            return null;
        }
        if ("giveWithSkin".equals(tokens[2]) && tokens.length >= 4) {
            return Identifier.tryParse(tokens[3]);
        }
        if ("skinPlayer".equals(tokens[2]) && tokens.length >= 5) {
            return Identifier.tryParse(tokens[4]);
        }
        return null;
    }

    private static int joinLobbyGame(ServerCommandSource source, String modeKey) {
        ServerPlayerEntity player;
        try {
            player = source.getPlayerOrThrow();
        } catch (Exception e) {
            source.sendError(Text.translatable("mercfrontcore.message.command.error.player"));
            return 0;
        }

        Object manager = getBfManager();
        if (manager == null) {
            source.sendError(Text.literal("BlockFront manager is unavailable."));
            return 0;
        }

        if (invokeGameWithPlayer(manager, player.getUuid()) != null) {
            source.sendError(Text.literal("You are already assigned to a BlockFront game."));
            return 0;
        }

        List<Object> candidateGames = findJoinableGames(source, manager, modeKey);
        if (candidateGames.isEmpty()) {
            List<Object> fallbackGames = selectLobbyFallbackGames(source, manager, modeKey);
            if (!fallbackGames.isEmpty()) {
                candidateGames = fallbackGames;
            } else {
                source.sendError(Text.literal(
                    modeKey == null
                        ? "No active or reusable configured BlockFront game is available right now."
                        : "No active or reusable configured " + modeKey + " game is available right now."
                ));
                return 0;
            }
        }

        Object joinedGame = null;
        for (Object candidateGame : candidateGames) {
            try {
                prepareConfiguredFallbackGame(source, candidateGame);
                if (assignPlayerToScheduledMatch(manager, player, candidateGame) || assignPlayerToGame(source, manager, player, candidateGame)) {
                    joinedGame = candidateGame;
                    break;
                }
            } catch (Throwable t) {
                MercFrontCore.LOGGER.warn(
                    "Lobby fallback candidate failed: mode={}, type={}, name={}, map={}",
                    resolveGameModeKey(candidateGame),
                    getGameType(candidateGame),
                    getGameName(candidateGame),
                    candidateGame instanceof AbstractGame<?, ?, ?> bfGame && bfGame.getMap() != null ? bfGame.getMap().getName() : "-",
                    t
                );
            }
        }
        if (joinedGame == null) {
            source.sendError(Text.literal("Failed to join the selected BlockFront game."));
            return 0;
        }

        String joinedMode = resolveGameModeKey(joinedGame);
        source.sendFeedback(
            () -> Text.literal("Joined " + (joinedMode == null ? "active game" : joinedMode + " game") + "."),
            false
        );
        return 1;
    }

    private static List<Object> findJoinableGames(ServerCommandSource source, Object manager, String modeKey) {
        return new ArrayList<>(findRankedJoinableGames(source, manager, modeKey).values().stream().map(RankedGame::game).toList());
    }

    private static List<Object> selectLobbyFallbackGames(ServerCommandSource source, Object manager, String modeKey) {
        if (!(manager instanceof BFAbstractManager<?, ?, ?> bfManager)) {
            return List.of();
        }

        if (modeKey != null) {
            List<AbstractGame<?, ?, ?>> games = getSortedConfiguredReusableGamesForMode(source, bfManager, modeKey);
            if (games.isEmpty()) {
                return List.of();
            }
            return rotateGames(games, nextIndex(LOBBY_MAP_INDEX, modeKey, games.size()));
        }

        List<String> availableModes = new ArrayList<>();
        Map<String, List<AbstractGame<?, ?, ?>>> gamesByMode = new HashMap<>();
        for (String candidateMode : LOBBY_MODE_ORDER) {
            List<AbstractGame<?, ?, ?>> games = getSortedConfiguredReusableGamesForMode(source, bfManager, candidateMode);
            if (games.isEmpty()) {
                continue;
            }
            availableModes.add(candidateMode);
            gamesByMode.put(candidateMode, games);
        }
        if (availableModes.isEmpty()) {
            return List.of();
        }

        String mode = availableModes.get(nextIndex(LOBBY_MODE_INDEX, "random", availableModes.size()));
        List<AbstractGame<?, ?, ?>> games = gamesByMode.getOrDefault(mode, List.of());
        if (games.isEmpty()) {
            return List.of();
        }
        return rotateGames(games, nextIndex(LOBBY_MAP_INDEX, mode, games.size()));
    }

    private static List<Object> rotateGames(List<AbstractGame<?, ?, ?>> games, int startIndex) {
        if (games.isEmpty()) {
            return List.of();
        }
        List<Object> ordered = new ArrayList<>(games.size());
        for (int i = 0; i < games.size(); i++) {
            ordered.add(games.get((startIndex + i) % games.size()));
        }
        return ordered;
    }

    private static List<AbstractGame<?, ?, ?>> getSortedConfiguredReusableGamesForMode(
        ServerCommandSource source,
        BFAbstractManager<?, ?, ?> manager,
        String modeKey
    ) {
        String requestedMode = resolveGameModeKey(modeKey);
        if (requestedMode == null) {
            return List.of();
        }
        List<AbstractGame<?, ?, ?>> games = new ArrayList<>();
        Object gamesMap = manager.getGames();
        if (!(gamesMap instanceof Map<?, ?> entries)) {
            return games;
        }
        for (Object asset : entries.values()) {
            Object game = extractConfiguredGame(asset);
            if (!(game instanceof AbstractGame<?, ?, ?> bfGame)) {
                continue;
            }
            String resolvedMode = resolveGameModeKey(bfGame);
            if (resolvedMode == null || !resolvedMode.equalsIgnoreCase(requestedMode)) {
                continue;
            }
            String status = getGameStatusText(bfGame);
            int onlinePlayerCount = getOnlineAssignedPlayerCount(source, manager, bfGame);
            if (!"IDLE".equalsIgnoreCase(status) && onlinePlayerCount > 0) {
                continue;
            }
            games.add(bfGame);
        }
        games.sort(
            Comparator.comparing((AbstractGame<?, ?, ?> game) -> safeString(game.getMap() == null ? null : game.getMap().getName()))
                .thenComparing(game -> safeString(game.getName()))
                .thenComparing(game -> safeString(game.getUUID() == null ? null : game.getUUID().toString()))
        );
        return games;
    }

    private static int getOnlineAssignedPlayerCount(
        ServerCommandSource source,
        BFAbstractManager<?, ?, ?> manager,
        AbstractGame<?, ?, ?> game
    ) {
        int count = 0;
        for (ServerPlayerEntity online : source.getServer().getPlayerManager().getPlayerList()) {
            Object assigned = invokeGameWithPlayer(manager, online.getUuid());
            if (assigned == game) {
                count++;
            }
        }
        return count;
    }

    private static void prepareConfiguredFallbackGame(ServerCommandSource source, Object game) {
        if (!(game instanceof AbstractGame<?, ?, ?> bfGame)) {
            return;
        }
        if (getGamePlayerCount(bfGame) > 0) {
            return;
        }
        if ("IDLE".equalsIgnoreCase(getGameStatusText(bfGame))) {
            return;
        }
        try {
            bfGame.reset(source.getWorld());
            bfGame.setStatus(com.boehmod.blockfront.game.GameStatus.IDLE);
        } catch (Throwable t) {
            MercFrontCore.LOGGER.warn(
                "Failed to prepare configured fallback game: type={}, name={}, map={}",
                getGameType(bfGame),
                getGameName(bfGame),
                bfGame.getMap() != null ? bfGame.getMap().getName() : "-",
                t
            );
        }
    }

    private static List<AbstractGame<?, ?, ?>> getSortedAvailableGamesForMode(BFAbstractManager<?, ?, ?> manager, String modeKey) {
        BFGameType type = resolveBfGameType(modeKey);
        if (type == null) {
            return List.of();
        }
        List<AbstractGame<?, ?, ?>> games = new ArrayList<>(manager.getAvailableGames(type, null));
        if (games.isEmpty()) {
            games.addAll(getSortedFallbackGamesForMode(manager, modeKey));
        }
        games.sort(
            Comparator.comparing((AbstractGame<?, ?, ?> game) -> safeString(game.getMap() == null ? null : game.getMap().getName()))
                .thenComparing(game -> safeString(game.getName()))
                .thenComparing(game -> safeString(game.getUUID() == null ? null : game.getUUID().toString()))
        );
        return games;
    }

    private static List<AbstractGame<?, ?, ?>> getSortedFallbackGamesForMode(BFAbstractManager<?, ?, ?> manager, String modeKey) {
        List<AbstractGame<?, ?, ?>> games = new ArrayList<>();
        BFGameType requestedType = resolveBfGameType(modeKey);
        String requestedName = requestedType == null ? null : requestedType.getName();
        Object gamesMap = manager.getGames();
        if (!(gamesMap instanceof Map<?, ?> entries)) {
            return games;
        }
        for (Object asset : entries.values()) {
            Object game = invokeNoArg(asset, "getGame");
            if (!(game instanceof AbstractGame<?, ?, ?> bfGame)) {
                continue;
            }
            if (modeKey == null) {
                games.add(bfGame);
                continue;
            }
            String gameType = getGameType(bfGame);
            if (requestedName != null && gameType != null && gameType.equalsIgnoreCase(requestedName)) {
                games.add(bfGame);
                continue;
            }
            String gameMode = resolveGameModeKey(gameType);
            if (gameMode == null || !gameMode.equalsIgnoreCase(modeKey)) {
                continue;
            }
            games.add(bfGame);
        }
        return games;
    }

    private static int nextIndex(Map<String, Integer> state, String key, int size) {
        if (size <= 0) {
            return 0;
        }
        int index = state.getOrDefault(key, -1) + 1;
        if (index >= size) {
            index = 0;
        }
        state.put(key, index);
        return index;
    }

    private static String safeString(String value) {
        return value == null ? "" : value;
    }

    private static Map<String, RankedGame> findRankedJoinableGames(ServerCommandSource source, Object manager, String modeKey) {
        Map<String, RankedGame> discoveredGames = new java.util.LinkedHashMap<>();
        collectAvailableGames(manager, modeKey, discoveredGames);
        collectLivePlayerGames(source, manager, discoveredGames);
        collectReflectedGames(manager, discoveredGames);

        Object gamesObj = invokeNoArg(manager, "getGamesInRotation");
        if (manager instanceof BFAbstractManager<?, ?, ?> bfManager) {
            gamesObj = bfManager.getGamesInRotation();
        } else if (!(gamesObj instanceof Map<?, ?>)) {
            gamesObj = getGamesMap();
        }
        if (gamesObj instanceof Map<?, ?> games) {
            for (Object gameAsset : games.values()) {
                Object game = extractConfiguredGame(gameAsset);
                addDiscoveredGame(discoveredGames, game, "listed");
            }
        }

        List<RankedGame> rankedGames = new ArrayList<>();
        for (RankedGame discovered : discoveredGames.values()) {
            Object game = discovered.game();
            String gameMode = resolveGameModeKey(game);
            if (modeKey != null && !modeKey.equalsIgnoreCase(gameMode)) {
                continue;
            }

            int priority = getJoinPriority(game);
            if (priority > 0) {
                rankedGames.add(new RankedGame(game, priority, discovered.source()));
            }
        }

        rankedGames.sort((left, right) -> Integer.compare(right.priority(), left.priority()));
        if (rankedGames.size() > 1) {
            int topPriority = rankedGames.getFirst().priority();
            List<RankedGame> topGames = new ArrayList<>();
            int index = 0;
            while (index < rankedGames.size() && rankedGames.get(index).priority() == topPriority) {
                topGames.add(rankedGames.get(index));
                index++;
            }
            java.util.Collections.shuffle(topGames, ThreadLocalRandom.current());
            for (int i = 0; i < topGames.size(); i++) {
                rankedGames.set(i, topGames.get(i));
            }
        }
        Map<String, RankedGame> out = new java.util.LinkedHashMap<>();
        for (RankedGame ranked : rankedGames) {
            out.put(gameKey(ranked.game()), ranked);
        }
        return out;
    }

    private static void collectAvailableGames(Object manager, String modeKey, Map<String, RankedGame> discoveredGames) {
        if (!(manager instanceof BFAbstractManager<?, ?, ?> bfManager)) {
            return;
        }

        if (modeKey != null) {
            BFGameType type = resolveBfGameType(modeKey);
            if (type != null) {
                for (AbstractGame<?, ?, ?> game : bfManager.getAvailableGames(type, null)) {
                    addDiscoveredGame(discoveredGames, game, "available:" + modeKey);
                }
            }
            return;
        }

        for (BFGameType type : BFGameType.values()) {
            for (AbstractGame<?, ?, ?> game : bfManager.getAvailableGames(type, null)) {
                addDiscoveredGame(discoveredGames, game, "available:" + type.getName());
            }
        }
    }

    private static void collectLivePlayerGames(ServerCommandSource source, Object manager, Map<String, RankedGame> discoveredGames) {
        for (ServerPlayerEntity online : source.getServer().getPlayerManager().getPlayerList()) {
            addDiscoveredGame(discoveredGames, invokeGameWithPlayer(manager, online.getUuid()), "player:" + online.getNameForScoreboard());
        }
    }

    private static int debugLobbyGames(ServerCommandSource source, String modeKey) {
        Object manager = getBfManager();
        if (manager == null) {
            source.sendError(Text.literal("BlockFront manager is unavailable."));
            return 0;
        }

        Map<String, RankedGame> rankedGames = findRankedJoinableGames(source, manager, modeKey);
        source.sendFeedback(
            () -> Text.literal(
                "Lobby debug: mode=" + (modeKey == null ? "random" : modeKey) + ", discovered=" + rankedGames.size()
            ),
            false
        );
        if (rankedGames.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No candidate games were discovered."), false);
        } else {
            for (RankedGame ranked : rankedGames.values()) {
                Object game = ranked.game();
                source.sendFeedback(
                    () -> Text.literal(
                        "[" + ranked.source() + "] priority=" + ranked.priority()
                            + ", mode=" + valueOrDash(resolveGameModeKey(game))
                            + ", status=" + valueOrDash(getGameStatusText(game))
                            + ", players=" + getGamePlayerCount(game)
                            + ", name=" + valueOrDash(getGameName(game))
                            + ", type=" + valueOrDash(getGameType(game))
                            + ", uuid=" + valueOrDash(String.valueOf(extractGameUuid(game)))
                    ),
                    false
                );
            }
        }

        List<AbstractGame<?, ?, ?>> reusableGames =
            manager instanceof BFAbstractManager<?, ?, ?> bfManager
                ? getSortedConfiguredReusableGamesForMode(source, bfManager, modeKey)
                : List.of();

        source.sendFeedback(
            () -> Text.literal(
                "Configured reusable games: " + reusableGames.size()
            ),
            false
        );
        for (AbstractGame<?, ?, ?> game : reusableGames) {
            source.sendFeedback(
                () -> Text.literal(
                    "[reusable] mode=" + valueOrDash(resolveGameModeKey(game))
                        + ", status=" + valueOrDash(getGameStatusText(game))
                        + ", players=" + getGamePlayerCount(game)
                        + ", onlinePlayers=" + (manager instanceof BFAbstractManager<?, ?, ?> bfManager ? getOnlineAssignedPlayerCount(source, bfManager, game) : 0)
                        + ", name=" + valueOrDash(getGameName(game))
                        + ", type=" + valueOrDash(getGameType(game))
                        + ", uuid=" + valueOrDash(String.valueOf(extractGameUuid(game)))
                ),
                false
            );
        }

        Object gamesMap = manager instanceof BFAbstractManager<?, ?, ?> bfManager ? bfManager.getGames() : getGamesMap();
        int configuredCount = 0;
        if (gamesMap instanceof Map<?, ?> entries) {
            configuredCount = entries.size();
            source.sendFeedback(() -> Text.literal("Configured BF game assets: " + entries.size()), false);
            for (Map.Entry<?, ?> entry : entries.entrySet()) {
                Object asset = entry.getValue();
                Object rawGame = extractConfiguredGame(asset);
                if (!(rawGame instanceof AbstractGame<?, ?, ?> game)) {
                    source.sendFeedback(
                        () -> Text.literal(
                            "[asset] key=" + valueOrDash(String.valueOf(entry.getKey()))
                                + ", assetClass=" + valueOrDash(asset == null ? null : asset.getClass().getName())
                                + ", game=-"
                        ),
                        false
                    );
                    continue;
                }
                int onlinePlayers = manager instanceof BFAbstractManager<?, ?, ?> bfManager2
                    ? getOnlineAssignedPlayerCount(source, bfManager2, game)
                    : 0;
                source.sendFeedback(
                    () -> Text.literal(
                        "[asset] key=" + valueOrDash(String.valueOf(entry.getKey()))
                            + ", mode=" + valueOrDash(resolveGameModeKey(game))
                            + ", status=" + valueOrDash(getGameStatusText(game))
                            + ", players=" + getGamePlayerCount(game)
                            + ", onlinePlayers=" + onlinePlayers
                            + ", name=" + valueOrDash(getGameName(game))
                            + ", type=" + valueOrDash(getGameType(game))
                    ),
                    false
                );
            }
        }

        if (configuredCount == 0) {
            source.sendFeedback(() -> Text.literal("Configured BF game assets: 0"), false);
        }
        return 1;
    }

    private static void collectReflectedGames(Object root, Map<String, RankedGame> discoveredGames) {
        java.util.IdentityHashMap<Object, Boolean> visited = new java.util.IdentityHashMap<>();
        collectReflectedGames(root, discoveredGames, visited, 0, "reflected");
    }

    private static void collectReflectedGames(
        Object value,
        Map<String, RankedGame> discoveredGames,
        java.util.IdentityHashMap<Object, Boolean> visited,
        int depth,
        String source
    ) {
        if (value == null || depth > 4 || visited.put(value, Boolean.TRUE) != null) {
            return;
        }
        if (value instanceof AbstractGame<?, ?, ?> game) {
            addDiscoveredGame(discoveredGames, game, source);
            return;
        }
        if (value instanceof Map<?, ?> map) {
            for (Object entryValue : map.values()) {
                collectReflectedGames(entryValue, discoveredGames, visited, depth + 1, source);
            }
            return;
        }
        if (value instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                collectReflectedGames(element, discoveredGames, visited, depth + 1, source);
            }
            return;
        }
        Class<?> type = value.getClass();
        if (type.isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                collectReflectedGames(java.lang.reflect.Array.get(value, i), discoveredGames, visited, depth + 1, source);
            }
            return;
        }
        if (isTerminalType(type)) {
            return;
        }
        for (Field field : getAllFields(type)) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                collectReflectedGames(field.get(value), discoveredGames, visited, depth + 1, source + "." + field.getName());
            } catch (Throwable ignored) {
            }
        }
    }

    private static boolean isTerminalType(Class<?> type) {
        return type.isPrimitive()
            || Number.class.isAssignableFrom(type)
            || CharSequence.class.isAssignableFrom(type)
            || Boolean.class == type
            || UUID.class == type
            || type.isEnum()
            || type.getName().startsWith("java.time.");
    }

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                fields.add(field);
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    private static void addDiscoveredGame(Map<String, RankedGame> discoveredGames, Object game, String source) {
        if (game == null) {
            return;
        }
        discoveredGames.putIfAbsent(gameKey(game), new RankedGame(game, 0, source));
    }

    private static String gameKey(Object game) {
        UUID gameUuid = extractGameUuid(game);
        String key = gameUuid == null ? null : gameUuid.toString();
        if (key == null || key.isBlank() || "null".equalsIgnoreCase(key)) {
            key = game.getClass().getName() + "@" + System.identityHashCode(game);
        }
        return key;
    }

    private static int getJoinPriority(Object game) {
        String statusText = getGameStatusText(game).toUpperCase(java.util.Locale.ROOT);
        int playerCount = getGamePlayerCount(game);
        if ("GAME".equals(statusText)) {
            return playerCount > 0 ? 5 : 4;
        }
        if ("PRE_GAME".equals(statusText)) {
            return playerCount > 0 ? 4 : 3;
        }
        if ("IDLE".equals(statusText)) {
            return 0;
        }
        return playerCount > 0 ? 2 : 1;
    }

    private static int getGamePlayerCount(Object game) {
        if (game instanceof AbstractGame<?, ?, ?> bfGame) {
            return bfGame.getPlayerManager().getPlayerUUIDs().size();
        }
        Object playerManager = invokeNoArg(game, "getPlayerManager");
        if (playerManager == null) {
            return 0;
        }
        Object uuids = invokeNoArg(playerManager, "getPlayerUUIDs");
        if (uuids instanceof Collection<?> players) {
            return players.size();
        }
        return 0;
    }

    private static String resolveGameModeKey(Object game) {
        if (game instanceof AbstractGame<?, ?, ?> bfGame) {
            for (BFGameType type : BFGameType.values()) {
                if (type.getGameClass().isInstance(bfGame)) {
                    return type.getName();
                }
            }
        }
        String resolved = resolveGameModeKey(getGameType(game));
        if (resolved != null) {
            return resolved;
        }
        resolved = resolveGameModeKey(game.getClass().getSimpleName());
        if (resolved != null) {
            return resolved;
        }
        resolved = resolveGameModeKey(getGameName(game));
        if (resolved != null) {
            return resolved;
        }
        return null;
    }

    private static String resolveGameModeKey(String rawValue) {
        if (rawValue == null) {
            return null;
        }
        String text = rawValue.toLowerCase();
        if (text.contains("dom")) return "dom";
        if (text.contains("conq") || text.contains("conquest")) return "conq";
        if (text.contains("ffa") || text.contains("free_for_all") || text.contains("freeforall") || text.contains("free for all")) return "ffa";
        if (text.contains("tdm") || text.contains("team_deathmatch") || text.contains("teamdeathmatch") || text.contains("deathmatch")) return "tdm";
        if (text.contains("inf") || text.contains("infected")) return "inf";
        if (text.contains("gg") || text.contains("gun_game") || text.contains("gungame") || text.contains("gun game")) return "gg";
        if (text.contains("ttt") || text.contains("trouble_town") || text.contains("troubletown") || text.contains("trouble in terrorist town")) return "ttt";
        if (text.contains("boot") || text.contains("bootcamp")) return "boot";
        return null;
    }

    private static BFGameType resolveBfGameType(String modeKey) {
        if (modeKey == null || modeKey.isBlank()) {
            return null;
        }
        String normalized = resolveGameModeKey(modeKey);
        if (normalized == null) {
            normalized = modeKey.toLowerCase(java.util.Locale.ROOT);
        }
        return BFGameType.getByName(normalized);
    }

    private static Object invokeGameWithPlayer(Object manager, UUID uuid) {
        if (manager instanceof BFAbstractManager<?, ?, ?> bfManager) {
            return bfManager.getGameWithPlayer(uuid);
        }
        try {
            return manager.getClass().getMethod("getGameWithPlayer", UUID.class).invoke(manager, uuid);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static boolean assignPlayerToGame(ServerCommandSource source, Object manager, ServerPlayerEntity player, Object game) {
        if (manager instanceof BFAbstractManager<?, ?, ?> bfManager && game instanceof AbstractGame<?, ?, ?> bfGame) {
            return bfManager.assignPlayerToGame(source.getWorld(), player, bfGame);
        }
        Object teamJoinType = findRandomTeamJoinType();
        for (var method : manager.getClass().getMethods()) {
            if (!method.getName().equals("assignPlayerToGame")) {
                continue;
            }
            try {
                Object[] args = buildAssignPlayerArgs(method.getParameterTypes(), source.getWorld(), player, game, teamJoinType, manager);
                if (args == null) {
                    continue;
                }
                Object result = method.invoke(manager, args);
                return !(result instanceof Boolean ok) || ok;
            } catch (Throwable ignored) {
            }
        }
        return tryPlayerJoinTeam(manager, source.getWorld(), player, game, teamJoinType);
    }

    private static boolean assignPlayerToScheduledMatch(Object manager, ServerPlayerEntity player, Object game) {
        UUID gameUuid = extractGameUuid(game);
        if (gameUuid == null) {
            return false;
        }

        Object mmCache = invokeNoArg(manager, "getMatchMakingCache");
        if (mmCache == null) {
            return false;
        }

        try {
            mmCache.getClass().getMethod("put", UUID.class, UUID.class).invoke(mmCache, player.getUuid(), gameUuid);
        } catch (Throwable ignored) {
            return false;
        }

        try {
            var method = manager.getClass().getDeclaredMethod("assignPlayerToMatch", player.getClass(), UUID.class);
            method.setAccessible(true);
            Object result = method.invoke(manager, player, player.getUuid());
            return result instanceof Boolean ok && ok;
        } catch (NoSuchMethodException ignored) {
            try {
                for (var method : manager.getClass().getDeclaredMethods()) {
                    if (!method.getName().equals("assignPlayerToMatch")) {
                        continue;
                    }
                    Object[] args = buildAssignPlayerToMatchArgs(method.getParameterTypes(), player);
                    if (args == null) {
                        continue;
                    }
                    method.setAccessible(true);
                    Object result = method.invoke(manager, args);
                    return result instanceof Boolean ok && ok;
                }
            } catch (Throwable ignoredToo) {
            }
            return false;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static Object[] buildAssignPlayerToMatchArgs(Class<?>[] parameterTypes, ServerPlayerEntity player) {
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type.isInstance(player)) {
                args[i] = player;
                continue;
            }
            if (UUID.class == type) {
                args[i] = player.getUuid();
                continue;
            }
            return null;
        }
        return args;
    }

    private static UUID extractGameUuid(Object game) {
        if (game instanceof AbstractGame<?, ?, ?> bfGame) {
            return bfGame.getUUID();
        }
        Object uuid = invokeNoArg(game, "getUUID");
        return uuid instanceof UUID value ? value : null;
    }

    private static Object[] buildAssignPlayerArgs(
        Class<?>[] parameterTypes,
        Object level,
        ServerPlayerEntity player,
        Object game,
        Object teamJoinType,
        Object manager
    ) {
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (level != null && type.isInstance(level)) {
                args[i] = level;
                continue;
            }
            if (type.isInstance(player)) {
                args[i] = player;
                continue;
            }
            if (game != null && type.isInstance(game)) {
                args[i] = game;
                continue;
            }
            if (teamJoinType != null && type.isInstance(teamJoinType)) {
                args[i] = teamJoinType;
                continue;
            }
            if (manager != null && type.isInstance(manager)) {
                args[i] = manager;
                continue;
            }
            return null;
        }
        return args;
    }

    private static boolean tryPlayerJoinTeam(
        Object manager,
        Object level,
        ServerPlayerEntity player,
        Object game,
        Object teamJoinType
    ) {
        if (game == null) {
            return false;
        }
        for (var method : game.getClass().getMethods()) {
            if (!method.getName().equals("playerJoinTeam")) {
                continue;
            }
            try {
                Object[] args = buildAssignPlayerArgs(method.getParameterTypes(), level, player, game, teamJoinType, manager);
                if (args == null) {
                    continue;
                }
                Object result = method.invoke(game, args);
                return result instanceof Boolean ok && ok;
            } catch (Throwable ignored) {
            }
        }
        return false;
    }

    private static Object findRandomTeamJoinType() {
        try {
            Class<?> type = Class.forName("com.boehmod.blockfront.game.TeamJoinType");
            if (!type.isEnum()) {
                return null;
            }
            Object[] values = type.getEnumConstants();
            if (values == null) {
                return null;
            }
            for (Object value : values) {
                String name = String.valueOf(value).toUpperCase();
                if (name.contains("NEW") || name.contains("RANDOM") || name.contains("AUTO")) {
                    return value;
                }
            }
            return values.length > 0 ? values[0] : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Object getGamesMap() {
        Object manager = getBfManager();
        if (manager == null) {
            return null;
        }
        if (manager instanceof BFAbstractManager<?, ?, ?> bfManager) {
            return bfManager.getGames();
        }
        return invokeNoArg(manager, "getGames");
    }

    private static Object getBfManager() {
        BlockFront blockFront = BlockFront.getInstance();
        if (blockFront == null) {
            return null;
        }
        BFAbstractManager<?, ?, ?> manager = blockFront.getManager();
        if (manager instanceof BFServerManager serverManager) {
            return serverManager;
        }
        return manager;
    }

    private static String valueOrDash(String value) {
        return value == null || value.isBlank() || "null".equalsIgnoreCase(value) ? "-" : value;
    }

    private static String getGameStatusText(Object game) {
        if (game instanceof AbstractGame<?, ?, ?> bfGame) {
            return String.valueOf(bfGame.getStatus());
        }
        Object status = invokeNoArg(game, "getStatus");
        return status == null ? "" : String.valueOf(status);
    }

    private static String getGameName(Object game) {
        if (game instanceof AbstractGame<?, ?, ?> bfGame) {
            return bfGame.getName();
        }
        return asString(invokeNoArg(game, "getName"), null);
    }

    private static String getGameType(Object game) {
        if (game instanceof AbstractGame<?, ?, ?> bfGame) {
            return bfGame.getType();
        }
        return asString(invokeNoArg(game, "getType"), null);
    }

    private static Object extractConfiguredGame(Object asset) {
        if (asset instanceof GameAsset gameAsset) {
            return gameAsset.getGame();
        }
        return invokeNoArg(asset, "getGame");
    }

    private record RankedGame(Object game, int priority, String source) {
    }

    private static Object invokeNoArg(Object target, String methodName) {
        if (target == null) {
            return null;
        }
        try {
            return target.getClass().getMethod(methodName).invoke(target);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Object invokeStaticNoArg(String className, String methodName) {
        try {
            Class<?> type = Class.forName(className);
            return type.getMethod(methodName).invoke(null);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static String asString(Object value, String fallback) {
        return value instanceof String s ? s : fallback;
    }

    private static int showStatus(ServerCommandSource source) {
        MercFrontCoreConfig cfg = MercFrontCoreConfigManager.get();
        String message = AddonConstants.MOD_NAME + " remake baseline loaded. "
            + "proxyCompatibility=" + ProxyCompatibility.isCompatibilityEnabled()
            + ", enforceDirectConnection=" + cfg.proxy.enforceDirectConnection
            + ", trustForwardedIdentity=" + cfg.proxy.trustForwardedIdentity
            + ", winnerSkinDrops=" + cfg.rewards.enableWinnerSkinDrops
            + ", winnerSkinDropChance=" + cfg.rewards.winnerSkinDropChance;
        source.sendFeedback(() -> Text.literal(message), false);
        return 1;
    }

    private static int giveWithSkin(ServerCommandSource source, Identifier id, String skin) {
        GunSkinIndex.ensureInitialized();
        ServerPlayerEntity player;
        try {
            player = source.getPlayerOrThrow();
        } catch (Exception e) {
            source.sendError(Text.translatable("mercfrontcore.message.command.error.player"));
            return -1;
        }

        Item item = Registries.ITEM.get(id);
        if (item == null || item == Items.AIR) {
            source.sendError(Text.literal("Unknown item: " + id));
            return -1;
        }

        Optional<Float> skinId = GunSkinIndex.getSkinId(item, skin);
        if (skinId.isEmpty()) {
            source.sendError(Text.literal("Unknown skin '" + skin + "' for item " + id));
            return -1;
        }

        ItemStack stack = new ItemStack(item);
        stack.set(BFDataComponents.SKIN_ID, skinId.get());
        player.giveItemStack(stack);
        player.currentScreenHandler.sendContentUpdates();
        source.sendFeedback(() -> Text.literal("Gave " + id + " with skin '" + skin + "'"), true);
        return 1;
    }

    private static int listGunModifiers(ServerCommandSource source) {
        if (GunModifier.ACTIVE.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No gun modifiers loaded."), false);
            return 1;
        }

        source.sendFeedback(() -> Text.literal("Gun modifiers: " + GunModifier.ACTIVE.size()), false);
        for (var entry : GunModifier.ACTIVE.entrySet()) {
            source.sendFeedback(() -> Text.literal(entry.getKey().getIdAsString()), false);
        }
        return 1;
    }

    private static int setPermanentPlayerSkin(
        ServerCommandSource source,
        ServerPlayerEntity target,
        Identifier id,
        String skin
    ) {
        GrantSkinResult result = grantPermanentPlayerSkinInternal(source.getServer(), target, id, skin);
        if (!result.success()) {
            source.sendError(Text.literal(result.message()));
            return 0;
        }

        target.sendMessage(
            Text.literal("You received gun skin '" + skin + "' for " + id + ". Use /fc gun skins to view and select your skins."),
            false
        );
        source.sendFeedback(
            () -> Text.literal(
                "Granted gun skin '" + skin + "' to " + target.getNameForScoreboard() + " for " + id
                    + (result.appliedCount() > 0 ? " (applied to current inventory)." : ".")
            ),
            true
        );
        return 1;
    }

    private static GrantSkinResult grantPermanentPlayerSkinInternal(
        net.minecraft.server.MinecraftServer server,
        ServerPlayerEntity target,
        Identifier id,
        String skin
    ) {
        GunSkinIndex.ensureInitialized();

        Item item = Registries.ITEM.get(id);
        if (item == null || item == Items.AIR) {
            return GrantSkinResult.failure("Unknown gun item: " + id);
        }

        Optional<Float> skinId = GunSkinIndex.getSkinId(item, skin);
        if (skinId.isEmpty()) {
            return GrantSkinResult.failure("Unknown skin '" + skin + "' for item " + id);
        }

        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        store.grantPlayerSkin(target.getUuid(), id.toString(), skin);
        if (!store.save(server)) {
            return GrantSkinResult.failure("Saved permanent skin in memory, but failed to write player skin data.");
        }

        int applied = store.applyToPlayer(target);
        PacketDistributor.sendToPlayer(target, store.toPacket(target.getUuid()));
        return GrantSkinResult.success(applied);
    }

    private static int revokePermanentPlayerSkin(
        ServerCommandSource source,
        ServerPlayerEntity target,
        Identifier id,
        String skin
    ) {
        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        PlayerGunSkinStore.RevokeResult result = store.revokePlayerSkin(target.getUuid(), id.toString(), skin);
        if (result.removedCount() <= 0) {
            source.sendError(Text.literal(
                skin == null
                    ? "No permanent skins are stored for " + target.getNameForScoreboard() + " on " + id
                    : "Player does not own skin '" + skin + "' for " + id
            ));
            return 0;
        }
        if (!store.save(source.getServer())) {
            source.sendError(Text.literal("Updated player skin data in memory, but failed to write player skin data."));
            return 0;
        }

        int applied = store.reconcilePlayerGun(target, id.toString(), result.previousState(), result.currentState());
        PacketDistributor.sendToPlayer(target, store.toPacket(target.getUuid()));
        target.sendMessage(
            Text.literal(
                skin == null
                    ? "An admin removed your permanent gun skins for " + id + "."
                    : "An admin removed your gun skin '" + skin + "' for " + id + "."
            ),
            false
        );
        source.sendFeedback(
            () -> Text.literal(
                skin == null
                    ? "Removed " + result.removedCount() + " permanent skin(s) from " + target.getNameForScoreboard() + " for " + id
                        + (applied > 0 ? " (updated current inventory)." : ".")
                    : "Removed permanent skin '" + skin + "' from " + target.getNameForScoreboard() + " for " + id
                        + (applied > 0 ? " (updated current inventory)." : ".")
            ),
            true
        );
        return 1;
    }

    private static int revokeAllPermanentPlayerSkins(ServerCommandSource source, ServerPlayerEntity target) {
        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        Map<String, PlayerGunSkinStore.OwnedGunSkins> existing = new java.util.LinkedHashMap<>(store.getPlayerSkins(target.getUuid()));
        if (existing.isEmpty()) {
            source.sendError(Text.literal("No permanent skins are stored for " + target.getNameForScoreboard() + "."));
            return 0;
        }

        int removedCount = 0;
        int applied = 0;
        for (Map.Entry<String, PlayerGunSkinStore.OwnedGunSkins> entry : existing.entrySet()) {
            PlayerGunSkinStore.RevokeResult result = store.revokePlayerSkin(target.getUuid(), entry.getKey(), null);
            removedCount += result.removedCount();
        }
        if (!store.save(source.getServer())) {
            source.sendError(Text.literal("Updated player skin data in memory, but failed to write player skin data."));
            return 0;
        }
        for (Map.Entry<String, PlayerGunSkinStore.OwnedGunSkins> entry : existing.entrySet()) {
            applied += store.reconcilePlayerGun(target, entry.getKey(), entry.getValue(), null);
        }

        PacketDistributor.sendToPlayer(target, store.toPacket(target.getUuid()));
        target.sendMessage(Text.literal("An admin removed all of your permanent gun skins."), false);
        int finalRemovedCount = removedCount;
        int finalApplied = applied;
        source.sendFeedback(
            () -> Text.literal(
                "Removed " + finalRemovedCount + " permanent skin(s) across all guns from " + target.getNameForScoreboard()
                    + (finalApplied > 0 ? " (updated current inventory)." : ".")
            ),
            true
        );
        return 1;
    }

    private static int randomDrop(ServerCommandSource source, Collection<ServerPlayerEntity> players, int count, String rarityFilter) {
        GunSkinIndex.ensureInitialized();
        if (GunSkinIndex.SKINS.isEmpty()) {
            source.sendError(Text.literal("No gun skins are currently available."));
            return 0;
        }

        String normalizedRarity = rarityFilter == null ? null : SkinRewardSelector.normalizeRarity(rarityFilter);

        int given = 0;
        for (ServerPlayerEntity player : players) {
            for (int i = 0; i < count; i++) {
                if (giveRandomDrop(player, normalizedRarity)) {
                    given++;
                }
            }
            player.currentScreenHandler.sendContentUpdates();
        }

        int finalGiven = given;
        String suffix = normalizedRarity == null ? "" : " with rarity " + normalizedRarity.toLowerCase(java.util.Locale.ROOT);
        source.sendFeedback(
            () -> Text.literal(
                "Granted " + finalGiven + " random gun skin drop(s)" + suffix + " to " + players.size() + " player(s)."
            ),
            true
        );
        return given;
    }

    private static boolean giveRandomDrop(ServerPlayerEntity player, String rarityFilter) {
        SkinRewardSelector.SelectedSkinReward reward = SkinRewardSelector.pickRandomReward(player.getUuid(), rarityFilter);
        if (reward == null) {
            return false;
        }

        GrantSkinResult result = grantPermanentPlayerSkinInternal(player.getServer(), player, reward.gunId(), reward.skin());
        if (!result.success()) {
            return false;
        }
        player.sendMessage(
            Text.literal(
                "You received " + reward.rarity().toLowerCase(java.util.Locale.ROOT)
                    + " gun skin '" + reward.skin() + "' for " + reward.gunId() + "."
            ),
            false
        );
        return true;
    }

    private static int forceInfectedVendorRelocate(ServerCommandSource source, boolean usePlayerPose) {
        ServerPlayerEntity player;
        try {
            player = source.getPlayerOrThrow();
        } catch (Exception e) {
            source.sendError(Text.translatable("mercfrontcore.message.command.error.player"));
            return 0;
        }

        Object manager = getBfManager();
        if (manager == null) {
            source.sendError(Text.literal("BlockFront manager is unavailable."));
            return 0;
        }

        Object game = invokeGameWithPlayer(manager, player.getUuid());
        if (!(game instanceof InfectedGame infectedGame)) {
            source.sendError(Text.literal("You must be in an infected game to force vendor relocation."));
            return 0;
        }

        FDSPose pose;
        if (usePlayerPose) {
            pose = new FDSPose(player);
        } else {
            pose = pickVendorRelocatePose(infectedGame);
            if (pose == null) {
                source.sendError(Text.literal("This infected game has no configured vendor spawns."));
                return 0;
            }
        }

        infectedGame.relocateVendor(player.getServerWorld(), pose);
        source.sendFeedback(
            () -> Text.literal(
                usePlayerPose
                    ? "Forced infected vendor relocate to your current position."
                    : "Forced infected vendor relocate to a configured vendor spawn."
            ),
            true
        );
        return 1;
    }

    private static FDSPose pickVendorRelocatePose(InfectedGame infectedGame) {
        if (infectedGame.vendorSpawns.isEmpty()) {
            return null;
        }
        if (infectedGame.vendorSpawns.size() == 1 || infectedGame.vendorEntity == null) {
            return infectedGame.vendorSpawns.get(ThreadLocalRandom.current().nextInt(infectedGame.vendorSpawns.size()));
        }

        var currentPos = infectedGame.vendorEntity.getPos();
        List<FDSPose> alternatives = infectedGame.vendorSpawns.stream()
            .filter(pose -> pose != null && pose.position != null && pose.position.squaredDistanceTo(currentPos) > 0.25)
            .toList();
        List<FDSPose> pool = alternatives.isEmpty() ? infectedGame.vendorSpawns : alternatives;
        return pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
    }

    private static int reloadConfig(ServerCommandSource source) {
        boolean ok = MercFrontCoreConfigManager.load();
        if (ok) {
            source.sendFeedback(
                () -> Text.literal("mercfrontcore config reloaded from " + MercFrontCoreConfigManager.getConfigPath()),
                true
            );
            return 1;
        }
        source.sendError(Text.literal("mercfrontcore config reload failed; using defaults in memory."));
        return 0;
    }

    private static int saveConfig(ServerCommandSource source) {
        boolean ok = MercFrontCoreConfigManager.save();
        if (ok) {
            source.sendFeedback(
                () -> Text.literal("mercfrontcore config saved to " + MercFrontCoreConfigManager.getConfigPath()),
                true
            );
            return 1;
        }
        source.sendError(Text.literal("mercfrontcore config save failed."));
        return 0;
    }

    private static int setCompatibility(ServerCommandSource source, boolean value) {
        MercFrontCoreConfigManager.get().proxy.enableProxyCompatibility = value;
        return saveAfterSetting(source, "proxy compatibility", value);
    }

    private static int setDirectOnly(ServerCommandSource source, boolean value) {
        MercFrontCoreConfigManager.get().proxy.enforceDirectConnection = value;
        return saveAfterSetting(source, "direct only mode", value);
    }

    private static int setTrustForwardedIdentity(ServerCommandSource source, boolean value) {
        MercFrontCoreConfigManager.get().proxy.trustForwardedIdentity = value;
        return saveAfterSetting(source, "trust forwarded identity", value);
    }

    private static int saveAfterSetting(ServerCommandSource source, String key, boolean value) {
        boolean ok = MercFrontCoreConfigManager.save();
        if (ok) {
            source.sendFeedback(() -> Text.literal("Updated " + key + " = " + value), true);
            return 1;
        }
        source.sendError(Text.literal("Updated " + key + " in memory, but failed to save config."));
        return 0;
    }

    private static int setProfileOverride(
        ServerCommandSource source,
        Collection<ServerPlayerEntity> targets,
        String displayName,
        int level,
        int prestige
    ) {
        ProfileOverrideData next = new ProfileOverrideData(displayName, level, prestige);
        int updated = 0;
        for (ServerPlayerEntity target : targets) {
            UUID uuid = target.getUuid();
            ProfileOverrideData prev = AddonCommonData.getInstance().getProfileOverrides().put(uuid, next);
            if (prev == null) {
                syncNewProfileOverride(uuid, next);
            } else {
                syncChangedProperties(uuid, prev, next);
            }
            AddonCommonData.getInstance().refreshLiveProfile(target);
            updated++;
        }
        persistProfileOverrides(source, false);
        int finalUpdated = updated;
        source.sendFeedback(() -> Text.literal("Set profile override for " + finalUpdated + " player(s)."), true);
        return updated > 0 ? 1 : 0;
    }

    private static int clearProfileOverride(ServerCommandSource source, Collection<ServerPlayerEntity> targets) {
        int cleared = 0;
        for (ServerPlayerEntity target : targets) {
            UUID uuid = target.getUuid();
            ProfileOverrideData removed = AddonCommonData.getInstance().getProfileOverrides().remove(uuid);
            if (removed == null) {
                continue;
            }
            AddonCommonData.getInstance().refreshLiveProfile(target);
            syncClearProfileOverride(uuid);
            cleared++;
        }
        if (cleared == 0) {
            source.sendError(Text.literal("No profile overrides existed for the selected player(s)."));
            return 0;
        }
        persistProfileOverrides(source, false);
        int finalCleared = cleared;
        source.sendFeedback(() -> Text.literal("Cleared profile override for " + finalCleared + " player(s)."), true);
        return 1;
    }

    private static int showProfileOverrides(ServerCommandSource source) {
        Map<UUID, ProfileOverrideData> overrides = AddonCommonData.getInstance().getProfileOverrides();
        if (overrides.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No profile overrides set."), false);
            return 1;
        }

        source.sendFeedback(() -> Text.literal("Profile overrides: " + overrides.size()), false);
        overrides.forEach((uuid, value) -> source.sendFeedback(
            () -> Text.literal(
                uuid + " -> name=" + value.displayName() + ", level=" + value.level() + ", prestige=" + value.prestige()
            ),
            false
        ));
        return 1;
    }

    private static void syncProfileOverrides() {
        PacketDistributor.sendToAllPlayers(new SetProfileOverridesPacket(
            Map.copyOf(AddonCommonData.getInstance().getProfileOverrides())
        ));
    }

    private static void syncNewProfileOverride(UUID uuid, ProfileOverrideData data) {
        PacketDistributor.sendToAllPlayers(new NewProfileOverridesPacket(Map.of(uuid, data)));
    }

    private static void syncClearProfileOverride(UUID uuid) {
        PacketDistributor.sendToAllPlayers(new ClearProfileOverridesPacket(Set.of(uuid)));
    }

    private static void syncChangedProperties(UUID uuid, ProfileOverrideData prev, ProfileOverrideData next) {
        if (!prev.displayName().equals(next.displayName())) {
            PacketDistributor.sendToAllPlayers(new SetProfileOverridesPropertyPacket(
                Set.of(uuid),
                SetProfileOverridesPropertyPacket.PROPERTY_DISPLAY_NAME,
                next.displayName(),
                0
            ));
        }
        if (prev.level() != next.level()) {
            PacketDistributor.sendToAllPlayers(new SetProfileOverridesPropertyPacket(
                Set.of(uuid),
                SetProfileOverridesPropertyPacket.PROPERTY_LEVEL,
                "",
                next.level()
            ));
        }
        if (prev.prestige() != next.prestige()) {
            PacketDistributor.sendToAllPlayers(new SetProfileOverridesPropertyPacket(
                Set.of(uuid),
                SetProfileOverridesPropertyPacket.PROPERTY_PRESTIGE,
                "",
                next.prestige()
            ));
        }
    }

    private static int saveProfileOverrides(ServerCommandSource source) {
        return persistProfileOverrides(source, true);
    }

    private static int persistProfileOverrides(ServerCommandSource source, boolean verbose) {
        boolean ok = AddonCommonData.getInstance().saveProfileOverrides(source.getServer());
        if (ok) {
            if (verbose) {
                source.sendFeedback(() -> Text.literal("Profile overrides saved."), false);
            }
            return 1;
        }
        source.sendError(Text.literal("Failed to save profile overrides."));
        return 0;
    }

    private static int reloadProfileOverrides(ServerCommandSource source) {
        int loaded = AddonCommonData.getInstance().loadProfileOverrides(source.getServer());
        AddonCommonData.getInstance().applyProfileOverrides(source.getServer());
        syncProfileOverrides();
        source.sendFeedback(() -> Text.literal("Profile overrides reloaded (" + loaded + ")."), true);
        return 1;
    }

    private static int reloadLoadouts(ServerCommandSource source) {
        int loaded = LoadoutStore.getInstance().load(source.getServer());
        source.sendFeedback(() -> Text.literal("Loadouts reloaded (" + loaded + ")."), true);
        return 1;
    }

    private record GrantSkinResult(boolean success, String message, int appliedCount) {
        private static GrantSkinResult success(int appliedCount) {
            return new GrantSkinResult(true, "", appliedCount);
        }

        private static GrantSkinResult failure(String message) {
            return new GrantSkinResult(false, message, 0);
        }
    }
}
