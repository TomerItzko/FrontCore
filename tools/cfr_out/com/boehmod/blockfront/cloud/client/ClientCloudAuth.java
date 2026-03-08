/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.exceptions.InvalidCredentialsException
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.User
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.cloud.client;

import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import java.math.BigInteger;
import java.security.SecureRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class ClientCloudAuth {
    private static final Component ERROR_TITLE = Component.translatable((String)"bf.cloud.auth.error.title");
    private static final Component ERROR_CREDENTIALS = Component.translatable((String)"bf.cloud.auth.error.message.credentials").withStyle(ChatFormatting.RED);
    private static final Component ERROR_UNKNOWN = Component.translatable((String)"bf.cloud.auth.error.message.unknown").withStyle(ChatFormatting.RED);
    private static final String SERVER_ID = ClientCloudAuth.generateServerId();
    private boolean authenticated = false;

    public void tryAuthenticate(@NotNull Minecraft minecraft) {
        try {
            User user = minecraft.getUser();
            minecraft.getMinecraftSessionService().joinServer(user.getProfileId(), user.getAccessToken(), SERVER_ID);
            this.authenticated = true;
        }
        catch (AuthenticationException authenticationException) {
            String string = authenticationException.getMessage();
            Component component = string != null ? Component.literal((String)string).withStyle(ChatFormatting.RED) : ERROR_UNKNOWN;
            Component component2 = authenticationException instanceof InvalidCredentialsException ? ERROR_CREDENTIALS : component;
            MutableComponent mutableComponent = Component.translatable((String)"bf.cloud.auth.error.message", (Object[])new Object[]{component2});
            BFNotification.show(minecraft, ERROR_TITLE, (Component)mutableComponent);
            BFLog.log("Failed to refresh Minecraft session! (%s)", component2);
            this.authenticated = false;
        }
    }

    @NotNull
    public String getServerId() {
        return SERVER_ID;
    }

    @NotNull
    private static String generateServerId() {
        return new BigInteger(200, new SecureRandom()).toString(16).substring(0, 40);
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }
}

