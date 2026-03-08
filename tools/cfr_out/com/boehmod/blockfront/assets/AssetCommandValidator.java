/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.context.CommandContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.assets;

import com.mojang.brigadier.context.CommandContext;
import java.util.function.BiPredicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class AssetCommandValidator {
    @NotNull
    private final BiPredicate<CommandContext<CommandSourceStack>, String[]> predicate;
    @NotNull
    private final Component message;

    public AssetCommandValidator(@NotNull BiPredicate<CommandContext<CommandSourceStack>, String[]> predicate, @NotNull Component message) {
        this.predicate = predicate;
        this.message = message;
    }

    @NotNull
    public BiPredicate<CommandContext<CommandSourceStack>, String[]> getPredicate() {
        return this.predicate;
    }

    @NotNull
    public Component getMessage() {
        return this.message;
    }
}

