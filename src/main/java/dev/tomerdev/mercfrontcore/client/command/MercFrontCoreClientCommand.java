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
import dev.tomerdev.mercfrontcore.client.screen.LeaderboardScreen;
import dev.tomerdev.mercfrontcore.client.screen.LoadoutEditorScreen;
import dev.tomerdev.mercfrontcore.client.screen.OwnedGunSkinsScreen;
import dev.tomerdev.mercfrontcore.client.screen.WeaponExtraScreen;
import dev.tomerdev.mercfrontcore.data.LeaderboardPeriod;
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
        dispatcher.register(rootCommand("frontcore"));
        dispatcher.register(rootCommand("fc"));
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<ServerCommandSource> rootCommand(String name) {
        var root = literal(name);
        root.then(
            literal("gun").then(
                literal("giveMenu").requires(source -> source.hasPermissionLevel(2)).then(
                    argument("item", IdentifierArgumentType.identifier())
                        .suggests(MercFrontCoreClientCommand::suggestGunItems)
                        .executes(MercFrontCoreClientCommand::gunGiveMenu)
                )
            ).then(
                literal("skins").executes(MercFrontCoreClientCommand::openOwnedGunSkins)
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
            literal("leaderboard")
                .executes(context -> openLeaderboard(context, LeaderboardPeriod.ALL_TIME))
                .then(literal("weekly").executes(context -> openLeaderboard(context, LeaderboardPeriod.WEEKLY)))
                .then(literal("monthly").executes(context -> openLeaderboard(context, LeaderboardPeriod.MONTHLY)))
                .then(literal("all").executes(context -> openLeaderboard(context, LeaderboardPeriod.ALL_TIME)))
        );
        root.then(
            literal("admin").requires(source -> source.hasPermissionLevel(2))
                .then(
                    literal("gun").then(
                        literal("debugOptions").then(
                            argument("item", IdentifierArgumentType.identifier())
                                .suggests(MercFrontCoreClientCommand::suggestGunItems)
                                .executes(MercFrontCoreClientCommand::gunDebugOptions)
                        )
                    )
                )
        );
        return root;
    }

    private static java.util.concurrent.CompletableFuture<Suggestions> suggestGunItems(CommandContext<ServerCommandSource> context, SuggestionsBuilder suggestions) {
        return CommandSource.suggestMatching(
            Registries.ITEM.getIds().stream()
                .filter(id -> Registries.ITEM.get(id) instanceof GunItem)
                .map(Object::toString),
            suggestions
        );
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
        java.util.Map<net.minecraft.registry.entry.RegistryEntry<Item>, dev.tomerdev.mercfrontcore.data.GunModifier> overrides =
            new it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap<>();
        AddonClientData.getInstance().tempGunModifiers.forEach((item, modifier) -> {
            if (modifier != null && modifier.hasData()) {
                overrides.put(item, modifier);
            }
        });
        PacketDistributor.sendToServer(new GunModifiersPacket(overrides, false));
        context.getSource().sendMessage(Text.translatable("mercfrontcore.message.command.gun.modifier.sync.success"));
        return 1;
    }

    public static int openOwnedGunSkins(CommandContext<ServerCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new OwnedGunSkinsScreen(client.currentScreen));
        return 1;
    }

    private static int openLeaderboard(CommandContext<ServerCommandSource> context, LeaderboardPeriod period) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.setScreen(new LeaderboardScreen(client.currentScreen, period));
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
