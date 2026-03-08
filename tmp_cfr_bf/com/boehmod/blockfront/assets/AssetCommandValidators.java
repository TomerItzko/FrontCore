/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets;

import com.boehmod.blockfront.assets.AssetCommandValidator;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class AssetCommandValidators {
    @NotNull
    public static final MutableComponent ERROR_MESSAGE = Component.translatable((String)"bf.message.command.assets.error.validator.player");
    @NotNull
    public static final AssetCommandValidator ONLY_PLAYERS = new AssetCommandValidator((context, args) -> ((CommandSourceStack)context.getSource()).source instanceof Player, (Component)ERROR_MESSAGE);

    @NotNull
    public static AssetCommandValidator count(@NotNull String[] args2) {
        MutableComponent mutableComponent = Component.empty();
        int n = args2.length;
        for (int i = 0; i < n; ++i) {
            String string = args2[i];
            mutableComponent.append((Component)Component.literal((String)string));
            if (i >= n - 1) continue;
            mutableComponent.append(", ");
        }
        mutableComponent = mutableComponent.withStyle(ChatFormatting.GRAY);
        return new AssetCommandValidator((context, args) -> ((String[])args).length >= n, (Component)Component.translatable((String)"bf.message.command.assets.error.validator.args.count", (Object[])new Object[]{mutableComponent}));
    }

    @NotNull
    public static AssetCommandValidator onlyAllowed(@NotNull String[] items) {
        MutableComponent mutableComponent = Component.empty();
        int n = items.length;
        for (int i = 0; i < n; ++i) {
            String string = items[i];
            mutableComponent.append((Component)Component.literal((String)string));
            if (i >= n - 1) continue;
            mutableComponent.append(", ");
        }
        mutableComponent = mutableComponent.withStyle(ChatFormatting.GRAY);
        return new AssetCommandValidator((context, args) -> {
            for (String string : args) {
                for (String string2 : items) {
                    if (!string.equals(string2)) continue;
                    return true;
                }
            }
            return false;
        }, (Component)Component.translatable((String)"bf.message.command.assets.error.validator.args.onlyAllowed", (Object[])new Object[]{mutableComponent}));
    }
}

