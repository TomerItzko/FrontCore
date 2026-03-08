/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandSource
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.command;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.AssetWatcher;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Map;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class AssetsCommand {
    public static final LiteralArgumentBuilder<CommandSourceStack> field_2465 = (LiteralArgumentBuilder)Commands.literal((String)"edit").then(AssetsCommand.method_2085().then(AssetsCommand.method_2096()));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        AssetStore assetStore = AssetStore.getInstance();
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal((String)"assets").requires(commandSourceStack -> commandSourceStack.hasPermission(3))).executes(commandContext -> {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.game.manage.error.usage"));
            return 1;
        })).then(Commands.literal((String)"list").then(AssetsCommand.method_2085().executes(commandContext -> {
            AssetsCommand.method_2092(((CommandSourceStack)commandContext.getSource()).source, assetStore, StringArgumentType.getString((CommandContext)commandContext, (String)"assetType"));
            return 1;
        })))).then(Commands.literal((String)"remove").then(AssetsCommand.method_2085().then(AssetsCommand.method_2096().executes(commandContext -> {
            AssetsCommand.method_2093(((CommandSourceStack)commandContext.getSource()).source, assetStore, StringArgumentType.getString((CommandContext)commandContext, (String)"assetType"), StringArgumentType.getString((CommandContext)commandContext, (String)"assetName"));
            return 1;
        }))))).then(Commands.literal((String)"reload").executes(commandContext -> {
            AssetsCommand.method_2091(((CommandSourceStack)commandContext.getSource()).source, assetStore);
            return 1;
        }))).then(Commands.literal((String)"save-all").executes(commandContext -> {
            AssetsCommand.method_2099(((CommandSourceStack)commandContext.getSource()).source, assetStore);
            return 1;
        }))).then(Commands.literal((String)"test").executes(commandContext -> {
            AssetsCommand.method_2102(((CommandSourceStack)commandContext.getSource()).source, assetStore);
            return 1;
        }))).then(Commands.literal((String)"create").then(AssetsCommand.method_2085().then(Commands.argument((String)"assetName", (ArgumentType)StringArgumentType.word()).then(Commands.argument((String)"assetArg", (ArgumentType)StringArgumentType.greedyString()).suggests((commandContext, suggestionsBuilder) -> {
            if (StringArgumentType.getString((CommandContext)commandContext, (String)"assetType").equals("games")) {
                return SharedSuggestionProvider.suggest(BFGameType.values().stream().map(BFGameType::getName), (SuggestionsBuilder)suggestionsBuilder);
            }
            return SharedSuggestionProvider.suggest((String[])new String[0], (SuggestionsBuilder)suggestionsBuilder);
        }).executes(commandContext -> {
            AssetsCommand.method_2094(((CommandSourceStack)commandContext.getSource()).source, assetStore, StringArgumentType.getString((CommandContext)commandContext, (String)"assetType"), StringArgumentType.getString((CommandContext)commandContext, (String)"assetName"), StringArgumentType.getString((CommandContext)commandContext, (String)"assetArg"));
            return 1;
        })))))).then(field_2465.then(AssetsCommand.method_2085().then(((RequiredArgumentBuilder)AssetsCommand.method_2096().then(Commands.argument((String)"args", (ArgumentType)StringArgumentType.greedyString()).executes(commandContext -> {
            AssetsCommand.method_2089((CommandContext<CommandSourceStack>)commandContext, assetStore, StringArgumentType.getString((CommandContext)commandContext, (String)"assetType"), StringArgumentType.getString((CommandContext)commandContext, (String)"assetName"), StringArgumentType.getString((CommandContext)commandContext, (String)"args").split(" "));
            return 1;
        }))).executes(commandContext -> {
            AssetsCommand.method_2089((CommandContext<CommandSourceStack>)commandContext, assetStore, StringArgumentType.getString((CommandContext)commandContext, (String)"assetType"), StringArgumentType.getString((CommandContext)commandContext, (String)"assetName"), new String[0]);
            return 1;
        })))));
    }

    private static void method_2092(@NotNull CommandSource commandSource, @NotNull AssetStore assetStore, @NotNull String string) {
        BFLog.log("[Command] Sending list of all assets...", new Object[0]);
        AssetRegistry assetRegistry = assetStore.method_1635(string);
        if (assetRegistry == null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.noregistry", (Object[])new Object[]{string}));
            return;
        }
        Map map = assetRegistry.getEntries();
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(map.size())).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)assetRegistry.getType()).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.command.assets.list", (Object[])new Object[]{mutableComponent, mutableComponent2});
        CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)"----------------------------------"));
        CommandUtils.sendBfa(commandSource, (Component)mutableComponent3);
        CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)"----------------------------------"));
        for (Map.Entry entry : map.entrySet()) {
            CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)(" - " + entry.getKey())));
        }
        CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)"------------------------------"));
    }

    private static void method_2093(@NotNull CommandSource commandSource, @NotNull AssetStore assetStore, @NotNull String string, @NotNull String string2) {
        BFLog.log("[Command] Attempting to remove asset...", new Object[0]);
        AssetRegistry assetRegistry = assetStore.method_1635(string);
        if (assetRegistry == null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.noregistry", (Object[])new Object[]{string}));
            return;
        }
        Object t = assetRegistry.getByName(string2);
        if (t == null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.noasset", (Object[])new Object[]{string2}));
            return;
        }
        assetRegistry.removeAsset(string2);
        CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)("Successfully unregistered asset '" + string2 + "'.")));
    }

    private static void method_2091(@NotNull CommandSource commandSource, @NotNull AssetStore assetStore) {
        BFLog.log("[Command] Reloading all assets...", new Object[0]);
        CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.reload.reloading"));
        assetStore.loadFromDisk();
        int n = assetStore.assetCount();
        int n2 = assetStore.numRegistered();
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(n2)).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.reload.complete", (Object[])new Object[]{mutableComponent, mutableComponent2}));
    }

    private static void method_2099(@NotNull CommandSource commandSource, @NotNull AssetStore assetStore) {
        AssetWatcher assetWatcher = assetStore.getWatcher();
        BFLog.log("[Command] Pausing file watcher for command execution.", new Object[0]);
        if (assetWatcher != null) {
            assetWatcher.method_1597(true);
        }
        BFLog.log("[Command] Saving all assets...", new Object[0]);
        CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.saveall.saving"));
        assetStore.saveAll(commandSource);
        assetStore.loadFromDisk();
        int n = assetStore.assetCount();
        int n2 = assetStore.numRegistered();
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(n2)).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.saveall.complete", (Object[])new Object[]{mutableComponent, mutableComponent2}));
        BFLog.log("[Command] Resuming file watcher for command execution.", new Object[0]);
        if (assetWatcher != null) {
            assetWatcher.method_1597(false);
        }
    }

    private static void method_2102(@NotNull CommandSource commandSource, @NotNull AssetStore assetStore) {
        BFLog.log("[Command] Testing all assets...", new Object[0]);
        CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.test.testing"));
        assetStore.testAll(commandSource);
        CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.test.testing.success"));
    }

    private static void method_2094(@NotNull CommandSource commandSource, @NotNull AssetStore assetStore, @NotNull String string, @NotNull String string2, @NotNull String string3) {
        BFLog.log("[Command] Attempting to create asset...", new Object[0]);
        MutableComponent mutableComponent = Component.literal((String)string).withStyle(BFStyles.LIME);
        AssetRegistry assetRegistry = assetStore.method_1635(string);
        if (assetRegistry == null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.noregistry", (Object[])new Object[]{string}));
            return;
        }
        MutableComponent mutableComponent2 = Component.literal((String)string2).withStyle(BFStyles.LIME);
        if (assetRegistry.getByName(string2) != null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.create.error.exists", (Object[])new Object[]{mutableComponent2, mutableComponent}));
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "The mod manager instance is null!";
        Object t = assetRegistry.getFactory().create(assetStore.isClient(), bFAbstractManager, string2, string3);
        if (t != null) {
            assetRegistry.putAsset(string2, t);
            CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.create.success", (Object[])new Object[]{mutableComponent2, mutableComponent}));
        } else {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.create.fail", (Object[])new Object[]{mutableComponent2, mutableComponent}));
        }
    }

    private static void method_2089(@NotNull CommandContext<CommandSourceStack> commandContext, @NotNull AssetStore assetStore, @NotNull String string, @NotNull String string2, @NotNull String[] stringArray) {
        BFLog.log("[Command] Attempting to edit asset...", new Object[0]);
        CommandSource commandSource = ((CommandSourceStack)commandContext.getSource()).source;
        AssetRegistry assetRegistry = assetStore.method_1635(string);
        if (assetRegistry == null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.noregistry", (Object[])new Object[]{string}));
            return;
        }
        Object t = assetRegistry.getByName(string2);
        if (t == null) {
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.noasset", (Object[])new Object[]{string2}));
            return;
        }
        t.getCommand().run(commandContext, stringArray);
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> method_2085() {
        return Commands.argument((String)"assetType", (ArgumentType)StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(AssetStore.getInstance().getTypes(), (SuggestionsBuilder)suggestionsBuilder));
    }

    private static RequiredArgumentBuilder<CommandSourceStack, String> method_2096() {
        return Commands.argument((String)"assetName", (ArgumentType)StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> {
            try {
                String string = StringArgumentType.getString((CommandContext)commandContext, (String)"assetType");
                AssetRegistry assetRegistry = AssetStore.getInstance().method_1635(string);
                if (assetRegistry != null) {
                    return SharedSuggestionProvider.suggest(assetRegistry.getEntries().keySet(), (SuggestionsBuilder)suggestionsBuilder);
                }
                return SharedSuggestionProvider.suggest((String[])new String[0], (SuggestionsBuilder)suggestionsBuilder);
            }
            catch (Exception exception) {
                return SharedSuggestionProvider.suggest((String[])new String[0], (SuggestionsBuilder)suggestionsBuilder);
            }
        });
    }
}

