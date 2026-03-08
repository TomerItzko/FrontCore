/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFVersionChecker;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.blaze3d.platform.MacosUtil;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class WindowUtils {
    private static final String ICON_16 = "/assets/bf/icons/icon_16x16.png";
    private static final String ICON_32 = "/assets/bf/icons/icon_32x32.png";
    private static final String ICON_UPDATE_16 = "/assets/bf/icons/icon_update_16x16.png";
    private static final String ICON_UPDATE_32 = "/assets/bf/icons/icon_update_32x32.png";
    private static final String WINDOW_TITLE = "Minecraft v" + SharedConstants.getCurrentVersion().getId() + " - BlockFront Mod v" + BlockFront.VERSION;
    private static boolean field_1700 = false;

    public static void setWindowIcon(@NotNull BFClientManager manager, @NotNull Window window) {
        if (field_1700) {
            return;
        }
        field_1700 = true;
        BFVersionChecker bFVersionChecker = manager.getVersionChecker();
        try {
            URL uRL = WindowUtils.class.getResource(ICON_16);
            URL uRL2 = WindowUtils.class.getResource(ICON_32);
            if (bFVersionChecker.hasUpdate()) {
                uRL = WindowUtils.class.getResource(ICON_UPDATE_16);
                uRL2 = WindowUtils.class.getResource(ICON_UPDATE_32);
            }
            if (uRL != null && uRL2 != null) {
                WindowUtils.setWindowIconNative(window, (IoSupplier<InputStream>)IoSupplier.create((Path)Paths.get(uRL2.toURI())));
            }
        }
        catch (IOException | URISyntaxException exception) {
            BFLog.logThrowable("[Brand] Failed to assign window icon.", exception, new Object[0]);
            throw new RuntimeException(exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void setWindowIconNative(@NotNull Window window, @NotNull IoSupplier<InputStream> ioSupplier) throws IOException {
        ObjectArrayList objectArrayList;
        block12: {
            RenderSystem.assertOnRenderThreadOrInit();
            if (Minecraft.ON_OSX) {
                MacosUtil.loadIcon(ioSupplier);
                return;
            }
            ObjectList objectList = ObjectLists.singleton(ioSupplier);
            int n = objectList.size();
            objectArrayList = new ObjectArrayList(n);
            try (MemoryStack memoryStack = MemoryStack.stackPush();){
                GLFWImage.Buffer buffer = GLFWImage.malloc((int)n, (MemoryStack)memoryStack);
                for (int i = 0; i < n; ++i) {
                    try (NativeImage nativeImage = NativeImage.read((InputStream)((InputStream)((IoSupplier)objectList.get(i)).get()));){
                        int n2 = nativeImage.getWidth();
                        int n3 = nativeImage.getHeight();
                        ByteBuffer byteBuffer = MemoryUtil.memAlloc((int)(n2 * n3 * 4));
                        objectArrayList.add((Object)byteBuffer);
                        byteBuffer.asIntBuffer().put(nativeImage.getPixelsRGBA());
                        buffer.position(i);
                        buffer.width(n2);
                        buffer.height(n3);
                        buffer.pixels(byteBuffer);
                        if (nativeImage == null) continue;
                    }
                }
                GLFW.glfwSetWindowIcon((long)window.getWindow(), (GLFWImage.Buffer)((GLFWImage.Buffer)buffer.position(0)));
                if (memoryStack == null) break block12;
            }
            catch (Throwable throwable) {
                objectArrayList.forEach(MemoryUtil::memFree);
                throw throwable;
            }
        }
        objectArrayList.forEach(MemoryUtil::memFree);
    }

    public static void setWindowTitle(@NotNull BFClientManager manager, @NotNull Minecraft minecraft, @NotNull String suffix) {
        BFVersionChecker bFVersionChecker = manager.getVersionChecker();
        minecraft.getWindow().setTitle(WINDOW_TITLE + " " + (String)(suffix.isEmpty() ? "" : " (" + (bFVersionChecker.hasUpdate() ? I18n.get((String)"bf.cloud.popup.update.title", (Object[])new Object[0]) : suffix) + ")"));
    }
}

