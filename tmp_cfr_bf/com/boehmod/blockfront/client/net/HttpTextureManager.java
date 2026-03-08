/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class HttpTextureManager {
    @NotNull
    public static final List<String> IN_PROGRESS = new ObjectArrayList();
    @NotNull
    public static final Object2ObjectMap<String, ResourceLocation> LOADED = new Object2ObjectOpenHashMap();
    @NotNull
    private static final ResourceLocation TEMP_TEXTURE = BFRes.loc("textures/gui/loading.png");

    @NotNull
    public static ResourceLocation getOrLoad(@NotNull Minecraft minecraft, @NotNull String url) {
        if (LOADED.containsKey((Object)url)) {
            return (ResourceLocation)LOADED.get((Object)url);
        }
        if (!IN_PROGRESS.contains(url)) {
            IN_PROGRESS.add(url);
            try {
                HttpTextureManager.putFromUrl(minecraft, url);
            }
            catch (IOException iOException) {
                BFLog.logThrowable("Error while loading image.", iOException, new Object[0]);
            }
        }
        return TEMP_TEXTURE;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void putFromUrl(@NotNull Minecraft minecraft, @NotNull String url) throws IOException {
        URI uRI = URI.create(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRI.toURL().openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        try {
            block6: {
                try (InputStream inputStream = httpURLConnection.getInputStream();){
                    NativeImage nativeImage = NativeImage.read((InputStream)inputStream);
                    DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
                    ResourceLocation resourceLocation = minecraft.getTextureManager().register("url" + url.hashCode(), dynamicTexture);
                    LOADED.put((Object)url, (Object)resourceLocation);
                    if (inputStream == null) break block6;
                }
            }
            httpURLConnection.disconnect();
        }
        catch (Throwable throwable) {
            httpURLConnection.disconnect();
            throw throwable;
        }
        IN_PROGRESS.remove(url);
    }
}

