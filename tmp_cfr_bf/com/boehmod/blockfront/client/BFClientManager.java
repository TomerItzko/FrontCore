/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.cloud.connection.ConnectionStatusContext;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.client.BFClient;
import com.boehmod.blockfront.client.BFKeyMappings;
import com.boehmod.blockfront.client.BFVersionChecker;
import com.boehmod.blockfront.client.SkinFetcher;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.corpse.ClientCorpseManager;
import com.boehmod.blockfront.client.data.BFCreditsEntry;
import com.boehmod.blockfront.client.gui.BFCrosshair;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.match.ClientMatchMaking;
import com.boehmod.blockfront.client.particle.BFParticleManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientFriendManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.PlayerChallengeManager;
import com.boehmod.blockfront.client.render.PlayerRendererChecker;
import com.boehmod.blockfront.client.render.minimap.MinimapRendering;
import com.boehmod.blockfront.client.screen.PopupScreen;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.client.sound.BFMusicManager;
import com.boehmod.blockfront.client.sound.EnvironmentManager;
import com.boehmod.blockfront.client.world.SkyTracker;
import com.boehmod.blockfront.cloud.client.BFClientCloudPacketHandlers;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.BFCommonCloudPacketHandlers;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.world.BiomeManager;
import com.boehmod.blockfront.discord.BFDiscordManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameCinematics;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.unnamed.BF_1177;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public final class BFClientManager
extends BFAbstractManager<ClientPlayerDataHandler, ClientConnectionManager, BFClientAntiCheat> {
    @NotNull
    private final ServerCollisionTracker field_6835 = new ServerCollisionTracker();
    @NotNull
    private static final Component CONNECTION_ESTABLISHED_MESSAGE = Component.translatable((String)"bf.cloud.message.connection.established");
    @NotNull
    private static final Component CONNECTION_LOST_MESSAGE = Component.translatable((String)"bf.cloud.message.connection.lost");
    @Nullable
    private AbstractGame<?, ?, ?> game;
    @Nullable
    private AbstractGameClient<?, ?> gameClient;
    @NotNull
    private final PlayerRendererChecker playerRendererChecker = new PlayerRendererChecker();
    @NotNull
    private final BFMusicManager musicManager = new BFMusicManager();
    private static final int field_2564 = 4;
    @NotNull
    private static final Component SAVING_LEVEL_MESSAGE = Component.translatable((String)"menu.savingLevel");
    @NotNull
    private final BFVersionChecker versionChecker;
    @NotNull
    private final GameCinematics cinematics;
    @NotNull
    private final BFCrosshair.Resource crosshairResource;
    @NotNull
    private final BFCreditsEntry.Resource creditsEntryResource;
    @NotNull
    private final SkyTracker skyTracker;
    @NotNull
    private final ClientFriendManager friendManager;
    @NotNull
    private final PlayerChallengeManager challengeManager;
    @NotNull
    private final BF_1177 field_6706;
    @NotNull
    private final BFDiscordManager discordManager;
    @NotNull
    private final ClientMatchMaking matchMaking;
    @NotNull
    private final ClientCorpseManager corpseManager;
    @NotNull
    private final BFParticleManager particleManager;
    @NotNull
    private final EnvironmentManager environmentManager;
    @NotNull
    private final Minecraft minecraft;
    @NotNull
    private String dataPath = "";
    private boolean corpsePhysicsShutdown = false;
    private int chunkReloadTimer = 20;
    @NotNull
    private final SkinFetcher skinFetcher;

    public BFClientManager(@NotNull Minecraft minecraft) {
        super(new ClientPlayerDataHandler());
        this.minecraft = minecraft;
        this.setDataPath(String.valueOf(minecraft.gameDirectory) + "/blockfront/");
        File file = new File(this.getClientDataPath());
        if (!file.exists() && file.mkdirs()) {
            BFLog.log("Could not find client directory. Creating...", new Object[0]);
        }
        String string = "cloud.blockfrontmc.com";
        PlayerCloudData playerCloudData = ((ClientPlayerDataHandler)this.getPlayerDataHandler()).getCloudData(minecraft);
        this.discordManager = new BFDiscordManager(this, playerCloudData);
        this.versionChecker = new BFVersionChecker("cloud.blockfrontmc.com");
        this.skyTracker = new SkyTracker();
        this.friendManager = new ClientFriendManager();
        this.challengeManager = new PlayerChallengeManager();
        this.field_6706 = new BF_1177(this);
        this.crosshairResource = new BFCrosshair.Resource(minecraft);
        this.creditsEntryResource = new BFCreditsEntry.Resource(minecraft);
        this.cinematics = new GameCinematics(this);
        this.matchMaking = new ClientMatchMaking();
        this.corpseManager = new ClientCorpseManager();
        this.skinFetcher = new SkinFetcher();
        this.particleManager = BFParticleManager.getInstance();
        this.environmentManager = new EnvironmentManager();
        BFCommonCloudPacketHandlers.register();
        BFClientCloudPacketHandlers.register();
    }

    @Nullable
    public static BFClientManager getInstance() {
        return BFClient.getManager();
    }

    public static void showPopup(@NotNull Component title, @NotNull Component message, @NotNull PopupType type, PopupButton ... buttons) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen((Screen)new PopupScreen(minecraft.screen, title, message, type, buttons));
    }

    private static void setBiomeColors(@NotNull Minecraft minecraft, @NotNull LocalPlayer player, @NotNull ClientLevel level, @Nullable AbstractGame<?, ?, ?> game) {
        int n;
        int n2;
        BFClientManager.updateBiomeColors(minecraft, player, level);
        if (game == null) {
            return;
        }
        Holder holder = level.getBiome(player.blockPosition());
        if (!holder.isBound()) {
            return;
        }
        BiomeSpecialEffects biomeSpecialEffects = ((Biome)holder.value()).getSpecialEffects();
        boolean bl = false;
        MapEnvironment mapEnvironment = game.getMapEnvironment();
        if (mapEnvironment.hasCustomSkyColor() && (n2 = biomeSpecialEffects.getSkyColor()) != (n = mapEnvironment.getCustomSkyColor())) {
            BFLog.log("Replacing biome sky color with '%d'.", n);
            BiomeManager.setSkyColor((Holder<Biome>)holder, n2);
            bl = true;
            biomeSpecialEffects.skyColor = n;
        }
        if (mapEnvironment.hasCustomWaterColor()) {
            n2 = mapEnvironment.getCustomWaterColor();
            n = biomeSpecialEffects.getWaterColor();
            if (n != n2) {
                BFLog.log("Replacing biome water color with '%d'.", n2);
                BiomeManager.setWaterColor((Holder<Biome>)holder, n);
                bl = true;
                biomeSpecialEffects.waterColor = n2;
            }
        }
        if (bl) {
            minecraft.levelRenderer.allChanged();
        }
    }

    private static void updateBiomeColors(@NotNull Minecraft minecraft, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        int n;
        Holder holder = level.getBiome(player.blockPosition());
        if (!holder.isBound()) {
            return;
        }
        BiomeSpecialEffects biomeSpecialEffects = ((Biome)holder.value()).getSpecialEffects();
        boolean bl = false;
        int n2 = BiomeManager.getSkyColor((Holder<Biome>)holder);
        if (n2 != -1) {
            BFLog.log("Restoring biome sky color to '%d'.", n2);
            biomeSpecialEffects.skyColor = n2;
            BiomeManager.resetBiome((Holder<Biome>)holder);
            bl = true;
        }
        if ((n = BiomeManager.getWaterColor((Holder<Biome>)holder)) != -1) {
            BFLog.log("Restoring biome water color to '%d'.", n);
            biomeSpecialEffects.waterColor = n;
            BiomeManager.resetBiome((Holder<Biome>)holder);
            bl = true;
        }
        if (bl) {
            minecraft.levelRenderer.allChanged();
        }
    }

    public void disconnect(@NotNull Minecraft minecraft, @NotNull ClientLevel level) {
        boolean bl = minecraft.isLocalServer();
        level.disconnect();
        if (bl) {
            minecraft.disconnect((Screen)new GenericMessageScreen(SAVING_LEVEL_MESSAGE));
        } else {
            minecraft.disconnect();
        }
        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            minecraft.setScreen((Screen)titleScreen);
        } else {
            minecraft.setScreen((Screen)new JoinMultiplayerScreen((Screen)titleScreen));
        }
    }

    @NotNull
    public BFMusicManager getMusicManager() {
        return this.musicManager;
    }

    public void update(@NotNull Minecraft minecraft, @NotNull Random random, @Nullable ClientLevel clientLevel, @Nullable LocalPlayer localPlayer, @NotNull PlayerCloudData playerCloudData, @NotNull BFClientPlayerData bFClientPlayerData, float f, @NotNull Vec3 vec3, @NotNull BlockPos blockPos) {
        BFKeyMappings.update(minecraft);
        ((ClientConnectionManager)this.connectionManager).update(minecraft, playerCloudData, (ClientPlayerDataHandler)this.playerDataHandler);
        this.friendManager.update((ClientConnectionManager)this.connectionManager, playerCloudData);
        this.field_6706.method_5639(minecraft, clientLevel);
        this.challengeManager.update(minecraft, (ClientConnectionManager)this.connectionManager);
        this.versionChecker.onUpdate();
        this.cinematics.update(minecraft, localPlayer, clientLevel);
        this.discordManager.update(playerCloudData);
        this.matchMaking.update(minecraft, clientLevel, localPlayer, this, (ClientPlayerDataHandler)this.playerDataHandler);
        this.musicManager.update(this);
        this.playerRendererChecker.update(minecraft, minecraft.font, minecraft.gameRenderer.itemInHandRenderer);
        if (clientLevel != null && localPlayer != null) {
            this.updateLevel(minecraft, clientLevel, localPlayer);
        }
        if (clientLevel == null && this.corpsePhysicsShutdown) {
            this.closeCorpsePhysics(bFClientPlayerData);
            this.corpsePhysicsShutdown = false;
        } else if (clientLevel != null && !this.corpsePhysicsShutdown) {
            this.initCorpsePhysics(clientLevel);
            this.corpsePhysicsShutdown = true;
        }
        if (this.game != null) {
            this.environmentManager.method_3178(minecraft, this.game);
            Set<UUID> set = ((AbstractGamePlayerManager)this.game.getPlayerManager()).getPlayerUUIDs();
            if (this.gameClient != null && localPlayer != null && clientLevel != null) {
                this.gameClient.update(minecraft, random, clientLevel.random, localPlayer, clientLevel, this, bFClientPlayerData, set, f, vec3, blockPos);
            }
        }
        if (((ClientConnectionManager)this.connectionManager).getConnection().getStatus().isConnected() && ((BFClientAntiCheat)this.antiCheat).isActive()) {
            if (clientLevel != null && !minecraft.isLocalServer()) {
                this.disconnect(minecraft, clientLevel);
            }
            ((ClientConnectionManager)this.connectionManager).getConnection().disconnect("Invalid Game State.", true);
        }
    }

    private void updateLevel(@NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player) {
        this.field_6835.update(this, level.entitiesForRendering(), level.getGameTime(), (LivingEntity)player);
        this.particleManager.onUpdate();
        this.corpseManager.update(minecraft, this, player, level);
        this.skyTracker.update(minecraft, level, player);
        if (this.chunkReloadTimer > 0) {
            --this.chunkReloadTimer;
            if (this.chunkReloadTimer == 0) {
                BFLog.log("Reloading chunks via the client manager...", new Object[0]);
                minecraft.levelRenderer.allChanged();
            }
        }
    }

    private void initCorpsePhysics(@NotNull ClientLevel level) {
        this.corpseManager.instantiateCollider(level);
    }

    private void closeCorpsePhysics(@NotNull BFClientPlayerData data) {
        this.corpseManager.freeCollider();
        this.cinematics.stop(this.minecraft);
        this.particleManager.clearAll();
        this.field_6706.method_5638(this.minecraft);
        this.resetGameState(this.minecraft, data, (ClientPlayerDataHandler)this.playerDataHandler, null, null);
        LobbyTitleScreen.updateDisplayTextures((ClientPlayerDataHandler)this.playerDataHandler);
    }

    public void queueChunkReload() {
        this.chunkReloadTimer = 4;
    }

    @NotNull
    public ClientFriendManager getFriendManager() {
        return this.friendManager;
    }

    @NotNull
    public PlayerChallengeManager getChallengeManager() {
        return this.challengeManager;
    }

    @NotNull
    public String getDataPath() {
        return this.dataPath;
    }

    public void setDataPath(@NotNull String path) {
        this.dataPath = path;
        File file = new File(this.dataPath);
        if (!file.mkdirs()) {
            BFLog.log("Failed to create BlockFront directory! (Does it already exist?)", new Object[0]);
        }
    }

    @NotNull
    public String getClientDataPath() {
        return this.dataPath + "client/";
    }

    @NotNull
    public BFCrosshair.Resource getCrosshairResource() {
        return this.crosshairResource;
    }

    @NotNull
    public BFCreditsEntry.Resource getCreditsEntryResource() {
        return this.creditsEntryResource;
    }

    @NotNull
    public SkyTracker getSkyTracker() {
        return this.skyTracker;
    }

    @NotNull
    public BFVersionChecker getVersionChecker() {
        return this.versionChecker;
    }

    @NotNull
    public ClientCorpseManager getCorpseManager() {
        return this.corpseManager;
    }

    @NotNull
    public BFParticleManager getParticleManager() {
        return this.particleManager;
    }

    @Override
    @NotNull
    protected ClientConnectionManager createConnectionManager() {
        String string = "cloud.blockfrontmc.com";
        return new ClientConnectionManager("cloud.blockfrontmc.com", this);
    }

    @Override
    @NotNull
    public ServerCollisionTracker getCollisionTracker() {
        return this.field_6835;
    }

    @Nullable
    public AbstractGame<?, ?, ?> getGame() {
        return this.game;
    }

    @Nullable
    public AbstractGameClient<?, ?> getGameClient() {
        return this.gameClient;
    }

    public void setCurrentGame(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @Nullable LocalPlayer player, @Nullable ClientLevel level, @Nullable AbstractGame<?, ?, ?> game) {
        if (this.game == game) {
            return;
        }
        if (this.game != null && game != null && this.game.getUUID().equals(game.getUUID())) {
            return;
        }
        if (this.game != null) {
            this.game.stop();
        }
        if (this.gameClient != null) {
            this.gameClient.stop();
        }
        this.environmentManager.method_5866(minecraft);
        this.game = game;
        this.initNewGame(minecraft, dataHandler, player, level);
    }

    private void initNewGame(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @Nullable LocalPlayer player, @Nullable ClientLevel level) {
        Object object;
        if (minecraft.screen instanceof MatchSummaryScreen) {
            minecraft.setScreen(null);
        }
        if (this.game != null) {
            this.gameClient = this.game.createGameClient(this);
            if (this.game.getStatus() == GameStatus.POST_GAME) {
                this.gameClient.getSummaryManager().updateScreens(true);
            }
            object = ((ClientConnectionManager)this.connectionManager).getRequester();
            for (UUID uUID : ((AbstractGamePlayerManager)this.game.getPlayerManager()).getPlayerUUIDs()) {
                ((CloudRequestManager)object).push(RequestType.PLAYER_DATA, uUID);
                ((CloudRequestManager)object).push(RequestType.PLAYER_INVENTORY, uUID);
                ((CloudRequestManager)object).push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
                ((CloudRequestManager)object).push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
            }
            this.game.getMapEnvironment().reset(minecraft);
            if (level != null) {
                MapEnvironment mapEnvironment = this.game.getMapEnvironment();
                int n = mapEnvironment.getTime();
                level.setDayTime((long)n);
            }
        } else {
            this.gameClient = null;
            this.getCinematics().stop(minecraft);
        }
        MinimapRendering.clearTerrainTextures(minecraft);
        this.corpseManager.clear();
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                minecraft.levelRenderer.allChanged();
                this.setGameShader(this.game);
            });
        } else {
            minecraft.levelRenderer.allChanged();
            this.setGameShader(this.game);
        }
        if (player != null && level != null) {
            BFClientManager.setBiomeColors(minecraft, player, level, this.game);
        }
        object = dataHandler.getPlayerData(minecraft);
        ((BFAbstractPlayerData)object).method_832(false);
        ((BFAbstractPlayerData)object).setOutOfGame(false);
    }

    public void setGameShader(@Nullable AbstractGame<?, ?, ?> game) {
        PostChain postChain;
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        if (game == null) {
            gameRenderer.shutdownEffect();
            return;
        }
        MapEnvironment mapEnvironment = game.getMapEnvironment();
        ResourceLocation resourceLocation = mapEnvironment.getShader();
        if (!(resourceLocation == null || (postChain = gameRenderer.currentEffect()) != null && postChain.getName().equals(resourceLocation.toString()))) {
            gameRenderer.loadEffect(resourceLocation);
        }
    }

    public void resetGameState(@NotNull Minecraft minecraft, @NotNull BFClientPlayerData data, @NotNull ClientPlayerDataHandler dataHandler, @Nullable LocalPlayer player, @Nullable ClientLevel level) {
        this.setCurrentGame(minecraft, dataHandler, player, level, null);
        data.method_832(false);
        data.setOutOfGame(false);
    }

    @NotNull
    public BF_1177 method_5643() {
        return this.field_6706;
    }

    @NotNull
    public ClientMatchMaking getMatchMaking() {
        return this.matchMaking;
    }

    @NotNull
    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    @NotNull
    public PlayerRendererChecker getPlayerRendererChecker() {
        return this.playerRendererChecker;
    }

    private void showConnectionToast(@NotNull ConnectionStatus fromStatus, @NotNull ConnectionStatus toStatus) {
        SoundEvent soundEvent = null;
        Component component = null;
        if (toStatus == ConnectionStatus.CONNECTED_VERIFIED) {
            soundEvent = SoundEvents.BEACON_ACTIVATE;
            component = CONNECTION_ESTABLISHED_MESSAGE;
        } else if (fromStatus == ConnectionStatus.CONNECTED_VERIFIED) {
            soundEvent = SoundEvents.BEACON_DEACTIVATE;
            component = CONNECTION_LOST_MESSAGE;
        }
        if (soundEvent != null) {
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f));
        }
        if (component != null) {
            MutableComponent mutableComponent = Component.literal((String)"BlockFront Cloud");
            BFNotification.show(Minecraft.getInstance(), (Component)mutableComponent, component, true);
        }
    }

    public void method_5926(@Nonnull PoseStack poseStack, @Nonnull GuiGraphics guiGraphics, @Nonnull Camera camera) {
        this.environmentManager.method_5927(poseStack, guiGraphics, camera);
    }

    @NotNull
    public SkinFetcher getSkinFetcher() {
        return this.skinFetcher;
    }

    @NotNull
    public GameCinematics getCinematics() {
        return this.cinematics;
    }

    @Override
    @NotNull
    protected AssetStore createAssetStore() {
        return new AssetStore(this, true);
    }

    @Override
    public BFClientAntiCheat createAntiCheat() {
        return new BFClientAntiCheat(this);
    }

    @Override
    @Nullable
    public AbstractGame<?, ?, ?> getGameWithPlayer(@NotNull UUID uuid) {
        if (this.game != null && ((AbstractGamePlayerManager)this.game.getPlayerManager()).getPlayerUUIDs().contains(uuid)) {
            return this.game;
        }
        return super.getGameWithPlayer(uuid);
    }

    @Override
    public boolean isMatchMakingEnabled() {
        return false;
    }

    @Override
    public boolean isCloudConnectionVerified() {
        return false;
    }

    @Override
    public void connectionStatusChanged(@NotNull ConnectionStatus fromStatus, @NotNull ConnectionStatus toStatus, @NotNull ConnectionStatusContext context) {
        super.connectionStatusChanged(fromStatus, toStatus, context);
        this.showConnectionToast(fromStatus, toStatus);
        CloudRequestManager cloudRequestManager = ((ClientConnectionManager)this.connectionManager).getRequester();
        UUID uUID = ((ClientConnectionManager)this.connectionManager).getUUID();
        if (toStatus == ConnectionStatus.CONNECTED_VERIFIED) {
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
            cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
            cloudRequestManager.push(RequestType.EVENTS);
            cloudRequestManager.updateRequestsNow();
        }
        if (!toStatus.isConnected()) {
            this.getMatchMaking().setSearching(false);
        }
    }
}

