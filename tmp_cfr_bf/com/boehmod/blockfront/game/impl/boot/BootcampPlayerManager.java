/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.boot.BootcampGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.CheckForNull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BootcampPlayerManager
extends AbstractGamePlayerManager<BootcampGame> {
    public static final String field_3353 = "Recruits";
    private static final int field_3354 = 16;

    public BootcampPlayerManager(@NotNull BootcampGame bootcampGame, @NotNull PlayerDataHandler<?> playerDataHandler) {
        super(bootcampGame, playerDataHandler);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)15067868));
        this.addTeam(new GameTeam(bootcampGame, field_3353, style, style2, 16));
    }

    @Override
    @javax.annotation.Nullable
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        return null;
    }

    @Override
    protected boolean method_2771(@NotNull ServerPlayer serverPlayer) {
        return true;
    }

    @Override
    public void method_2753(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull GameTeam gameTeam) {
        FDSPose fDSPose = this.getLobbySpawn();
        if (fDSPose != null) {
            BFUtils.teleportPlayer(playerDataHandler, serverPlayer, fDSPose);
        }
    }

    @Override
    public void method_2791(@NotNull ServerPlayer serverPlayer) {
    }

    @Override
    public void specificTickPlayer(@NotNull ServerPlayer serverPlayer, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
    }

    @Override
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @CheckForNull @javax.annotation.Nullable ServerPlayer serverPlayer2, @javax.annotation.Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
    }

    @Override
    public void initPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel leve, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        BFUtils.initPlayerForGame(dataHandler, leve, player);
        BFUtils.teleportPlayer(dataHandler, player, this.getLobbySpawn());
        BFUtils.clearInventory(player.getUUID());
    }

    @Override
    public boolean method_2778(@NotNull Player player, @NotNull UUID uUID, @NotNull DamageSource damageSource, Entity entity) {
        return false;
    }

    @Override
    public boolean method_2775(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource) {
        if (livingEntity instanceof HumanEntity) {
            Entity entity = damageSource.getEntity();
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                if (Math.random() < 0.1 && !serverPlayer.getMainHandItem().isEmpty() && serverPlayer.getMainHandItem().getItem() instanceof GunItem) {
                    BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MATCH_GAMEMODE_BOOT_INSTRUCTOR_FRIENDLYFIRE.get(), SoundSource.NEUTRAL);
                }
            }
            return false;
        }
        return true;
    }

    @Override
    @javax.annotation.Nullable
    public List<ItemEntity> method_2776(@NotNull Player player, @NotNull List<ItemEntity> list) {
        return null;
    }

    @Override
    public boolean canPlayerDropItem(@NotNull Player player, @NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean method_2779(@NotNull Player player, @NotNull ItemEntity itemEntity, @NotNull ItemStack itemStack) {
        UUID uUID = player.getUUID();
        Inventory inventory = player.getInventory();
        Item item = itemStack.getItem();
        if (item instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            if (gunItem.isSecondary()) {
                if (((ItemStack)inventory.items.getFirst()).isEmpty()) {
                    BFUtils.setPlayerStat(this.game, uUID, BFStats.ITEM_PICKED_UP, 1);
                    return true;
                }
            } else if (((ItemStack)inventory.items.get(1)).isEmpty()) {
                BFUtils.setPlayerStat(this.game, uUID, BFStats.ITEM_PICKED_UP, 1);
                return true;
            }
        } else if (itemStack.getItem() instanceof BFUtilityItem && (((ItemStack)inventory.items.get(3)).isEmpty() || ((ItemStack)inventory.items.get(4)).isEmpty() || ((ItemStack)inventory.items.get(5)).isEmpty())) {
            BFUtils.setPlayerStat(this.game, uUID, BFStats.ITEM_PICKED_UP, 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean method_2781(@NotNull Player player, @NotNull Block block) {
        return false;
    }

    @Override
    public boolean method_2786(@NotNull Player player, @NotNull Block block) {
        return false;
    }

    @Override
    public boolean method_2751(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Level level, @NotNull Player player, @NotNull Block block, @NotNull BlockPos blockPos) {
        return true;
    }

    @Override
    public boolean isAcceptingPlayers() {
        return true;
    }
}

