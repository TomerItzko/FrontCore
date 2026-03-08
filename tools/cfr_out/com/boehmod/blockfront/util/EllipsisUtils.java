/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EllipsisUtils {
    private static final int field_195 = 10;
    @NotNull
    private static final StringBuilder SB = new StringBuilder();

    @NotNull
    public static String cyclingEllipsis(float time) {
        SB.setLength(0);
        int n = (int)(time / 10.0f % 4.0f);
        SB.append(".".repeat(Math.max(0, n)));
        return SB.toString();
    }

    @NotNull
    public static Component method_5900(float f) {
        return Component.literal((String)EllipsisUtils.cyclingEllipsis(f));
    }
}

