/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PunishmentType
 *  com.boehmod.bflib.common.PatternReferences
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.player;

import com.boehmod.bflib.cloud.common.player.PunishmentType;
import com.boehmod.bflib.common.PatternReferences;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.server.BFServerManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class BFChatFilter {
    private static final Component MESSAGE_MUTED = Component.translatable((String)"bf.message.chat.spam.muted").withStyle(ChatFormatting.RED);
    private static final Component MESSAGE_CAPS = Component.translatable((String)"bf.message.chat.spam.caps").withStyle(ChatFormatting.RED);
    private static final Component MESSAGE_LINK = Component.translatable((String)"bf.message.chat.spam.link").withStyle(ChatFormatting.RED);
    private static final Component MESSAGE_PROFANITY = Component.translatable((String)"bf.message.chat.spam.profanity").withStyle(ChatFormatting.RED);
    @NotNull
    private static final List<Filter> FILTERS = new ObjectArrayList();

    public static Component checkMessage(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerPlayer player, @NotNull ServerLevel level, @NotNull BFAbstractPlayerData<?, ?, ?, ?> playerData, @NotNull PlayerCloudData cloudData, @NotNull String message) {
        return FILTERS.stream().map(filter -> filter.getMessage(manager, level, player, playerData, cloudData, message)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public static double getUppercasePercent(@NotNull ServerLevel level, @NotNull String message) {
        for (ServerPlayer serverPlayer : level.players()) {
            message = message.replace(serverPlayer.getScoreboardName(), "");
        }
        int n = message.length();
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (!Character.isUpperCase(message.charAt(i))) continue;
            ++n2;
        }
        return (double)n2 * 100.0 / (double)n;
    }

    static {
        FILTERS.add((bFAbstractManager, serverLevel, serverPlayer, bFAbstractPlayerData, playerCloudData, string) -> {
            if (playerCloudData.hasActivePunishment(PunishmentType.MUTE) || bFAbstractPlayerData.isMuted()) {
                return MESSAGE_MUTED;
            }
            return null;
        });
        FILTERS.add((bFAbstractManager, serverLevel, serverPlayer, bFAbstractPlayerData, playerCloudData, string) -> {
            if (string.length() > 5 && BFChatFilter.getUppercasePercent(serverLevel, string) > 50.0) {
                return MESSAGE_CAPS;
            }
            return null;
        });
        FILTERS.add((bFAbstractManager, serverLevel, serverPlayer, bFAbstractPlayerData, playerCloudData, string) -> {
            if (PatternReferences.LINK.matcher(string).find()) {
                return MESSAGE_LINK;
            }
            return null;
        });
        FILTERS.add((bFAbstractManager, serverLevel, serverPlayer, bFAbstractPlayerData, playerCloudData, string) -> {
            BFServerManager bFServerManager;
            if (bFAbstractManager instanceof BFServerManager && (bFServerManager = (BFServerManager)bFAbstractManager).getProfanityManager().processMessage(serverPlayer.getUUID(), string) > 1) {
                return MESSAGE_PROFANITY;
            }
            return null;
        });
        FILTERS.add((bFAbstractManager, serverLevel, serverPlayer, bFAbstractPlayerData, playerCloudData, string) -> {
            long l = bFAbstractPlayerData.getLastChatTime();
            long l2 = Calendar.getInstance().getTimeInMillis();
            long l3 = l2 - l;
            long l4 = TimeUnit.MILLISECONDS.toSeconds(l3);
            int n = 10;
            String string2 = bFAbstractPlayerData.getLastMessage();
            if (string2 != null && string2.equals(string) && l4 <= 10L) {
                return Component.translatable((String)"bf.message.chat.spam.clone", (Object[])new Object[]{10L - l4}).withStyle(ChatFormatting.RED);
            }
            bFAbstractPlayerData.addMessage(string);
            bFAbstractPlayerData.setLastChatTime(l2);
            return null;
        });
    }

    private static interface Filter {
        @Nullable
        public Component getMessage(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull ServerLevel var2, @NotNull ServerPlayer var3, @NotNull BFAbstractPlayerData<?, ?, ?, ?> var4, @NotNull PlayerCloudData var5, @NotNull String var6);
    }
}

