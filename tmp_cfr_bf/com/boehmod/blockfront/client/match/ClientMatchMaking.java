/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.match;

import com.boehmod.bflib.cloud.common.mm.MatchParty;
import com.boehmod.bflib.cloud.common.mm.SearchGame;
import com.boehmod.bflib.cloud.common.mm.SearchRegion;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMSearch;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.math.MathUtils;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class ClientMatchMaking {
    private boolean searching = false;
    @NotNull
    public SearchGame searchGame = SearchGame.DOMINATION;
    @NotNull
    public SearchRegion searchRegion = SearchRegion.ALL;
    public float field_2272;
    public float field_2276 = 0.0f;
    public float field_2277;
    public float field_2278 = 0.0f;
    @Nullable
    private InetSocketAddress address;
    private boolean inMatch;

    public void update(@NotNull Minecraft minecraft, @Nullable ClientLevel clientLevel, @Nullable LocalPlayer localPlayer, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        boolean bl = this.searching;
        Optional optional = playerCloudData.getParty();
        if (optional.isPresent()) {
            bl = ((MatchParty)optional.get()).isSearching();
        }
        if (this.searching != bl) {
            this.searching = bl;
        }
        this.field_2278 = this.field_2277;
        this.field_2277 = MathUtils.lerpf2(this.field_2277, this.searching ? 1.0f : 0.0f, 0.1f);
        this.method_1910(minecraft, clientLevel, localPlayer, bFClientManager, clientPlayerDataHandler);
    }

    @NotNull
    public SearchGame getSearchGame() {
        return this.searchGame;
    }

    public void setSearchGame(@NotNull SearchGame searchGame) {
        this.searchGame = searchGame;
    }

    @NotNull
    public SearchRegion getSearchRegion() {
        return this.searchRegion;
    }

    public void setSearchRegion(@NotNull SearchRegion searchRegion) {
        this.searchRegion = searchRegion;
    }

    private void method_1910(@NotNull Minecraft minecraft, @Nullable ClientLevel clientLevel, @Nullable LocalPlayer localPlayer, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        this.field_2276 = this.field_2272;
        if (this.inMatch) {
            ServerData serverData;
            if (localPlayer != null && (serverData = localPlayer.connection.getServerData()) != null) {
                Connection connection = localPlayer.connection.getConnection();
                connection.disconnect((Component)Component.empty());
                connection.setReadOnly();
                connection.handleDisconnection();
                this.method_1914(minecraft, clientLevel, localPlayer, bFClientManager, clientPlayerDataHandler);
                return;
            }
            if (this.field_2272 >= 1.0f) {
                this.method_1914(minecraft, clientLevel, localPlayer, bFClientManager, clientPlayerDataHandler);
            }
            this.field_2272 = MathUtils.moveTowards(this.field_2272, 1.0f, 0.05f);
        } else {
            this.field_2272 = MathUtils.moveTowards(this.field_2272, 0.0f, 0.025f);
        }
    }

    private void method_1914(@NotNull Minecraft minecraft, @Nullable ClientLevel clientLevel, @Nullable LocalPlayer localPlayer, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        if (!this.inMatch) {
            return;
        }
        this.inMatch = false;
        if (this.address == null) {
            BFLog.log("Failed to join MM game due to missing server address or match UUID", new Object[0]);
            return;
        }
        String string = this.address.getHostString();
        int n = this.address.getPort();
        if (clientLevel != null) {
            clientLevel.disconnect();
            bFClientManager.setCurrentGame(minecraft, clientPlayerDataHandler, localPlayer, clientLevel, null);
        }
        try {
            Thread.sleep(10 + ThreadLocalRandom.current().nextInt(1000));
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bFClientManager.getGame() == null) {
            BFLog.log("Joining MM game on address '" + string + ":" + n + "'", new Object[0]);
            ServerData serverData = new ServerData("mm-server", "", ServerData.Type.OTHER);
            ServerAddress serverAddress = new ServerAddress(string, n);
            ConnectScreen.startConnecting((Screen)new LobbyTitleScreen(), (Minecraft)minecraft, (ServerAddress)serverAddress, (ServerData)serverData, (boolean)false, null);
        }
        this.setSearching(false);
    }

    public void join(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull InetSocketAddress address) {
        SoundManager soundManager = minecraft.getSoundManager();
        this.address = address;
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)manager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        playerCloudData.getParty().ifPresent(party -> {
            PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(party.getHost());
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.party.mm.match.new", (Object[])new Object[]{playerCloudData.getUsername()});
            BFNotification.show(minecraft, (Component)mutableComponent);
        });
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_JOIN.get()), (float)1.0f));
        this.inMatch = true;
    }

    public void reset() {
        this.inMatch = false;
        this.address = null;
    }

    public float getFadeAlpha(float delta) {
        return MathUtils.lerpf1(this.field_2272, this.field_2276, delta);
    }

    public float method_1912(float f) {
        return MathUtils.lerpf1(this.field_2277, this.field_2278, f);
    }

    public boolean isSearching() {
        return this.searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public void search(@NotNull Minecraft minecraft, @NotNull BFClientManager manager) {
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)manager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)manager.getConnectionManager();
        SoundManager soundManager = minecraft.getSoundManager();
        BFLog.log("Attempting to search for game...", new Object[0]);
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_SEARCH.get()), (float)1.0f));
        if (!playerCloudData.hasCompletedBootcamp()) {
            this.setSearchGame(BFGameType.BOOTCAMP.getSearchGame());
        }
        ((ClientConnectionManager)manager.getConnectionManager()).updateParty(clientPlayerDataHandler, minecraft, manager);
        clientConnectionManager.sendPacket((IPacket)new PacketMMSearch(this.getSearchGame(), this.getSearchRegion()));
        this.setSearching(true);
    }
}

