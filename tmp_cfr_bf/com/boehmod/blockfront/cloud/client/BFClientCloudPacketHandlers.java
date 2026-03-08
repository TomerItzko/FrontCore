/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud.client;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.cloud.common.discord.DiscordEvent;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.connection.Connection;
import com.boehmod.bflib.cloud.packet.PacketRegistry;
import com.boehmod.bflib.cloud.packet.common.PacketChatMessageFromCloud;
import com.boehmod.bflib.cloud.packet.common.PacketClientMessagePopup;
import com.boehmod.bflib.cloud.packet.common.PacketClientSound;
import com.boehmod.bflib.cloud.packet.common.PacketNotificationFromCloud;
import com.boehmod.bflib.cloud.packet.common.PacketTriggerNewRank;
import com.boehmod.bflib.cloud.packet.common.discord.PacketDiscordLinkResponse;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryCaseResults;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemAdded;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemRemoved;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMJoinServer;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMSearchCloudCanceled;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyJoined;
import com.boehmod.bflib.cloud.packet.common.patreon.PacketPatreonLinkResponse;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedChallenges;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedClanData;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedCloudData;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedEvents;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedFriends;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.screen.armory.ArmoryOpenCaseScreen;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.client.screen.title.ScoreboardTitleScreen;
import com.boehmod.blockfront.client.screen.title.overlay.RankupScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFStyles;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class BFClientCloudPacketHandlers {
    private static final Component MESSAGE_OKAY = Component.translatable((String)"bf.message.okay");
    private static final Component DISCORD_BUTTON = Component.translatable((String)"bf.cloud.popup.discord.button");
    private static final Component DISCORD_BUTTON_TIP = Component.translatable((String)"bf.cloud.popup.discord.button.tip", (Object[])new Object[]{"BlockFront"});
    private static final Component VERIFY_CHANNEL = Component.literal((String)"#verify").withStyle(BFStyles.BLUE);
    private static final Component VERIFY_COMMAND = Component.literal((String)"/verify").withStyle(BFStyles.BLUE);
    private static final Component DISCORD_TITLE = Component.translatable((String)"bf.cloud.popup.discord.title");
    private static final Component DISCORD_MESSAGE = Component.translatable((String)"bf.cloud.popup.discord.message", (Object[])new Object[]{VERIFY_CHANNEL, VERIFY_COMMAND}).withColor(0xFFFFFF);

    static void register() {
        BFLog.log("[Cloud] Registering client-side cloud packet handlers...", new Object[0]);
        PacketRegistry.registerPacketHandler(PacketNotificationFromCloud.class, BFClientCloudPacketHandlers::notificationFromCloud, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketChatMessageFromCloud.class, BFClientCloudPacketHandlers::chatMessageFromCloud, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketClientSound.class, BFClientCloudPacketHandlers::clientSound, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketTriggerNewRank.class, BFClientCloudPacketHandlers::triggerNewRank, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketClientMessagePopup.class, BFClientCloudPacketHandlers::clientMessagePopup, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketInventoryCaseResults.class, BFClientCloudPacketHandlers::inventoryCaseResults, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketInventoryItemAdded.class, BFClientCloudPacketHandlers::inventoryItemAdded, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketInventoryItemRemoved.class, BFClientCloudPacketHandlers::inventoryItemRemoved, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedFriends.class, BFClientCloudPacketHandlers::requestedFriends, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedCloudData.class, BFClientCloudPacketHandlers::requestedCloudData, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedEvents.class, BFClientCloudPacketHandlers::requestedEvents, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedChallenges.class, BFClientCloudPacketHandlers::requestedChallenges, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedClanData.class, BFClientCloudPacketHandlers::requestedClanData, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketPartyJoined.class, BFClientCloudPacketHandlers::partyJoined, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketMMJoinServer.class, BFClientCloudPacketHandlers::mmJoinServer, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketMMSearchCloudCanceled.class, BFClientCloudPacketHandlers::mmSearchCloudCanceled, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketDiscordLinkResponse.class, BFClientCloudPacketHandlers::discordLinkResponse, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketPatreonLinkResponse.class, BFClientCloudPacketHandlers::patreonLinkResponse, BFConnection.class);
        BFLog.log("[Cloud] Finished registering client-side cloud packet handlers.", new Object[0]);
    }

    public static void inventoryItemAdded(@NotNull PacketInventoryItemAdded packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        for (CloudItemStack cloudItemStack : packet.stacks()) {
            ((PlayerCloudInventory)playerCloudData.getInventory()).addItemStackLocal(cloudItemStack);
        }
    }

    public static void inventoryItemRemoved(@NotNull PacketInventoryItemRemoved packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        for (UUID uUID : packet.uuids()) {
            playerCloudInventory.removeItemStackLocal(uUID);
        }
    }

    private static void requestedClanData(@NotNull PacketRequestedClanData packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        clientPlayerDataHandler.putClanData(packet.uuid(), packet.clanData());
    }

    private static void requestedChallenges(@NotNull PacketRequestedChallenges packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        playerCloudData.setChallenges((List)packet.challenges());
    }

    public static void chatMessageFromCloud(@NotNull PacketChatMessageFromCloud packet, @NotNull Connection<?> connection) {
        MutableComponent mutableComponent = Component.Serializer.fromJson((String)packet.message(), (HolderLookup.Provider)RegistryAccess.EMPTY);
        if (mutableComponent == null) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        if (localPlayer != null && bFClientManager.getGame() != null) {
            localPlayer.sendSystemMessage((Component)mutableComponent);
        }
    }

    public static void partyJoined(@NotNull PacketPartyJoined packet, @NotNull Connection<?> connection) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen((Screen)new LobbyTitleScreen());
    }

    public static void mmJoinServer(@NotNull PacketMMJoinServer packet, @NotNull Connection<?> connection) {
        Minecraft minecraft = Minecraft.getInstance();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        bFClientManager.getMatchMaking().join(minecraft, bFClientManager, packet.address());
    }

    public static void discordLinkResponse(@NotNull PacketDiscordLinkResponse packet, @NotNull Connection<?> connection) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof BFTitleScreen)) {
            return;
        }
        minecraft.keyboardHandler.setClipboard(packet.auth());
        BFClientManager.showPopup(DISCORD_TITLE, DISCORD_MESSAGE, PopupType.DISCORD_LINK, new PopupButton(MESSAGE_OKAY, null, button -> minecraft.setScreen((Screen)new LobbyTitleScreen())), new PopupButton(DISCORD_BUTTON, DISCORD_BUTTON_TIP, button -> BFTitleScreen.openUrl(minecraft, "https://discord.blockfrontmc.com/")));
    }

    public static void patreonLinkResponse(@NotNull PacketPatreonLinkResponse packet, @NotNull Connection<?> connection) {
        Minecraft minecraft = Minecraft.getInstance();
        BFTitleScreen.openUrl(minecraft, packet.oauthUrl());
        BFNotification.show(minecraft, (Component)Component.translatable((String)"bf.message.patreon.browser.opened"));
    }

    public static void mmSearchCloudCanceled(@NotNull PacketMMSearchCloudCanceled packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        bFClientManager.getMatchMaking().setSearching(false);
    }

    public static void requestedEvents(@NotNull PacketRequestedEvents packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        clientConnectionManager.method_1395((Collection<DiscordEvent>)packet.events());
    }

    public static void requestedFriends(@NotNull PacketRequestedFriends packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        bFClientManager.getFriendManager().replaceAll((Set<UUID>)packet.friends(), (Set<UUID>)packet.requests(), (Set<UUID>)packet.clanRequests());
    }

    public static void requestedCloudData(@NotNull PacketRequestedCloudData packet, @NotNull Connection<?> connection) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        clientConnectionManager.usersOnline = packet.getUsersOnline();
        BFClientSettings.GAME_PLAYER_COUNT.clear();
        Object2IntMap object2IntMap = packet.getGamePlayerCount();
        BFClientSettings.GAME_PLAYER_COUNT.putAll((Map)object2IntMap);
        BFClientSettings.CLAN_SCORES.clear();
        BFClientSettings.PLAYER_SCORES.clear();
        BFClientSettings.SCOREBOARD_RESET_TIME = packet.getScoreboardResetTime();
        Object2IntMap object2IntMap2 = packet.getClanScores();
        BFClientSettings.CLAN_SCORES.putAll((Map)object2IntMap2);
        Object2IntMap object2IntMap3 = packet.getPlayerScores();
        BFClientSettings.PLAYER_SCORES.putAll((Map)object2IntMap3);
        Screen screen = minecraft.screen;
        if (screen instanceof ScoreboardTitleScreen) {
            ScoreboardTitleScreen scoreboardTitleScreen = (ScoreboardTitleScreen)screen;
            scoreboardTitleScreen.method_1034();
        }
    }

    public static void inventoryCaseResults(@NotNull PacketInventoryCaseResults packet, @NotNull Connection<?> connection) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof ArmoryOpenCaseScreen) {
            ArmoryOpenCaseScreen armoryOpenCaseScreen = (ArmoryOpenCaseScreen)screen;
            screen = new ObjectArrayList();
            IntListIterator intListIterator = packet.results().iterator();
            while (intListIterator.hasNext()) {
                int n = (Integer)intListIterator.next();
                CloudItemStack cloudItemStack = new CloudItemStack(UUID.randomUUID(), n);
                screen.add((Object)cloudItemStack);
            }
            armoryOpenCaseScreen.method_729((ObjectList<CloudItemStack>)screen);
        }
    }

    public static void clientMessagePopup(@NotNull PacketClientMessagePopup packet, @NotNull Connection<?> connection) {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        MutableComponent mutableComponent = Component.Serializer.fromJson((String)packet.title(), (HolderLookup.Provider)RegistryAccess.EMPTY);
        MutableComponent mutableComponent2 = Component.Serializer.fromJson((String)packet.message(), (HolderLookup.Provider)RegistryAccess.EMPTY);
        if (mutableComponent == null || mutableComponent2 == null) {
            BFLog.log("Failed to parse popup message: " + packet.title() + " | " + packet.message(), new Object[0]);
            return;
        }
        BFClientManager.showPopup((Component)mutableComponent, (Component)mutableComponent2, packet.type(), new PopupButton(MESSAGE_OKAY, null, button -> minecraft.setScreen(screen)));
    }

    public static void notificationFromCloud(@NotNull PacketNotificationFromCloud packet, @NotNull Connection<?> connection) {
        if (!BFClientSettings.shouldSendNotifications(packet.type())) {
            return;
        }
        String string = packet.message();
        MutableComponent mutableComponent = Component.Serializer.fromJson((String)string, (HolderLookup.Provider)RegistryAccess.EMPTY);
        if (mutableComponent == null) {
            BFLog.log("Failed to parse notification message from cloud! %s", string);
            return;
        }
        MutableComponent mutableComponent2 = Component.literal((String)"BlockFront Cloud");
        BFNotification.show(Minecraft.getInstance(), (Component)mutableComponent2, (Component)mutableComponent, true);
    }

    public static void clientSound(@NotNull PacketClientSound packet, @NotNull Connection<?> connection) {
        Minecraft minecraft = Minecraft.getInstance();
        SoundEvent soundEvent = BFSounds.retrieve(packet.sound());
        if (soundEvent != null) {
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f, (float)1.0f));
        }
    }

    public static void triggerNewRank(@NotNull PacketTriggerNewRank packet, @NotNull Connection<?> connection) {
        RankupScreen.newRank = true;
    }
}

