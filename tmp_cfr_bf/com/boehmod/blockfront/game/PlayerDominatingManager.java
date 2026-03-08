/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class PlayerDominatingManager {
    @NotNull
    public final Object2IntMap<Entry> entries = new Object2IntOpenHashMap();
    private final int field_3289;

    public PlayerDominatingManager(int n) {
        this.field_3289 = n;
    }

    public void clear() {
        this.entries.clear();
    }

    public boolean playerIsDominated(@NotNull UUID dominated, @NotNull UUID dominator) {
        Entry entry = new Entry(dominated, dominator);
        if (this.entries.containsKey((Object)entry)) {
            return this.entries.getInt((Object)entry) >= this.field_3289;
        }
        return false;
    }

    public void handleKill(@NotNull PlayerDataHandler<?> dataHandler, @NotNull UUID killer, @NotNull UUID killed, @NotNull Set<UUID> players) {
        Entry entry = new Entry(killer, killed);
        Entry entry2 = new Entry(killed, killer);
        PlayerCloudData playerCloudData = dataHandler.getCloudProfile(killer);
        PlayerCloudData playerCloudData2 = dataHandler.getCloudProfile(killed);
        String string = playerCloudData.getUsername();
        String string2 = playerCloudData2.getUsername();
        if (this.entries.containsKey((Object)entry2) && this.entries.getInt((Object)entry2) > 0) {
            if (this.entries.getInt((Object)entry2) >= this.field_3289) {
                this.method_3197(dataHandler, killed, killer, players);
            }
            this.entries.removeInt((Object)entry2);
        }
        if (this.entries.containsKey((Object)entry)) {
            int n = this.entries.getInt((Object)entry) + 1;
            this.entries.put((Object)entry, n);
            if (n == this.field_3289) {
                BFUtils.playSound(killed, (SoundEvent)BFSounds.MATCH_DOMINATING.get(), SoundSource.MASTER, 1.5f);
                BFUtils.sendPopupMessage(killed, new BFPopup((Component)Component.translatable((String)"bf.popup.message.dominating", (Object[])new Object[]{string}).withStyle(ChatFormatting.GOLD), 60));
                BFUtils.playSound(killer, (SoundEvent)BFSounds.MATCH_DOMINATING.get(), SoundSource.MASTER, 1.5f);
                BFUtils.sendPopupMessage(killer, new BFPopup((Component)Component.translatable((String)"bf.popup.message.dominating.by", (Object[])new Object[]{string2}).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), 60));
                MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.dominating", (Object[])new Object[]{Component.literal((String)string2).withColor(0xFFFFFF), Component.literal((String)string).withColor(0xFFFFFF)}).withStyle(ChatFormatting.RED);
                BFUtils.sendNoticeMessage(players, (Component)mutableComponent);
            }
        } else {
            this.entries.put((Object)entry, 0);
        }
    }

    public void method_3197(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull UUID uUID, @NotNull UUID uUID2, @NotNull Set<UUID> set) {
        PlayerCloudData playerCloudData = playerDataHandler.getCloudProfile(uUID);
        PlayerCloudData playerCloudData2 = playerDataHandler.getCloudProfile(uUID2);
        String string = playerCloudData.getUsername();
        String string2 = playerCloudData2.getUsername();
        SoundEvent soundEvent = (SoundEvent)BFSounds.MATCH_REVENGE.get();
        BFUtils.playSound(uUID, soundEvent, SoundSource.MASTER);
        BFUtils.sendPopupMessage(uUID, new BFPopup((Component)Component.translatable((String)"bf.popup.message.revenge", (Object[])new Object[]{string2}).withStyle(ChatFormatting.GOLD), 60));
        BFUtils.playSound(uUID2, soundEvent, SoundSource.MASTER);
        BFUtils.sendPopupMessage(uUID2, new BFPopup((Component)Component.translatable((String)"bf.popup.message.revenge.by", (Object[])new Object[]{string}).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD), 60));
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.revenge", (Object[])new Object[]{Component.literal((String)string).withColor(0xFFFFFF), Component.literal((String)string2).withColor(0xFFFFFF)}).withStyle(ChatFormatting.RED);
        BFUtils.sendNoticeMessage(set, (Component)mutableComponent);
    }

    public static class Entry {
        @NotNull
        private final UUID field_3290;
        @NotNull
        private final UUID field_3291;

        public Entry(@NotNull UUID uUID, @NotNull UUID uUID2) {
            this.field_3290 = uUID;
            this.field_3291 = uUID2;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry)object;
            return Objects.equals(this.field_3290, entry.field_3290) && Objects.equals(this.field_3291, entry.field_3291);
        }

        public int hashCode() {
            return Objects.hash(this.field_3290, this.field_3291) + Objects.hash(this.field_3291, this.field_3290);
        }
    }
}

