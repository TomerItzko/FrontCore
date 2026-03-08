/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFUtils;
import com.mojang.brigadier.context.CommandContextBuilder;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.CommandEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public final class BFCommandSubscriber {
    private static final Component field_4355 = Component.translatable((String)"bf.message.gamemode.command.fail").withStyle(ChatFormatting.RED);

    @SubscribeEvent
    public static void onCommand(@NotNull CommandEvent event) {
        CommandContextBuilder commandContextBuilder = event.getParseResults().getContext();
        ServerPlayer serverPlayer = ((CommandSourceStack)commandContextBuilder.getSource()).getPlayer();
        if (serverPlayer == null) {
            return;
        }
        UUID uUID = serverPlayer.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        PlayerCloudData playerCloudData = ((PlayerDataHandler)bFAbstractManager.getPlayerDataHandler()).getCloudProfile((Player)serverPlayer);
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame != null && !serverPlayer.hasPermissions(3) && !playerCloudData.hasPermission("staff.ingame.admin")) {
            event.setCanceled(true);
            BFUtils.sendNoticeMessage(serverPlayer, field_4355);
        }
    }
}

