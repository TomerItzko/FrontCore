/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server;

import com.boehmod.bflib.cloud.common.MatchData;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.mm.SearchRegion;
import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.cloud.connection.ConnectionStatusContext;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.server.PacketServerGameStatus;
import com.boehmod.bflib.fds.BFCFile;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.cloud.server.ServerConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.MMScheduleCache;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.server.ac.BFServerAntiCheat;
import com.boehmod.blockfront.server.ac.BFServerScreenshotManager;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.server.ac.bullet.ShotValidationManager;
import com.boehmod.blockfront.server.net.PacketListenerInteraction;
import com.boehmod.blockfront.server.net.PacketListenerPlayerAction;
import com.boehmod.blockfront.server.net.PacketListenerPlayerMove;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.server.player.ServerProfanityManager;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.EnvironmentUtils;
import io.netty.channel.ChannelHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.NotNull;

public final class BFServerManager
extends BFAbstractManager<ServerPlayerDataHandler, ServerConnectionManager, BFServerAntiCheat> {
    private static final Component field_6952 = Component.translatable((String)"bf.message.disconnect.mm.error.no.match").withStyle(ChatFormatting.RED);
    private static final Component field_6953 = Component.translatable((String)"bf.message.disconnect.mm.error", (Object[])new Object[]{field_6952});
    private static final Component field_6954 = Component.translatable((String)"bf.message.disconnect.mm.error.admin").withStyle(ChatFormatting.RED);
    private static final int GAMES_STATUS_UPDATE_INTERVAL = 100;
    @NotNull
    private final ServerProfanityManager profanityManager;
    @NotNull
    private final MMScheduleCache mmCache = new MMScheduleCache();
    @NotNull
    private final ServerCollisionTracker collisionTracker = new ServerCollisionTracker();
    @NotNull
    private String cloudUrl = "cloud.blockfrontmc.com";
    private boolean matchMakingShutdown = false;
    private boolean isMatchMakingEnabled = false;
    @NotNull
    private String matchMakingPassword;
    @NotNull
    private SearchRegion matchMakingRegion = SearchRegion.ALL;
    private int statusUpdateTimer = 0;

    public BFServerManager() {
        super(new ServerPlayerDataHandler());
        this.readMatchMakingSettings();
        this.profanityManager = new ServerProfanityManager(this);
    }

    @NotNull
    public ServerProfanityManager getProfanityManager() {
        return this.profanityManager;
    }

    @NotNull
    public MMScheduleCache getMatchMakingCache() {
        return this.mmCache;
    }

    private void readMatchMakingSettings() {
        MinecraftServer minecraftServer;
        BFCFile bFCFile = new BFCFile("server", BlockFront.DATA_FOLDER + "mm_server.properties");
        String string = bFCFile.getString("cloud.ip", "default", "Cloud IP, default for hard coded ip address");
        this.isMatchMakingEnabled = bFCFile.getBoolean("cloud.matchmaking", false);
        this.matchMakingPassword = bFCFile.getString("cloud.matchmaking.password", "password");
        this.matchMakingRegion = SearchRegion.fromId((String)bFCFile.getString("cloud.matchmaking.region", "all"));
        this.matchMakingShutdown = bFCFile.getBoolean("cloud.matchmaking.shutdown", true);
        if (!string.equals("default")) {
            this.cloudUrl = string;
        }
        if (this.isMatchMakingEnabled && (minecraftServer = EnvironmentUtils.getServer()) != null) {
            String string2 = "bf MM Server (P:'" + minecraftServer.getPort() + "' R:'" + this.matchMakingRegion.getId() + "')";
            minecraftServer.setMotd(string2);
        }
    }

    public void shutdown(@NotNull String reason) {
        this.sendSystemMessage("*** Shutting Down by BlockFront ***");
        this.sendSystemMessage(reason);
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer != null) {
            minecraftServer.halt(true);
        }
    }

    public void method_4575(@NotNull ServerPlayer serverPlayer) {
        ServerLevel serverLevel = serverPlayer.serverLevel();
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<BFServerAntiCheat, BFServerAntiCheat, BFServerAntiCheat> abstractGame = this.getGameWithPlayer(uUID);
        if (abstractGame != null) {
            abstractGame.removePlayer(this, serverLevel, serverPlayer);
        }
        if (EnvironmentUtils.isServer()) {
            ((ServerPlayerDataHandler)this.playerDataHandler).removeCloudData(uUID);
        }
        ((ServerPlayerDataHandler)this.playerDataHandler).removeData(uUID);
        ((BFServerScreenshotManager)((BFServerAntiCheat)this.antiCheat).getScreenshotManager()).unqueue(uUID);
    }

    public void handlePlayerJoin(@NotNull ServerPlayer player) {
        Object object;
        ServerLevel serverLevel;
        MinecraftServer minecraftServer;
        UUID uUID = player.getUUID();
        ServerPlayerDataHandler serverPlayerDataHandler = (ServerPlayerDataHandler)this.getPlayerDataHandler();
        if (!this.isMatchMakingEnabled()) {
            minecraftServer = EnvironmentUtils.getServer();
            assert (minecraftServer != null);
            serverLevel = minecraftServer.getLevel(Level.OVERWORLD);
            if (serverLevel != null) {
                serverPlayerDataHandler.method_862(this, (Level)serverLevel);
            }
        }
        if (this.isMatchMakingEnabled()) {
            minecraftServer = player.serverLevel();
            serverLevel = (ServerLevelData)minecraftServer.getLevelData();
            object = serverLevel.getSpawnPos();
            player.setPos((double)object.getX(), (double)object.getY(), (double)object.getZ());
            player.teleportTo((double)object.getX(), (double)object.getY(), (double)object.getZ());
            BFUtils.initPlayerForGame(serverPlayerDataHandler, (ServerLevel)minecraftServer, player);
            if (!this.assignPlayerToMatch(player, uUID)) {
                if (!player.hasPermissions(3)) {
                    BFLog.log("Kicking player '%s' (%s) from the server, no match found!", player.getScoreboardName(), uUID);
                    player.sendSystemMessage(field_6953);
                    player.connection.disconnect(field_6953);
                    return;
                }
                player.sendSystemMessage(field_6953);
                player.sendSystemMessage(field_6954);
            }
        }
        minecraftServer = player.connection.getConnection();
        BFLog.log("Added custom packet listener for player connection '" + String.valueOf(player.getUUID()) + "'.", new Object[0]);
        serverLevel = minecraftServer.channel().pipeline();
        serverLevel.addBefore("packet_handler", "mod_packet_handler_actions", (ChannelHandler)new PacketListenerPlayerAction());
        serverLevel.addBefore("packet_handler", "mod_packet_handler_interaction", (ChannelHandler)new PacketListenerInteraction());
        serverLevel.addBefore("packet_handler", "mod_packet_handler_move", (ChannelHandler)new PacketListenerPlayerMove());
        object = (ServerConnectionManager)this.getConnectionManager();
        CloudRequestManager cloudRequestManager = ((AbstractConnectionManager)object).getRequester();
        cloudRequestManager.push(RequestType.PLAYER_DATA, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
    }

    private boolean assignPlayerToMatch(@NotNull ServerPlayer player, @NotNull UUID uUID) {
        String string = player.getScoreboardName();
        MMScheduleCache mMScheduleCache = this.getMatchMakingCache();
        BFLog.log(String.format("Attempting to assign player '%s' (%s) to scheduled match...", string, uUID), new Object[0]);
        UUID uUID2 = mMScheduleCache.getAssignedMatch(player.getUUID());
        if (uUID2 == null) {
            BFLog.log("Failed to assign player to scheduled match, no match found!", new Object[0]);
            return false;
        }
        BFLog.log(String.format("Found scheduled match '%s' for player '%s' (%s)", uUID2, string, uUID), new Object[0]);
        AbstractGame<?, ?, ?> abstractGame = this.retrieveGame(uUID2);
        if (abstractGame != null && this.assignPlayerToGame(player.serverLevel(), player, abstractGame)) {
            BFLog.log(String.format("Assigned player '%s' (%s) to match '%s'", string, uUID, abstractGame.getUUID()), new Object[0]);
            return true;
        }
        BFLog.log("Failed to assign player to scheduled match, no game found!", new Object[0]);
        return false;
    }

    public void shutdown() {
        ((ServerConnectionManager)this.connectionManager).shutdown();
    }

    public void sendSystemMessage(@NotNull String message) {
        MinecraftServer minecraftServer;
        if (EnvironmentUtils.isServer() && (minecraftServer = EnvironmentUtils.getServer()) != null) {
            minecraftServer.sendSystemMessage((Component)Component.literal((String)message));
        }
    }

    private void tickGamesStatus() {
        if (this.statusUpdateTimer++ > 100) {
            this.statusUpdateTimer = 0;
            this.updateGamesStatus();
        }
    }

    private void updateGamesStatus() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        Collection<GameAsset> collection = this.getGamesInRotation().values();
        for (GameAsset gameAsset : collection) {
            BFGameType bFGameType;
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null || (bFGameType = BFGameType.getByName(abstractGame.getType())) == null) continue;
            Object obj = abstractGame.getPlayerManager();
            int n = abstractGame.hasPlayerLimit() ? ((AbstractGamePlayerManager)obj).getMaxPlayers() : 512;
            objectArrayList.add((Object)new MatchData(((AbstractGamePlayerManager)obj).getPlayerUUIDs(), abstractGame.getUUID(), abstractGame.getMap().getName(), bFGameType.getSearchGame(), n, ((AbstractGamePlayerManager)obj).isAcceptingPlayers()));
        }
        ((ServerConnectionManager)this.connectionManager).sendPacket((IPacket)new PacketServerGameStatus((ObjectList)objectArrayList, this.getMatchMakingRegion()));
    }

    @NotNull
    public String getMatchMakingPassword() {
        return this.matchMakingPassword;
    }

    @NotNull
    public SearchRegion getMatchMakingRegion() {
        return this.matchMakingRegion;
    }

    public boolean getMatchMakingShutdown() {
        return this.matchMakingShutdown;
    }

    @NotNull
    public String getCloudUrl() {
        return this.cloudUrl;
    }

    @Override
    public boolean isMatchMakingEnabled() {
        return this.isMatchMakingEnabled;
    }

    @Override
    public boolean isCloudConnectionVerified() {
        return ((ServerConnectionManager)this.connectionManager).getStatus().isVerified();
    }

    @Override
    public void connectionStatusChanged(@NotNull ConnectionStatus fromStatus, @NotNull ConnectionStatus toStatus, @NotNull ConnectionStatusContext context) {
        super.connectionStatusChanged(fromStatus, toStatus, context);
    }

    @Override
    @NotNull
    protected AssetStore createAssetStore() {
        return new AssetStore(this, false);
    }

    @Override
    public BFServerAntiCheat createAntiCheat() {
        return new BFServerAntiCheat();
    }

    @Override
    @NotNull
    protected ServerConnectionManager createConnectionManager() {
        return new ServerConnectionManager(this);
    }

    @Override
    public void update(@Nullable MinecraftServer server, @NotNull ServerLevel level) {
        super.update(server, level);
        ((BFServerAntiCheat)this.antiCheat).onUpdate(this);
        ((ServerConnectionManager)this.connectionManager).onUpdate();
        this.collisionTracker.update(this, level.getAllEntities(), level.getGameTime(), null);
        ShotValidationManager.runChecks(this.collisionTracker);
        this.tickGamesStatus();
    }

    @Override
    @NotNull
    public ServerCollisionTracker getCollisionTracker() {
        return this.collisionTracker;
    }
}

