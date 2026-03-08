/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.PacketPlayerRandomDrop;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMBattleReport;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.assets.AssetInstance;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.MapVoteManager;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.net.packet.BFVictoryPacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameCombatManager;
import com.boehmod.blockfront.game.GameMatchStreakTracker;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.PlayerDominatingManager;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.TimedStage;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.event.GameEvent;
import com.boehmod.blockfront.game.tag.IAllowsSoundboard;
import com.boehmod.blockfront.game.tag.IAnnounceFirstBlood;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.game.tag.IHasDominations;
import com.boehmod.blockfront.game.tag.ISendBattleReports;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGame<G extends AbstractGame<G, P, S>, P extends AbstractGamePlayerManager<G>, S extends GameStageManager<G, P>>
implements Comparable<AbstractGame<?, ?, ?>> {
    protected static final int field_2844 = 2400;
    private static final Component SWITCHTEAMS_ERROR_NOTEAM = Component.translatable((String)"bf.message.gamemode.switchteams.error.noteam");
    private static final Component SWITCHTEAMS_ERROR_UNBALANCED = Component.translatable((String)"bf.message.gamemode.switchteams.error.unbalanced");
    private static final Component SWITCHTEAMS_ERROR_POSTGAME = Component.translatable((String)"bf.message.gamemode.switchteams.error.postgame");
    private static final Component GAMES_TP_FAIL = Component.translatable((String)"bf.message.command.assets.edit.games.tp.fail");
    private static final Component GAMES_LOBBY_MESSAGE = Component.translatable((String)"bf.message.command.assets.edit.games.lobby");
    private static final Component FIRST_BLOOD_MESSAGE = Component.translatable((String)"bf.popup.message.firstblood").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD);
    private static final Component SOUNDBOARD_FAIL_COUNT_MESSAGE = Component.translatable((String)"bf.message.ingame.soundboard.fail.count").withStyle(ChatFormatting.RED);
    private static final Component MM_RESTARTING_MESSAGE = Component.translatable((String)"bf.message.disconnect.mm.restarting");
    private static final int field_2845 = 4;
    private static final int field_2846 = 5;
    private static final int field_6958 = 3;
    @NotNull
    private final ObjectList<GameEvent> gameEvents = new ObjectArrayList();
    @NotNull
    private final Object2ObjectMap<UUID, FDSTagCompound> playerStatData = new Object2ObjectOpenHashMap();
    @NotNull
    private final Component displayName;
    @NotNull
    private final MapVoteManager mapVoteManager;
    @NotNull
    private final GameCombatManager<G> combatManager;
    @NotNull
    private final PlayerDominatingManager dominatingManager;
    @NotNull
    protected GameStatus status = GameStatus.IDLE;
    protected String name = "";
    @NotNull
    protected String type;
    @Nullable
    protected MapAsset map;
    @Nullable
    private MapEnvironment mapEnvironment = new MapEnvironment("default");
    @Nullable
    protected AssetInstance<MapAsset> mapInstance;
    @NotNull
    public final AssetCommandBuilder baseCommand = new AssetCommandBuilder().subCommand("tp", new AssetCommandBuilder((commandContext, stringArray) -> {
        ServerPlayer serverPlayer = (ServerPlayer)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = ((AbstractGamePlayerManager)this.getPlayerManager()).getLobbySpawn();
        if (fDSPose == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, GAMES_TP_FAIL);
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        BFUtils.teleportPlayer(obj, serverPlayer, fDSPose);
        MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.games.tp", (Object[])new Object[]{mutableComponent}));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("map", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        MutableComponent mutableComponent = Component.literal((String)string).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.games.mapname", (Object[])new Object[]{mutableComponent}));
        this.setMapInstance(new AssetInstance<MapAsset>(MapAsset.class, string, MapAsset.DEFAULT));
    }).validator(AssetCommandValidators.count(new String[]{"mapName"}))).subCommand("lobby", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, GAMES_LOBBY_MESSAGE);
        ((AbstractGamePlayerManager)this.getPlayerManager()).setLobbySpawn(new FDSPose(player));
    }).validator(AssetCommandValidators.ONLY_PLAYERS));
    @Nullable
    private UUID firstBloodPlayer = null;
    private int soundboardCooldown = 0;
    private boolean isPaused = false;
    @NotNull
    private UUID uuid = UUID.randomUUID();
    @NotNull
    protected final P playerManager;
    protected final PlayerDataHandler<?> dataHandler;
    @NotNull
    protected final GameStageManager<G, P> stageManager;
    private boolean hasPlayerLimit = true;
    private boolean shouldRestart = true;

    public AbstractGame(@NotNull BFAbstractManager<?, ?, ?> manager) {
        this(manager, "Unknown", "Unknown");
    }

    public AbstractGame(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull String type, @NotNull String typeDisplayName) {
        int n;
        this.dataHandler = manager.getPlayerDataHandler();
        this.playerManager = this.createPlayerManager();
        this.stageManager = new GameStageManager(this);
        this.type = type;
        this.displayName = Component.literal((String)typeDisplayName);
        this.mapVoteManager = new MapVoteManager(this);
        this.combatManager = new GameCombatManager<AbstractGame>(this);
        ServerLevel serverLevel = null;
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer != null) {
            serverLevel = minecraftServer.getLevel(Level.OVERWORLD);
        }
        BFUtils.discardMatchEntities(serverLevel, this, this.playerManager);
        AbstractGame abstractGame = this;
        if (abstractGame instanceof IHasDominations) {
            IHasDominations iHasDominations = (IHasDominations)((Object)abstractGame);
            n = iHasDominations.getDominationThreshold();
        } else {
            n = 1;
        }
        int n2 = n;
        this.dominatingManager = new PlayerDominatingManager(n2);
    }

    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public abstract AbstractGameClient<?, ?> createGameClient(@NotNull BFClientManager var1);

    @NotNull
    protected abstract P createPlayerManager();

    @NotNull
    public Set<MatchClass> getBannedClasses() {
        return EnumSet.noneOf(MatchClass.class);
    }

    @NotNull
    public abstract AbstractGameStage<G, P> createFirstStage();

    @Override
    public int compareTo(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        return Integer.compare(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs().size(), ((AbstractGamePlayerManager)this.playerManager).getPlayerUUIDs().size());
    }

    public void refreshMapEnvironment() {
        MapAsset mapAsset = this.getMap();
        ObjectArrayList objectArrayList = new ObjectArrayList(mapAsset.getEnvironments().values());
        BFLog.log("Refreshing random environment for map '%s'... (%d Available)", this.getMap().getName(), objectArrayList.size());
        this.mapEnvironment = (MapEnvironment)objectArrayList.get(ThreadLocalRandom.current().nextInt(objectArrayList.size()));
        BFLog.log("Selected '%s' as the random environment for map '%s'.", this.mapEnvironment.getName(), mapAsset.getName());
    }

    public void update(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level) {
        Set<UUID> set = ((AbstractGamePlayerManager)this.playerManager).getPlayerUUIDs();
        if (this.updateIdleStatus(level, set)) {
            return;
        }
        this.shouldRestart = false;
        if (this.isPaused) {
            return;
        }
        if (this.soundboardCooldown > 0) {
            --this.soundboardCooldown;
        }
        this.getMapEnvironment().updateGame(this, manager, level, set);
        this.getGameEvents().removeIf(gameEvent -> !gameEvent.method_3444(level, this, set));
        ((AbstractGamePlayerManager)this.playerManager).method_5993(set);
        this.updateStageManager(manager, this.dataHandler, level, set);
    }

    private boolean updateIdleStatus(@NotNull ServerLevel level, @NotNull Set<UUID> playerUUIDs) {
        if (playerUUIDs.isEmpty()) {
            this.setStatus(GameStatus.IDLE);
            if (!this.shouldRestart) {
                this.shouldRestart = true;
                BFLog.log("[STAGE] No players in match '%s'! Restarting game.", this.getName());
                this.reset(level);
            }
            return true;
        }
        return false;
    }

    public abstract void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull PlayerDataHandler<?> var2, @NotNull ServerLevel var3, @NotNull Set<UUID> var4);

    public boolean changeMap(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull String gameName) {
        AbstractGame<?, ?, ?> abstractGame = manager.retrieveGame(gameName);
        return abstractGame != null && this.moveLobby(manager, level, abstractGame);
    }

    public boolean moveLobby(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull UUID otherGame) {
        AbstractGame<?, ?, ?> abstractGame = manager.retrieveGame(otherGame);
        if (abstractGame == null) {
            BFLog.log("[Move] Failed to find game with UUID '%s'.", otherGame);
            return false;
        }
        if (manager.mapIsOccupied(abstractGame.getMap(), this)) {
            BFLog.log("[Move] Map '%s' is occupied.", abstractGame.getMap().getName());
            return false;
        }
        if (abstractGame.status != GameStatus.IDLE) {
            BFLog.log("[Move] Game '%s' is not idle.", new Object[]{abstractGame.status});
            return false;
        }
        BFLog.log("[Move] Moving lobby to game '%s'...", abstractGame.getName());
        return this.moveLobby(manager, level, abstractGame);
    }

    public boolean moveLobby(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull AbstractGame<?, ?, ?> game) {
        Set<UUID> set = ((AbstractGamePlayerManager)this.playerManager).getPlayerUUIDs();
        GameMatchStreakTracker gameMatchStreakTracker = ((AbstractGamePlayerManager)this.playerManager).getMatchStreakTracker();
        gameMatchStreakTracker.incrementStreak();
        ((AbstractGamePlayerManager)game.getPlayerManager()).getMatchStreakTracker().merge(gameMatchStreakTracker);
        Set<UUID> set2 = BFUtils.sortBySkillLevel(this.dataHandler, set);
        for (UUID uUID : set2) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            ((AbstractGamePlayerManager)this.playerManager).removePlayer(manager, level, serverPlayer);
            if (manager.assignPlayerToGame(level, serverPlayer, game)) continue;
            BFLog.log(String.format("Failed to assign player '%s' to game '%s', running game-mode '%s' on map '%s'", serverPlayer.getScoreboardName(), game.uuid, game.type, game.getMap().getName()), new Object[0]);
            BFUtils.disconnectServerPlayer(serverPlayer, MM_RESTARTING_MESSAGE);
        }
        if (manager instanceof BFServerManager) {
            BFServerManager bFServerManager = (BFServerManager)manager;
            bFServerManager.getMatchMakingCache().changeMatch(this.uuid, game.uuid);
        }
        return true;
    }

    public void finishMatch(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, @Nullable BFStat topStat, boolean bl, @NotNull WinningTeamData winningTeam, @NotNull Set<UUID> players) {
        MutableComponent mutableComponent;
        Object object;
        boolean bl2 = manager instanceof BFServerManager && ((BFServerManager)(object = (BFServerManager)manager)).isMatchMakingEnabled();
        BFUtils.discardMatchEntities(level, this, this.playerManager);
        object = new TreeMap(UUID::compareTo);
        HashSet<UUID> hashSet = new HashSet<UUID>();
        players.forEach(arg_0 -> AbstractGame.method_2647((Map)object, arg_0));
        if (winningTeam.team != null) {
            hashSet.addAll(winningTeam.team.getPlayers());
        }
        if (winningTeam.topPlayers != null) {
            hashSet.addAll(winningTeam.topPlayers);
        }
        hashSet.forEach(arg_0 -> AbstractGame.method_2627((Map)object, arg_0));
        if (winningTeam.team != null) {
            DivisionData divisionData = winningTeam.team.getDivisionData(this);
            mutableComponent = Component.translatable((String)"bf.message.gamemode.victorious.team", (Object[])new Object[]{divisionData.getCountry().getName()});
        } else if (winningTeam.topPlayers != null && !winningTeam.topPlayers.isEmpty()) {
            ObjectArrayList objectArrayList = new ObjectArrayList();
            for (UUID uUID : winningTeam.topPlayers) {
                ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
                if (serverPlayer == null) continue;
                objectArrayList.add((Object)serverPlayer.getScoreboardName());
            }
            String string = FormatUtils.proseStrings((List<String>)objectArrayList);
            mutableComponent = Component.translatable((String)"bf.message.gamemode.victorious.team", (Object[])new Object[]{string});
        } else {
            mutableComponent = Component.empty();
        }
        object.forEach((arg_0, arg_1) -> this.method_2617(manager, bl2, (Component)mutableComponent, arg_0, arg_1));
        if (bl2 && this.isMatchSuccess()) {
            BFUtils.incrementPlayerStat(manager, this, players, BFStats.GAMES);
            this.requestSupplyDrops(manager, 4, players);
            if (topStat != null) {
                this.incrementTrophies(manager, 5, topStat, bl, players);
            }
            if (winningTeam.team != null && this instanceof ISendBattleReports) {
                this.sendBattleReport(manager, winningTeam.team, topStat);
            }
        }
    }

    protected abstract boolean isMatchSuccess();

    private void sendBattleReport(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull GameTeam team, @Nullable BFStat topStat) {
        GameTeam gameTeam = ((AbstractGamePlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((AbstractGamePlayerManager)this.playerManager).getTeamByName("Allies");
        int n = 0;
        int n2 = 0;
        if (gameTeam != null) {
            n = gameTeam.getObjectInt(BFStats.DEATHS);
        }
        if (gameTeam2 != null) {
            n2 = gameTeam2.getObjectInt(BFStats.DEATHS);
        }
        ObjectSortedSet<UUID> objectSortedSet = topStat != null ? BFUtils.topPlayers(this, 3, topStat, team) : ObjectSets.emptySet();
        ((AbstractConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketMMBattleReport(this.getMap().getName(), team.getDivisionData(this).getCountry().getName(), n2, n, objectSortedSet));
    }

    private void onPlayerMatchFinished(@NotNull BFAbstractManager<?, ?, ?> manager, boolean awardAchievement, @NotNull UUID uuid, @NotNull Component subtitle, boolean victory) {
        ServerPlayer serverPlayer;
        if (victory && awardAchievement && (serverPlayer = this.getVictoryAchievement()) != null) {
            BFUtils.awardAchievement(manager, uuid, (CloudAchievement)serverPlayer);
        }
        if ((serverPlayer = BFUtils.getPlayerByUUID(uuid)) != null) {
            PacketUtils.sendToPlayer(new BFVictoryPacket(subtitle, victory), serverPlayer);
        }
    }

    public void readAllFDS(@NotNull FDSTagCompound root) {
        this.readFDS(root);
        this.readSpecificFDS(root);
        this.clearPlayerStatData();
    }

    public void writeAllFDS(@NotNull FDSTagCompound root) {
        this.writeFDS(root);
        this.writeSpecificFDS(root);
    }

    private void writeFDS(@NotNull FDSTagCompound root) {
        root.setString("name", this.name);
        if (this.mapInstance != null) {
            root.setString("map", this.mapInstance.get().getName());
        }
        root.setString("type", this.type);
        ((AbstractGamePlayerManager)this.playerManager).writeFDS(root);
        FDSPose fDSPose = ((AbstractGamePlayerManager)this.playerManager).getLobbySpawn();
        if (fDSPose != null) {
            ((AbstractGamePlayerManager)this.playerManager).getLobbySpawn().writeNamedFDS("lobby", root);
        }
    }

    private void readFDS(@NotNull FDSTagCompound root) {
        String string;
        String string2;
        String string3 = root.getString("name");
        if (string3 != null) {
            this.name = string3;
        }
        if ((string2 = root.getString("type")) != null) {
            this.type = string2;
        }
        if ((string = root.getString("map")) != null) {
            this.mapInstance = new AssetInstance<MapAsset>(MapAsset.class, string, MapAsset.DEFAULT);
        }
        ((AbstractGamePlayerManager)this.playerManager).readFDS(root);
        FDSPose fDSPose = FDSPose.readNamedFDS("lobby", root);
        if (fDSPose != null) {
            ((AbstractGamePlayerManager)this.playerManager).setLobbySpawn(fDSPose);
        }
    }

    void write(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeString((ByteBuf)buf, (String)this.name);
        IPacket.writeBoolean((ByteBuf)buf, (this.mapInstance != null ? 1 : 0) != 0);
        if (this.mapInstance != null) {
            IPacket.writeString((ByteBuf)buf, (String)this.mapInstance.get().getName());
        }
        IPacket.writeString((ByteBuf)buf, (String)this.type);
        IPacket.writeUUID((ByteBuf)buf, (UUID)this.uuid);
        FDSPose fDSPose = ((AbstractGamePlayerManager)this.playerManager).getLobbySpawn();
        IPacket.writeBoolean((ByteBuf)buf, (fDSPose != null ? 1 : 0) != 0);
        if (fDSPose != null) {
            fDSPose.write(buf);
        }
    }

    void read(@NotNull ByteBuf buf) throws IOException {
        this.name = IPacket.readString((ByteBuf)buf);
        if (IPacket.readBoolean((ByteBuf)buf)) {
            String string = IPacket.readString((ByteBuf)buf);
            this.mapInstance = new AssetInstance<MapAsset>(MapAsset.class, string, MapAsset.DEFAULT);
        }
        this.type = IPacket.readString((ByteBuf)buf);
        this.uuid = IPacket.readUUID((ByteBuf)buf);
        boolean bl = IPacket.readBoolean((ByteBuf)buf);
        if (bl) {
            FDSPose fDSPose = new FDSPose();
            fDSPose.read(buf);
            ((AbstractGamePlayerManager)this.playerManager).setLobbySpawn(fDSPose);
        }
    }

    @NotNull
    public final P getPlayerManager() {
        return this.playerManager;
    }

    public float getFirearmDamageMultiplier() {
        return 1.0f;
    }

    public float getFireRateMultiplier() {
        return 1.0f;
    }

    public int method_2638() {
        return 0;
    }

    @NotNull
    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(@NotNull GameStatus status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public MapAsset getMap() {
        if (this.map != null) {
            return this.map;
        }
        return this.mapInstance == null || this.mapInstance.isEmpty() ? MapAsset.DEFAULT : this.mapInstance.get();
    }

    public void setMapInstance(@NotNull AssetInstance<MapAsset> mapInstance) {
        this.mapInstance = mapInstance;
    }

    public void setMapInstance(@NotNull MapAsset mapAsset) {
        this.mapInstance = new AssetInstance<MapAsset>(mapAsset);
    }

    @NotNull
    public String getType() {
        return this.type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public int entityBoundSize() {
        return 512;
    }

    public abstract boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull TeamJoinType var2, @NotNull ServerLevel var3, @NotNull ServerPlayer var4);

    @NotNull
    public FDSTagCompound getPlayerStatData(@NotNull UUID uuid) {
        return (FDSTagCompound)this.playerStatData.computeIfAbsent((Object)uuid, object -> new FDSTagCompound(object.toString()));
    }

    protected void writePlayerStatData(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeBoolean((ByteBuf)buf, (!this.playerStatData.isEmpty() ? 1 : 0) != 0);
        if (this.playerStatData.isEmpty()) {
            return;
        }
        buf.writeInt(this.playerStatData.size());
        for (Map.Entry entry : this.playerStatData.entrySet()) {
            IPacket.writeUUID((ByteBuf)buf, (UUID)((UUID)entry.getKey()));
            ((FDSTagCompound)entry.getValue()).writeData(buf);
        }
    }

    protected void readPlayerStatData(@NotNull ByteBuf buf) throws IOException {
        if (!IPacket.readBoolean((ByteBuf)buf)) {
            return;
        }
        this.playerStatData.clear();
        int n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            UUID uUID = IPacket.readUUID((ByteBuf)buf);
            FDSTagCompound fDSTagCompound = new FDSTagCompound();
            fDSTagCompound.readData(buf);
            this.playerStatData.put((Object)uUID, (Object)fDSTagCompound);
        }
    }

    public void removePlayerStatData(@NotNull UUID uuid) {
        this.playerStatData.remove((Object)uuid);
    }

    private void clearPlayerStatData() {
        this.playerStatData.clear();
    }

    public void reset(@Nullable ServerLevel level) {
        BFLog.log("Resetting game '%s'...", this.getName());
        this.uuid = UUID.randomUUID();
        this.isPaused = false;
        this.hasPlayerLimit = true;
        ((AbstractGamePlayerManager)this.playerManager).reset();
        this.stageManager.initFirstStage();
        this.dominatingManager.clear();
        this.firstBloodPlayer = null;
        this.mapVoteManager.reset();
        this.getMap().reset();
        this.refreshMapEnvironment();
        this.clearPlayerStatData();
        BFUtils.discardMatchEntities(level, this, this.playerManager);
        this.specificReset((Level)level);
    }

    public void stopMatchMaking(@NotNull BFAbstractManager<?, ?, ?> manager) {
        if (!(manager instanceof BFServerManager)) {
            return;
        }
        BFServerManager bFServerManager = (BFServerManager)manager;
        if (bFServerManager.isMatchMakingEnabled() && bFServerManager.getMatchMakingShutdown()) {
            bFServerManager.shutdown("Stopping MM server. Game over.");
        }
    }

    public abstract void specificReset(@Nullable Level var1);

    public abstract void writeSpecificFDS(@NotNull FDSTagCompound var1);

    public abstract void readSpecificFDS(@NotNull FDSTagCompound var1);

    public abstract boolean shouldUseStamina(@NotNull Player var1);

    @NotNull
    public Component getDisplayName() {
        return this.displayName;
    }

    public abstract boolean shouldRespawnAutomatically(@NotNull Player var1);

    @OverridingMethodsMustInvokeSuper
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        IPacket.writeBoolean((ByteBuf)buf, (boolean)writeMap);
        this.write(buf);
        this.writePlayerStatData(buf);
        IPacket.writeEnum((ByteBuf)buf, (Enum)this.status);
        ((AbstractGamePlayerManager)this.playerManager).write(buf);
        this.mapVoteManager.write(buf);
        if (writeMap) {
            this.getMap().writeBuf(buf);
            buf.writeBoolean(this.mapEnvironment != null);
            if (this.mapEnvironment != null) {
                this.mapEnvironment.write(buf);
            }
        }
    }

    public void writeForGamePacket(@NotNull ByteBuf buf) throws IOException {
        Object object;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        AbstractGameStage<G, P> abstractGameStage = this.stageManager.getCurrentStage();
        if (abstractGameStage instanceof TimedStage) {
            object = (TimedStage)((Object)abstractGameStage);
            GameStageTimer gameStageTimer = object.getStageTimer(this);
            if (gameStageTimer != null) {
                IPacket.writeBoolean((ByteBuf)buf, (boolean)true);
                gameStageTimer.write(buf);
            } else {
                IPacket.writeBoolean((ByteBuf)buf, (boolean)false);
            }
        } else {
            IPacket.writeBoolean((ByteBuf)buf, (boolean)false);
        }
        IPacket.writeBoolean((ByteBuf)buf, (bFAbstractManager instanceof BFServerManager && ((BFServerManager)(object = (BFServerManager)bFAbstractManager)).isMatchMakingEnabled() ? 1 : 0) != 0);
    }

    @OverridingMethodsMustInvokeSuper
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        boolean bl = IPacket.readBoolean((ByteBuf)buf);
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Mod manager is null!";
        this.read(buf);
        this.readPlayerStatData(buf);
        this.status = (GameStatus)IPacket.readEnum((ByteBuf)buf, GameStatus.class);
        ((AbstractGamePlayerManager)this.playerManager).read(buf);
        this.mapVoteManager.read(buf);
        if (bl) {
            if (this.map == null) {
                this.map = new MapAsset(bFClientManager);
            }
            this.map.readBuf(buf);
            if (buf.readBoolean()) {
                this.mapEnvironment = new MapEnvironment(buf);
            }
        }
    }

    public int getMinimumPlayers() {
        return 2;
    }

    public boolean hasMinimumPlayers(@NotNull Set<UUID> players) {
        return players.size() >= this.getMinimumPlayers();
    }

    protected void incrementTrophies(@NotNull BFAbstractManager<?, ?, ?> manager, int numTopPlayers, @NotNull BFStat topStat, boolean bl, @NotNull Set<UUID> players) {
        this.incrementTrophies(manager, numTopPlayers, topStat.getKey(), bl, players);
    }

    protected void incrementTrophies(@NotNull BFAbstractManager<?, ?, ?> manager, int n, @NotNull String string, boolean bl, @NotNull Set<UUID> players) {
        ObjectSortedSet<UUID> objectSortedSet = BFUtils.topPlayers(this, n, string, players);
        for (UUID uUID : objectSortedSet) {
            if (!manager.isCloudConnectionVerified() || !bl) continue;
            BFUtils.triggerPlayerStat(manager, this, uUID, BFStats.TROPHIES, 1);
        }
    }

    public abstract boolean shouldAnnounceRageQuits();

    protected void requestSupplyDrops(@NotNull BFAbstractManager<?, ?, ?> manager, int randomMax, @NotNull Set<UUID> players) {
        int n = players.size();
        if (n < 4) {
            return;
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int n2 = threadLocalRandom.nextInt(randomMax);
        if (n >= 10) {
            n2 += threadLocalRandom.nextInt(randomMax);
        }
        if (n2 <= 0) {
            return;
        }
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.match.drops", (Object[])new Object[]{n2}).withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
        BFUtils.sendNoticeIcon(players);
        BFUtils.sendNoticeMessage(players, (Component)mutableComponent);
        Set<UUID> set = BFUtils.randomUUIDs(players, n2);
        for (UUID uUID : set) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) {
                return;
            }
            MutableComponent mutableComponent2 = Component.literal((String)serverPlayer.getScoreboardName()).withStyle(ChatFormatting.GOLD);
            BFUtils.sendNoticeMessage(players, (Component)Component.literal((String)"  ").append((Component)mutableComponent2));
            ((AbstractConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketPlayerRandomDrop(uUID));
        }
    }

    @NotNull
    public DivisionData getAlliesDivision() {
        return this.getMap().getAlliesDivision();
    }

    @NotNull
    public DivisionData getAxisDivision() {
        return this.getMap().getAxisDivision();
    }

    public void addGameEvent(@NotNull GameEvent gameEvent) {
        this.gameEvents.add((Object)gameEvent);
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    @NotNull
    public MapVoteManager getMapVoteManager() {
        return this.mapVoteManager;
    }

    @NotNull
    public GameCombatManager<? extends AbstractGame<G, P, S>> getCombatManager() {
        return this.combatManager;
    }

    public void removePlayer(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        UUID uUID = player.getUUID();
        ((AbstractGamePlayerManager)this.playerManager).removePlayer(manager, level, player);
        this.removePlayerStatData(uUID);
    }

    public boolean shouldAllowPlayerMessage(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerPlayer player, @NotNull Component message) {
        IAllowsSoundboard iAllowsSoundboard;
        AbstractGame abstractGame = this;
        return !(abstractGame instanceof IAllowsSoundboard) || this.onPlayerSoundboard(manager, iAllowsSoundboard = (IAllowsSoundboard)((Object)abstractGame), player, player.getUUID(), message, ((AbstractGamePlayerManager)this.playerManager).getPlayerUUIDs());
    }

    private boolean onPlayerSoundboard(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull IAllowsSoundboard game, @NotNull ServerPlayer player, @NotNull UUID uuid, @NotNull Component message, @NotNull Set<UUID> players) {
        int n = game.getMaximumPlayerSounds(player);
        int n2 = game.getSoundboardCooldown();
        String[] stringArray = message.getString().split(" ");
        if (stringArray.length != 1) {
            return true;
        }
        String string = stringArray[0];
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = BFSounds.MEMES.get(string);
        if (deferredHolder == null) {
            return true;
        }
        int n3 = BFUtils.getPlayerStat(this, uuid, BFStats.SOUNDBOARD_SOUNDS);
        if (n3 <= n) {
            if (this.soundboardCooldown <= 0) {
                this.soundboardCooldown = n2;
                BFUtils.playPositionedSound(players, (SoundEvent)deferredHolder.get(), SoundSource.MASTER, 100.0f, player.position());
                BFUtils.incrementPlayerStat(manager, this, uuid, BFStats.SOUNDBOARD_SOUNDS);
                MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.AQUA);
                MutableComponent mutableComponent2 = Component.literal((String)player.getScoreboardName()).withStyle(ChatFormatting.AQUA);
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.ingame.soundboard.play", (Object[])new Object[]{mutableComponent2, mutableComponent}).withStyle(ChatFormatting.BLUE);
                BFUtils.sendNoticeMessage(players, (Component)mutableComponent3);
            } else {
                MutableComponent mutableComponent = Component.translatable((String)"bf.message.ingame.soundboard.fail.time", (Object[])new Object[]{String.valueOf(this.soundboardCooldown / 20)}).withStyle(ChatFormatting.RED);
                BFUtils.sendNoticeMessage(player, (Component)mutableComponent);
            }
        } else {
            BFUtils.sendNoticeMessage(player, SOUNDBOARD_FAIL_COUNT_MESSAGE);
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (!(object instanceof AbstractGame)) return false;
        AbstractGame abstractGame = (AbstractGame)object;
        if (!abstractGame.uuid.equals(this.uuid)) return false;
        return true;
    }

    public boolean shouldShowPlayerMessage(@NotNull UUID uUID, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID2, boolean bl, boolean bl2) {
        Object obj = this.dataHandler.getPlayerData((Player)serverPlayer);
        GameTeam gameTeam = ((AbstractGamePlayerManager)this.playerManager).getPlayerTeam(uUID);
        if (bl && gameTeam != null && !gameTeam.equals(((AbstractGamePlayerManager)this.playerManager).getPlayerTeam(uUID2))) {
            return false;
        }
        if (BFUtils.isPlayerUnavailable((Player)serverPlayer, obj)) {
            return true;
        }
        return !bl2 || this.shouldShowDeadMessages();
    }

    public abstract boolean shouldShowDeadMessages();

    @Nullable
    public abstract CloudAchievement getVictoryAchievement();

    @NotNull
    public ObjectList<GameEvent> getGameEvents() {
        return this.gameEvents;
    }

    public void handleClassChangeRequest(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull UUID uuid, @NotNull MatchClass matchClass, int classLevel) {
        BFLog.log("Class type '%s' was requested by '%s'", new Object[]{matchClass, player.getScoreboardName()});
        if (level.isClientSide()) {
            return;
        }
        if (this instanceof IHasClasses) {
            if (matchClass.isAllowed(this)) {
                if (((AbstractGamePlayerManager)this.playerManager).changePlayerClass(manager, level, player, uuid, matchClass, classLevel)) {
                    BFLog.log("Player '%s' has changed class to '%s'", new Object[]{player.getScoreboardName(), matchClass});
                } else {
                    BFLog.log("Player '%s' failed to change class to '%s'", new Object[]{player.getScoreboardName(), matchClass});
                }
            } else {
                BFLog.log("Player '%s' attempted to use an un-allowed class! (%s)", new Object[]{player.getScoreboardName(), matchClass});
            }
        }
    }

    protected void playerSwitchTeamInternal(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        FDSTagCompound fDSTagCompound = this.getPlayerStatData(uuid);
        fDSTagCompound.setInteger(BFStats.CLASS.getKey(), -1);
        fDSTagCompound.setInteger(BFStats.CLASS_INDEX.getKey(), -1);
        GameTeam gameTeam = ((AbstractGamePlayerManager)this.playerManager).getPlayerTeam(uuid);
        GameTeam gameTeam2 = null;
        if (gameTeam != null) {
            gameTeam2 = gameTeam.isAxis() ? ((AbstractGamePlayerManager)this.playerManager).getTeamByName("Allies") : ((AbstractGamePlayerManager)this.playerManager).getTeamByName("Axis");
        }
        if (gameTeam2 != null && !gameTeam2.isAcceptingPlayers()) {
            gameTeam.removePlayer(uuid);
            this.removePlayerStatData(uuid);
            player.getInventory().clearContent();
            ((AbstractGamePlayerManager)this.playerManager).playerJoinTeam(manager, TeamJoinType.SWITCH_TEAMS, level, player, gameTeam2);
            MutableComponent mutableComponent = Component.literal((String)player.getScoreboardName());
            MutableComponent mutableComponent2 = Component.literal((String)gameTeam2.getName().toUpperCase()).withStyle(gameTeam2.getStyleText());
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.player.teamswitch", (Object[])new Object[]{mutableComponent, mutableComponent2});
            BFUtils.sendNoticeMessage(((AbstractGamePlayerManager)this.playerManager).getPlayerUUIDs(), (Component)mutableComponent3);
            BFUtils.awardAchievement(manager, uuid, CloudAchievements.ACH_USER_SWITCH_TEAM);
        }
    }

    protected Component getSwitchTeamMessage(@NotNull Player player, int n, int n2) {
        GameTeam gameTeam = ((AbstractGamePlayerManager)this.playerManager).getPlayerTeam(player.getUUID());
        Component component = null;
        if (gameTeam == null) {
            component = SWITCHTEAMS_ERROR_NOTEAM;
        }
        if (gameTeam != null && (gameTeam.isAxis() ? n2 > n : n > n2)) {
            component = SWITCHTEAMS_ERROR_UNBALANCED;
        }
        if (this.getStatus() == GameStatus.POST_GAME) {
            component = SWITCHTEAMS_ERROR_POSTGAME;
        }
        return component;
    }

    @NotNull
    public BFStat getPrimaryStat() {
        return BFStats.SCORE;
    }

    public boolean canPlayersSwapItems() {
        return false;
    }

    @NotNull
    public AssetCommandBuilder getCommand() {
        return this.baseCommand;
    }

    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        Set<ErrorType> set = this.getErrorTypes();
        if (set.contains((Object)ErrorType.LOBBY) && ((AbstractGamePlayerManager)this.playerManager).lobbySpawn == null) {
            messages.add(Component.literal((String)("Lobby position for game '" + this.name + "' doesn't exist.")));
        }
        if (set.contains((Object)ErrorType.TEAM_SPAWNS)) {
            for (GameTeam gameTeam : ((AbstractGamePlayerManager)this.playerManager).getTeams()) {
                if (!gameTeam.getPlayerSpawns().isEmpty()) continue;
                messages.add(Component.literal((String)("Team spawns for team '" + gameTeam.getName() + "' for game '" + this.name + "' don't exist.")));
            }
        }
        if (set.contains((Object)ErrorType.MAP_TYPE)) {
            if (this.mapInstance == null) {
                messages.add(Component.literal((String)("Map type for game '" + this.name + "' is null.")));
            } else if (this.mapInstance.isEmpty()) {
                messages.add(Component.literal((String)("Map type for game '" + this.name + "' doesn't exist. (Custom by default)")));
            }
        }
    }

    @NotNull
    public PlayerDominatingManager getDominatingManager() {
        return this.dominatingManager;
    }

    @NotNull
    public Set<ErrorType> getErrorTypes() {
        return EnumSet.allOf(ErrorType.class);
    }

    public void onFirstBlood(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        Object object = this;
        if (!(object instanceof IAnnounceFirstBlood)) {
            return;
        }
        IAnnounceFirstBlood iAnnounceFirstBlood = (IAnnounceFirstBlood)object;
        if (!iAnnounceFirstBlood.shouldAnnounceFirstBlood(player) || this.firstBloodPlayer != null) {
            return;
        }
        this.firstBloodPlayer = uuid;
        object = ((AbstractGamePlayerManager)this.playerManager).getPlayerUUIDs();
        BFUtils.sendPopupMessage(player, new BFPopup(FIRST_BLOOD_MESSAGE, 80));
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.firstblood.announce", (Object[])new Object[]{player.getScoreboardName()}).withStyle(ChatFormatting.DARK_RED);
        BFUtils.sendNoticeMessage((Set<UUID>)object, (Component)mutableComponent);
        BFUtils.incrementPlayerStat(manager, this, uuid, BFStats.SCORE);
        BFUtils.playSound((Set<UUID>)object, (SoundEvent)BFSounds.MATCH_GAMEMODE_TDM_ANNOUNCER_FIRSTBLOOD.get(), SoundSource.MASTER);
        BFUtils.incrementPlayerStat(manager, this, uuid, BFStats.FIRST_BLOOD);
    }

    @NotNull
    public GameStageManager<G, P> getStageManager() {
        return this.stageManager;
    }

    public void togglePlayerLimit() {
        this.hasPlayerLimit = !this.hasPlayerLimit;
    }

    public boolean hasPlayerLimit() {
        return this.hasPlayerLimit;
    }

    @NotNull
    public MapEnvironment getMapEnvironment() {
        return this.mapEnvironment;
    }

    public void stop() {
        BFLog.log("Stopping all systems for match '%s'", this.getUUID());
        this.clearPlayerStatData();
    }

    @Override
    public /* synthetic */ int compareTo(@NotNull Object other) {
        return this.compareTo((AbstractGame)other);
    }

    private /* synthetic */ void method_2617(BFAbstractManager bFAbstractManager, boolean bl, Component component, UUID uuid, Boolean bl2) {
        this.onPlayerMatchFinished(bFAbstractManager, bl, uuid, component, bl2);
    }

    private static /* synthetic */ void method_2627(Map map, UUID uUID) {
        map.merge(uUID, true, (bl, bl2) -> true);
    }

    private static /* synthetic */ void method_2647(Map map, UUID uUID) {
        map.put(uUID, false);
    }

    public static enum ErrorType {
        MAP_TYPE,
        LOBBY,
        TEAM_SPAWNS;

    }
}

