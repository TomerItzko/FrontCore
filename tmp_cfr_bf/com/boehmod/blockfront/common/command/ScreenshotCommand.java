/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.command;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.BFScreenshotRequester;
import com.boehmod.blockfront.server.ac.BFServerAntiCheat;
import com.boehmod.blockfront.server.ac.BFServerScreenshotManager;
import com.boehmod.blockfront.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class ScreenshotCommand {
    public static void regiser(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal((String)"screenshot").requires(CommandUtils::isAdmin);
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)literalArgumentBuilder.then(Commands.argument((String)"player", (ArgumentType)EntityArgument.players()).executes(context -> {
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Object obj = bFAbstractManager.getPlayerDataHandler();
            EntityArgument.getPlayers((CommandContext)context, (String)"player").forEach(player -> ScreenshotCommand.runScreenshot(bFAbstractManager, obj, (CommandContext<CommandSourceStack>)context, player));
            return 1;
        }))).executes(context -> 1));
    }

    private static void runScreenshot(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> playerData, @NotNull CommandContext<CommandSourceStack> context, @NotNull ServerPlayer player) {
        Object object = ((CommandSourceStack)context.getSource()).getEntity();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        if (!(manager instanceof BFServerManager)) {
            return;
        }
        object = (BFServerManager)manager;
        PlayerCloudData playerCloudData = playerData.getCloudProfile((Player)serverPlayer);
        BFServerAntiCheat bFServerAntiCheat = (BFServerAntiCheat)((BFAbstractManager)object).getAntiCheat();
        BFServerScreenshotManager bFServerScreenshotManager = (BFServerScreenshotManager)bFServerAntiCheat.getScreenshotManager();
        if (player.hasPermissions(3) || playerCloudData.hasPermission("staff.ingame.admin")) {
            BFScreenshotRequester bFScreenshotRequester = bFServerScreenshotManager.enqueue(player);
            if (bFScreenshotRequester != null) {
                bFScreenshotRequester.doRequest(serverPlayer);
            }
        } else {
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.command.game.admin.screenshot.error.permission", (Object[])new Object[]{player.getDisplayName()});
            serverPlayer.sendSystemMessage((Component)mutableComponent.withStyle(ChatFormatting.RED));
        }
    }
}

