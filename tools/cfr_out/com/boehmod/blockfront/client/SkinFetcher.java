/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  com.google.gson.Gson
 *  com.google.gson.JsonObject
 *  com.mojang.blaze3d.platform.NativeImage
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client;

import com.boehmod.blockfront.util.BFLog;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.NativeImage;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SkinFetcher {
    private static final String URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s";
    @NotNull
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    @NotNull
    private final Cache<UUID, CompletableFuture<ResourceLocation>> cache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).removalListener(notification -> {
        CompletableFuture completableFuture = (CompletableFuture)notification.getValue();
        if (completableFuture != null && completableFuture.isDone()) {
            try {
                Minecraft.getInstance().getTextureManager().release((ResourceLocation)completableFuture.get());
                BFLog.log("Released texture for: " + String.valueOf(notification.getKey()), new Object[0]);
            }
            catch (Exception exception) {
                BFLog.log("Failed to release texture for: " + String.valueOf(notification.getKey()) + " due to: " + exception.getMessage(), new Object[0]);
            }
        }
    }).build();

    @NotNull
    public ResourceLocation fetchSkinCached(@NotNull Minecraft minecraft, @NotNull UUID playerId) {
        try {
            CompletableFuture completableFuture = (CompletableFuture)this.cache.get((Object)playerId, () -> CompletableFuture.supplyAsync(() -> this.fetchSkin(minecraft, playerId), this.executor));
            return completableFuture.getNow(DefaultPlayerSkin.get((UUID)playerId).texture());
        }
        catch (Exception exception) {
            BFLog.logThrowable("Failed to fetch skin for player '" + String.valueOf(playerId) + "' due to: ", exception, new Object[0]);
            return DefaultPlayerSkin.get((UUID)playerId).texture();
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    @NotNull
    private ResourceLocation fetchSkin(@NotNull Minecraft minecraft, @NotNull UUID playerId) {
        BFLog.log("Fetching skin for player '%s'...", playerId);
        try {
            String string = String.format(URL, playerId.toString().replace("-", ""));
            URL uRL = URI.create(string).toURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            try {
                ResourceLocation resourceLocation;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));){
                    String string2;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((string2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(string2);
                    }
                    String string3 = ((JsonObject)new Gson().fromJson(stringBuilder.toString(), JsonObject.class)).getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
                    String string4 = new String(Base64.getDecoder().decode(string3));
                    String string5 = ((JsonObject)new Gson().fromJson(string4, JsonObject.class)).getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                    BufferedImage bufferedImage = ImageIO.read(URI.create(string5).toURL());
                    NativeImage nativeImage = new NativeImage(bufferedImage.getWidth(), bufferedImage.getHeight(), true);
                    for (int i = 0; i < bufferedImage.getWidth(); ++i) {
                        for (int j = 0; j < bufferedImage.getHeight(); ++j) {
                            int n = bufferedImage.getRGB(i, j);
                            int n2 = n >> 24 & 0xFF;
                            int n3 = n >> 16 & 0xFF;
                            int n4 = n >> 8 & 0xFF;
                            int n5 = n & 0xFF;
                            int n6 = n2 << 24 | n5 << 16 | n4 << 8 | n3;
                            nativeImage.setPixelRGBA(i, j, n6);
                        }
                    }
                    CompletableFuture completableFuture = new CompletableFuture();
                    minecraft.submit(() -> {
                        try {
                            ResourceLocation resourceLocation = Minecraft.getInstance().getTextureManager().register("skins/" + String.valueOf(playerId), new DynamicTexture(nativeImage));
                            completableFuture.complete(resourceLocation);
                            BFLog.log("Successfully fetched and registered skin for player '" + String.valueOf(playerId) + "'!", new Object[0]);
                        }
                        catch (Exception exception) {
                            completableFuture.completeExceptionally(exception);
                        }
                    });
                    resourceLocation = (ResourceLocation)completableFuture.get();
                }
                return resourceLocation;
            }
            finally {
                httpURLConnection.disconnect();
            }
        }
        catch (Exception exception) {
            BFLog.logError("Failed to fetch or register skin for player '" + String.valueOf(playerId) + "'. Retrying.", new Object[0]);
            return DefaultPlayerSkin.get((UUID)playerId).texture();
        }
    }

    public void shutdown() {
        this.executor.shutdown();
    }
}

