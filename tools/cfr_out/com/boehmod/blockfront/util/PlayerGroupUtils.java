/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerGroup
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.bflib.cloud.common.player.PlayerGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class PlayerGroupUtils {
    @NotNull
    public static MutableComponent getComponent(@NotNull PlayerGroup playerGroup) {
        if (playerGroup.getName().equals("Default")) {
            return Component.empty();
        }
        return Component.literal((String)playerGroup.getTag()).withColor(playerGroup.getColor());
    }
}

