/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class CommandUtils {
    public static boolean isAdmin(@NotNull CommandSourceStack source) {
        Object object = source.getEntity();
        if (!(object instanceof ServerPlayer)) {
            return false;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        Object h = ((BFAbstractManager)object).getPlayerDataHandler();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)h).getCloudProfile((Player)serverPlayer);
        return playerCloudData.hasPermission("staff.ingame.admin") || source.hasPermission(3);
    }

    public static void sendBf(@NotNull ServerPlayer player, @NotNull Component message) {
        MutableComponent mutableComponent = Component.literal((String)"bf".toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.BOLD);
        BFUtils.sendFancyMessage(player, (Component)mutableComponent.append(" "), message);
    }

    public static void sendBfaWarn(@NotNull CommandSource source, @NotNull Component message) {
        MutableComponent mutableComponent = Component.literal((String)"[BFA Warn]:").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED);
        MutableComponent mutableComponent2 = Component.empty().append((Component)mutableComponent).append(" ").append((Component)message.copy().withColor(0xFFFFFF));
        source.sendSystemMessage((Component)mutableComponent2);
    }

    public static void sendBfa(@NotNull CommandSource source, @NotNull Component component) {
        MutableComponent mutableComponent = Component.literal((String)"[BFA]:").withStyle(ChatFormatting.BOLD).withStyle(BFStyles.GREEN);
        MutableComponent mutableComponent2 = Component.empty().append((Component)mutableComponent).append(" ").append((Component)component.copy().withColor(0xFFFFFF));
        source.sendSystemMessage((Component)mutableComponent2);
    }
}

