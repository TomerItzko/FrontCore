/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class BFLog {
    @NotNull
    private static final Logger LOGGER = LogManager.getLogger((String)"BlockFront");

    public static void log(@NotNull String fmt, Object ... args) {
        LOGGER.info("[{}] {}", (Object)"BlockFront", (Object)String.format(fmt, args));
    }

    public static void logWarn(@NotNull String fmt, Object ... args) {
        LOGGER.warn("[{}] {}", (Object)"BlockFront", (Object)String.format(fmt, args));
    }

    public static void logError(@NotNull String fmt, Object ... args) {
        LOGGER.error("[{}] {}", (Object)"BlockFront", (Object)String.format(fmt, args));
    }

    public static void logThrowable(@NotNull String fmt, @NotNull Throwable throwable, Object ... args) {
        LOGGER.error("[{}] {}", (Object)"BlockFront", (Object)String.format(fmt, args), (Object)throwable);
    }
}

