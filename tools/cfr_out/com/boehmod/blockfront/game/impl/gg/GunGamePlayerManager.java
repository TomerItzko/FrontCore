/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectSortedSet
 *  javax.annotation.CheckForNull
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.gg;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class GunGamePlayerManager
extends AbstractGamePlayerManager<GunGame> {
    private static final Component DEGRADE_MESSAGE = Component.translatable((String)"bf.message.gamemode.gungame.degrade").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD);
    private static final Component UPGRADE_MESSAGE = Component.translatable((String)"bf.message.gamemode.gungame.upgrade").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.BOLD);
    private static final Component KNIFE_MESSAGE = Component.translatable((String)"bf.message.gamemode.gungame.knife").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD);
    @Nullable
    public UUID field_3517 = null;

    public GunGamePlayerManager(@NotNull GunGame game, @NotNull PlayerDataHandler<?> playerData) {
        super(game, playerData);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)15067868));
        Style style3 = Style.EMPTY.withColor(TextColor.fromRgb((int)8271921));
        Style style4 = Style.EMPTY.withColor(TextColor.fromRgb((int)14996948));
        this.addTeam(new GameTeam(game, "Allies", style, style2, 8));
        this.addTeam(new GameTeam(game, "Axis", style3, style4, 8));
    }

    @Override
    @Nullable
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        if (this.field_3517 != null) {
            return new WinningTeamData(null, Collections.singleton(this.field_3517));
        }
        if (timer != null && !timer.isDone()) {
            return null;
        }
        ObjectSortedSet<UUID> objectSortedSet = BFUtils.topPlayers(this.game, 5, BFStats.SCORE, players);
        if (objectSortedSet.isEmpty()) {
            return new WinningTeamData(null, null);
        }
        return new WinningTeamData(null, Collections.singleton((UUID)objectSortedSet.getFirst()));
    }

    @Override
    protected boolean method_2771(@NotNull ServerPlayer serverPlayer) {
        return true;
    }

    @Override
    public void method_2753(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull GameTeam gameTeam) {
        BFUtils.teleportPlayer(playerDataHandler, serverPlayer, gameTeam.randomSpawn(this.game));
        this.method_3605(serverPlayer);
    }

    @Override
    public void method_2791(@NotNull ServerPlayer serverPlayer) {
    }

    @Override
    public void specificTickPlayer(@NotNull ServerPlayer serverPlayer, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
    }

    @Override
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @CheckForNull @Nullable ServerPlayer serverPlayer2, @CheckForNull @Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
        GameTeam gameTeam;
        if (serverPlayer2 != null && uUID2 != null && (gameTeam = this.getPlayerTeam(uUID2)) != null && ((GunGame)this.game).getStatus() == GameStatus.GAME && !serverPlayer2.equals((Object)serverPlayer)) {
            BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID2, BFStats.GG_STAGE);
            BFUtils.sendPopupMessage(serverPlayer2, new BFPopup(UPGRADE_MESSAGE, 60));
            serverPlayer2.playSound(SoundEvents.PLAYER_LEVELUP, 2.5f, 1.0f);
            serverPlayer2.playSound(SoundEvents.PLAYER_LEVELUP, 2.5f, 1.0f);
            this.method_3605(serverPlayer2);
        }
        if (((GunGame)this.game).getStatus() == GameStatus.GAME) {
            boolean bl = false;
            Entity entity = damageSource.getEntity();
            if (serverPlayer2 != null && !serverPlayer2.getMainHandItem().isEmpty() && serverPlayer2.getMainHandItem().getItem() instanceof MeleeItem) {
                bl = true;
            }
            if (entity == null || entity.equals((Object)serverPlayer) || bl) {
                int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.GG_STAGE) - 1;
                if (n < 0) {
                    n = 0;
                }
                BFUtils.setPlayerStat(this.game, uUID, BFStats.GG_STAGE, n);
                BFUtils.sendPopupMessage(serverPlayer, new BFPopup(DEGRADE_MESSAGE, 60));
                serverPlayer.playSound(SoundEvents.WITHER_DEATH, 2.5f, 1.0f);
            }
        }
    }

    @Override
    public void initPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel leve, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        BFUtils.initPlayerForGame(dataHandler, leve, player);
        GameTeam gameTeam = this.getPlayerTeam(uuid);
        if (gameTeam == null) {
            return;
        }
        BFUtils.teleportPlayer(dataHandler, player, gameTeam.randomSpawn(this.game));
        BFUtils.method_2999(player);
        this.method_3605(player);
    }

    @Override
    public boolean method_2778(@NotNull Player player, @NotNull UUID uUID, @NotNull DamageSource damageSource, Entity entity) {
        if (entity instanceof Player) {
            Player player2 = (Player)entity;
            UUID uUID2 = player2.getUUID();
            if (player.getScoreboardName().equalsIgnoreCase(player2.getScoreboardName())) {
                return damageSource.is(DamageTypeTags.IS_EXPLOSION) || damageSource.is(DamageTypeTags.IS_FIRE);
            }
            GameTeam gameTeam = this.getPlayerTeam(uUID);
            GameTeam gameTeam2 = this.getPlayerTeam(uUID2);
            return gameTeam == null || gameTeam2 == null || !gameTeam.getName().equalsIgnoreCase(gameTeam2.getName());
        }
        return true;
    }

    @Override
    public boolean method_2775(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource) {
        return true;
    }

    public void method_3605(@NotNull ServerPlayer serverPlayer) {
        UUID uUID = serverPlayer.getUUID();
        int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.GG_STAGE);
        if (n > ((GunGame)this.game).GUN_STAGES.size()) {
            if (this.field_3517 == null) {
                this.field_3517 = uUID;
            }
            return;
        }
        serverPlayer.getInventory().clearContent();
        if (n < ((GunGame)this.game).GUN_STAGES.size()) {
            GunItem gunItem = ((GunGame)this.game).getCurrentGun(n);
            if (gunItem != null) {
                serverPlayer.getInventory().setItem(0, new ItemStack((ItemLike)gunItem));
            }
        } else {
            BFUtils.sendPopupMessage(serverPlayer, new BFPopup(KNIFE_MESSAGE, 80));
        }
        serverPlayer.getInventory().setItem(2, new ItemStack((ItemLike)BFItems.MELEE_ITEM_KNIFE_M1905.get()));
    }

    @Override
    @Nullable
    public List<ItemEntity> method_2776(@NotNull Player player, @NotNull List<ItemEntity> list) {
        return null;
    }

    @Override
    public boolean canPlayerDropItem(@NotNull Player player, @NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean method_2779(@NotNull Player player, @NotNull ItemEntity itemEntity, @NotNull ItemStack itemStack) {
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

    @Override
    public void reset() {
        super.reset();
        this.field_3517 = null;
    }
}

