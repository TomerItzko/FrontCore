/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.ChatGraphic
 *  com.boehmod.bflib.cloud.common.player.PunishmentType
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.mm.PacketMMPlayerLeft
 *  com.boehmod.bflib.common.ColorReferences
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.ButtonBlock
 *  net.minecraft.world.level.block.DoorBlock
 *  net.minecraft.world.level.block.FenceGateBlock
 *  net.minecraft.world.level.block.LeverBlock
 *  net.minecraft.world.level.block.TrapDoorBlock
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.common.ChatGraphic;
import com.boehmod.bflib.cloud.common.player.PunishmentType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMPlayerLeft;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.BFButtonBlock;
import com.boehmod.blockfront.common.block.CrateGunBlock;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnErrorPacket;
import com.boehmod.blockfront.common.net.packet.BFGamePacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameMatchStreakTracker;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGamePlayerManager<G extends AbstractGame<G, ?, ?>> {
    private static final int field_2917 = 2400;
    private static final int field_2918 = 600;
    private static final int field_6714 = 120;
    @NotNull
    private final GameMatchStreakTracker matchStreakTracker = new GameMatchStreakTracker();
    @NotNull
    protected final G game;
    @NotNull
    private final List<GameTeam> teams = new ObjectArrayList();
    @javax.annotation.Nullable
    protected FDSPose lobbySpawn = null;
    @NotNull
    private final PlayerDataHandler<?> playerData;

    public AbstractGamePlayerManager(@NotNull G game, @NotNull PlayerDataHandler<?> playerData) {
        this.game = game;
        this.playerData = playerData;
    }

    public void method_5993(@Nonnull Set<UUID> set) {
        UUID uUID = this.matchStreakTracker.getStreakLeader();
        if (uUID != null && !set.contains(uUID)) {
            this.matchStreakTracker.removePlayerStreak(uUID);
        }
        for (GameTeam gameTeam : this.teams) {
            gameTeam.onUpdate();
        }
    }

    public boolean changePlayerClass(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull UUID uuid, @NotNull MatchClass matchClass, int classLevel) {
        String string;
        GameTeam gameTeam = this.getPlayerTeam(uuid);
        if (gameTeam == null) {
            return false;
        }
        DivisionData divisionData = gameTeam.getDivisionData((AbstractGame<?, ?, ?>)this.game);
        Loadout loadout = divisionData.getLoadout(matchClass, classLevel);
        if (loadout == null) {
            return false;
        }
        MutableComponent mutableComponent = this.checkClassChangeIsValid(manager, uuid, player, gameTeam, matchClass, loadout);
        if (mutableComponent != null) {
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket((Component)mutableComponent), player);
            return false;
        }
        FDSTagCompound fDSTagCompound = ((AbstractGame)this.game).getPlayerStatData(uuid);
        boolean bl = fDSTagCompound.getInteger(string = BFStats.CLASS.getKey(), -1) == -1;
        fDSTagCompound.setInteger(string, matchClass.ordinal());
        fDSTagCompound.setInteger(BFStats.CLASS_INDEX.getKey(), classLevel);
        if (bl || this.method_2777(player, uuid, 4.0) && !BFUtils.playerHasUsedGuns(player)) {
            MutableComponent mutableComponent2;
            fDSTagCompound.setInteger(BFStats.CLASS_ALIVE.getKey(), matchClass.ordinal());
            BFUtils.giveLoadout(level, player, loadout);
            BFUtils.playSound((Level)level, player.position(), (SoundEvent)BFSounds.MATCH_CLASSES_CHANGE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            MutableComponent mutableComponent3 = Component.translatable((String)matchClass.getDisplayTitle()).withStyle(ChatFormatting.GRAY);
            if (classLevel > 0) {
                mutableComponent2 = Component.translatable((String)("enchantment.level." + (classLevel + 1))).withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
                mutableComponent3 = mutableComponent3.append(" ").append((Component)mutableComponent2);
            }
            if (!bl) {
                mutableComponent2 = Component.translatable((String)"bf.message.gamemode.class", (Object[])new Object[]{mutableComponent3}).withColor(0xFFFFFF);
                BFUtils.sendNoticeMessage(player, (Component)mutableComponent2);
            }
        } else {
            MutableComponent mutableComponent4 = Component.translatable((String)matchClass.getDisplayTitle()).withStyle(ChatFormatting.GRAY);
            MutableComponent mutableComponent5 = Component.translatable((String)"bf.message.gamemode.class.respawn", (Object[])new Object[]{mutableComponent4}).withColor(0xFFFFFF);
            BFUtils.sendNoticeMessage(player, (Component)mutableComponent5);
        }
        return true;
    }

    @javax.annotation.Nullable
    public MutableComponent checkClassChangeIsValid(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull UUID uuid, @NotNull ServerPlayer player, @NotNull GameTeam team, @NotNull MatchClass matchClass, @NotNull Loadout loadout) {
        IHasClasses iHasClasses;
        Object object;
        MatchClass matchClass2 = null;
        int n = ((AbstractGame)this.game).getPlayerStatData(uuid).getInteger(BFStats.CLASS.getKey(), -1);
        if (n != -1) {
            matchClass2 = MatchClass.values()[n];
        }
        if ((object = this.game) instanceof IHasClasses && (iHasClasses = (IHasClasses)object).method_3401()) {
            boolean bl;
            int n2 = iHasClasses.method_3400(matchClass);
            boolean bl2 = bl = BFUtils.method_2899(this.game, matchClass, team) >= n2 && n2 > 0;
            if (bl && matchClass != matchClass2) {
                MutableComponent mutableComponent = Component.literal((String)String.valueOf(n2)).withStyle(ChatFormatting.GRAY);
                return Component.translatable((String)"bf.message.gamemode.class.error.full", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
            }
        }
        if (manager.isMatchMakingEnabled() && !player.isCreative()) {
            boolean bl;
            G g = this.game;
            boolean bl3 = bl = g instanceof IHasClasses && (object = (IHasClasses)g).method_3403();
            if (bl && !matchClass.canPlayerUse(this.playerData, (Player)player)) {
                object = Component.literal((String)matchClass.getMinRankRequired().getTitle()).withStyle(ChatFormatting.GRAY);
                return Component.translatable((String)"bf.message.gamemode.class.error.rank", (Object[])new Object[]{object}).withStyle(ChatFormatting.RED);
            }
        }
        if (manager.isMatchMakingEnabled()) {
            G g = this.game;
            boolean bl = g instanceof IHasClasses && (object = (IHasClasses)g).method_3402();
            object = this.playerData.getCloudProfile((Player)player);
            int n3 = object.getExpForClass(matchClass.ordinal());
            int n4 = loadout.getMinimumXp();
            if (bl && n3 < n4) {
                MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(n4)).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(n3)).withStyle(ChatFormatting.GRAY);
                return Component.translatable((String)"bf.message.gamemode.class.error.exp", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.RED);
            }
        }
        return null;
    }

    public void initPlayerForGame(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull GameTeam team) {
        UUID uUID = player.getUUID();
        Object obj = this.playerData.getPlayerData((Player)player);
        ((BFAbstractPlayerData)obj).reset();
        player.tickCount = 0;
        player.getInventory().clearContent();
        player.setGameMode(GameType.ADVENTURE);
        this.matchStreakTracker.addPlayerStreak(uUID);
        BFUtils.initPlayerForGame(this.playerData, level, player);
        AbstractGameStage abstractGameStage = ((AbstractGame)this.game).stageManager.getCurrentStage();
        abstractGameStage.onPlayerJoin(this.playerData, manager, joinType, level, player, this.game, uUID);
        if (!this.method_2771(player) && ((AbstractGame)this.game).getStatus() == GameStatus.GAME) {
            player.setGameMode(GameType.SPECTATOR);
            ((BFAbstractPlayerData)obj).setOutOfGame(true);
            ((BFAbstractPlayerData)obj).setRespawnTimer(this.method_2770(player));
        } else {
            G g = this.game;
            if (g instanceof IAllowsRespawning) {
                IAllowsRespawning iAllowsRespawning = (IAllowsRespawning)g;
                BFUtils.setPlayerStat(this.game, uUID, BFStats.SPAWN_PRO, iAllowsRespawning.getSpawnProLength());
            }
        }
        this.method_2753(manager, this.playerData, level, player, uUID, team);
        ((BFAbstractPlayerData)obj).method_839(manager, (Level)level, true);
    }

    public int method_2770(@NotNull ServerPlayer serverPlayer) {
        return 120;
    }

    public void removePlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerPlayer player) {
        Object obj = manager.getPlayerDataHandler();
        Object d = ((PlayerDataHandler)obj).getPlayerData((Player)player);
        UUID uUID = player.getUUID();
        ((BFAbstractPlayerData)d).reset();
        ((AbstractGame)this.game).removePlayerStatData(uUID);
        FDSPose fDSPose = this.getLobbySpawn();
        if (fDSPose != null) {
            BFUtils.teleportPlayer(this.playerData, player, fDSPose);
        }
        player.getInventory().clearContent();
        FDSTagCompound fDSTagCompound = ((AbstractGame)this.game).getPlayerStatData(uUID);
        fDSTagCompound.setInteger(BFStats.CLASS.getKey(), -1);
        if (fDSTagCompound.getInteger(BFStats.PLAY_TIME.getKey()) > 600 && manager.isCloudConnectionVerified()) {
            BFUtils.triggerPlayerStat(manager, this.game, uUID, BFStats.GAMES, 1);
        }
        ((AbstractGame)this.game).getMapVoteManager().method_3721(uUID);
        BFUtils.handlePlayerRageQuit(manager, this.playerData, this.game, this.getPlayerUUIDs(), player);
        if (manager.isCloudConnectionVerified()) {
            ((AbstractConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketMMPlayerLeft(uUID, ((AbstractGame)this.game).getUUID()));
        }
        this.method_2791(player);
    }

    public void tickPlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull String playerName, @NotNull UUID playerId, boolean isMatchMakingEnabled, @NotNull PlayerCloudData cloudData, @NotNull BFAbstractPlayerData<?, ?, ?, ?> playerData) {
        int n;
        player.getFoodData().setFoodLevel(20);
        Object object = this.game;
        if (object instanceof IAllowsRegeneration) {
            IAllowsRegeneration iAllowsRegeneration = (IAllowsRegeneration)object;
            this.updateHealTime(player, playerId, iAllowsRegeneration);
        }
        if (((AbstractGame)this.game).getStatus() == GameStatus.GAME && playerData.playTimer++ >= 2400) {
            playerData.playTimer = 0;
            BFUtils.triggerPlayerStat(manager, this.game, playerId, BFStats.PLAY_TIME, 2400);
        }
        if (this.game instanceof IAllowsRespawning) {
            int n2 = BFUtils.getPlayerStat(this.game, playerId, BFStats.SPAWN_PRO);
            if (n2 > 0) {
                BFUtils.decrementPlayerStat(this.game, playerId, BFStats.SPAWN_PRO);
            }
            if ((double)n2 < 120.0) {
                object = player.position();
                if (player.tickCount >= 20 && (player.xOld != ((Vec3)object).x || player.zOld != ((Vec3)object).z || player.isCrouching() || ((Vec3)object).y > player.yOld)) {
                    BFUtils.setPlayerStat(this.game, playerId, BFStats.SPAWN_PRO, 0);
                }
            }
        }
        if ((n = BFUtils.getPlayerStat(this.game, playerId, BFStats.TEAM_SWITCH)) > 0) {
            BFUtils.decrementPlayerStat(this.game, playerId, BFStats.TEAM_SWITCH);
        }
        if (this.game instanceof IAllowsWarCry) {
            int n3;
            int n4 = BFUtils.getPlayerStat(this.game, playerId, BFStats.WAR_CRY);
            if (n4 > 0) {
                BFUtils.decrementPlayerStat(this.game, playerId, BFStats.WAR_CRY);
            }
            if ((n3 = BFUtils.getPlayerStat(this.game, playerId, BFStats.WAR_CRY_COMMANDER)) > 0) {
                BFUtils.decrementPlayerStat(this.game, playerId, BFStats.WAR_CRY_COMMANDER);
            }
        }
        this.updatePlayerRespawning(manager, level, player, playerData);
        this.specificTickPlayer(player, playerData);
        if (isMatchMakingEnabled) {
            this.checkPlayerPunishments(player, playerName, playerData, cloudData);
        }
    }

    private void checkPlayerPunishments(@NotNull ServerPlayer player, @NotNull String playerName, @NotNull BFAbstractPlayerData<?, ?, ?, ?> playerData, @NotNull PlayerCloudData cloudData) {
        if (cloudData.punishmentCheckTimer++ < 40) {
            return;
        }
        cloudData.punishmentCheckTimer = 0;
        if (cloudData.hasActivePunishment(PunishmentType.BAN_MM) || cloudData.hasActivePunishment(PunishmentType.BAN_CLOUD)) {
            Set<UUID> set = ((AbstractGamePlayerManager)((AbstractGame)this.game).getPlayerManager()).getPlayerUUIDs();
            MutableComponent mutableComponent = Component.literal((String)playerName).withStyle(ChatFormatting.GRAY);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.cloud.notification.suspended", (Object[])new Object[]{mutableComponent, "BlockFront"}).withStyle(ChatFormatting.RED);
            BFUtils.sendChatGraphic(ChatGraphic.ANTI_CHEAT, set, (Component)mutableComponent2);
            BFUtils.disconnectServerPlayer(player, (Component)Component.translatable((String)"bf.message.disconnect.punishment"));
        }
        playerData.setMuted(cloudData.hasActivePunishment(PunishmentType.MUTE));
    }

    private void updatePlayerRespawning(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull BFAbstractPlayerData<?, ?, ?, ?> playerData) {
        if (!playerData.isOutOfGame()) {
            player.setInvisible(false);
            return;
        }
        player.tickCount = 0;
        player.setInvisible(true);
        if (playerData.getRespawnTimer() > 0) {
            if (((AbstractGame)this.game).shouldRespawnAutomatically((Player)player)) {
                playerData.method_828(1);
                if (playerData.getRespawnTimer() == 20) {
                    BFUtils.playSound(player, (SoundEvent)BFSounds.MATCH_RESPAWN.get(), SoundSource.MUSIC, 2.0f);
                }
            }
        } else {
            BFUtils.initPlayerForGame(manager, this.playerData, level, player);
        }
    }

    private void updateHealTime(@NotNull ServerPlayer player, @NotNull UUID playerId, @NotNull IAllowsRegeneration game) {
        if (!game.playerCanRegenerate((Player)player)) {
            return;
        }
        int n = BFUtils.getPlayerStat(this.game, playerId, BFStats.HEALTIME);
        if (n <= 0) {
            BFUtils.setPlayerStat(this.game, playerId, BFStats.HEALTIME, game.method_3419((Player)player));
            player.heal(game.method_3418((Player)player));
        } else {
            BFUtils.decrementPlayerStat(this.game, playerId, BFStats.HEALTIME);
        }
    }

    public void handlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer killedPlayer, @NotNull UUID killedUuid, @javax.annotation.Nullable ServerPlayer sourcePlayer, @javax.annotation.Nullable UUID sourceUuid, @NotNull DamageSource source, @NotNull Set<UUID> players) {
        GameTeam gameTeam;
        GameTeam gameTeam2 = this.getPlayerTeam(killedUuid);
        if (gameTeam2 != null) {
            int n = gameTeam2.getObjectInt(BFStats.DEATHS, 0);
            gameTeam2.setObject(BFStats.DEATHS, n + 1);
        }
        if (sourceUuid != null && (gameTeam = this.getPlayerTeam(sourceUuid)) != null) {
            int n = gameTeam.getObjectInt(BFStats.KILLS, 0);
            gameTeam.setObject(BFStats.KILLS, n + 1);
        }
        this.specificHandlePlayerDeath(manager, level, killedPlayer, killedUuid, sourcePlayer, sourceUuid, source, players);
    }

    public int getMaxPlayers() {
        int n = 0;
        for (GameTeam gameTeam : this.teams) {
            n += gameTeam.getMaxPlayers();
        }
        return n;
    }

    public boolean hasPlayer(@NotNull UUID uuid) {
        return this.getPlayerUUIDs().contains(uuid);
    }

    public List<ItemEntity> method_2776(@NotNull Player player, @NotNull List<ItemEntity> list) {
        return list;
    }

    public boolean canPlayerDropItem(@NotNull Player player, @NotNull ItemStack itemStack) {
        return false;
    }

    public boolean method_2779(@NotNull Player player, @NotNull ItemEntity itemEntity, @NotNull ItemStack itemStack) {
        return true;
    }

    public boolean canInteractWithBlock(@NotNull PlayerDataHandler<?> dataHandler, @NotNull Level level, @NotNull Player player, @NotNull UUID uuid, @NotNull Block block, @NotNull BlockPos blockPos) {
        return block instanceof DoorBlock || block instanceof ButtonBlock || block instanceof LeverBlock || block instanceof FenceGateBlock || block instanceof TrapDoorBlock || block instanceof CrateGunBlock || block instanceof BFButtonBlock;
    }

    @javax.annotation.Nullable
    public FDSPose getLobbySpawn() {
        return this.lobbySpawn;
    }

    public void setLobbySpawn(@NotNull FDSPose pose) {
        this.lobbySpawn = pose;
    }

    public void addTeam(@NotNull GameTeam team) {
        this.teams.add(team);
    }

    @javax.annotation.Nullable
    public GameTeam getPlayerTeam(@NotNull UUID uuid) {
        return this.teams.stream().filter(team -> team.hasPlayer(uuid)).findFirst().orElse(null);
    }

    public void removePlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        UUID uUID = player.getUUID();
        this.matchStreakTracker.removePlayerStreak(uUID);
        this.teams.forEach(team -> team.removePlayer(uUID));
        BFUtils.method_2979(this.playerData, player);
        BFUtils.initPlayerForGame(manager, this.playerData, level, player);
        this.removePlayer(manager, player);
        PacketUtils.sendToPlayer(new BFGamePacket(null, false), player);
    }

    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull GameTeam team) {
        String string = player.getScoreboardName();
        BFLog.log("Attempting to add player %s to team %s...", string, team.getName());
        if (team.addPlayer(player)) {
            BFLog.log("Successfully added player %s to team %s!", string, team.getName());
            this.initPlayerForGame(manager, joinType, level, player, team);
            return true;
        }
        BFLog.log("Failed to add player %s to team %s!", string, team.getName());
        return false;
    }

    @javax.annotation.Nullable
    public GameTeam getNextJoiningTeam() {
        GameTeam gameTeam = this.getTeamByName("Allies");
        GameTeam gameTeam2 = this.getTeamByName("Axis");
        assert (gameTeam2 != null);
        assert (gameTeam != null);
        Set<UUID> set = gameTeam2.getPlayers();
        Set<UUID> set2 = gameTeam.getPlayers();
        if (set.isEmpty() && set2.isEmpty()) {
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            return threadLocalRandom.nextBoolean() ? gameTeam2 : gameTeam;
        }
        return set.size() < set2.size() ? gameTeam2 : gameTeam;
    }

    @javax.annotation.Nullable
    public GameTeam getTeamByName(@NotNull String name) {
        for (GameTeam gameTeam : this.teams) {
            if (!gameTeam.getName().equalsIgnoreCase(name)) continue;
            return gameTeam;
        }
        return null;
    }

    public void writeFDS(@NotNull FDSTagCompound root) {
        for (GameTeam gameTeam : this.teams) {
            gameTeam.writeFDS(root);
        }
    }

    public void readFDS(@NotNull FDSTagCompound root) {
        for (GameTeam gameTeam : this.teams) {
            if (gameTeam == null) continue;
            gameTeam.readFDS(root);
            gameTeam.clearPlayers();
            gameTeam.clearTeamObjects();
        }
    }

    @NotNull
    public List<GameTeam> getTeams() {
        return Collections.unmodifiableList(this.teams);
    }

    @NotNull
    public Set<UUID> getPlayerUUIDs() {
        int n = this.getMaxPlayers();
        ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet(n);
        for (GameTeam gameTeam : this.teams) {
            objectOpenHashSet.addAll(gameTeam.getPlayers());
        }
        return objectOpenHashSet;
    }

    @NotNull
    public Set<UUID> method_2795() {
        Set<UUID> set = this.getPlayerUUIDs();
        ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet(set.size());
        for (UUID uUID : set) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            Object obj = this.playerData.getPlayerData(uUID);
            if (serverPlayer == null || BFUtils.isPlayerUnavailable((Player)serverPlayer, obj)) continue;
            objectOpenHashSet.add(uUID);
        }
        return objectOpenHashSet;
    }

    public void method_2789(@NotNull UUID uUID) {
        IAllowsRespawning iAllowsRespawning;
        G g = this.game;
        if (g instanceof IAllowsRespawning && (iAllowsRespawning = (IAllowsRespawning)g).method_3435(uUID)) {
            BFUtils.setPlayerStat(this.game, uUID, BFStats.SPAWN_PRO, 0);
        }
    }

    public boolean method_2777(@NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, double d) {
        GameTeam gameTeam = this.getPlayerTeam(uUID);
        if (gameTeam == null) {
            return false;
        }
        for (FDSPose fDSPose : gameTeam.getPlayerSpawns()) {
            double d2 = Mth.sqrt((float)((float)serverPlayer.distanceToSqr(fDSPose.position)));
            if (!(d2 <= d)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    public GameMatchStreakTracker getMatchStreakTracker() {
        return this.matchStreakTracker;
    }

    @OverridingMethodsMustInvokeSuper
    public void reset() {
        this.matchStreakTracker.reset();
        for (GameTeam gameTeam : this.teams) {
            gameTeam.clearPlayers();
            gameTeam.clearTeamObjects();
        }
    }

    @OverridingMethodsMustInvokeSuper
    protected void write(@NotNull ByteBuf buf) throws IOException {
        this.matchStreakTracker.write(buf);
        for (GameTeam gameTeam : this.teams) {
            gameTeam.writeBuf(buf);
        }
    }

    @OverridingMethodsMustInvokeSuper
    protected void read(@NotNull ByteBuf buf) throws IOException {
        this.matchStreakTracker.read(buf);
        for (GameTeam gameTeam : this.teams) {
            gameTeam.readBuf(buf);
        }
    }

    public abstract boolean method_2781(@NotNull Player var1, @NotNull Block var2);

    public abstract boolean method_2786(@NotNull Player var1, @NotNull Block var2);

    public abstract boolean method_2751(@NotNull PlayerDataHandler<?> var1, @NotNull Level var2, @NotNull Player var3, @NotNull Block var4, @NotNull BlockPos var5);

    @javax.annotation.Nullable
    public abstract WinningTeamData getWinningTeam(@NotNull ServerLevel var1, @NotNull Set<UUID> var2, @Nullable GameStageTimer var3);

    protected abstract boolean method_2771(@NotNull ServerPlayer var1);

    public abstract void method_2753(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull PlayerDataHandler<?> var2, @NotNull ServerLevel var3, @NotNull ServerPlayer var4, @NotNull UUID var5, @NotNull GameTeam var6);

    public abstract void method_2791(@NotNull ServerPlayer var1);

    public abstract void specificTickPlayer(@NotNull ServerPlayer var1, @NotNull BFAbstractPlayerData<?, ?, ?, ?> var2);

    public abstract void specificHandlePlayerDeath(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull ServerLevel var2, @NotNull ServerPlayer var3, @NotNull UUID var4, @javax.annotation.Nullable ServerPlayer var5, @javax.annotation.Nullable UUID var6, @NotNull DamageSource var7, @NotNull Set<UUID> var8);

    public abstract void initPlayer(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull PlayerDataHandler<?> var2, @NotNull ServerLevel var3, @NotNull ServerPlayer var4, @NotNull UUID var5);

    public abstract boolean method_2778(@NotNull Player var1, @NotNull UUID var2, @NotNull DamageSource var3, @javax.annotation.Nullable Entity var4);

    public abstract boolean method_2775(@NotNull LivingEntity var1, @NotNull DamageSource var2);

    public abstract boolean isAcceptingPlayers();
}

