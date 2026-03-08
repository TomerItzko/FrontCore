package dev.tomerdev.mercfrontcore.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.client.screen.GunModifierEditorScreen;
import dev.tomerdev.mercfrontcore.client.screen.LoadoutEditorScreen;
import dev.tomerdev.mercfrontcore.client.screen.WeaponExtraScreen;
import dev.tomerdev.mercfrontcore.net.packet.LoadoutsPacket;
import dev.tomerdev.mercfrontcore.net.packet.GunModifiersPacket;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;
import dev.tomerdev.mercfrontcore.setup.CloudRegistryDebug;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class MercFrontCoreClientCommand {
    private MercFrontCoreClientCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var root = literal("mercfrontcore").requires(source -> source.hasPermissionLevel(2));
        root.then(
            literal("editorMode").then(
                literal("off").executes(MercFrontCoreClientCommand::editorModeOff)
            ).then(
                literal("on").then(
                    argument("mapAsset", StringArgumentType.word()).suggests(MercFrontCoreClientCommand::suggestMapAssets).then(
                        argument("environment", StringArgumentType.word()).suggests(MercFrontCoreClientCommand::suggestMapEnvironments).executes(MercFrontCoreClientCommand::editorModeOn)
                    )
                )
            )
        );
        root.then(
            literal("gun").then(
                literal("giveMenu").then(
                    argument("item", IdentifierArgumentType.identifier())
                        .suggests(MercFrontCoreClientCommand::suggestGunItems)
                        .executes(MercFrontCoreClientCommand::gunGiveMenu)
                )
            ).then(
                literal("modifier").requires(source -> source.hasPermissionLevel(4)).then(
                    literal("editor").then(
                        argument("item", IdentifierArgumentType.identifier())
                            .suggests(MercFrontCoreClientCommand::suggestGunItems)
                            .executes(MercFrontCoreClientCommand::gunModifierEditor)
                    )
                ).then(
                    literal("sync").executes(MercFrontCoreClientCommand::gunModifierSync)
                )
            ).then(
                literal("debugOptions").then(
                    argument("item", IdentifierArgumentType.identifier())
                        .suggests(MercFrontCoreClientCommand::suggestGunItems)
                        .executes(MercFrontCoreClientCommand::gunDebugOptions)
                )
            )
        );
        root.then(
            literal("loadout").requires(source -> source.hasPermissionLevel(4)).then(
                literal("editor").executes(MercFrontCoreClientCommand::loadoutEditor)
            ).then(
                literal("sync").executes(MercFrontCoreClientCommand::loadoutSync)
            )
        );
        root.then(
            literal("spawnView").requires(source -> source.hasPermissionLevel(3)).then(
                literal("disable").executes(MercFrontCoreClientCommand::spawnViewDisable)
            )
        );
        dispatcher.register(root);
    }

    private static int editorModeOff(CommandContext<ServerCommandSource> context) {
        AddonClientData clientData = AddonClientData.getInstance();
        clientData.editingMapName = null;
        clientData.editingEnvName = null;
        return 1;
    }

    private static int editorModeOn(CommandContext<ServerCommandSource> context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return 0;
        }
        if (!(player.isCreative() || player.isSpectator())) {
            context.getSource().sendError(Text.translatable("mercfrontcore.message.command.editorMode.error.mode"));
            return -1;
        }

        String mapAsset = StringArgumentType.getString(context, "mapAsset");
        String environment = StringArgumentType.getString(context, "environment");

        if (!bfEnvironmentExists(mapAsset, environment)) {
            context.getSource().sendError(Text.translatable("mercfrontcore.message.command.editorMode.error.environment", environment, mapAsset));
            return -1;
        }

        AddonClientData clientData = AddonClientData.getInstance();
        clientData.editingMapName = mapAsset;
        clientData.editingEnvName = environment;
        context.getSource().sendMessage(Text.literal("Editor mode enabled: " + mapAsset + " / " + environment));
        return 1;
    }

    private static java.util.concurrent.CompletableFuture<Suggestions> suggestMapAssets(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        return CommandSource.suggestMatching(getMapAssetNames(), suggestions);
    }

    private static java.util.concurrent.CompletableFuture<Suggestions> suggestMapEnvironments(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        String mapAsset;
        try {
            mapAsset = StringArgumentType.getString(context, "mapAsset");
        } catch (IllegalArgumentException e) {
            return suggestions.buildFuture();
        }
        return CommandSource.suggestMatching(getMapEnvironmentNames(mapAsset), suggestions);
    }

    private static java.util.concurrent.CompletableFuture<Suggestions> suggestGunItems(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        return CommandSource.suggestMatching(
            Registries.ITEM.getIds().stream()
                .filter(id -> Registries.ITEM.get(id) instanceof GunItem)
                .map(Object::toString),
            suggestions
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static java.util.Collection<String> getMapAssetNames() {
        try {
            Class<?> assetStoreClass = Class.forName("com.boehmod.blockfront.assets.AssetStore");
            Object store = assetStoreClass.getMethod("getInstance").invoke(null);
            Class<?> mapAssetClass = Class.forName("com.boehmod.blockfront.assets.impl.MapAsset");
            Object registry = assetStoreClass.getMethod("getRegistry", Class.class).invoke(store, mapAssetClass);
            Object entries = registry.getClass().getMethod("getEntries").invoke(registry);
            if (entries instanceof java.util.Map<?, ?> map) {
                return (java.util.Collection<String>) map.keySet();
            }
        } catch (Throwable ignored) {
        }
        return java.util.List.of();
    }

    @SuppressWarnings("unchecked")
    private static java.util.Collection<String> getMapEnvironmentNames(String mapAsset) {
        try {
            Class<?> assetStoreClass = Class.forName("com.boehmod.blockfront.assets.AssetStore");
            Object store = assetStoreClass.getMethod("getInstance").invoke(null);
            Class<?> mapAssetClass = Class.forName("com.boehmod.blockfront.assets.impl.MapAsset");
            Object registry = assetStoreClass.getMethod("getRegistry", Class.class).invoke(store, mapAssetClass);
            Object asset = registry.getClass().getMethod("getByName", String.class).invoke(registry, mapAsset);
            if (asset == null) {
                return java.util.List.of();
            }
            Object environments = asset.getClass().getMethod("getEnvironments").invoke(asset);
            if (environments instanceof java.util.Map<?, ?> map) {
                return (java.util.Collection<String>) map.keySet();
            }
        } catch (Throwable ignored) {
        }
        return java.util.List.of();
    }

    private static boolean bfEnvironmentExists(String mapAsset, String environment) {
        return getMapEnvironmentNames(mapAsset).contains(environment);
    }

    private static int loadoutEditor(CommandContext<ServerCommandSource> context) {
        if (isInBlockFrontMatch()) {
            context.getSource().sendError(Text.translatable("mercfrontcore.message.command.loadout.openEditor.error.client.match"));
            return -1;
        }

        try {
            AddonClientData.getInstance().reloadLoadouts();
            MinecraftClient.getInstance().setScreen(new LoadoutEditorScreen());
            return 1;
        } catch (NoClassDefFoundError e) {
            context.getSource().sendError(Text.literal(
                "Loadout editor failed due to runtime class mismatch (" + e.getMessage() + "). " +
                "Install the remapped MERCFront-core build on this client."
            ));
            return 0;
        } catch (Throwable t) {
            context.getSource().sendError(Text.literal("Failed to open loadout editor: " + t.getClass().getSimpleName()));
            return 0;
        }
    }

    private static int loadoutSync(CommandContext<ServerCommandSource> context) {
        if (isInBlockFrontMatch()) {
            context.getSource().sendError(Text.translatable("mercfrontcore.message.command.loadout.sync.error.client.match"));
            return -1;
        }

        PacketDistributor.sendToServer(new LoadoutsPacket(LoadoutIndex.currentFlat()));
        context.getSource().sendMessage(Text.translatable("mercfrontcore.message.packet.loadouts.success"));
        return 1;
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

    private static int gunDebugOptions(CommandContext<ServerCommandSource> context) {
        Identifier itemId = IdentifierArgumentType.getIdentifier(context, "item");
        Item item = Registries.ITEM.get(itemId);
        if (!(item instanceof GunItem)) {
            context.getSource().sendError(Text.translatable("mercfrontcore.message.command.error.gunItem"));
            return -1;
        }
        GunSkinIndex.ensureInitialized();

        var data = AddonClientData.getInstance();
        var synced = data.getGunExtraOptions(itemId);
        context.getSource().sendMessage(Text.literal("[mercfrontcore] gun=" + itemId));
        if (synced == null) {
            context.getSource().sendMessage(Text.literal("[mercfrontcore] synced options: <none>"));
        } else {
            context.getSource().sendMessage(Text.literal("[mercfrontcore] synced magTypes=" + synced.magTypes()));
            context.getSource().sendMessage(Text.literal("[mercfrontcore] synced barrelTypes=" + synced.barrelTypes()));
            context.getSource().sendMessage(Text.literal("[mercfrontcore] synced skins=" + synced.skins()));
        }

        var skinMap = GunSkinIndex.SKINS.get(itemId);
        if (skinMap == null || skinMap.isEmpty()) {
            context.getSource().sendMessage(Text.literal("[mercfrontcore] GunSkinIndex: <empty> (trackedGuns=" + GunSkinIndex.SKINS.size() + ")"));
        } else {
            context.getSource().sendMessage(Text.literal("[mercfrontcore] GunSkinIndex skinCount=" + skinMap.size() + " skins=" + skinMap.keySet()));
        }
        context.getSource().sendMessage(Text.literal("[mercfrontcore] CloudRegistry: " + CloudRegistryDebug.describe()));
        return 1;
    }

    private static int spawnViewDisable(CommandContext<ServerCommandSource> context) {
        AddonClientData.getInstance().spawnView = null;
        context.getSource().sendMessage(Text.translatable("mercfrontcore.message.command.viewSpawns.disable.success"));
        return 1;
    }

    private static boolean isInBlockFrontMatch() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return false;
        }

        try {
            Class<?> managerClass = Class.forName("com.boehmod.blockfront.client.BFClientManager");
            Object manager = managerClass.getMethod("getInstance").invoke(null);
            if (manager == null) {
                return false;
            }

            Object game = managerClass.getMethod("getGame").invoke(manager);
            return game != null;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
