/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  com.mojang.authlib.GameProfile
 *  de.jcm.discordgamesdk.Core
 *  de.jcm.discordgamesdk.CreateParams
 *  de.jcm.discordgamesdk.DiscordEventAdapter
 *  de.jcm.discordgamesdk.LogLevel
 *  de.jcm.discordgamesdk.activity.Activity
 *  de.jcm.discordgamesdk.activity.ActivityAssets
 *  de.jcm.discordgamesdk.activity.ActivityButton
 *  de.jcm.discordgamesdk.activity.ActivityButtonsMode
 *  de.jcm.discordgamesdk.activity.ActivityParty
 *  de.jcm.discordgamesdk.activity.ActivityPartySize
 *  de.jcm.discordgamesdk.activity.ActivityType
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.discord;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.discord.BFDiscordEventAdapter;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.authlib.GameProfile;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityAssets;
import de.jcm.discordgamesdk.activity.ActivityButton;
import de.jcm.discordgamesdk.activity.ActivityButtonsMode;
import de.jcm.discordgamesdk.activity.ActivityParty;
import de.jcm.discordgamesdk.activity.ActivityPartySize;
import de.jcm.discordgamesdk.activity.ActivityType;
import java.time.Instant;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BFDiscordManager {
    private static final int POPULATE_INTERVAL = 100;
    private static final long CLIENT_ID = 664231140651040768L;
    public static final int INIT_INTERVAL = 1200;
    private final Instant timeNow = Instant.now();
    private final ActivityButton activityButton = new ActivityButton("Download & Play!", "https://www.blockfrontmc.com");
    private Core core;
    private int populateTimer = 0;
    private int initTimer = 0;
    private Thread callbackThread;
    @NotNull
    private final BFClientManager manager;

    public BFDiscordManager(@NotNull BFClientManager manager, @NotNull PlayerCloudData profile) {
        this.manager = manager;
        this.init(profile);
    }

    private static String getHeadImageUrl(@NotNull GameProfile mcProfile) {
        return String.format("https://mc-heads.net/avatar/%s", mcProfile.getId().toString());
    }

    private static String getPlayerBadge(@NotNull PlayerCloudData profile, @NotNull GameProfile mcProfile) {
        PlayerRank playerRank = profile.getRank();
        return playerRank.getTitle() + " " + mcProfile.getName();
    }

    @NotNull
    private static String getCurrentMapInfo(@Nullable AbstractGame<?, ?, ?> game, @NotNull PlayerCloudData profile) {
        BFGameType bFGameType;
        Object object = Component.translatable((String)profile.getStatus().getOnlineStatus().toString()).getString();
        if (game != null && (bFGameType = BFGameType.getByName(game.getType())) != null) {
            object = "Playing " + bFGameType.getDisplayName().getString() + " on map '" + game.getMap().getName() + "'";
        }
        return object;
    }

    private void init(@NotNull PlayerCloudData profile) {
        BFLog.log("Attempting to initialize Discord manager...", new Object[0]);
        try {
            this.core = this.createCore();
            this.populateInfo(profile);
            this.startCallbackThread();
            BFLog.log("Successfully initialized Discord manager.", new Object[0]);
        }
        catch (Exception exception) {
            BFLog.logError("Failed to initialize Discord manager. (%s)", exception.getMessage());
            this.core = null;
        }
    }

    private Core createCore() {
        BFLog.log("Attempting to create Discord Game API core...", new Object[0]);
        CreateParams createParams = new CreateParams();
        createParams.setClientID(664231140651040768L);
        createParams.setFlags(CreateParams.getDefaultFlags());
        createParams.registerEventHandler((DiscordEventAdapter)new BFDiscordEventAdapter(this.manager));
        Core core = new Core(createParams);
        core.setLogHook(LogLevel.VERBOSE, (level, message) -> {});
        return core;
    }

    private void startCallbackThread() {
        if (this.callbackThread != null) {
            return;
        }
        this.callbackThread = new Thread(() -> {
            while (true) {
                this.discordCallback();
            }
        }, "Discord Callback Thread");
        this.callbackThread.start();
    }

    public void shutdown() {
        BFLog.log("Shutting down Discord manager...", new Object[0]);
        this.close();
        this.core = null;
    }

    private void close() {
        BFLog.log("Closing Discord core...", new Object[0]);
        if (this.core != null) {
            try {
                this.core.close();
                BFLog.log("Successfully closed Discord core.", new Object[0]);
            }
            catch (Exception exception) {
                BFLog.logError("Failed to close Discord core. (%s)", exception.getMessage());
            }
        }
    }

    public void update(@NotNull PlayerCloudData profile) {
        if (this.core == null) {
            if (this.initTimer-- <= 0) {
                this.init(profile);
                this.initTimer = 1200;
            }
            return;
        }
        if (this.populateTimer-- <= 0) {
            this.populateTimer = 100;
            this.populateInfo(profile);
        }
    }

    private void populateInfo(@NotNull PlayerCloudData profile) {
        Minecraft minecraft = Minecraft.getInstance();
        GameProfile gameProfile = minecraft.getGameProfile();
        AbstractGame<?, ?, ?> abstractGame = this.manager.getGame();
        Activity activity = new Activity();
        activity.setDetails("BlockFront - v" + BlockFront.VERSION);
        activity.setState("A Minecraft WW2 Mod");
        activity.setType(ActivityType.PLAYING);
        activity.timestamps().setStart(this.timeNow);
        activity.setActivityButtonsMode(ActivityButtonsMode.BUTTONS);
        activity.addButton(this.activityButton);
        this.setActivityPartySize(activity.party(), abstractGame);
        ActivityAssets activityAssets = activity.assets();
        activityAssets.setLargeText(BFDiscordManager.getCurrentMapInfo(abstractGame, profile));
        activityAssets.setLargeImage("https://www.blockfrontmc.com/image/discord/rpc_logo_animated_new.gif");
        activityAssets.setSmallText(BFDiscordManager.getPlayerBadge(profile, gameProfile));
        activityAssets.setSmallImage(BFDiscordManager.getHeadImageUrl(gameProfile));
        if (this.core != null) {
            try {
                this.core.activityManager().updateActivity(activity);
            }
            catch (Exception exception) {
                BFLog.logError("Failed to update Discord rich presence. (%s)", exception.getMessage());
                this.shutdown();
            }
        }
    }

    private void setActivityPartySize(@NotNull ActivityParty party, @Nullable AbstractGame<?, ?, ?> game) {
        if (game != null) {
            Object obj = game.getPlayerManager();
            ActivityPartySize activityPartySize = party.size();
            activityPartySize.setCurrentSize(((AbstractGamePlayerManager)obj).getPlayerUUIDs().size());
            activityPartySize.setMaxSize(((AbstractGamePlayerManager)obj).getMaxPlayers());
        }
    }

    private void discordCallback() {
        if (this.core == null) {
            return;
        }
        try {
            this.core.runCallbacks();
            Thread.sleep(16L);
        }
        catch (Exception exception) {
            BFLog.logError("Failed to run Discord callbacks. (%s)", exception.getMessage());
            this.shutdown();
        }
    }
}

