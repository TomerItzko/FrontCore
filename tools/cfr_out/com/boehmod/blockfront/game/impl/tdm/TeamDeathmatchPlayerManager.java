/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Style
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.util.BFUtils;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class TeamDeathmatchPlayerManager
extends AbstractGamePlayerManager<TeamDeathmatchGame> {
    public TeamDeathmatchPlayerManager(@NotNull TeamDeathmatchGame teamDeathmatchGame, @NotNull PlayerDataHandler<?> playerDataHandler) {
        super(teamDeathmatchGame, playerDataHandler);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)15067868));
        Style style3 = Style.EMPTY.withColor(TextColor.fromRgb((int)8271921));
        Style style4 = Style.EMPTY.withColor(TextColor.fromRgb((int)14996948));
        this.addTeam(new GameTeam(teamDeathmatchGame, "Allies", style, style2, 8));
        this.addTeam(new GameTeam(teamDeathmatchGame, "Axis", style3, style4, 8));
    }

    @Override
    @Nullable
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        GameTeam gameTeam = this.getTeamByName("Axis");
        GameTeam gameTeam2 = this.getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        int n = gameTeam.getObjectInt(BFStats.SCORE);
        if (n >= 50) {
            return new WinningTeamData(gameTeam, null);
        }
        int n2 = gameTeam2.getObjectInt(BFStats.SCORE);
        if (n2 >= 50) {
            return new WinningTeamData(gameTeam2, null);
        }
        if (timer != null && !timer.isDone()) {
            return null;
        }
        return n > n2 ? new WinningTeamData(gameTeam, null) : new WinningTeamData(gameTeam2, null);
    }

    @Override
    protected boolean method_2771(@NotNull ServerPlayer serverPlayer) {
        return true;
    }

    @Override
    public void method_2753(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull GameTeam gameTeam) {
        BFUtils.teleportPlayer(playerDataHandler, serverPlayer, gameTeam.randomSpawn(this.game));
    }

    @Override
    public void method_2791(@NotNull ServerPlayer serverPlayer) {
    }

    @Override
    public void specificTickPlayer(@NotNull ServerPlayer serverPlayer, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
    }

    @Override
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @CheckForNull @Nullable ServerPlayer serverPlayer2, @CheckForNull @Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
        if (serverPlayer2 == null || uUID2 == null) {
            return;
        }
        for (ItemStack itemStack : serverPlayer2.getInventory().items) {
            if (itemStack.isEmpty()) continue;
            BFUtils.method_2967(itemStack);
        }
        if (((TeamDeathmatchGame)this.game).getStatus() != GameStatus.GAME || serverPlayer.equals((Object)serverPlayer2)) {
            return;
        }
        GameTeam gameTeam = this.getPlayerTeam(uUID2);
        if (gameTeam != null) {
            int n = gameTeam.getObjectInt(BFStats.SCORE);
            gameTeam.setObject(BFStats.SCORE, n + 1);
        }
        BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID2, BFStats.SCORE);
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
        BFUtils.method_2950(leve, player, this.game, gameTeam);
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
}

