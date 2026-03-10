package dev.tomerdev.mercfrontcore.server.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
import com.boehmod.blockfront.util.math.FDSPose;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import dev.tomerdev.mercfrontcore.data.GunModifier;
import dev.tomerdev.mercfrontcore.data.LoadoutData;
import dev.tomerdev.mercfrontcore.data.LoadoutStore;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import dev.tomerdev.mercfrontcore.data.ProfileOverrideData;
import dev.tomerdev.mercfrontcore.net.packet.ClearProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.NewProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPropertyPacket;
import dev.tomerdev.mercfrontcore.net.packet.ViewSpawnsPacket;
import dev.tomerdev.mercfrontcore.server.ProxyCompatibility;
import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;

public final class MercFrontCoreCommand {
    private MercFrontCoreCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(rootCommand("frontcore"));
        dispatcher.register(rootCommand("fc"));
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<ServerCommandSource> rootCommand(String name) {
        return literal(name)
            .requires(source -> source.hasPermissionLevel(2))
            .then(literal("status").executes(ctx -> showStatus(ctx.getSource())))
            .then(
                literal("profile")
                    .then(literal("show").executes(ctx -> showProfileOverrides(ctx.getSource())))
                    .then(literal("save").executes(ctx -> saveProfileOverrides(ctx.getSource())))
                    .then(literal("reload").executes(ctx -> reloadProfileOverrides(ctx.getSource())))
                    .then(
                        literal("clear")
                            .then(argument("target", EntityArgumentType.player()).executes(ctx ->
                                clearProfileOverride(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "target"))
                            ))
                    )
                    .then(
                        literal("set")
                            .then(argument("target", EntityArgumentType.player())
                                .then(argument("displayName", StringArgumentType.word())
                                    .then(argument("level", IntegerArgumentType.integer(0))
                                        .then(argument("prestige", IntegerArgumentType.integer(0)).executes(ctx ->
                                            setProfileOverride(
                                                ctx.getSource(),
                                                EntityArgumentType.getPlayer(ctx, "target"),
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
                        literal("giveWithSkin")
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
                        literal("skinPlayer")
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
                        literal("removeSkinPlayer")
                            .then(argument("target", EntityArgumentType.player())
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
                        literal("modifier")
                            .then(literal("list").executes(ctx -> listGunModifiers(ctx.getSource())))
                    )
            )
            .then(
                literal("loadout")
                    .then(literal("list").executes(ctx -> showLoadouts(ctx.getSource())))
                    .then(literal("save").executes(ctx -> saveLoadouts(ctx.getSource())))
                    .then(literal("reload").executes(ctx -> reloadLoadouts(ctx.getSource())))
                    .then(
                        literal("remove")
                            .then(argument("name", StringArgumentType.word())
                                .suggests(MercFrontCoreCommand::suggestLoadoutNames)
                                .executes(ctx ->
                                    removeLoadout(ctx.getSource(), StringArgumentType.getString(ctx, "name"))
                                ))
                    )
                    .then(
                        literal("set")
                            .then(argument("name", StringArgumentType.word())
                                .suggests(MercFrontCoreCommand::suggestLoadoutNames)
                                .then(argument("primary", StringArgumentType.word())
                                    .suggests(MercFrontCoreCommand::suggestItemIds)
                                    .then(argument("secondary", StringArgumentType.word())
                                        .suggests(MercFrontCoreCommand::suggestItemIds)
                                        .executes(ctx ->
                                            setLoadout(
                                                ctx.getSource(),
                                                StringArgumentType.getString(ctx, "name"),
                                                StringArgumentType.getString(ctx, "primary"),
                                                StringArgumentType.getString(ctx, "secondary")
                                            )
                                        ))
                                ))
                    )
                    .then(
                        literal("give")
                            .then(argument("target", EntityArgumentType.player())
                                .then(argument("name", StringArgumentType.word())
                                    .suggests(MercFrontCoreCommand::suggestLoadoutNames)
                                    .executes(ctx ->
                                        giveLoadout(
                                            ctx.getSource(),
                                            EntityArgumentType.getPlayer(ctx, "target"),
                                            StringArgumentType.getString(ctx, "name")
                                        )
                                    ))
                            )
                    )
            )
            .then(
                literal("admin")
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
                                randomDrop(ctx.getSource(), EntityArgumentType.getPlayers(ctx, "players"), 1)
                            ))
                            .then(argument("players", EntityArgumentType.players())
                                .then(argument("count", IntegerArgumentType.integer(1, 100)).executes(ctx ->
                                    randomDrop(
                                        ctx.getSource(),
                                        EntityArgumentType.getPlayers(ctx, "players"),
                                        IntegerArgumentType.getInteger(ctx, "count")
                                    )
                                ))
                            )
                    )
                    .then(
                        literal("spawnView")
                            .then(
                                literal("enable")
                                    .then(argument("game", StringArgumentType.word())
                                        .suggests(MercFrontCoreCommand::suggestGames)
                                        .executes(ctx -> enableSpawnView(
                                            ctx.getSource(),
                                            StringArgumentType.getString(ctx, "game")
                                        ))
                                    )
                            )
                    )
            );
    }

    private static CompletableFuture<Suggestions> suggestGames(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        return CommandSource.suggestMatching(getGameNames(), suggestions);
    }

    private static CompletableFuture<Suggestions> suggestGunIds(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        GunSkinIndex.ensureInitialized();
        return CommandSource.suggestMatching(
            GunSkinIndex.SKINS.keySet().stream().map(Identifier::toString),
            suggestions
        );
    }

    private static CompletableFuture<Suggestions> suggestItemIds(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        return CommandSource.suggestMatching(
            Registries.ITEM.getIds().stream().map(Identifier::toString),
            suggestions
        );
    }

    private static CompletableFuture<Suggestions> suggestLoadoutNames(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        return CommandSource.suggestMatching(
            LoadoutStore.getInstance().getLoadouts().keySet(),
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
        var declaredFallbackSkins = GunExtraOptionsIndex.getDeclaredFallbackSkins(id);
        if (!declaredFallbackSkins.isEmpty()) {
            return CommandSource.suggestMatching(declaredFallbackSkins, suggestions);
        }
        Item item = Registries.ITEM.get(id);
        if (item != null && item != Items.AIR) {
            var strictSkins = GunSkinIndex.getStrictSkinNames(item);
            if (!strictSkins.isEmpty()) {
                return CommandSource.suggestMatching(strictSkins, suggestions);
            }
        }
        var gunOptions = GunExtraOptionsIndex.snapshot().get(id);
        if (gunOptions != null && !gunOptions.skins().isEmpty()) {
            return CommandSource.suggestMatching(gunOptions.skins(), suggestions);
        }
        return suggestions.buildFuture();
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

    private static int enableSpawnView(ServerCommandSource source, String gameName) {
        ServerPlayerEntity player;
        try {
            player = source.getPlayerOrThrow();
        } catch (Exception e) {
            source.sendError(Text.translatable("mercfrontcore.message.command.error.player"));
            return -1;
        }

        Object gameAsset = findGameAssetByName(gameName);
        if (gameAsset == null) {
            source.sendError(Text.translatable("mercfrontcore.message.command.viewSpawns.enable.error.type"));
            return -1;
        }
        Object game = invokeNoArg(gameAsset, "getGame");
        if (game == null) {
            source.sendError(Text.translatable("mercfrontcore.message.command.viewSpawns.enable.error.type"));
            return -1;
        }
        if (!game.getClass().getName().contains(".ffa.")) {
            source.sendError(Text.translatable("mercfrontcore.message.command.viewSpawns.enable.error.type"));
            return -1;
        }

        Object playerManager = invokeNoArg(game, "getPlayerManager");
        if (playerManager == null) {
            source.sendError(Text.translatable("mercfrontcore.message.command.viewSpawns.enable.error.type"));
            return -1;
        }

        var spawns = findSpawnList(playerManager);
        if (spawns == null) {
            source.sendError(Text.translatable("mercfrontcore.message.command.viewSpawns.enable.error.type"));
            return -1;
        }

        String resolvedGameName = asString(invokeNoArg(game, "getName"), gameName);
        PacketDistributor.sendToPlayer(player, new ViewSpawnsPacket(resolvedGameName, spawns));
        return 1;
    }

    @SuppressWarnings("unchecked")
    private static java.util.List<FDSPose> findSpawnList(Object playerManager) {
        for (String methodName : new String[]{"getSpawns", "method_3566", "getPlayerSpawns"}) {
            Object value = invokeNoArg(playerManager, methodName);
            if (value instanceof java.util.List<?> list && (list.isEmpty() || list.getFirst() instanceof FDSPose)) {
                return (java.util.List<FDSPose>) list;
            }
        }
        for (var method : playerManager.getClass().getMethods()) {
            if (method.getParameterCount() != 0 || !java.util.List.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            try {
                Object value = method.invoke(playerManager);
                if (value instanceof java.util.List<?> list && (list.isEmpty() || list.getFirst() instanceof FDSPose)) {
                    return (java.util.List<FDSPose>) list;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private static java.util.List<String> getGameNames() {
        Object gamesObj = getGamesMap();
        if (!(gamesObj instanceof Map<?, ?> games)) {
            return java.util.List.of();
        }
        java.util.List<String> names = new java.util.ArrayList<>(games.size());
        for (Object gameAsset : games.values()) {
            String name = asString(invokeNoArg(gameAsset, "getName"), null);
            if (name != null && !name.isBlank()) {
                names.add(name);
            }
        }
        return names;
    }

    private static Object findGameAssetByName(String gameName) {
        Object gamesObj = getGamesMap();
        if (!(gamesObj instanceof Map<?, ?> games)) {
            return null;
        }
        for (Object gameAsset : games.values()) {
            String name = asString(invokeNoArg(gameAsset, "getName"), null);
            if (gameName.equalsIgnoreCase(name)) {
                return gameAsset;
            }
        }
        return null;
    }

    private static Object getGamesMap() {
        Object blockFront = invokeStaticNoArg("com.boehmod.blockfront.BlockFront", "getInstance");
        if (blockFront == null) {
            return null;
        }
        Object manager = invokeNoArg(blockFront, "getManager");
        if (manager == null) {
            return null;
        }
        return invokeNoArg(manager, "getGames");
    }

    private static Object getBfManager() {
        Object blockFront = invokeStaticNoArg("com.boehmod.blockfront.BlockFront", "getInstance");
        if (blockFront == null) {
            return null;
        }
        return invokeNoArg(blockFront, "getManager");
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
            + ", trustForwardedIdentity=" + cfg.proxy.trustForwardedIdentity;
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
        GunSkinIndex.ensureInitialized();

        Item item = Registries.ITEM.get(id);
        if (item == null || item == Items.AIR) {
            source.sendError(Text.literal("Unknown gun item: " + id));
            return 0;
        }

        Optional<Float> skinId = GunSkinIndex.getSkinId(item, skin);
        if (skinId.isEmpty()) {
            source.sendError(Text.literal("Unknown skin '" + skin + "' for item " + id));
            return 0;
        }

        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        store.grantPlayerSkin(target.getUuid(), id.toString(), skin);
        if (!store.save(source.getServer())) {
            source.sendError(Text.literal("Saved permanent skin in memory, but failed to write player skin data."));
            return 0;
        }

        int applied = store.applyToPlayer(target);
        PacketDistributor.sendToPlayer(target, store.toPacket(target.getUuid()));
        target.sendMessage(
            Text.literal("You received gun skin '" + skin + "' for " + id + ". Use /fc gun skins to view and select your skins."),
            false
        );
        source.sendFeedback(
            () -> Text.literal(
                "Granted gun skin '" + skin + "' to " + target.getNameForScoreboard() + " for " + id
                    + (applied > 0 ? " (applied to current inventory)." : ".")
            ),
            true
        );
        return 1;
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

    private static int randomDrop(ServerCommandSource source, Collection<ServerPlayerEntity> players, int count) {
        Object manager = getBfManager();
        if (manager == null) {
            source.sendError(Text.literal("BlockFront manager is unavailable."));
            return 0;
        }

        Object registryObj = invokeNoArg(manager, "getCloudRegistry");
        if (!(registryObj instanceof CloudRegistry cloudRegistry)) {
            source.sendError(Text.literal("Cloud registry is unavailable."));
            return 0;
        }

        int given = 0;
        for (ServerPlayerEntity player : players) {
            for (int i = 0; i < count; i++) {
                if (giveRandomDrop(player, cloudRegistry)) {
                    given++;
                }
            }
            player.currentScreenHandler.sendContentUpdates();
        }

        int finalGiven = given;
        source.sendFeedback(() -> Text.literal(
            "Gave " + finalGiven + " random drops to " + players.size() + " player(s)."
        ), true);
        return given;
    }

    private static boolean giveRandomDrop(ServerPlayerEntity player, CloudRegistry cloudRegistry) {
        try {
            CloudItemStack dropStack = CloudItem.getRandomDrop(cloudRegistry);
            CloudItem<?> dropItem = dropStack.getCloudItem(cloudRegistry);
            if (dropItem == null) {
                return false;
            }
            Identifier itemId = BFRes.fromCloud(dropItem.getMinecraftItem());
            Item item = Registries.ITEM.get(itemId);
            if (item == null || item == Items.AIR) {
                return false;
            }
            player.giveItemStack(new ItemStack(item));
            return true;
        } catch (Throwable ignored) {
            return false;
        }
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
        ServerPlayerEntity target,
        String displayName,
        int level,
        int prestige
    ) {
        UUID uuid = target.getUuid();
        ProfileOverrideData next = new ProfileOverrideData(displayName, level, prestige);
        ProfileOverrideData prev = AddonCommonData.getInstance().getProfileOverrides().put(uuid, next);
        if (prev == null) {
            syncNewProfileOverride(uuid, next);
        } else {
            syncChangedProperties(uuid, prev, next);
        }
        AddonCommonData.getInstance().refreshLiveProfile(target);
        persistProfileOverrides(source, false);
        source.sendFeedback(() -> Text.literal("Set profile override for " + target.getNameForScoreboard()), true);
        return 1;
    }

    private static int clearProfileOverride(ServerCommandSource source, ServerPlayerEntity target) {
        UUID uuid = target.getUuid();
        ProfileOverrideData removed = AddonCommonData.getInstance().getProfileOverrides().remove(uuid);
        if (removed == null) {
            source.sendError(Text.literal("No profile override exists for " + target.getNameForScoreboard()));
            return 0;
        }
        AddonCommonData.getInstance().refreshLiveProfile(target);
        syncClearProfileOverride(uuid);
        persistProfileOverrides(source, false);
        source.sendFeedback(() -> Text.literal("Cleared profile override for " + target.getNameForScoreboard()), true);
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

    private static int showLoadouts(ServerCommandSource source) {
        Map<String, LoadoutData> loadouts = LoadoutStore.getInstance().getLoadouts();
        if (loadouts.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No loadouts are defined."), false);
            return 1;
        }

        source.sendFeedback(() -> Text.literal("Loadouts: " + loadouts.size()), false);
        loadouts.forEach((name, data) -> source.sendFeedback(
            () -> Text.literal(name + " -> primary=" + data.primaryItem() + ", secondary=" + data.secondaryItem()),
            false
        ));
        return 1;
    }

    private static int setLoadout(ServerCommandSource source, String name, String primary, String secondary) {
        String key = name.toLowerCase();
        LoadoutStore.getInstance().getLoadouts().put(key, new LoadoutData(primary, secondary));
        boolean saved = LoadoutStore.getInstance().save(source.getServer());
        if (!saved) {
            source.sendError(Text.literal("Loadout set in memory but failed to save."));
            return 0;
        }
        source.sendFeedback(() -> Text.literal("Saved loadout '" + key + "'"), true);
        return 1;
    }

    private static int removeLoadout(ServerCommandSource source, String name) {
        String key = name.toLowerCase();
        LoadoutData removed = LoadoutStore.getInstance().getLoadouts().remove(key);
        if (removed == null) {
            source.sendError(Text.literal("Loadout not found: " + key));
            return 0;
        }
        boolean saved = LoadoutStore.getInstance().save(source.getServer());
        if (!saved) {
            source.sendError(Text.literal("Loadout removed in memory but failed to save."));
            return 0;
        }
        source.sendFeedback(() -> Text.literal("Removed loadout '" + key + "'"), true);
        return 1;
    }

    private static int saveLoadouts(ServerCommandSource source) {
        boolean ok = LoadoutStore.getInstance().save(source.getServer());
        if (ok) {
            source.sendFeedback(() -> Text.literal("Loadouts saved."), false);
            return 1;
        }
        source.sendError(Text.literal("Failed to save loadouts."));
        return 0;
    }

    private static int reloadLoadouts(ServerCommandSource source) {
        int loaded = LoadoutStore.getInstance().load(source.getServer());
        source.sendFeedback(() -> Text.literal("Loadouts reloaded (" + loaded + ")."), true);
        return 1;
    }

    private static int giveLoadout(ServerCommandSource source, ServerPlayerEntity target, String name) {
        String key = name.toLowerCase();
        LoadoutData loadout = LoadoutStore.getInstance().getLoadouts().get(key);
        if (loadout == null) {
            source.sendError(Text.literal("Loadout not found: " + key));
            return 0;
        }

        Item primary = resolveItem(loadout.primaryItem());
        Item secondary = resolveItem(loadout.secondaryItem());
        if (primary == Items.AIR || secondary == Items.AIR) {
            source.sendError(Text.literal("Loadout contains invalid item id(s): " + loadout.primaryItem() + ", " + loadout.secondaryItem()));
            return 0;
        }

        target.giveItemStack(new ItemStack(primary));
        target.giveItemStack(new ItemStack(secondary));
        source.sendFeedback(() -> Text.literal("Gave loadout '" + key + "' to " + target.getNameForScoreboard()), true);
        return 1;
    }

    private static Item resolveItem(String itemId) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null) {
            return Items.AIR;
        }
        Item item = Registries.ITEM.get(id);
        return item == null ? Items.AIR : item;
    }
}
