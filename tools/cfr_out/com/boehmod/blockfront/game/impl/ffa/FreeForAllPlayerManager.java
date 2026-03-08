/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
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
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game.impl.ffa;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGame;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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
import org.jetbrains.annotations.Nullable;

public final class FreeForAllPlayerManager
extends AbstractGamePlayerManager<FreeForAllGame> {
    private static final int field_6721 = 5;
    private static final int field_6992 = 10;
    @NotNull
    private final List<FDSPose> field_3492 = new ObjectArrayList();

    public FreeForAllPlayerManager(@NotNull FreeForAllGame freeForAllGame, @NotNull PlayerDataHandler<?> playerDataHandler) {
        super(freeForAllGame, playerDataHandler);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)15067868));
        this.addTeam(new GameTeam(freeForAllGame, "Allies", style, style2, 10));
    }

    @Override
    @javax.annotation.Nullable
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        for (UUID uUID : players) {
            int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.KILLS);
            if (n < 30) continue;
            return new WinningTeamData(null, Collections.singleton(uUID));
        }
        if (timer != null && !timer.isDone()) {
            return null;
        }
        Iterator<UUID> iterator = BFUtils.topPlayers(this.game, 5, BFStats.KILLS, players);
        if (iterator.isEmpty()) {
            return new WinningTeamData(null, null);
        }
        return new WinningTeamData(null, Collections.singleton((UUID)iterator.getFirst()));
    }

    @NotNull
    public List<FDSPose> method_3566() {
        return Collections.unmodifiableList(this.field_3492);
    }

    public void method_3571(@NotNull FDSPose fDSPose) {
        this.field_3492.add(fDSPose);
    }

    public void method_3570() {
        this.field_3492.clear();
    }

    @Override
    protected boolean method_2771(@NotNull ServerPlayer serverPlayer) {
        return true;
    }

    @Override
    public void method_2753(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull GameTeam gameTeam) {
        BFUtils.teleportPlayer(playerDataHandler, serverPlayer, this.method_3569(serverLevel));
    }

    @Override
    public GameTeam getNextJoiningTeam() {
        return this.getTeamByName("Allies");
    }

    @Override
    public void method_2791(@NotNull ServerPlayer serverPlayer) {
    }

    @Override
    public void specificTickPlayer(@NotNull ServerPlayer serverPlayer, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
    }

    @Override
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @javax.annotation.Nullable ServerPlayer serverPlayer2, @javax.annotation.Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
        if (serverPlayer2 == null || uUID2 == null || ((FreeForAllGame)this.game).getStatus() != GameStatus.GAME) {
            return;
        }
        GameTeam gameTeam = this.getPlayerTeam(uUID2);
        if (gameTeam != null && !serverPlayer2.getScoreboardName().equalsIgnoreCase(serverPlayer.getScoreboardName())) {
            int n = gameTeam.getObjectInt(BFStats.SCORE);
            gameTeam.setObject(BFStats.SCORE, n + 1);
        }
        for (ItemStack itemStack : serverPlayer2.getInventory().items) {
            BFUtils.method_2967(itemStack);
        }
        serverPlayer2.setHealth(serverPlayer2.getHealth() + 5.0f);
    }

    @Override
    public void initPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel leve, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        BFUtils.initPlayerForGame(dataHandler, leve, player);
        GameTeam gameTeam = this.getPlayerTeam(uuid);
        BFUtils.teleportPlayer(dataHandler, player, this.method_3569(leve));
        BFUtils.method_2999(player);
        if (gameTeam != null) {
            BFUtils.method_2950(leve, player, this.game, gameTeam);
        }
    }

    @Override
    public boolean method_2778(@NotNull Player player, @NotNull UUID uUID, @NotNull DamageSource damageSource, Entity entity) {
        if (entity instanceof Player) {
            Player player2 = (Player)entity;
            if (player.getScoreboardName().equalsIgnoreCase(player2.getScoreboardName())) {
                return damageSource.is(DamageTypeTags.IS_EXPLOSION) || damageSource.is(DamageTypeTags.IS_FIRE);
            }
        }
        return true;
    }

    @Override
    public boolean method_2775(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource) {
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

    public FDSPose method_3569(@NotNull ServerLevel serverLevel) {
        FDSPose fDSPose = null;
        int n = 50;
        ObjectArrayList objectArrayList = new ObjectArrayList(this.field_3492);
        Collections.shuffle(objectArrayList);
        while (fDSPose == null) {
            for (FDSPose fDSPose2 : objectArrayList) {
                if (fDSPose2.getNearestPlayer((Level)serverLevel, n) != null) continue;
                fDSPose = fDSPose2.copy();
            }
            if (fDSPose != null) continue;
            if (n > 0) {
                n -= 5;
                continue;
            }
            fDSPose = (FDSPose)objectArrayList.get(serverLevel.getRandom().nextInt(objectArrayList.size()));
        }
        return fDSPose;
    }

    public FDSPose method_3568(Random random) {
        return this.field_3492.get(random.nextInt(this.field_3492.size()));
    }

    public void method_3567(@NotNull PlayerDataHandler<?> playerDataHandler) {
        Set<UUID> set = this.getPlayerUUIDs();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (UUID uUID : set) {
            BFUtils.teleportPlayerAndSync(playerDataHandler, uUID, this.method_3568(threadLocalRandom));
        }
    }
}

