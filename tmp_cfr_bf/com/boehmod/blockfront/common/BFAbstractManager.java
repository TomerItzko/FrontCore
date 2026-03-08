/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItems;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.cloud.connection.ConnectionStatusContext;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.assets.impl.GameAssetFactory;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.assets.impl.MapAssetFactory;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.ac.BFAbstractAntiCheat;
import com.boehmod.blockfront.common.match.MapVoteManager;
import com.boehmod.blockfront.common.net.packet.BFGamePacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BFAbstractManager<H extends PlayerDataHandler<?>, C extends AbstractConnectionManager<?>, AC extends BFAbstractAntiCheat<?, ?>> {
    @NotNull
    protected final H playerDataHandler;
    @NotNull
    private final UUID instanceUUID = UUID.randomUUID();
    private static final Component LOGIN_PREFIX = Component.literal((String)String.valueOf('\ue011')).append(" ").withColor(0xFFFFFF);
    private static final int field_2036 = 120;
    private int pingUpdateTimer = 20;
    private boolean isGameRulesSet = false;
    @NotNull
    private final AssetStore assetStore;
    @NotNull
    protected final AC antiCheat;
    @NotNull
    protected final C connectionManager;
    @NotNull
    protected final CloudRegistry cloudRegistry;

    public BFAbstractManager(@NotNull H playerDataHandler) {
        this.playerDataHandler = playerDataHandler;
        this.antiCheat = this.createAntiCheat();
        this.assetStore = this.createAssetStore();
        this.assetStore.init();
        this.assetStore.registerAssetType(MapAsset.class, "maps", MapAssetFactory::new);
        this.assetStore.registerAssetType(GameAsset.class, "games", GameAssetFactory::new);
        this.connectionManager = this.createConnectionManager();
        this.cloudRegistry = new CloudRegistry();
        CloudAchievements.registerAchievements((CloudRegistry)this.cloudRegistry);
        CloudItems.registerItems((CloudRegistry)this.cloudRegistry);
    }

    @NotNull
    protected abstract AssetStore createAssetStore();

    protected abstract AC createAntiCheat();

    @NotNull
    public final AC getAntiCheat() {
        return this.antiCheat;
    }

    @NotNull
    public C getConnectionManager() {
        return this.connectionManager;
    }

    @NotNull
    protected abstract C createConnectionManager();

    @OverridingMethodsMustInvokeSuper
    public void update(@Nullable MinecraftServer server, @NotNull ServerLevel level) {
        this.updateGames(server, level);
        this.updatePlayerPings(level);
    }

    private void updatePlayerPings(@NotNull ServerLevel level) {
        if (this.pingUpdateTimer++ >= 120) {
            this.pingUpdateTimer = 0;
            level.players().forEach(BFUtils::broadcastPlayerPing);
        }
    }

    private void updateGames(@Nullable MinecraftServer server, @NotNull ServerLevel level) {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        Map<String, GameAsset> map = assetRegistry.getEntries();
        if (map.isEmpty()) {
            return;
        }
        this.setGameRules((Level)level, server);
        map.values().forEach(game -> {
            AbstractGame<?, ?, ?> abstractGame = game.getGame();
            if (abstractGame != null) {
                abstractGame.update(this, level);
            }
        });
    }

    public boolean assignPlayerToGame(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull AbstractGame<?, ?, ?> game) {
        BFLog.log("Attempting to assign player '%s' to game '%s'.", player.getScoreboardName(), game.getUUID());
        UUID uUID = player.getUUID();
        if (!game.playerJoinTeam(this, TeamJoinType.NEW, level, player)) {
            return false;
        }
        Object obj = game.getPlayerManager();
        Set<UUID> set = ((AbstractGamePlayerManager)obj).getPlayerUUIDs();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uUID);
        MutableComponent mutableComponent = Component.literal((String)player.getScoreboardName());
        Component component = LOGIN_PREFIX;
        if (gameTeam != null) {
            mutableComponent = mutableComponent.withStyle(gameTeam.getStyleText());
            component = component.copy().withStyle(gameTeam.getStyleIcon());
        }
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.player.join", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GRAY);
        for (UUID uUID2 : set) {
            if (uUID2.equals(uUID)) continue;
            BFUtils.sendFancyMessage(uUID2, component, (Component)mutableComponent2);
        }
        BFLog.log("Sending initial game packet to player '%s'.", player.getScoreboardName());
        PacketUtils.sendToPlayer(new BFGamePacket(game, true), player);
        return true;
    }

    private void setGameRules(@NotNull Level level, @Nullable MinecraftServer server) {
        BFServerManager bFServerManager;
        if (this.isGameRulesSet) {
            return;
        }
        this.isGameRulesSet = true;
        GameRules gameRules = level.getGameRules();
        BFAbstractManager bFAbstractManager = this;
        if (bFAbstractManager instanceof BFServerManager && (bFServerManager = (BFServerManager)bFAbstractManager).isMatchMakingEnabled() && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            serverLevel.noSave = true;
        }
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DAYLIGHT)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DISABLE_RAIDS)).set(true, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DOBLOCKDROPS)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DOENTITYDROPS)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DOMOBSPAWNING)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_WEATHER_CYCLE)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DOFIRETICK)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_LOGADMINCOMMANDS)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_DO_TRADER_SPAWNING)).set(false, server);
        ((GameRules.BooleanValue)gameRules.getRule(GameRules.RULE_SHOWDEATHMESSAGES)).set(false, server);
        ((GameRules.IntegerValue)gameRules.getRule(GameRules.RULE_RANDOMTICKING)).set(0, server);
    }

    @NotNull
    public H getPlayerDataHandler() {
        return this.playerDataHandler;
    }

    @NotNull
    public abstract ServerCollisionTracker getCollisionTracker();

    @Nullable
    public AbstractGame<?, ?, ?> getGameWithPlayer(@NotNull Player player) {
        return this.getGameWithPlayer(player.getUUID());
    }

    @Nullable
    public AbstractGame<?, ?, ?> getGameWithPlayer(@NotNull UUID uuid) {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        Map<String, GameAsset> map = assetRegistry.getEntries();
        for (GameAsset gameAsset : map.values()) {
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null || !((AbstractGamePlayerManager)abstractGame.getPlayerManager()).hasPlayer(uuid)) continue;
            return abstractGame;
        }
        return null;
    }

    public void initPlayerForGame(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        AbstractGame<?, ?, ?> abstractGame = this.getGameWithPlayer(uuid);
        if (abstractGame == null) {
            return;
        }
        if (abstractGame instanceof IAllowsRespawning) {
            IAllowsRespawning iAllowsRespawning = (IAllowsRespawning)((Object)abstractGame);
            BFUtils.setPlayerStat(abstractGame, uuid, BFStats.SPAWN_PRO, iAllowsRespawning.getSpawnProLength());
        }
        ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).initPlayer(manager, dataHandler, level, player, uuid);
    }

    public boolean isPlayerInGame(@NotNull UUID uuid) {
        return this.getGameWithPlayer(uuid) != null;
    }

    public boolean gameHasPlayer(@NotNull UUID game, @NotNull UUID player) {
        AbstractGame<?, ?, ?> abstractGame = this.getGameWithPlayer(game);
        return abstractGame != null && ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).hasPlayer(player);
    }

    @Nullable
    public AbstractGame<?, ?, ?> retrieveGame(@NotNull String name) {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        Map<String, GameAsset> map = assetRegistry.getEntries();
        for (GameAsset gameAsset : map.values()) {
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null || !abstractGame.getName().equalsIgnoreCase(name)) continue;
            return abstractGame;
        }
        return null;
    }

    @Nullable
    public AbstractGame<?, ?, ?> retrieveGame(@NotNull UUID uuid) {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        if (assetRegistry == null) {
            return null;
        }
        Map<String, GameAsset> map = assetRegistry.getEntries();
        for (GameAsset gameAsset : map.values()) {
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null || !abstractGame.getUUID().equals(uuid)) continue;
            return abstractGame;
        }
        return null;
    }

    @NotNull
    public Map<String, GameAsset> getGames() {
        return this.assetStore.getRegistry(GameAsset.class).getEntries();
    }

    @NotNull
    public Map<String, GameAsset> getGamesInRotation() {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        Object2ObjectOpenHashMap object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
        for (Map.Entry<String, GameAsset> entry : assetRegistry.getEntries().entrySet()) {
            String string = entry.getKey();
            GameAsset gameAsset = entry.getValue();
            if (!gameAsset.isInRotation()) continue;
            object2ObjectOpenHashMap.put(string, gameAsset);
        }
        return object2ObjectOpenHashMap;
    }

    @Nullable
    public AbstractGame<?, ?, ?> getNearestActiveGame(@NotNull BlockPos blockPos, double maxDistance) {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        Map<String, GameAsset> map = assetRegistry.getEntries();
        AbstractGame<?, ?, ?> abstractGame = null;
        double d = -1.0;
        for (GameAsset gameAsset : map.values()) {
            double d2;
            FDSPose fDSPose;
            AbstractGame<?, ?, ?> abstractGame2 = gameAsset.getGame();
            if (abstractGame2 == null || abstractGame2.getStatus() != GameStatus.GAME || (fDSPose = ((AbstractGamePlayerManager)abstractGame2.getPlayerManager()).getLobbySpawn()) == null || !((d2 = fDSPose.distance(blockPos)) <= maxDistance) || !(d < 0.0) && !(d2 < d)) continue;
            abstractGame = abstractGame2;
            d = d2;
        }
        return abstractGame;
    }

    @NotNull
    public List<AbstractGame<?, ?, ?>> getAvailableGames(@NotNull BFGameType gameEntry, @Nullable AbstractGame<?, ?, ?> game) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        String string = gameEntry.getName();
        for (GameAsset gameAsset : this.getGamesInRotation().values()) {
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null || !abstractGame.getType().equalsIgnoreCase(string) || this.mapIsOccupied(abstractGame.getMap().getName(), game)) continue;
            objectArrayList.add(abstractGame);
        }
        return objectArrayList;
    }

    public boolean mapIsOccupied(@NotNull MapAsset map, @Nullable AbstractGame<?, ?, ?> game) {
        return this.mapIsOccupied(map.getName(), game);
    }

    public boolean mapIsOccupied(@NotNull String mapName, @Nullable AbstractGame<?, ?, ?> game) {
        AssetRegistry<GameAsset> assetRegistry = this.assetStore.getRegistry(GameAsset.class);
        Map<String, GameAsset> map = assetRegistry.getEntries();
        for (GameAsset gameAsset : map.values()) {
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null || abstractGame.equals(game)) continue;
            if (abstractGame.getStatus() != GameStatus.IDLE && abstractGame.getMap().getName().equalsIgnoreCase(mapName)) {
                return true;
            }
            MapVoteManager mapVoteManager = abstractGame.getMapVoteManager();
            if (!mapVoteManager.isEntry(mapName)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    public UUID getInstanceUUID() {
        return this.instanceUUID;
    }

    @NotNull
    public AssetStore getAssetStore() {
        return this.assetStore;
    }

    @NotNull
    public CloudRegistry getCloudRegistry() {
        return this.cloudRegistry;
    }

    public abstract boolean isMatchMakingEnabled();

    public abstract boolean isCloudConnectionVerified();

    @OverridingMethodsMustInvokeSuper
    public void connectionStatusChanged(@NotNull ConnectionStatus fromStatus, @NotNull ConnectionStatus toStatus, @NotNull ConnectionStatusContext context) {
        if (fromStatus.isClosed() && toStatus.isConnected()) {
            BFLog.log("[Cloud] Established connection to the cloud.", new Object[0]);
        } else if (fromStatus.isConnected() && toStatus.isClosed()) {
            BFLog.log("[Cloud] Lost connection to the cloud.", new Object[0]);
        }
    }
}

