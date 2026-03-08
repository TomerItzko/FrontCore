/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.neoforged.neoforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.neoforged.neoforge.event.level.BlockEvent$BreakEvent
 *  net.neoforged.neoforge.event.level.BlockEvent$EntityPlaceEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

public final class BFBlocksSubscriber {
    public static void onBreak(@NotNull BlockEvent.BreakEvent event) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Player player = event.getPlayer();
        UUID uUID = player.getUUID();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame != null && !((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2781(player, block)) {
            event.setCanceled(true);
        }
    }

    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        Object object = event.getEntity();
        if (object instanceof Player) {
            Player player = (Player)object;
            object = BlockFront.getInstance().getManager();
            assert (object != null) : "Mod manager is null!";
            AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer(player.getUUID());
            if (abstractGame != null && !((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2786(player, event.getLevel().getBlockState(event.getPos()).getBlock())) {
                event.setCanceled(true);
            }
        }
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        UUID uUID = player.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        Object obj = bFAbstractManager.getPlayerDataHandler();
        BlockPos blockPos = event.getPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (abstractGame != null && !((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2751((PlayerDataHandler<?>)obj, level, player, blockState.getBlock(), blockPos)) {
            event.setCanceled(true);
        }
    }
}

