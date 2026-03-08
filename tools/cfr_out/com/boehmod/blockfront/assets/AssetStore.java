/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSource
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.assets;

import com.boehmod.blockfront.assets.AssetFactory;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetWatcher;
import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.CommandUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssetStore {
    private static AssetStore INSTANCE;
    @NotNull
    private final Map<Class<? extends IAsset>, AssetRegistry<? extends IAsset>> registeredAssets;
    private int timesLoaded = 0;
    @Nullable
    private AssetWatcher watcher;
    private final boolean isClient;
    @NotNull
    private final BFAbstractManager<?, ?, ?> manager;

    public AssetStore(@NotNull BFAbstractManager<?, ?, ?> manager, boolean isClient) {
        INSTANCE = this;
        this.manager = manager;
        this.isClient = isClient;
        this.registeredAssets = new HashMap<Class<? extends IAsset>, AssetRegistry<? extends IAsset>>();
    }

    @NotNull
    public static AssetStore getInstance() {
        return INSTANCE;
    }

    @Nullable
    public AssetWatcher getWatcher() {
        return this.watcher;
    }

    public void init() {
        BFLog.log("[Asset Store] Attempting to start file watcher...", new Object[0]);
        try {
            BFLog.log("[Asset Store] Successfully initialized file watcher!", new Object[0]);
            this.watcher = new AssetWatcher();
            Thread thread = new Thread(this.watcher);
            thread.start();
        }
        catch (IOException iOException) {
            BFLog.logThrowable("[Asset Store] Failed to initialize file watcher!", iOException, new Object[0]);
        }
    }

    public <T extends IAsset> void registerAssetType(@NotNull Class<T> assetClass, @NotNull String name, @NotNull Supplier<AssetFactory<T>> factory) {
        BFLog.log("[Asset Store] Registering new asset type '%s'", name);
        if (!this.registeredAssets.containsKey(assetClass)) {
            this.registeredAssets.put(assetClass, new AssetRegistry<T>(this.manager, this, assetClass, name, factory.get()));
        }
    }

    public <T extends IAsset> AssetRegistry<T> getRegistry(@NotNull Class<T> clazz) {
        return this.registeredAssets.get(clazz);
    }

    @Nullable
    public <T extends IAsset> AssetRegistry<T> method_1635(@NotNull String string) {
        for (AssetRegistry<? extends IAsset> assetRegistry : this.registeredAssets.values()) {
            if (!assetRegistry.getType().equalsIgnoreCase(string)) continue;
            return assetRegistry;
        }
        return null;
    }

    public void testAll(@NotNull CommandSource source) {
        for (AssetRegistry<? extends IAsset> assetRegistry : this.registeredAssets.values()) {
            assetRegistry.testAll(source);
        }
    }

    public void saveAll(@Nullable CommandSource source) {
        BFLog.log("[Asset Store] Saving all assets for %d asset types", this.registeredAssets.size());
        try {
            for (AssetRegistry<? extends IAsset> assetRegistry : this.registeredAssets.values()) {
                BFLog.log("[Asset Store] Saving all assets for asset type '%s' ('%s')", assetRegistry.getType(), assetRegistry.getBasePath());
                assetRegistry.saveAll(source);
                assetRegistry.deleteAll();
            }
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Asset Store] An error occurred while saving all assets.", exception, new Object[0]);
            if (source != null) {
                MutableComponent mutableComponent = Component.literal((String)exception.getMessage()).withStyle(ChatFormatting.GRAY);
                CommandUtils.sendBfaWarn(source, (Component)Component.translatable((String)"bf.message.command.assets.saveall.error", (Object[])new Object[]{mutableComponent}));
            }
            throw exception;
        }
    }

    public int assetCount() {
        return this.registeredAssets.values().stream().mapToInt(store -> store.getEntries().size()).sum();
    }

    public int numRegistered() {
        return this.registeredAssets.size();
    }

    public void loadFromDisk() {
        ++this.timesLoaded;
        for (AssetRegistry<? extends IAsset> assetRegistry : this.registeredAssets.values()) {
            assetRegistry.loadFromDisk();
        }
    }

    @NotNull
    public Set<String> getTypes() {
        return this.registeredAssets.values().stream().map(AssetRegistry::getType).collect(Collectors.toSet());
    }

    public int getTimesLoaded() {
        return this.timesLoaded;
    }

    @NotNull
    public String getBasePath() {
        String string = "BlockFront".toLowerCase(Locale.ROOT);
        String string2 = this.isClient ? "client" : "server";
        return string + "/assets/" + string2;
    }

    public boolean isClient() {
        return this.isClient;
    }
}

