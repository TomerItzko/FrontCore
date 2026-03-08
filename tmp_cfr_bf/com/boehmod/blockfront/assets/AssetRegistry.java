/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets;

import com.boehmod.bflib.fds.FDSUtils;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetFactory;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.CommandUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class AssetRegistry<T extends IAsset> {
    @NotNull
    private final Class<T> assetClass;
    @NotNull
    private final String type;
    @NotNull
    private final String basePath;
    @NotNull
    private final Map<String, T> entries = new ConcurrentHashMap<String, T>();
    @NotNull
    private final List<String> assetsToRemove = new ObjectArrayList();
    @NotNull
    private final AssetFactory<T> factory;
    @NotNull
    private final BFAbstractManager<?, ?, ?> manager;

    public AssetRegistry(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AssetStore assetStore, @NotNull Class<T> assetClass, @NotNull String type, @NotNull AssetFactory<T> factory) {
        this.manager = manager;
        this.assetClass = assetClass;
        this.type = type;
        this.factory = factory;
        this.basePath = assetStore.getBasePath() + "/" + type;
        this.createDir();
        this.loadFromDisk();
    }

    public void loadFromDisk() {
        this.entries.clear();
        try {
            Path path2 = Paths.get(this.basePath, new String[0]);
            Files.walk(path2, new FileVisitOption[0]).filter(path -> Files.isRegularFile(path, new LinkOption[0])).filter(path -> path.toString().endsWith(".json")).forEach(this::readAssetLogging);
            BFLog.log("[Asset Registry] Found %d assets for asset type '%s'", this.entries.size(), this.type);
        }
        catch (IOException iOException) {
            BFLog.logError("Failed to load all assets.", iOException);
        }
    }

    public void testAll(@NotNull CommandSource source) {
        this.entries.values().forEach(entry -> entry.sendErrorMessages(source));
    }

    private void readAssetLogging(@NotNull Path path) {
        try {
            this.readAsset(path);
        }
        catch (FileNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException exception) {
            BFLog.logThrowable("[Asset Registry] Error while attempting to load asset path.", exception, new Object[0]);
            String string = String.format("[Asset Registry] Failed to load asset for path '%s' for asset type '%s' (%s)", path, this.type, exception.getMessage());
            BFLog.logError(string, new Object[0]);
        }
    }

    private void readAsset(@NotNull Path path) throws NoSuchMethodException, InvocationTargetException, FileNotFoundException, InstantiationException, IllegalAccessException {
        try {
            FDSTagCompound fDSTagCompound = FDSUtils.readFromFile((File)path.toFile());
            IAsset iAsset = (IAsset)this.assetClass.getDeclaredConstructor(BFAbstractManager.class).newInstance(this.manager);
            iAsset.readFDS(fDSTagCompound);
            this.entries.put(this.baseFilename(path), iAsset);
        }
        catch (Exception exception) {
            BFLog.logThrowable("[Asset Registry] Failed to load asset '%s'.", exception, path.getFileName());
        }
    }

    public void putAsset(@NotNull String name, @NotNull T asset) {
        this.assetsToRemove.remove(name);
        if (this.entries.containsKey(name)) {
            BFLog.logError("[Asset Registry] Asset '%s' for asset type '%s' already exists and has been replaced!", name, this.type);
        }
        BFLog.log("[Asset Registry] Registered new asset '%s' for asset type '%s'", name, this.type);
        this.entries.put(name, asset);
    }

    @Nullable
    public T getByName(@NotNull String name) {
        return (T)((IAsset)this.entries.get(name));
    }

    private void createDir() {
        Path path = Paths.get(this.basePath, new String[0]);
        try {
            Files.createDirectories(path, new FileAttribute[0]);
            BFLog.log("[Asset Registry] Created new directory '%s' for asset type '%s'", path.toAbsolutePath(), this.type);
        }
        catch (IOException iOException) {
            BFLog.logThrowable("[Asset Registry] Failed to create directory '%s' for asset type '%s'", iOException, path.toAbsolutePath(), this.type);
        }
    }

    public void saveAll(@Nullable CommandSource source) {
        for (Map.Entry<String, T> entry : this.entries.entrySet()) {
            String string = entry.getKey();
            try {
                this.writeFDS(string, (IAsset)entry.getValue());
            }
            catch (Exception exception) {
                BFLog.logThrowable("[Asset Store] An error occurred while saving asset " + string + ".", exception, new Object[0]);
                if (source != null) {
                    MutableComponent mutableComponent = Component.literal((String)exception.getMessage()).withStyle(ChatFormatting.GRAY);
                    CommandUtils.sendBfaWarn(source, (Component)Component.translatable((String)"bf.message.command.assets.save.error", (Object[])new Object[]{string, mutableComponent}));
                }
                throw exception;
            }
        }
    }

    private void writeFDS(@NotNull String name, @NotNull T entry) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound();
        entry.writeFDS(fDSTagCompound);
        File file = Paths.get(this.basePath, name + ".json").toFile();
        FDSUtils.writeToFile((File)file, (FDSTagCompound)fDSTagCompound);
    }

    public void deleteAll() {
        this.assetsToRemove.forEach(this::deleteAssetFile);
    }

    private void deleteAssetFile(@NotNull String filename) {
        File file = Paths.get(this.basePath, filename + ".json").toFile();
        if (!file.delete()) {
            BFLog.log("[Asset Registry] Failed to delete file '%s' during asset refresh. (Does the file exist?)", file.getName());
        }
    }

    @NotNull
    private String baseFilename(@NotNull Path path) {
        String string = path.getFileName().toString();
        int n = string.lastIndexOf(46);
        return n != -1 ? string.substring(0, n) : string;
    }

    @NotNull
    public String getBasePath() {
        return this.basePath;
    }

    @NotNull
    public String getType() {
        return this.type;
    }

    @NotNull
    public Map<String, T> getEntries() {
        return Collections.unmodifiableMap(this.entries);
    }

    public void removeAsset(@NotNull String name) {
        this.entries.remove(name);
        this.assetsToRemove.add(name);
    }

    @NotNull
    public AssetFactory<T> getFactory() {
        return this.factory;
    }
}

