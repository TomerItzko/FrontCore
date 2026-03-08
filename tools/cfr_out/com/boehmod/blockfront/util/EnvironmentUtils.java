/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.server.MinecraftServer
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.fml.loading.FMLEnvironment
 *  net.neoforged.neoforge.server.ServerLifecycleHooks
 */
package com.boehmod.blockfront.util;

import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class EnvironmentUtils {
    public static boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public static boolean isServer() {
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
    }

    @Nullable
    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}

