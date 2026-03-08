/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.common.ColorReferences
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class ClanUtils {
    public static float getSkillLevel(@NotNull PlayerDataHandler<?> handler, @NotNull AbstractClanData clan) {
        Set set = clan.getMembers();
        return set.stream().map(uuid -> {
            PlayerCloudData playerCloudData = handler.getCloudProfile((UUID)uuid);
            int n = Math.max(1, playerCloudData.getDeaths());
            return Float.valueOf((float)playerCloudData.getKills() / (float)n);
        }).reduce(Float.valueOf(0.0f), Float::sum).floatValue() / (float)set.size();
    }

    @NotNull
    public static MutableComponent getClanPrefix(@NotNull PlayerCloudData profile) {
        AbstractClanData abstractClanData = profile.getClanData();
        if (abstractClanData != null) {
            return ClanUtils.makeClanPrefix(abstractClanData);
        }
        return Component.empty();
    }

    @NotNull
    public static MutableComponent makeClanPrefix(@NotNull AbstractClanData clan) {
        String string = String.format("[%s] ", clan.getName());
        return Component.literal((String)string).withColor(ColorReferences.COLOR_THEME_CLANS_SOLID);
    }
}

