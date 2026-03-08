/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.DEDICATED_SERVER})
public final class BFInteractionSubscriber {
    @SubscribeEvent
    public static void onAttackEntityEvent(@NotNull AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() instanceof GunItem) {
            event.setCanceled(true);
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        if (bFAbstractManager.getGameWithPlayer(player) == null) {
            return;
        }
        if (event.getTarget() instanceof HangingEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteractEntityEvent(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(player);
        if (abstractGame == null) {
            return;
        }
        Entity entity = event.getTarget();
        if (entity instanceof HangingEntity) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos;
        Level level = event.getLevel();
        Block block = level.getBlockState(blockPos = event.getPos()).getBlock();
        if (block instanceof AirBlock) {
            return;
        }
        Player player = event.getEntity();
        UUID uUID = player.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        if (!((AbstractGamePlayerManager)abstractGame.getPlayerManager()).canInteractWithBlock((PlayerDataHandler<?>)obj, level, player, uUID, block, blockPos)) {
            event.setCanceled(true);
        }
    }
}

