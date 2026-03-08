/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.context.CommandContext
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSource
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.HoverEvent
 *  net.minecraft.network.chat.HoverEvent$Action
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.assets;

import com.boehmod.blockfront.assets.AssetCommandValidator;
import com.boehmod.blockfront.util.CommandUtils;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

public class AssetCommandBuilder {
    @NotNull
    public final List<AssetCommandValidator> validators;
    @NotNull
    public final Map<String, AssetCommandBuilder> subCommands;
    @Nullable
    public BiConsumer<CommandContext<CommandSourceStack>, String[]> runner;

    public AssetCommandBuilder() {
        this(null);
    }

    public AssetCommandBuilder(@Nullable BiConsumer<CommandContext<CommandSourceStack>, String[]> runner) {
        this.runner = runner;
        this.subCommands = new HashMap<String, AssetCommandBuilder>();
        this.validators = new ObjectArrayList();
    }

    @NotNull
    public AssetCommandBuilder inherit(@NotNull AssetCommandBuilder builder) {
        this.subCommands.putAll(builder.subCommands);
        return this;
    }

    @NotNull
    public AssetCommandBuilder validator(@NotNull AssetCommandValidator validator) {
        this.validators.add(validator);
        return this;
    }

    public void run(@NotNull CommandContext<CommandSourceStack> context, @NotNull String[] args) {
        CommandSource commandSource = ((CommandSourceStack)context.getSource()).source;
        for (AssetCommandValidator assetCommandValidator : this.validators) {
            if (assetCommandValidator.getPredicate().test(context, args)) continue;
            CommandUtils.sendBfaWarn(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error", (Object[])new Object[]{assetCommandValidator.getMessage()}));
            return;
        }
        this.runThis(context, args);
        this.runSubCommands(context, args);
    }

    public void runSubCommands(@NotNull CommandContext<CommandSourceStack> context, @NotNull String[] args) {
        if (this.subCommands.isEmpty()) {
            return;
        }
        CommandSource commandSource = ((CommandSourceStack)context.getSource()).source;
        if (args.length == 0) {
            CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.args", (Object[])new Object[]{this.getErrorSuggestions()}).withStyle(ChatFormatting.RED));
            return;
        }
        AssetCommandBuilder assetCommandBuilder = this.subCommands.get(args[0]);
        if (assetCommandBuilder == null) {
            CommandUtils.sendBfa(commandSource, (Component)Component.translatable((String)"bf.message.command.assets.error.usage", (Object[])new Object[]{this.getErrorSuggestions()}).withStyle(ChatFormatting.RED));
            return;
        }
        String[] stringArray = Arrays.copyOfRange(args, 1, args.length);
        assetCommandBuilder.run(context, stringArray);
    }

    @NotNull
    public AssetCommandBuilder subCommands(@NotNull Map<String, AssetCommandBuilder> commands) {
        this.subCommands.putAll(commands);
        return this;
    }

    @NotNull
    public AssetCommandBuilder subCommand(@NotNull String name, @NotNull AssetCommandBuilder builder) {
        this.subCommands.put(name, builder);
        return this;
    }

    @NotNull
    public AssetCommandBuilder subCommand(@NotNull String[] names, @NotNull AssetCommandBuilder builder) {
        for (String string : names) {
            this.subCommand(string, builder);
        }
        return this;
    }

    private void runThis(@NotNull CommandContext<CommandSourceStack> context, @NotNull String[] args) {
        if (this.runner != null) {
            this.runner.accept(context, args);
        }
    }

    @NotNull
    public MutableComponent getErrorSuggestions() {
        MutableComponent mutableComponent = Component.empty();
        int n = this.subCommands.size();
        int n2 = 0;
        for (Map.Entry<String, AssetCommandBuilder> entry : this.subCommands.entrySet()) {
            String string = entry.getKey();
            AssetCommandBuilder assetCommandBuilder = entry.getValue();
            Style style = Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (Object)assetCommandBuilder.getErrorSuggestions()));
            mutableComponent.append((Component)Component.literal((String)string).withStyle(style));
            if (n2 < n - 1) {
                mutableComponent.append(", ");
            }
            ++n2;
        }
        return mutableComponent.withStyle(ChatFormatting.GRAY);
    }
}

