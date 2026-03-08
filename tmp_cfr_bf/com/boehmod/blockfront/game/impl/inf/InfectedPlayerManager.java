/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.inf.InfectedDoor;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.ZombieZone;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.CheckForNull;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InfectedPlayerManager
extends AbstractGamePlayerManager<InfectedGame> {
    private static final int field_3547 = 5;
    private static final int field_3548 = 600;
    private static final int field_3549 = 6;
    @NotNull
    public static final String field_3543 = "Survivors";
    @NotNull
    public static final Loadout LOADOUT = new Loadout(null, new ItemStack((ItemLike)BFItems.GUN_COLT.get()), new ItemStack((ItemLike)BFItems.MELEE_ITEM_KNIFE_M1905.get()), null, null, null, null, null);
    @NotNull
    private final List<FDSPose> field_3545 = new ObjectArrayList();
    @NotNull
    private final Set<UUID> field_3546 = new ObjectOpenHashSet();

    public InfectedPlayerManager(@NotNull InfectedGame infectedGame, @NotNull PlayerDataHandler<?> playerDataHandler) {
        super(infectedGame, playerDataHandler);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)7101608));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)14407935));
        this.addTeam(new GameTeam(infectedGame, field_3543, style, style2, 6));
    }

    @NotNull
    public List<FDSPose> method_3676() {
        return Collections.unmodifiableList(this.field_3545);
    }

    public void method_3681(FDSPose fDSPose) {
        this.field_3545.add(fDSPose);
    }

    public void method_3680() {
        this.field_3545.clear();
    }

    private void method_3679(Level level, InfectedDoor infectedDoor3, UUID uUID, String string, Set<UUID> set) {
        int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.POINTS);
        if (n >= infectedDoor3.getCost()) {
            MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.GRAY);
            BFUtils.decreasePlayerStat(this.game, uUID, BFStats.POINTS, infectedDoor3.getCost());
            BFUtils.sendNoticeMessage(set, (Component)Component.translatable((String)"bf.message.gamemode.infected.door.open", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.DARK_RED));
            for (ZombieZone zombieZone : ((InfectedGame)this.game).zombieZones) {
                String string2 = zombieZone.getId();
                if (!infectedDoor3.getZones().contains(string2) || zombieZone.isActive()) continue;
                MutableComponent mutableComponent2 = Component.literal((String)string2).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.infected.zone.discover", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.DARK_RED);
                BFUtils.sendNoticeMessage(set, (Component)mutableComponent3);
                zombieZone.setActive(true);
            }
            infectedDoor3.method_3697(level, true);
            ((InfectedGame)this.game).doors.stream().filter(infectedDoor2 -> infectedDoor2.getId().equals(infectedDoor3.getId())).forEach(infectedDoor -> infectedDoor.method_3697(level, true));
        }
    }

    @Override
    @javax.annotation.Nullable
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        if (((InfectedGame)this.game).currentRound >= 30) {
            return new WinningTeamData(null, players);
        }
        if (players.isEmpty() || ((InfectedGame)this.game).method_3638(players).isEmpty()) {
            return new WinningTeamData(null, null);
        }
        return null;
    }

    @Override
    protected boolean method_2771(@NotNull ServerPlayer serverPlayer) {
        return false;
    }

    @Override
    public void method_2753(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull GameTeam gameTeam) {
        FDSPose fDSPose = RandomUtils.randomFromList(this.field_3545);
        BFUtils.giveLoadout(serverLevel, serverPlayer, LOADOUT);
        BFUtils.teleportPlayer(playerDataHandler, serverPlayer, fDSPose);
        BFUtils.setPlayerStat(this.game, uUID, BFStats.CHAR, ((InfectedGame)this.game).method_3647((Player)serverPlayer));
        if (((InfectedGame)this.game).currentRound >= 5 && !this.field_3546.contains(uUID)) {
            MutableComponent mutableComponent = Component.literal((String)"$600").withStyle(ChatFormatting.GREEN);
            MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(5)).withColor(0xFFFFFF);
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.infected.reimbursed", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.DARK_RED);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent3);
            BFUtils.setPlayerStat(this.game, uUID, BFStats.POINTS, 600);
        }
        this.field_3546.add(uUID);
    }

    @Override
    public boolean canInteractWithBlock(@NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Player player, @NotNull UUID uuid, @NotNull Block block, @NotNull BlockPos blockPos) {
        Object obj = dataHandler.getPlayerData(player);
        if (BFUtils.isPlayerUnavailable(player, obj)) {
            return false;
        }
        String string = player.getScoreboardName();
        if (block == Blocks.IRON_DOOR) {
            Set<UUID> set = this.getPlayerUUIDs();
            for (InfectedDoor infectedDoor : ((InfectedGame)this.game).doors) {
                if (!infectedDoor.canOpenDoor(blockPos) || infectedDoor.isOpen()) continue;
                this.method_3679(level, infectedDoor, uuid, string, set);
            }
        }
        return super.canInteractWithBlock(dataHandler, level, player, uuid, block, blockPos);
    }

    @Override
    public GameTeam getNextJoiningTeam() {
        return this.getTeamByName(field_3543);
    }

    @Override
    public void method_2791(@NotNull ServerPlayer serverPlayer) {
    }

    @Override
    public void specificTickPlayer(@NotNull ServerPlayer serverPlayer, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
    }

    @Override
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @CheckForNull @javax.annotation.Nullable ServerPlayer serverPlayer2, @javax.annotation.Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
        serverLevel.playSound(null, (Entity)serverPlayer, SoundEvents.WITHER_HURT, SoundSource.AMBIENT, 10.0f, 0.5f);
    }

    @Override
    public void initPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel leve, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        BFUtils.initPlayerForGame(dataHandler, leve, player);
        BFUtils.giveLoadout(leve, player, LOADOUT);
        FDSPose fDSPose = RandomUtils.randomFromList(this.field_3545);
        BFUtils.teleportPlayer(dataHandler, player, fDSPose);
        BFUtils.method_2999(player);
    }

    @Override
    public boolean method_2778(@NotNull Player player, @NotNull UUID uUID, @NotNull DamageSource damageSource, Entity entity) {
        return !(entity instanceof Player);
    }

    @Override
    public boolean method_2775(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource) {
        return !(livingEntity instanceof VendorEntity);
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
}

