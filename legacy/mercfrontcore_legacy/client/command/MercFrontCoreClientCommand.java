package red.vuis.mercfrontcore.client.command;

import java.util.concurrent.CompletableFuture;

import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.item.GunItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;

import red.vuis.mercfrontcore.client.data.AddonClientData;
import red.vuis.mercfrontcore.client.screen.GunModifierEditorScreen;
import red.vuis.mercfrontcore.client.screen.LoadoutEditorScreen;
import red.vuis.mercfrontcore.client.screen.WeaponExtraScreen;
import red.vuis.mercfrontcore.command.AddonArguments;
import red.vuis.mercfrontcore.net.packet.GunModifiersPacket;
import red.vuis.mercfrontcore.net.packet.LoadoutsPacket;
import red.vuis.mercfrontcore.setup.GunItemIndex;
import red.vuis.mercfrontcore.setup.LoadoutIndex;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class MercFrontCoreClientCommand {
	private MercFrontCoreClientCommand() {
	}
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		var root = literal("mercfrontcore").requires(source -> source.hasPermissionLevel(2));
		
		//			.then(
//			literal("editorMode").then(
//				literal("off").executes(MercFrontCoreClientCommand::editorModeOff)
//			).then(
//				literal("on").then(
//					argument("mapAsset", AssetArgumentType.asset(MapAsset.class)).then(
//						argument("environment", StringArgumentType.word()).suggests(MercFrontCoreClientCommand::suggestMapEnvironments).executes(MercFrontCoreClientCommand::editorModeOn)
//					)
//				)
//			)
//		)
		root.then(
			literal("gun").then(
				literal("giveMenu").then(
					argument("item", IdentifierArgumentType.identifier()).suggests(MercFrontCoreClientCommand::suggestGunItems).executes(MercFrontCoreClientCommand::gunGiveMenu)
				)
			).then(
				literal("modifier").requires(source -> source.hasPermissionLevel(4)).then(
					literal("editor").then(
						argument("item", IdentifierArgumentType.identifier()).suggests(MercFrontCoreClientCommand::suggestGunItems).executes(MercFrontCoreClientCommand::gunModifierEditor)
					)
				).then(
					literal("sync").executes(MercFrontCoreClientCommand::gunModifierSync)
				)
			)
		).then(
			literal("loadout").requires(source -> source.hasPermissionLevel(4)).then(
				literal("editor").executes(MercFrontCoreClientCommand::loadoutEditor)
			).then(
				literal("sync").executes(MercFrontCoreClientCommand::loadoutSync)
			)
		).then(
			literal("spawnView").requires(source -> source.hasPermissionLevel(3)).then(
				literal("disable").executes(MercFrontCoreClientCommand::spawnViewDisable)
			)
		);
		
		dispatcher.register(root);
	}
	
	private static int editorModeOff(CommandContext<ServerCommandSource> context) {
		AddonClientData clientData = AddonClientData.getInstance();
		clientData.editingMapName = null;
		clientData.editingEnv = null;
		
		return 1;
	}
	
	private static CompletableFuture<Suggestions> suggestMapEnvironments(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) throws CommandSyntaxException {
		MapAsset asset = AddonArguments.getAsset(context, "mapAsset", MapAsset.class);
		return CommandSource.suggestMatching(asset.getEnvironments().keySet(), suggestions);
	}
	
	private static int editorModeOn(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		PlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;
		
		if (!(player.isCreative() || player.isSpectator())) {
			context.getSource().sendError(Text.translatable("mercfrontcore.message.command.editorMode.error.mode"));
			return -1;
		}
		
		MapAsset asset = AddonArguments.getAsset(context, "mapAsset", MapAsset.class);
		String envStr = StringArgumentType.getString(context, "environment");
		
		if (!asset.environments.containsKey(envStr)) {
			context.getSource().sendError(Text.translatable("mercfrontcore.message.command.editorMode.error.environment"));
			return -1;
		}
		
		AddonClientData clientData = AddonClientData.getInstance();
		clientData.editingMapName = asset.getName();
		clientData.editingEnv = asset.environments.get(envStr);
		
		return 1;
	}
	
	private static CompletableFuture<Suggestions> suggestGunItems(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
		return CommandSource.suggestMatching(GunItemIndex.GUN_ITEMS.stream().map(Identifier::toString), suggestions);
	}
	
	private static int gunGiveMenu(CommandContext<ServerCommandSource> context) {
		Item item = Registries.ITEM.get(IdentifierArgumentType.getIdentifier(context, "item"));
		if (!(item instanceof GunItem gunItem)) {
			context.getSource().sendError(Text.translatable("mercfrontcore.message.command.error.gunItem"));
			return -1;
		}
		
		MinecraftClient.getInstance().setScreen(new WeaponExtraScreen(null, gunItem).sendGivePacket());
		
		return 1;
	}
	
	private static int gunModifierEditor(CommandContext<ServerCommandSource> context) {
		Item item = Registries.ITEM.get(IdentifierArgumentType.getIdentifier(context, "item"));
		if (!(item instanceof GunItem gunItem)) {
			context.getSource().sendError(Text.translatable("mercfrontcore.message.command.error.gunItem"));
			return -1;
		}
		
		MinecraftClient.getInstance().setScreen(new GunModifierEditorScreen(gunItem));
		
		return 1;
	}
	
	private static int gunModifierSync(CommandContext<ServerCommandSource> context) {
		PacketDistributor.sendToServer(new GunModifiersPacket(AddonClientData.getInstance().tempGunModifiers, false));
		context.getSource().sendMessage(Text.translatable("mercfrontcore.message.command.gun.modifier.sync.success"));
		return 1;
	}
	
	private static int loadoutEditor(CommandContext<ServerCommandSource> context) {
		BFClientManager manager = BFClientManager.getInstance();
		if (manager == null) {
			return 0;
		}
		if (manager.getGame() != null) {
			context.getSource().sendError(Text.translatable("mercfrontcore.message.command.loadout.openEditor.error.client.match"));
			return -1;
		}
		
		MinecraftClient.getInstance().setScreen(new LoadoutEditorScreen());
		return 1;
	}
	
	private static int loadoutSync(CommandContext<ServerCommandSource> context) {
		BFClientManager manager = BFClientManager.getInstance();
		if (manager == null) {
			return 0;
		}
		if (manager.getGame() != null) {
			context.getSource().sendError(Text.translatable("mercfrontcore.message.command.loadout.sync.error.client.match"));
			return -1;
		}
		
		PacketDistributor.sendToServer(new LoadoutsPacket(LoadoutIndex.currentFlat()));
		context.getSource().sendMessage(Text.translatable("mercfrontcore.message.packet.loadouts.success"));
		return 1;
	}
	
	private static int spawnViewDisable(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		
		AddonClientData.getInstance().spawnView = null;
		source.sendMessage(Text.translatable("mercfrontcore.message.command.viewSpawns.disable.success"));
		
		return 1;
	}
}
