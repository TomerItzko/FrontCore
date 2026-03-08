/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.registries.VanillaRegistries
 *  net.neoforged.neoforge.event.RegisterCommandsEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.setup;

import com.boehmod.blockfront.common.command.AssetsCommand;
import com.boehmod.blockfront.common.command.BFCoreCommand;
import com.boehmod.blockfront.common.command.ScreenshotCommand;
import com.boehmod.blockfront.common.command.ShakeCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

public class BFCommandSetup {
    public static void register(@NotNull RegisterCommandsEvent event) {
        CommandDispatcher commandDispatcher = event.getDispatcher();
        BFCoreCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher, Commands.createValidationContext((HolderLookup.Provider)VanillaRegistries.createLookup()));
        ScreenshotCommand.regiser((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        AssetsCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
        ShakeCommand.register((CommandDispatcher<CommandSourceStack>)commandDispatcher);
    }
}

