/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.assets;

import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.EnvironmentUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class AssetWatcher
implements Runnable {
    @NotNull
    private final Path field_2063 = Paths.get(AssetStore.getInstance().getBasePath(), new String[0]);
    @NotNull
    private final WatchService field_2064 = FileSystems.getDefault().newWatchService();
    private volatile boolean running = true;
    private volatile boolean field_2065 = false;

    private static boolean method_1598(@NotNull WatchEvent<?> watchEvent) {
        Path path = (Path)watchEvent.context();
        if (!path.toString().endsWith(".json")) {
            return false;
        }
        File file = path.toFile();
        BFLog.log("[Asset Watcher] Detected change in asset '" + file.getName() + "'. (Path: '" + String.valueOf(path.toAbsolutePath()) + "')", new Object[0]);
        MutableComponent mutableComponent = Component.literal((String)file.getName()).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)watchEvent.kind().name()).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.command.assets.file.watcher", (Object[])new Object[]{mutableComponent, mutableComponent2});
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer == null) {
            return false;
        }
        for (ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
            serverPlayer.sendSystemMessage((Component)mutableComponent3);
        }
        AssetStore.getInstance().loadFromDisk();
        BFLog.log("[Asset Watcher] Reloaded all assets after detecting change.", new Object[0]);
        return true;
    }

    public void method_1597(boolean bl) {
        BFLog.log("[Asset Watcher] " + (bl ? "Paused" : "Un-paused") + " watch for file changes.", new Object[0]);
        this.field_2065 = bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void method_1599() {
        BFLog.log("[Asset Watcher] Started watch for file changes.", new Object[0]);
        try {
            this.method_1601();
            while (this.running) {
                WatchKey watchKey;
                try {
                    watchKey = this.field_2064.poll(100L, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException interruptedException) {
                    break;
                }
                if (watchKey == null || this.field_2065) continue;
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                    if (watchEvent.kind() != StandardWatchEventKinds.OVERFLOW && AssetWatcher.method_1598(watchEvent)) break;
                }
                watchKey.reset();
            }
        }
        catch (IOException iOException) {
        }
        finally {
            this.method_1600();
        }
    }

    public void method_1600() {
        BFLog.log("[Asset Watcher] Stopping watch for file changes.", new Object[0]);
        this.running = false;
        try {
            this.field_2064.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private void method_1601() throws IOException {
        BFLog.log("[Asset Watcher] Initializing file watcher for asset store...", new Object[0]);
        Files.walkFileTree(this.field_2063, (FileVisitor<? super Path>)new SimpleFileVisitor<Path>(){

            @Override
            @NotNull
            public FileVisitResult preVisitDirectory(@NotNull Path path, @NotNull BasicFileAttributes basicFileAttributes) throws IOException {
                path.register(AssetWatcher.this.field_2064, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                return FileVisitResult.CONTINUE;
            }

            @Override
            @NotNull
            public /* synthetic */ FileVisitResult preVisitDirectory(@NotNull Object object, @NotNull BasicFileAttributes basicFileAttributes) throws IOException {
                return this.preVisitDirectory((Path)object, basicFileAttributes);
            }
        });
        BFLog.log("[Asset Watcher] Finished initializing file watcher for asset store...", new Object[0]);
    }

    @Override
    public void run() {
        this.method_1599();
    }
}

