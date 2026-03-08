/*
 * Decompiled with CFR.
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

