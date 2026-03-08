/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  it.unimi.dsi.fastutil.objects.ObjectSortedSet
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
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.net.packet.BFGrenadeFlashPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.ttt.TTTDeadBody;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerTeam;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class TroubleTownPlayerManager
extends AbstractGamePlayerManager<TroubleTownGame> {
    public static final Loadout DETECTIVE_LOADOUT = new Loadout(null, null, null, null, null, null, null, null).addExtra(new ItemStack((ItemLike)BFItems.MAGNIFYING_GLASS.get()));
    public static final Component field_3476 = Component.translatable((String)"bf.message.gamemode.ttt.random.kill").withStyle(ChatFormatting.RED);
    public static final Component field_3477 = Component.translatable((String)"bf.message.gamemode.ttt.jester.death.direct").withColor(TTTPlayerRole.JESTER.getSubColor());
    public final List<FDSPose> playerSpawns = new ObjectArrayList();

    public TroubleTownPlayerManager(@NotNull TroubleTownGame troubleTownGame, @NotNull PlayerDataHandler<?> playerDataHandler) {
        super(troubleTownGame, playerDataHandler);
        Style style = Style.EMPTY.withColor(TextColor.fromRgb((int)8159560));
        Style style2 = Style.EMPTY.withColor(TextColor.fromRgb((int)15067868));
        this.addTeam(new GameTeam(troubleTownGame, "Allies", style, style2, 16));
    }

    @Override
    @NotNull
    public WinningTeamData getWinningTeam(@NotNull ServerLevel level, @NotNull Set<UUID> players, @Nullable GameStageTimer timer) {
        ObjectSortedSet<UUID> objectSortedSet = BFUtils.topPlayers(this.game, 3, BFStats.SCORE, players);
        if (objectSortedSet.isEmpty()) {
            return new WinningTeamData(null, null);
        }
        return new WinningTeamData(null, Collections.singleton((UUID)objectSortedSet.getFirst()));
    }

    @Override
    protected boolean method_2771(@NotNull ServerPlayer serverPlayer) {
        return false;
    }

    @Override
    public void method_2753(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull GameTeam gameTeam) {
        BFUtils.teleportPlayer(playerDataHandler, serverPlayer, this.method_3541());
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
    public void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @CheckForNull @Nullable ServerPlayer serverPlayer2, @Nullable UUID uUID2, @NotNull DamageSource damageSource, @NotNull Set<UUID> set) {
        String string = serverPlayer.getScoreboardName();
        ItemStack itemStack = null;
        if (serverPlayer2 != null && uUID2 != null) {
            Object object;
            MutableComponent mutableComponent;
            MutableComponent mutableComponent2;
            MutableComponent mutableComponent3;
            String string2 = serverPlayer2.getScoreboardName();
            TTTPlayerRole tTTPlayerRole = ((TroubleTownGame)this.game).getPlayerRole(uUID);
            TTTPlayerRole tTTPlayerRole2 = ((TroubleTownGame)this.game).getPlayerRole(uUID2);
            if (tTTPlayerRole.getTeam() == tTTPlayerRole2.getTeam() && !string2.equals(string)) {
                PacketUtils.sendToPlayer(new BFGrenadeFlashPacket(80.0f), serverPlayer2);
            }
            if (tTTPlayerRole == TTTPlayerRole.JESTER && tTTPlayerRole2.getTeam() == TTTPlayerTeam.GOOD) {
                BFUtils.sendNoticeMessage(serverPlayer2, field_3476);
                this.method_3540(bFAbstractManager, serverPlayer, set, string, serverPlayer2, string2);
                return;
            }
            if (tTTPlayerRole == TTTPlayerRole.INNOCENT && tTTPlayerRole2.getTeam() == TTTPlayerTeam.BAD) {
                BFUtils.playSound(uUID, (SoundEvent)BFSounds.MATCH_GAMEMODE_TTT_DEATH_TRAITOR.get(), SoundSource.MASTER);
            }
            Object object2 = new ObjectOpenHashSet();
            int n = 0;
            if (tTTPlayerRole == TTTPlayerRole.TRAITOR) {
                object2 = ((TroubleTownGame)this.game).method_3516(TTTPlayerRole.DETECTIVE, set);
                n = 1;
            } else if (tTTPlayerRole == TTTPlayerRole.DETECTIVE) {
                object2 = ((TroubleTownGame)this.game).method_3516(TTTPlayerRole.TRAITOR, set);
                n = 2;
            } else if (tTTPlayerRole == TTTPlayerRole.INNOCENT) {
                object2 = ((TroubleTownGame)this.game).method_3516(TTTPlayerRole.TRAITOR, set);
                n = 1;
            }
            if (n > 0) {
                mutableComponent3 = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
                mutableComponent2 = Component.translatable((String)"bf.message.gamemode.ttt.points.gain", (Object[])new Object[]{mutableComponent3}).withStyle(ChatFormatting.GRAY);
                mutableComponent = object2.iterator();
                while (mutableComponent.hasNext()) {
                    object = (UUID)mutableComponent.next();
                    BFUtils.triggerPlayerStat(bFAbstractManager, this.game, (UUID)object, BFStats.POINTS, n);
                    BFUtils.triggerPlayerStat(bFAbstractManager, this.game, (UUID)object, BFStats.SCORE, n);
                    BFUtils.sendNoticeMessage((UUID)object, (Component)mutableComponent2);
                }
            }
            mutableComponent3 = Component.literal((String)string2).withColor(0xFFFFFF);
            mutableComponent2 = Component.literal((String)tTTPlayerRole2.getKey()).withColor(TextColor.fromRgb((int)tTTPlayerRole2.getColor()).getValue());
            mutableComponent = Component.translatable((String)"bf.message.gamemode.ttt.killed", (Object[])new Object[]{mutableComponent3, mutableComponent2}).withStyle(ChatFormatting.GRAY);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent);
            object = this.getPlayerTeam(uUID2);
            itemStack = serverPlayer2.getMainHandItem();
            if (((TroubleTownGame)this.game).getStatus() == GameStatus.GAME && !string2.equalsIgnoreCase(string)) {
                int n2 = ((GameTeam)object).getObjectInt(BFStats.SCORE);
                ((GameTeam)object).setObject(BFStats.SCORE, n2 + 1);
            }
        }
        ((TroubleTownGame)this.game).field_3468.add((Object)new TTTDeadBody(string, uUID, itemStack != null && !itemStack.isEmpty() ? itemStack.getDisplayName().getString() : "", serverPlayer.getX(), serverPlayer.getY() + 0.5, serverPlayer.getZ(), 0.0f, 0.0f));
    }

    private void method_3540(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerPlayer serverPlayer, @NotNull Set<UUID> set, @NotNull String string, @NotNull ServerPlayer serverPlayer2, @NotNull String string2) {
        serverPlayer2.kill();
        UUID uUID = serverPlayer.getUUID();
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.ttt.jester.death", (Object[])new Object[]{string2, string}).withColor(TTTPlayerRole.JESTER.getColor());
        BFUtils.sendNoticeMessage(set, (Component)mutableComponent);
        serverPlayer.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 5.0f, 1.0f);
        serverPlayer.playSound(SoundEvents.FIREWORK_ROCKET_TWINKLE, 5.0f, 1.5f);
        serverPlayer.playSound((SoundEvent)BFSounds.MATCH_MEMES_HAHA.get(), 5.0f, 1.5f);
        int n = 4;
        BFUtils.triggerPlayerStat(bFAbstractManager, this.game, uUID, BFStats.POINTS, 4);
        BFUtils.triggerPlayerStat(bFAbstractManager, this.game, uUID, BFStats.SCORE, 4);
        BFUtils.sendNoticeMessage(serverPlayer, field_3477);
    }

    @Override
    public void initPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel leve, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        UUID uUID;
        ServerPlayer serverPlayer;
        BFUtils.initPlayerForGame(dataHandler, leve, player);
        player.setGameMode(GameType.SPECTATOR);
        FDSPose fDSPose = this.lobbySpawn;
        Set<UUID> set = this.method_2795();
        if (!set.isEmpty() && (serverPlayer = BFUtils.getPlayerByUUID(uUID = RandomUtils.randomFromSet(this.method_2795()))) != null) {
            fDSPose = new FDSPose((Player)serverPlayer);
        }
        BFUtils.teleportPlayer(dataHandler, player, fDSPose);
        BFUtils.method_2979(dataHandler, player);
    }

    @Override
    public boolean method_2778(@NotNull Player player, @NotNull UUID uUID, @NotNull DamageSource damageSource, Entity entity) {
        ServerPlayer serverPlayer;
        TTTPlayerRole tTTPlayerRole = ((TroubleTownGame)this.game).getPlayerRole(player.getUUID());
        if (tTTPlayerRole == TTTPlayerRole.PENDING) {
            return false;
        }
        Object object = damageSource.getEntity();
        if (object instanceof ServerPlayer && (object = ((TroubleTownGame)this.game).getPlayerRole((serverPlayer = (ServerPlayer)object).getUUID())) == TTTPlayerRole.JESTER) {
            return false;
        }
        return ((TroubleTownGame)this.game).getStatus() == GameStatus.GAME;
    }

    @Override
    public boolean method_2775(@NotNull LivingEntity livingEntity, @NotNull DamageSource damageSource) {
        return true;
    }

    @Override
    public boolean canPlayerDropItem(@NotNull Player player, @NotNull ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean method_2779(@NotNull Player player, @NotNull ItemEntity itemEntity, @NotNull ItemStack itemStack) {
        if (((TroubleTownGame)this.game).getStatus() != GameStatus.GAME) {
            return false;
        }
        UUID uUID = player.getUUID();
        Inventory inventory = player.getInventory();
        if (itemStack.getItem() instanceof GunItem) {
            if (((ItemStack)inventory.items.getFirst()).isEmpty() || ((ItemStack)inventory.items.get(1)).isEmpty()) {
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

    public FDSPose method_3541() {
        return RandomUtils.randomFromList(this.playerSpawns);
    }

    public void method_3539(@NotNull PlayerDataHandler<?> playerDataHandler) {
        for (UUID uUID : this.getPlayerUUIDs()) {
            BFUtils.teleportPlayerAndSync(playerDataHandler, uUID, this.method_3541());
        }
    }
}

