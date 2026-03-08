/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
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
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnErrorPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.unnamed.BF_797;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.List;
import java.util.Locale;
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
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ConquestPlayerManager
extends AbstractGamePlayerManager<ConquestGame> {
    private static final Component field_6366 = Component.translatable((String)"bf.popup.message.cpoint.defended").withStyle(ChatFormatting.GOLD);
    private static final int field_6722 = 200;

    public ConquestPlayerManager(@NotNull ConquestGame conquestGame, @NotNull PlayerDataHandler<?> playerDataHandler) {
        super(conquestGame, playerDataHandler);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)15067868));
        Style style3 = Style.EMPTY.withColor(TextColor.fromRgb((int)8271921));
        Style style4 = Style.EMPTY.withColor(TextColor.fromRgb((int)14996948));
        this.addTeam(new GameTeam(conquestGame, "Allies", style, style2, 32));
        this.addTeam(new GameTeam(conquestGame, "Axis", style3, style4, 32));
    }

    public void method_3304(@NotNull ServerPlayer serverPlayer, int n) {
        List<ConquestCapturePoint> list = ((ConquestGame)this.game).getCapturePoints();
        if (n < 0 || n >= list.size()) {
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.conquest.capture.point.error.invalid", (Object[])new Object[]{Component.literal((String)String.valueOf(n))}).withStyle(ChatFormatting.RED);
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket((Component)mutableComponent), serverPlayer);
            return;
        }
        ConquestCapturePoint conquestCapturePoint = list.get(n);
        if (!(conquestCapturePoint instanceof ConquestCapturePoint)) {
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.conquest.capture.point.error.invalid", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket((Component)mutableComponent2), serverPlayer);
            return;
        }
        ConquestCapturePoint conquestCapturePoint2 = conquestCapturePoint;
        if (conquestCapturePoint.name.equalsIgnoreCase("HQ")) {
            MutableComponent mutableComponent = Component.literal((String)"HQ").withColor(0xFFFFFF);
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.conquest.capture.point.selected", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GREEN);
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket((Component)mutableComponent3), serverPlayer);
            BFUtils.setPlayerStat(this.game, serverPlayer.getUUID(), BFStats.CAPTURE_POINT_SPAWN, -1);
            return;
        }
        Component component = this.method_3303(serverPlayer.getUUID(), conquestCapturePoint2);
        if (component != null) {
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket(component), serverPlayer);
            return;
        }
        MutableComponent mutableComponent = Component.literal((String)conquestCapturePoint.name.toUpperCase(Locale.ROOT)).withColor(0xFFFFFF);
        MutableComponent mutableComponent4 = Component.translatable((String)"bf.message.gamemode.conquest.capture.point.selected", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GREEN);
        PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket((Component)mutableComponent4), serverPlayer);
        BFUtils.setPlayerStat(this.game, serverPlayer.getUUID(), BFStats.CAPTURE_POINT_SPAWN, n);
    }

    @javax.annotation.Nullable
    public FDSPose method_3302(@NotNull UUID uUID) {
        GameTeam gameTeam = this.getPlayerTeam(uUID);
        if (gameTeam == null) {
            return null;
        }
        FDSPose fDSPose = gameTeam.randomSpawn(this.game);
        int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.CAPTURE_POINT_SPAWN, -1);
        List<ConquestCapturePoint> list = ((ConquestGame)this.game).getCapturePoints();
        if (n == -1 || n >= list.size()) {
            return fDSPose;
        }
        ConquestCapturePoint conquestCapturePoint = list.get(n);
        Component component = this.method_3303(uUID, conquestCapturePoint);
        if (component != null) {
            BFUtils.sendNoticeMessage(uUID, component);
            BFUtils.setPlayerStat(this.game, uUID, BFStats.CAPTURE_POINT_SPAWN, -1);
            return fDSPose;
        }
        Vec3 vec3 = conquestCapturePoint.method_3293();
        if (vec3 != null) {
            return new FDSPose(vec3, 0.0f, 0.0f);
        }
        return fDSPose;
    }

    @javax.annotation.Nullable
    public Component method_3303(@NotNull UUID uUID, @NotNull ConquestCapturePoint conquestCapturePoint) {
        if (conquestCapturePoint.method_3290()) {
            MutableComponent mutableComponent = Component.literal((String)conquestCapturePoint.name.toUpperCase(Locale.ROOT)).withColor(0xFFFFFF);
            return Component.translatable((String)"bf.message.gamemode.conquest.capture.point.error.overrun", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
        }
        GameTeam gameTeam = conquestCapturePoint.getCbTeam();
        if (gameTeam == null) {
            MutableComponent mutableComponent = Component.literal((String)conquestCapturePoint.name.toUpperCase(Locale.ROOT)).withColor(0xFFFFFF);
            return Component.translatable((String)"bf.message.gamemode.conquest.capture.point.error.not.captured.neutral", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
        }
        GameTeam gameTeam2 = this.getPlayerTeam(uUID);
        if (gameTeam2 != null && !gameTeam2.equals(gameTeam)) {
            MutableComponent mutableComponent = Component.literal((String)conquestCapturePoint.name.toUpperCase(Locale.ROOT)).withColor(0xFFFFFF);
            return Component.translatable((String)"bf.message.gamemode.conquest.capture.point.error.not.captured", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
        }
        return null;
    }

    @Override
    public int method_2770(@NotNull ServerPlayer serverPlayer) {
        return 200;
    }

    @Override
    @javax.annotation.Nullable
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        GameTeam gameTeam = this.getTeamByName("Axis");
        GameTeam gameTeam2 = this.getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        int n = gameTeam.getObjectInt(BF_797.field_3381);
        if (n <= 0) {
            return new WinningTeamData(gameTeam2, null);
        }
        int n2 = gameTeam2.getObjectInt(BF_797.field_3381);
        if (n2 <= 0) {
            return new WinningTeamData(gameTeam, null);
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
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @CheckForNull @javax.annotation.Nullable ServerPlayer serverPlayer2, @javax.annotation.Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
        if (serverPlayer2 == null || uUID2 == null) {
            return;
        }
        GameTeam gameTeam = this.getPlayerTeam(uUID2);
        if (gameTeam == null) {
            return;
        }
        if (((ConquestGame)this.game).getStatus() == GameStatus.GAME && !serverPlayer2.getScoreboardName().equalsIgnoreCase(serverPlayer.getScoreboardName())) {
            BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID2, BFStats.SCORE);
            ((ConquestGame)this.game).getCapturePoints().stream().filter(conquestCapturePoint -> conquestCapturePoint.isInRadius((Entity)serverPlayer, ((ConquestGame)this.game).method_3398((ConquestCapturePoint)conquestCapturePoint)) && gameTeam.equals(conquestCapturePoint.getCbTeam())).forEach(conquestCapturePoint -> {
                BFUtils.sendPopupMessage(serverPlayer2, new BFPopup(field_6366, 40));
                BFUtils.triggerPlayerStat(bFAbstractManager, this.game, uUID2, BFStats.SCORE, 2);
            });
        }
    }

    @Override
    public void initPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel leve, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        BFUtils.initPlayerForGame(dataHandler, leve, player);
        GameTeam gameTeam = this.getPlayerTeam(uuid);
        if (gameTeam == null) {
            return;
        }
        FDSPose fDSPose = this.method_3302(uuid);
        if (fDSPose != null) {
            BFUtils.teleportPlayer(dataHandler, player, fDSPose);
        }
        BFUtils.method_2999(player);
        BFUtils.method_2950(leve, player, this.game, gameTeam);
    }

    @Override
    public boolean method_2778(@NotNull Player player, @NotNull UUID uUID, @NotNull DamageSource damageSource, Entity entity) {
        if (entity instanceof Player) {
            Player player2 = (Player)entity;
            UUID uUID2 = player2.getUUID();
            if (uUID.equals(uUID2)) {
                return damageSource.is(DamageTypeTags.IS_EXPLOSION) || damageSource.is(DamageTypeTags.IS_FIRE);
            }
            GameTeam gameTeam = this.getPlayerTeam(uUID);
            return gameTeam == null || !gameTeam.getPlayers().contains(uUID2);
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
}

