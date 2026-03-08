/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud.client;

import com.boehmod.bflib.cloud.common.ConnectionType;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.discord.DiscordEvent;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.PacketClientUpdateStatus;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyEditSettings;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.HardwareIdentifier;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.match.ClientMatchMaking;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.player.ClientFriendManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientCloudAuth;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class ClientConnectionManager
extends AbstractConnectionManager<BFClientManager> {
    public static final int field_1800 = 80;
    public static final int field_1801 = 120;
    public static final int field_1802 = 40;
    @NotNull
    private final String location;
    @NotNull
    private final ClientCloudAuth auth = new ClientCloudAuth();
    @NotNull
    private final ObjectList<DiscordEvent> discordEvents = new ObjectArrayList();
    public int usersOnline = 0;
    private int field_1804 = 0;
    private int field_1805 = 0;
    private int field_1806 = 0;
    private boolean field_1808 = false;
    @NotNull
    private ConnectionMode connectionMode = ConnectionMode.UNDECIDED;

    public ClientConnectionManager(@NotNull String string, @NotNull BFClientManager bFClientManager) {
        super(bFClientManager);
        this.location = string;
    }

    @NotNull
    public ConnectionMode getConnectionMode() {
        return this.connectionMode;
    }

    public void setConnectionMode(@NotNull ConnectionMode connectionMode) {
        this.connectionMode = connectionMode;
    }

    public void updateParty(@NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFClientManager manager) {
        PlayerCloudData playerCloudData = dataHandler.getCloudData(minecraft);
        Optional optional = playerCloudData.getParty();
        UUID uUID = minecraft.getUser().getProfileId();
        optional.ifPresent(party -> {
            if (party.isHost(uUID)) {
                ClientMatchMaking clientMatchMaking = manager.getMatchMaking();
                this.sendPacket((IPacket)new PacketPartyEditSettings(clientMatchMaking.getSearchGame(), clientMatchMaking.getSearchRegion()));
            }
        });
    }

    public void update(@NotNull Minecraft minecraft, @NotNull PlayerCloudData playerCloudData, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super.onUpdate();
        if (this.getConnection().getStatus().isConnected() && ((BFClientAntiCheat)((BFClientManager)this.manager).getAntiCheat()).isActive()) {
            this.disconnect("", true);
            return;
        }
        this.method_1397(minecraft, clientPlayerDataHandler, playerCloudData);
    }

    private void method_1397(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PlayerCloudData playerCloudData) {
        if (this.getConnection().getStatus().isClosed()) {
            this.field_1808 = false;
            return;
        }
        if (this.field_1804++ >= 80) {
            this.field_1804 = 0;
            this.method_1400(minecraft, clientPlayerDataHandler, playerCloudData);
        }
        if (this.field_1806++ >= 120) {
            this.field_1806 = 0;
            this.method_1388();
        }
        if (this.field_1805++ >= 40) {
            this.field_1805 = 0;
            this.method_1393(playerCloudData);
        }
    }

    private void method_1400(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PlayerCloudData playerCloudData) {
        CloudRequestManager cloudRequestManager = this.getRequester();
        UUID uUID = this.getUUID();
        cloudRequestManager.push(RequestType.PLAYER_DATA, uUID);
        cloudRequestManager.push(RequestType.PLAYER_STATUS, uUID);
        UUID uUID2 = playerCloudData.getClanId();
        if (uUID2 != null) {
            cloudRequestManager.push(RequestType.CLAN_DATA, uUID2);
        }
        ClientFriendManager clientFriendManager = ((BFClientManager)this.manager).getFriendManager();
        clientFriendManager.getFriends().forEach(clientPlayerDataHandler::getPlayerData);
        clientFriendManager.getFriendRequests().forEach(clientPlayerDataHandler::getPlayerData);
        for (UUID uUID3 : clientFriendManager.getFriends()) {
            cloudRequestManager.push(RequestType.PLAYER_STATUS, uUID3);
        }
        if (!this.field_1808) {
            this.field_1808 = true;
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
            cloudRequestManager.push(RequestType.PLAYER_CHALLENGES, uUID);
            cloudRequestManager.push(RequestType.EVENTS);
        }
        if (((PlayerCloudInventory)clientPlayerDataHandler.getCloudData(minecraft).getInventory()).getItems().isEmpty()) {
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
        }
        this.sendPacket((IPacket)new PacketClientUpdateStatus());
    }

    public void method_1393(@NotNull PlayerCloudData playerCloudData) {
        playerCloudData.getParty().ifPresent(matchParty -> {
            ClientConnectionManager clientConnectionManager = (ClientConnectionManager)((BFClientManager)this.manager).getConnectionManager();
            CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
            for (UUID uUID : matchParty.getPlayers()) {
                cloudRequestManager.push(RequestType.PLAYER_DATA, uUID);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
            }
        });
    }

    @Override
    @NotNull
    public UUID getUUID() {
        return Objects.requireNonNull(Minecraft.getInstance().getUser().getProfileId());
    }

    @Override
    @NotNull
    public String getUsername() {
        return Minecraft.getInstance().getUser().getName();
    }

    @Override
    public byte[] getHardwareId() {
        return HardwareIdentifier.get();
    }

    @Override
    @NotNull
    public String getVersion() {
        return BlockFront.VERSION;
    }

    @Override
    @NotNull
    public String getVersionHash() {
        String string = ((BFClientManager)this.manager).getVersionChecker().getVersionHash();
        return string != null ? string : "Unknown";
    }

    @Override
    @NotNull
    public ConnectionType getType() {
        return ConnectionType.PLAYER;
    }

    @Override
    public int getMinecraftPort() {
        return 0;
    }

    @Override
    @NotNull
    public String getLocation() {
        return this.location;
    }

    @Override
    @NotNull
    public String getServerId() {
        return this.auth.getServerId();
    }

    @Override
    public int getCloudPort() {
        return 1924;
    }

    @Override
    @NotNull
    protected CloudRequestManager<BFClientManager> createRequester(BFClientManager bFClientManager) {
        return new CloudRequestManager<BFClientManager>(bFClientManager);
    }

    @Override
    public void sendPacket(@NotNull IPacket iPacket) {
        BFConnection bFConnection = this.getConnection();
        if (bFConnection.getStatus().isConnected()) {
            bFConnection.sendPacket(iPacket);
        }
    }

    @Override
    public void disconnect(@NotNull String reason) {
        super.disconnect(reason);
        MutableComponent mutableComponent = Component.literal((String)reason).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.cloud.disconnect.reason", (Object[])new Object[]{mutableComponent});
        BFNotification.show(Minecraft.getInstance(), (Component)mutableComponent2);
        ((ClientPlayerDataHandler)((BFClientManager)this.manager).getPlayerDataHandler()).invalidateCache();
    }

    @Override
    public boolean updateMatchMaking() {
        if (this.connectionMode != ConnectionMode.ONLINE) {
            return false;
        }
        if (((BFClientManager)this.manager).getVersionChecker().hasUpdate()) {
            return false;
        }
        if (((BFClientAntiCheat)((BFClientManager)this.manager).getAntiCheat()).isActive()) {
            return false;
        }
        this.auth.tryAuthenticate(Minecraft.getInstance());
        return this.auth.isAuthenticated();
    }

    @NotNull
    public ObjectList<DiscordEvent> method_1387() {
        return ObjectLists.unmodifiable(this.discordEvents);
    }

    public void method_1395(@NotNull Collection<DiscordEvent> collection) {
        collection.clear();
        this.discordEvents.addAll(collection);
    }

    public void method_1388() {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)((BFClientManager)this.manager).getConnectionManager();
        CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
        ClientFriendManager clientFriendManager = ((BFClientManager)this.manager).getFriendManager();
        clientFriendManager.getFriends().forEach(uUID -> cloudRequestManager.push(RequestType.PLAYER_DATA, (UUID)uUID));
        clientFriendManager.getFriendRequests().forEach(uUID -> cloudRequestManager.push(RequestType.PLAYER_DATA, (UUID)uUID));
    }
}

