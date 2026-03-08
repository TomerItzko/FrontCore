/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class SyringeItem
extends BFUtilityItem {
    public static final int field_3974 = 8;

    public SyringeItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
    }

    abstract boolean method_4079(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull Player var2, @NotNull Player var3, @NotNull AbstractGame<?, ?, ?> var4, @NotNull AbstractGamePlayerManager<?> var5, @NotNull Set<UUID> var6);

    abstract boolean method_4082(@NotNull Player var1, @NotNull Player var2);

    abstract void method_4083(@NotNull ItemStack var1, @NotNull Player var2, @NotNull Player var3);

    @NotNull
    abstract Component method_4081(@NotNull Player var1);

    @NotNull
    abstract Component method_4080(@NotNull Component var1);

    public void method_4084(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull Player player2) {
        itemStack.shrink(1);
        player.swing(InteractionHand.MAIN_HAND);
        this.method_4083(itemStack, player, player2);
    }

    @NotNull
    public InteractionResult interactLivingEntity(@NotNull ItemStack itemStack, @NotNull Player player, LivingEntity livingEntity, @NotNull InteractionHand interactionHand) {
        if (livingEntity.level().isClientSide()) {
            player.swing(InteractionHand.MAIN_HAND);
            ShakeManager.applyShake(ShakeNodePresets.field_1928);
            return InteractionResult.PASS;
        }
        UUID uUID = player.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame != null) {
            Object obj = abstractGame.getPlayerManager();
            if (livingEntity instanceof Player) {
                Player player2 = (Player)livingEntity;
                Object obj2 = bFAbstractManager.getPlayerDataHandler();
                Object d = ((PlayerDataHandler)obj2).getPlayerData(player2);
                boolean bl = BFUtils.isPlayerUnavailable(player2, d);
                Set<UUID> set = ((AbstractGamePlayerManager)obj).getPlayerUUIDs();
                if (!bl && this.method_4079(bFAbstractManager, player, player2, abstractGame, (AbstractGamePlayerManager<?>)obj, set)) {
                    Component component;
                    ServerPlayer serverPlayer;
                    String string = player.getScoreboardName();
                    GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uUID);
                    MutableComponent mutableComponent = Component.literal((String)string);
                    if (gameTeam != null) {
                        mutableComponent = mutableComponent.copy().withStyle(gameTeam.getStyleText());
                    }
                    if (this.method_4082(player, player2)) {
                        BFUtils.triggerPlayerStat(bFAbstractManager, abstractGame, uUID, BFStats.SCORE, 2);
                    }
                    if (player instanceof ServerPlayer) {
                        serverPlayer = (ServerPlayer)player;
                        component = this.method_4081(player);
                        BFUtils.sendPopupMessage(serverPlayer, new BFPopup(component, 40));
                    }
                    if (player2 instanceof ServerPlayer) {
                        serverPlayer = (ServerPlayer)player2;
                        component = this.method_4080((Component)mutableComponent);
                        BFUtils.sendNoticeMessage(serverPlayer, component);
                    }
                    this.method_4084(itemStack, player, player2);
                    return InteractionResult.SUCCESS;
                }
            }
        } else if (livingEntity instanceof Player) {
            Player player3 = (Player)livingEntity;
            this.method_4084(itemStack, player, player3);
        }
        return InteractionResult.PASS;
    }
}

