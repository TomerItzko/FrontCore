package red.vuis.mercfrontcore.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGame;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;

import red.vuis.mercfrontcore.data.AddonCommonData;
import red.vuis.mercfrontcore.data.GunModifier;
import red.vuis.mercfrontcore.net.packet.ClearProfileOverridesPacket;
import red.vuis.mercfrontcore.net.packet.LoadoutsPacket;
import red.vuis.mercfrontcore.net.packet.NewProfileOverridesPacket;
import red.vuis.mercfrontcore.net.packet.SetProfileOverridesPropertyPacket;
import red.vuis.mercfrontcore.net.packet.ViewSpawnsPacket;
import red.vuis.mercfrontcore.setup.GunSkinIndex;
import red.vuis.mercfrontcore.setup.LoadoutIndex;
import red.vuis.mercfrontcore.util.AddonCommandUtils;
import red.vuis.mercfrontcore.util.AddonUtils;
import red.vuis.mercfrontcore.util.function.BiArgCommand;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class MercFrontCoreCommand {
	private static final Text C_INFECTED_VENDOR_RELOCATE = Text.translatable("bf.message.gamemode.infected.store.relocate").formatted(Formatting.YELLOW);
	
	private MercFrontCoreCommand() {
	}
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		var root = literal("mercfrontcore").requires(stack -> stack.hasPermissionLevel(3));
		
		root.then(
			literal("changeClass").then(
				argument("players", EntityArgumentType.players()).then(
					argument("class", StringArgumentType.word()).suggests(MercFrontCoreCommand::suggestMatchClass).then(
						argument("level", IntegerArgumentType.integer(1)).executes(MercFrontCoreCommand::changeClass)
					)
				)
			)
		).then(
			literal("gun").then(
				literal("giveWithSkin").then(
					argument("id", IdentifierArgumentType.identifier()).suggests(MercFrontCoreCommand::suggestGunGiveId).then(
						argument("skin", StringArgumentType.word()).suggests(MercFrontCoreCommand::suggestGunGiveSkin).executes(MercFrontCoreCommand::gunGiveWithSkin)
					)
				)
			).then(
				literal("modifier").then(
					literal("list").executes(MercFrontCoreCommand::gunModifierList)
				)
			)
		).then(
			literal("loadout").then(
				literal("give").then(
					argument("players", EntityArgumentType.players()).then(
						argument("nation", StringArgumentType.word()).suggests(MercFrontCoreCommand::suggestBfCountry).then(
							argument("skin", StringArgumentType.word()).suggests(AddonArguments.suggestSkins("nation")).then(
								argument("class", StringArgumentType.word()).suggests(MercFrontCoreCommand::suggestMatchClass).then(
									argument("level", IntegerArgumentType.integer(1)).executes(MercFrontCoreCommand::loadoutGive)
								)
							)
						)
					)
				)
			).then(
				literal("list").executes(MercFrontCoreCommand::loadoutList)
			).then(
				literal("read").then(
					argument("filename", StringArgumentType.word()).executes(MercFrontCoreCommand::loadoutRead)
				)
			).then(
				literal("write").then(
					argument("filename", StringArgumentType.word()).executes(MercFrontCoreCommand::loadoutWrite)
				)
			)
		).then(
			literal("match").then(
				literal("infected").then(
					literal("giveCash").then(
						argument("players", EntityArgumentType.players()).then(
							argument("amount", IntegerArgumentType.integer(1)).executes(MercFrontCoreCommand::matchInfectedGiveCash)
						)
					)
				).then(
					literal("vendorRelocate").executes(MercFrontCoreCommand::matchInfectedVendorRelocate)
				)
			).then(
				literal("timer").then(
					literal("add").then(
						argument("seconds", IntegerArgumentType.integer(0)).executes(MercFrontCoreCommand::matchTimerAdd)
					)
				).then(
					literal("set").then(
						argument("seconds", IntegerArgumentType.integer(0)).executes(MercFrontCoreCommand::matchTimerSet)
					)
				)
			)
		).then(
			literal("profile").then(
				literal("overrides").then(
					argument("targets", GameProfileArgumentType.gameProfile()).then(
						literal("clear").executes(MercFrontCoreCommand::profileOverridesClear)
					).then(
						literal("fetch").executes(MercFrontCoreCommand::profileOverridesFetch)
					).then(
						literal("new").executes(MercFrontCoreCommand::profileOverridesNew)
					).then(
						literal("set")
							.then(AddonArguments.setter(ArgumentTypePair.integer(0), "exp", profileOverrideSetter(PlayerCloudData::setExp)))
							.then(AddonArguments.setter(ArgumentTypePair.integer(0), "prestige", profileOverrideSetter(PlayerCloudData::setPrestigeLevel)))
					)
				)
			)
		).then(
			literal("randomDrop").then(
				argument("players", EntityArgumentType.players()).executes(context -> randomDrop(context, 1)).then(
					argument("count", IntegerArgumentType.integer(1, 100)).executes(context -> randomDrop(context, IntegerArgumentType.getInteger(context, "count")))
				)
			)
		).then(
			literal("spawnView").then(
				literal("enable").then(
					AddonArguments.asset("game", GameAsset.class).executes(MercFrontCoreCommand::spawnViewEnable)
				)
			)
		);
		
		dispatcher.register(root);
	}
	
	private static CompletableFuture<Suggestions> suggestGunGiveId(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
		return CommandSource.suggestMatching(GunSkinIndex.SKINS.keySet().stream().map(Identifier::toString), suggestions);
	}
	
	private static CompletableFuture<Suggestions> suggestGunGiveSkin(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
		Identifier id = IdentifierArgumentType.getIdentifier(context, "id");
		if (!GunSkinIndex.SKINS.containsKey(id)) {
			return Suggestions.empty();
		}
		return CommandSource.suggestMatching(GunSkinIndex.SKINS.get(id).keySet(), suggestions);
	}

	private static CompletableFuture<Suggestions> suggestBfCountry(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
		return CommandSource.suggestMatching(Stream.of(BFCountry.values()).map(v -> v.name().toLowerCase()), suggestions);
	}

	private static CompletableFuture<Suggestions> suggestMatchClass(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
		return CommandSource.suggestMatching(Stream.of(MatchClass.values()).map(v -> v.name().toLowerCase()), suggestions);
	}

	private static BFCountry parseBfCountry(ServerCommandSource source, String input) {
		for (BFCountry value : BFCountry.values()) {
			if (value.name().equalsIgnoreCase(input)) {
				return value;
			}
		}
		source.sendError(Text.literal("Invalid nation: " + input));
		return null;
	}

	private static MatchClass parseMatchClass(ServerCommandSource source, String input) {
		for (MatchClass value : MatchClass.values()) {
			if (value.name().equalsIgnoreCase(input)) {
				return value;
			}
		}
		source.sendError(Text.literal("Invalid class: " + input));
		return null;
	}
	
	private static int changeClass(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		
		Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
		MatchClass matchClass = parseMatchClass(source, StringArgumentType.getString(context, "class"));
		if (matchClass == null) {
			return -1;
		}
		int level = IntegerArgumentType.getInteger(context, "level") - 1;
		
		BFAbstractManager<?, ?, ?> manager = AddonUtils.getBfManager();
		Set<Integer> sentMissingErrors = new HashSet<>();
		
		AssetStore.getInstance().getRegistry(GameAsset.class).getEntries().values().stream().map(GameAsset::getGame).filter(Objects::nonNull).forEach(game -> {
			AbstractGamePlayerManager<?> playerManager = game.getPlayerManager();
			players.stream().filter(player -> playerManager.hasPlayer(player.getUuid())).forEach(player -> {
				GameTeam team = playerManager.getPlayerTeam(player.getUuid());
				if (team == null) {
					return;
				}
				
				DivisionData division = team.getDivisionData(game);
				if (division.getLoadout(matchClass, level) == null && sentMissingErrors.add(division.getId())) {
					source.sendError(Text.translatable(
						"mercfrontcore.message.command.changeClass.error.missing",
						AddonUtils.getMatchClassText(matchClass, level).formatted(Formatting.GOLD),
						AddonUtils.getDivisionText(division).formatted(Formatting.GOLD)
					));
					return;
				}
				
				playerManager.changePlayerClass(manager, player.getServerWorld(), player, player.getUuid(), matchClass, level);
			});
		});
		
		return 1;
	}
	
	private static int gunGiveWithSkin(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
		if (player == null) {
			return -1;
		}
		
		Identifier id = IdentifierArgumentType.getIdentifier(context, "id");
		String skin = StringArgumentType.getString(context, "skin");
		
		Item item = Registries.ITEM.get(id);
		if (item == Items.AIR) {
			return -1;
		}
		if (!GunSkinIndex.SKINS.get(id).containsKey(skin)) {
			return -1;
		}
		
		ItemStack itemStack = new ItemStack(item);
		itemStack.set(BFDataComponents.SKIN_ID, GunSkinIndex.SKINS.get(id).get(skin));
		
		player.giveItemStack(itemStack);
		player.currentScreenHandler.sendContentUpdates();
		return 1;
	}
	
	private static int gunModifierList(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		CommandOutput output = source.output;
		
		for (RegistryEntry<Item> itemEntry : GunModifier.ACTIVE.keySet()) {
			output.sendMessage(Text.literal(itemEntry.getIdAsString()));
		}
		
		return 1;
	}
	
	private static int loadoutGive(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		ServerWorld world = source.getWorld();
		
		Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
		BFCountry nation = parseBfCountry(source, StringArgumentType.getString(context, "nation"));
		if (nation == null) {
			return -1;
		}
		String skin = StringArgumentType.getString(context, "skin");
		MatchClass matchClass = parseMatchClass(source, StringArgumentType.getString(context, "class"));
		if (matchClass == null) {
			return -1;
		}
		int level = IntegerArgumentType.getInteger(context, "level");
		
		DivisionData division = DivisionData.getByCountryAndSkin(nation, skin);
		if (division == null) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.give.error.missing"));
			return -1;
		}
		Loadout loadout = division.getLoadout(matchClass, level - 1);
		if (loadout == null) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.give.error.missing"));
			return -1;
		}
		
		for (ServerPlayerEntity player : players) {
			BFUtils.giveLoadout(world, player, loadout, false);
		}
		
		if (players.size() == 1) {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.loadout.give.success.single", players.iterator().next().getDisplayName()), true);
		} else {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.loadout.give.success.multiple", players.size()), true);
		}
		return players.size();
	}
	
	private static int loadoutList(CommandContext<ServerCommandSource> context) {
		ServerCommandSource sourceStack = context.getSource();
		
		Path loadoutsPath = AddonUtils.getServerDataPath(sourceStack.getServer()).resolve("loadouts");
		if (!Files.isDirectory(loadoutsPath)) {
			sourceStack.sendError(Text.translatable("mercfrontcore.message.command.loadout.list.none"));
			return 0;
		}
		
		String filenameList;
		try (Stream<Path> filesList = Files.list(loadoutsPath)) {
			filenameList = AddonUtils.listify(
				filesList
					.map(path -> {
						String pathStr = path.getFileName().toString();
						return pathStr.substring(0, pathStr.lastIndexOf('.'));
					})
					.iterator()
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		sourceStack.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.loadout.list.success", filenameList), false);
		return 1;
	}
	
	private static int loadoutRead(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		MinecraftServer server = source.getServer();
		
		if (!server.isDedicated()) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.read.error.server"));
			return -1;
		}
		
		if (AddonUtils.anyGamesActive()) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.read.game"));
			return -1;
		}
		
		String filename = StringArgumentType.getString(context, "filename") + ".dat";
		Path indexPath = AddonUtils.getServerDataPath(server).resolve("loadouts").resolve(filename);
		
		if (!LoadoutIndex.parseAndApply(indexPath)) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.read.error"));
			return -1;
		}
		
		PacketDistributor.sendToAllPlayers(new LoadoutsPacket(LoadoutIndex.currentFlat()));
		
		source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.loadout.read.success", filename), true);
		return 1;
	}
	
	private static int loadoutWrite(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		MinecraftServer server = source.getServer();
		
		if (!server.isDedicated()) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.write.error.server"));
			return -1;
		}
		
		String filename = StringArgumentType.getString(context, "filename") + ".dat";
		
		Path loadoutsPath = AddonUtils.getServerDataPath(server).resolve("loadouts");
		if (!Files.isDirectory(loadoutsPath)) {
			try {
				Files.createDirectory(loadoutsPath);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		Path indexPath = loadoutsPath.resolve(filename);
		
		if (!LoadoutIndex.saveCurrent(indexPath)) {
			source.sendError(Text.translatable("mercfrontcore.message.command.loadout.write.error"));
			return -1;
		}
		
		return 1;
	}
	
	private static int matchInfectedGiveCash(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
		int amount = IntegerArgumentType.getInteger(context, "amount");
		
		BFAbstractManager<?, ?, ?> manager = AddonUtils.getBfManager();
		
		int affected = 0;
		for (ServerPlayerEntity player : players) {
			UUID uuid = player.getUuid();
			
			if (!(manager.getGameWithPlayer(uuid) instanceof InfectedGame game)) {
				continue;
			}
			
			BFUtils.triggerPlayerStat(manager, game, uuid, BFStats.POINTS, amount);
			BFUtils.sendFancyMessage(
				uuid, BFUtils.ADMIN_PREFIX,
				Text.translatable("mercfrontcore.message.command.match.infected.giveCash.success", player.getNameForScoreboard(), amount)
			);
			affected++;
		}
		
		return affected;
	}
	
	private static int matchInfectedVendorRelocate(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
		if (player == null) {
			return -1;
		}
		
		if (!(AddonUtils.getBfManager().getGameWithPlayer(player) instanceof InfectedGame game)) {
			source.sendError(Text.translatable("mercfrontcore.message.command.match.infected.error.none"));
			return -1;
		}
		
		Set<UUID> players = game.getPlayerManager().getPlayerUUIDs();
		BFUtils.playSound(players, BFSounds.ENTITY_GUNDEALER_RELOCATE.get(), SoundCategory.MASTER);
		BFUtils.sendNoticeMessage(players, C_INFECTED_VENDOR_RELOCATE);
		game.relocateVendor(source.getWorld(), RandomUtils.randomFromList(game.vendorSpawns));
		
		return 1;
	}
	
	private static int matchTimerAdd(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
		if (player == null) {
			return -1;
		}
		
		int seconds = IntegerArgumentType.getInteger(context, "seconds");
		
		return handleMatchTimerOperation(
			context, source, player, timer -> timer.setSecondsRemaining(timer.getSecondsRemaining() + seconds),
			Text.translatable("mercfrontcore.message.command.match.timer.add.success", player.getNameForScoreboard(), seconds)
		);
	}
	
	private static int matchTimerSet(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
		if (player == null) {
			return -1;
		}
		
		int seconds = IntegerArgumentType.getInteger(context, "seconds");
		
		return handleMatchTimerOperation(
			context, source, player, timer -> timer.setSecondsRemaining(seconds),
			Text.translatable("mercfrontcore.message.command.match.timer.set.success", player.getNameForScoreboard(), seconds)
		);
	}
	
	private static int handleMatchTimerOperation(CommandContext<ServerCommandSource> context, ServerCommandSource source, ServerPlayerEntity player, Consumer<GameStageTimer> timerOperation, Text message) {
		AbstractGame<?, ?, ?> game = AddonUtils.getBfManager().getGameWithPlayer(player);
		if (game == null) {
			source.sendError(Text.translatable("mercfrontcore.message.command.match.error.none"));
			return -1;
		}
		
		if (!(game.getStageManager().getCurrentStage() instanceof TimedStage<?, ?> stage)) {
			source.sendError(Text.translatable("mercfrontcore.message.command.match.timer.error.invalid"));
			return -1;
		}
		
		@SuppressWarnings("DataFlowIssue") // unused parameter
		GameStageTimer timer = stage.getStageTimer(null);
		if (timer == null) {
			return 0;
		}
		timerOperation.accept(timer);
		BFUtils.sendFancyMessage(
			game.getPlayerManager().getPlayerUUIDs(), BFUtils.ADMIN_PREFIX,
			message
		);
		
		return 1;
	}
	
	private static int profileOverridesClear(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		
		Collection<GameProfile> targets = GameProfileArgumentType.getProfileArgument(context, "targets");
		if (targets.isEmpty()) {
			return 0;
		}
		
		Set<UUID> uuids = targets.stream().map(GameProfile::getId).collect(Collectors.toUnmodifiableSet());
		Map<UUID, PlayerCloudData> profileOverrides = AddonCommonData.getInstance().profileOverrides;
		
		Set<UUID> cleared = uuids.stream().filter(uuid -> profileOverrides.remove(uuid) != null).collect(Collectors.toUnmodifiableSet());
		
		if (FMLEnvironment.dist.isDedicatedServer()) {
			PacketDistributor.sendToAllPlayers(new ClearProfileOverridesPacket(cleared));
		}
		
		if (cleared.size() == 1) {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.profile.overrides.clear.success.single", targets.iterator().next().getName()), true);
		} else {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.profile.overrides.clear.success.multiple", cleared.size()), true);
		}
		return targets.size();
	}
	
	private static int profileOverridesFetch(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		
		Collection<GameProfile> targets = GameProfileArgumentType.getProfileArgument(context, "targets");
		if (targets.isEmpty()) {
			return 0;
		}
		
		Set<Pair<UUID, String>> idPairs = targets.stream().map(AddonUtils::createIdPair).collect(Collectors.toUnmodifiableSet());
		AddonCommonData.getInstance().fetchCloudProfiles(idPairs);
		
		if (targets.size() == 1) {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.profile.overrides.fetch.success.single", targets.iterator().next().getName()), true);
		} else {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.profile.overrides.fetch.success.multiple", targets.size()), true);
		}
		return targets.size();
	}
	
	private static int profileOverridesNew(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		
		Collection<GameProfile> targets = GameProfileArgumentType.getProfileArgument(context, "targets");
		if (targets.isEmpty()) {
			return 0;
		}
		Set<Pair<UUID, String>> idPairs = targets.stream().map(AddonUtils::createIdPair).collect(Collectors.toUnmodifiableSet());
		
		AddonCommonData.getInstance().putNewProfileOverrides(idPairs);
		if (FMLEnvironment.dist.isDedicatedServer()) {
			PacketDistributor.sendToAllPlayers(new NewProfileOverridesPacket(idPairs));
		}
		
		if (idPairs.size() == 1) {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.profile.overrides.new.success.single", idPairs.iterator().next().right()), true);
		} else {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.profile.overrides.new.success.multiple", idPairs.size()), true);
		}
		return idPairs.size();
	}
	
	private static <T> BiArgCommand<ServerCommandSource, String, T> profileOverrideSetter(BiConsumer<PlayerCloudData, T> setter) {
		return (context, name, value) -> profileOverridesSet(context, name, value, setter);
	}
	
	private static <T> int profileOverridesSet(CommandContext<ServerCommandSource> context, String propertyName, T value, BiConsumer<PlayerCloudData, T> setter) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		
		Collection<GameProfile> targets = GameProfileArgumentType.getProfileArgument(context, "targets");
		if (targets.isEmpty()) {
			return 0;
		}
		Set<Pair<UUID, String>> idPairs = targets.stream().map(AddonUtils::createIdPair).collect(Collectors.toUnmodifiableSet());
		
		Map<UUID, PlayerCloudData> profileOverrides = AddonCommonData.getInstance().profileOverrides;
		int affected = 0;
		for (GameProfile target : targets) {
			UUID uuid = target.getId();
			
			if (!profileOverrides.containsKey(uuid)) {
				source.sendError(Text.translatable("mercfrontcore.message.command.profile.overrides.error.missing", target.getName()));
				continue;
			}
			
			PlayerCloudData cloudData = profileOverrides.get(uuid);
			setter.accept(cloudData, value);
			affected++;
		}
		
		if (FMLEnvironment.dist.isDedicatedServer() && affected > 0) {
			PacketDistributor.sendToAllPlayers(new SetProfileOverridesPropertyPacket(
				idPairs,
				IntObjectPair.of(switch (propertyName) {
					case "exp" -> 1;
					case "prestige" -> 2;
					default -> throw new AssertionError();
				}, value)
			));
		}
		
		if (affected == 1) {
			source.sendFeedback(() -> Text.stringifiedTranslatable("mercfrontcore.message.command.profile.overrides.set.success.single", propertyName, value, targets.iterator().next().getName()), true);
		} else if (affected > 1) {
			int finalAffected = affected;
			source.sendFeedback(() -> Text.stringifiedTranslatable("mercfrontcore.message.command.profile.overrides.set.success.multiple", propertyName, value, finalAffected), true);
		}
		return affected;
	}
	
	private static int randomDrop(CommandContext<ServerCommandSource> context, int count) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		
		Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
		
		BFAbstractManager<?, ?, ?> manager = AddonUtils.getBfManager();
		CloudRegistry cloudRegistry = manager.getCloudRegistry();
		
		for (ServerPlayerEntity player : players) {
			for (int i = 0; i < count; i++) {
				handleRandomDrop(player, cloudRegistry);
			}
			player.currentScreenHandler.sendContentUpdates();
		}
		
		if (players.size() == 1) {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.randomDrop.single.success", count, players.iterator().next().getDisplayName()), true);
		} else {
			source.sendFeedback(() -> Text.translatable("mercfrontcore.message.command.randomDrop.multiple.success", count, players.size()), true);
		}
		return players.size();
	}
	
	private static void handleRandomDrop(ServerPlayerEntity player, CloudRegistry cloudRegistry) {
		CloudItemStack dropStack = CloudItem.getRandomDrop(cloudRegistry);
		CloudItem<?> dropItem = dropStack.getCloudItem(cloudRegistry);
		assert dropItem != null;
		
		Item item = Registries.ITEM.get(BFRes.fromCloud(dropItem.getMinecraftItem()));
		if (item == Items.AIR) {
			throw new RuntimeException("Cloud item id %d does not have a valid mc item location!".formatted(dropStack.getItemId()));
		}
		ItemStack itemStack = new ItemStack(item);
		
		player.giveItemStack(itemStack);
	}
	
	private static int spawnViewEnable(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = AddonCommandUtils.getContextPlayer(context);
		if (player == null) {
			return -1;
		}
		
		GameAsset gameAsset = AddonArguments.getAsset(context, "game", GameAsset.class);
		
		switch (gameAsset.getGame()) {
			case FreeForAllGame game -> {
				List<FDSPose> spawns = game.getPlayerManager().method_3566();
				PacketDistributor.sendToPlayer(player, new ViewSpawnsPacket(game.getName(), spawns));
			}
			case null, default -> {
				source.sendError(Text.translatable("mercfrontcore.message.command.viewSpawns.enable.error.type"));
				return -1;
			}
		}
		
		return 1;
	}
}
