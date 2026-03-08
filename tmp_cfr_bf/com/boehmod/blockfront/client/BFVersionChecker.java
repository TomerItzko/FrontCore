/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.gui.BFNotificationType;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.boehmod.blockfront.util.HashUtils;
import com.boehmod.blockfront.util.HttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

public final class BFVersionChecker {
    private static final int QUERY_PORT = 8001;
    private static final int QUERY_INTERVAL = 6000;
    @NotNull
    private final String cloudUrl;
    @NotNull
    private final AtomicBoolean isThreadRunning = new AtomicBoolean(false);
    private boolean hasUpdate = false;
    @NotNull
    private String currentVersion = BlockFront.VERSION;
    private int ticksUntilQuery = 1;
    @Nullable
    private final String versionHash = this.calculateJarHash();

    public BFVersionChecker(@NotNull String cloudUrl) {
        this.cloudUrl = cloudUrl;
    }

    public boolean hasUpdate() {
        return this.hasUpdate;
    }

    @NotNull
    public String getCurrentVersion() {
        return this.currentVersion;
    }

    @Nullable
    public String getVersionHash() {
        return this.versionHash;
    }

    public void onUpdate() {
        if (!this.hasUpdate && this.ticksUntilQuery-- <= 0) {
            this.ticksUntilQuery = 6000;
            if (!this.isThreadRunning.get()) {
                this.isThreadRunning.set(true);
                new Thread(new APIThread(this)).start();
            }
        }
    }

    @Nullable
    public String calculateJarHash() {
        String string = null;
        for (IModInfo iModInfo : ModList.get().getMods()) {
            if (!iModInfo.getModId().equals("bf")) continue;
            Path path = iModInfo.getOwningFile().getFile().getFilePath();
            try {
                string = HashUtils.hashMD5(path);
                BFLog.log("Found HASH '%s' for mod jar.", string);
            }
            catch (IOException | NoSuchAlgorithmException exception) {
                BFLog.logThrowable("[API] Failed to calculate HASH of the mod jar.", exception, new Object[0]);
            }
            break;
        }
        return string;
    }

    public final class APIThread
    implements Runnable {
        @NotNull
        private final BFVersionChecker versionChecker;

        public APIThread(BFVersionChecker versionChecker) {
            this.versionChecker = versionChecker;
        }

        @Override
        public void run() {
            BFLog.log("[API] Checking for updates...", new Object[0]);
            String string = this.versionChecker.calculateJarHash();
            try {
                boolean bl;
                URI uRI = new URI("http://" + BFVersionChecker.this.cloudUrl + ":8001/api/v2/?type=version");
                String string2 = HttpUtils.get(uRI);
                JsonObject jsonObject = JsonParser.parseString((String)string2).getAsJsonObject();
                String string3 = jsonObject.get("version").getAsString();
                String string4 = jsonObject.get("hash").getAsString();
                BFLog.log("[API] Received version '%s' and hash '%s'.", string3, string4);
                boolean bl2 = !string3.isEmpty() && !string3.equals(BlockFront.VERSION);
                boolean bl3 = bl = string != null && !string4.isEmpty() && !string4.equals(string);
                if (bl2 || bl) {
                    this.versionChecker.hasUpdate = true;
                    this.versionChecker.currentVersion = string3;
                    if (EnvironmentUtils.isClient()) {
                        MutableComponent mutableComponent = Component.literal((String)this.versionChecker.getCurrentVersion()).withColor(0xFFFFFF);
                        MutableComponent mutableComponent2 = Component.translatable((String)"bf.menu.text.update.title", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.YELLOW);
                        BFNotification.show(Minecraft.getInstance(), (Component)mutableComponent2, BFNotificationType.MOD_UPDATE_NOTIFICATION);
                    }
                }
            }
            catch (IOException iOException) {
                BFLog.logThrowable("[API] Failed to receive latest update for BlockFront.", iOException, new Object[0]);
            }
            catch (InterruptedException interruptedException) {
                BFLog.logThrowable("[API] Update check was interrupted.", interruptedException, new Object[0]);
            }
            catch (URISyntaxException uRISyntaxException) {
                BFLog.logThrowable("[API] Invalid URI for update check.", uRISyntaxException, new Object[0]);
            }
            BFVersionChecker.this.isThreadRunning.set(false);
        }
    }
}

