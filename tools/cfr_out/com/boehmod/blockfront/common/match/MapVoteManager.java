/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.MapVoteEntry;
import com.boehmod.blockfront.common.net.packet.BFGameMapVoteInfoPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class MapVoteManager {
    private static final int field_6593 = 3;
    private static final int field_6594 = 3;
    @NotNull
    private final AbstractGame<?, ?, ?> field_3581;
    @NotNull
    private final List<MapVoteEntry> entries = new ObjectArrayList();
    @NotNull
    private final Set<UUID> field_3582 = new ObjectOpenHashSet();
    private boolean field_3579 = false;

    public MapVoteManager(@NotNull AbstractGame<?, ?, ?> game) {
        this.field_3581 = game;
    }

    public void method_3721(@NotNull UUID uUID) {
        if (!this.field_3579) {
            return;
        }
        for (MapVoteEntry mapVoteEntry : this.entries) {
            mapVoteEntry.getPlayerVotes().remove(uUID);
        }
    }

    public void method_3717(@NotNull ServerPlayer serverPlayer, @NotNull String string, @NotNull String string2) {
        UUID uUID = serverPlayer.getUUID();
        Set<UUID> set = ((AbstractGamePlayerManager)this.field_3581.getPlayerManager()).getPlayerUUIDs();
        for (MapVoteEntry mapVoteEntry : this.entries) {
            if (!mapVoteEntry.getPlayerVotes().contains(uUID) || !mapVoteEntry.getName().equals(string)) continue;
            BFUtils.playSound(uUID, (SoundEvent)BFSounds.GUI_GAME_VOTE_ERROR.get(), SoundSource.MASTER);
            return;
        }
        if (this.field_3582.contains(uUID)) {
            for (MapVoteEntry mapVoteEntry : this.entries) {
                mapVoteEntry.getPlayerVotes().remove(uUID);
            }
            this.field_3582.remove(uUID);
        }
        this.field_3582.add(uUID);
        for (MapVoteEntry mapVoteEntry : this.entries) {
            if (!mapVoteEntry.getName().equals(string) || !mapVoteEntry.getGameType().equals(string2)) continue;
            mapVoteEntry.addPlayerVote(uUID);
            BFUtils.playSound(set, (SoundEvent)BFSounds.GUI_GAME_VOTE_VOTE.get(), SoundSource.MASTER);
            BFUtils.playSound(uUID, (SoundEvent)BFSounds.GUI_GAME_VOTE_VOTE_SELF.get(), SoundSource.MASTER);
            BFGameMapVoteInfoPacket bFGameMapVoteInfoPacket = new BFGameMapVoteInfoPacket(this.field_3581);
            PacketUtils.sendToGamePlayers(bFGameMapVoteInfoPacket, this.field_3581);
            return;
        }
    }

    public void startVoteSequence(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        BFLog.log("[Map Vote] Starting map-vote sequence!", new Object[0]);
        this.field_3579 = true;
        this.entries.clear();
        this.field_3582.clear();
        String string = this.field_3581.getType();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        BFGameType bFGameType = BFGameType.getByName(string);
        if (bFGameType == null) {
            BFLog.log("[Map Vote] Game type '" + string + "' not found! Skipping map vote...", new Object[0]);
            return;
        }
        BFLog.log("[Map Vote] Finding map-vote entries for game-type '" + bFGameType.getName() + "' (" + bFGameType.getDisplayName().getString() + ")", new Object[0]);
        List<AbstractGame<?, ?, ?>> list = bFAbstractManager.getAvailableGames(bFGameType, this.field_3581);
        list.remove(this.field_3581);
        if (list.isEmpty()) {
            BFLog.log("[Map Vote] No other games of type '" + bFGameType.getName() + "' available! Looking for different gamemodes only.", new Object[0]);
            this.method_5610(bFAbstractManager, bFGameType);
            if (this.entries.isEmpty()) {
                BFLog.log("[Map Vote] No votable games found! Current game will be used.", new Object[0]);
            }
        } else {
            this.method_5611(list, threadLocalRandom);
            this.method_5610(bFAbstractManager, bFGameType);
        }
        int n = 6;
        BFLog.log("[Map Vote] Map-vote sequence complete! Total vote options: " + this.entries.size() + " out of 6 expected.", new Object[0]);
    }

    private void method_5611(@NotNull List<AbstractGame<?, ?, ?>> list, @NotNull ThreadLocalRandom threadLocalRandom) {
        while (list.size() > 3) {
            list.remove(threadLocalRandom.nextInt(list.size()));
        }
        for (AbstractGame<?, ?, ?> abstractGame : list) {
            BFLog.log("[Map Vote] Adding match '" + String.valueOf(abstractGame.getUUID()) + "' to vote.", new Object[0]);
            this.entries.add(new MapVoteEntry(abstractGame));
        }
    }

    private void method_5610(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull BFGameType bFGameType) {
        List<AbstractGame<?, ?, ?>> list;
        List<BFGameType> list2 = bFGameType.getAlternativeTypes();
        if (list2.isEmpty()) {
            BFLog.log("[Map Vote] No other game-types available for vote! Skipping different gamemode maps.", new Object[0]);
            return;
        }
        BFLog.log("[Map Vote] Other game-types available for vote! Attempting to add different gamemode maps...", new Object[0]);
        int n = 0;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (BFGameType bFGameType2 : list2) {
            list = bFAbstractManager.getAvailableGames(bFGameType2, this.field_3581);
            if (list.isEmpty()) continue;
            BFLog.log("[Map Vote] Found " + list.size() + " games from game-type '" + bFGameType2.getName() + "' (" + bFGameType2.getDisplayName().getString() + ")", new Object[0]);
            objectArrayList.addAll(list);
        }
        if (objectArrayList.isEmpty()) {
            BFLog.log("[Map Vote] No games from different gamemodes are available!", new Object[0]);
            return;
        }
        BFLog.log("[Map Vote] Found a total of " + objectArrayList.size() + " games from different gamemodes. Will randomly select 3", new Object[0]);
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        while (n < 3 && !objectArrayList.isEmpty()) {
            int n2 = threadLocalRandom.nextInt(objectArrayList.size());
            list = (AbstractGame)objectArrayList.get(n2);
            objectArrayList.remove(n2);
            this.entries.add(new MapVoteEntry((AbstractGame<?, ?, ?>)((Object)list)));
            ++n;
            BFLog.log("[Map Vote] Added game '" + String.valueOf(((AbstractGame)((Object)list)).getUUID()) + "' of type '" + ((AbstractGame)((Object)list)).getType() + "' to vote options", new Object[0]);
        }
        if (n == 0) {
            BFLog.log("[Map Vote] Could not add any different gamemode maps.", new Object[0]);
        } else if (n < 3) {
            BFLog.log("[Map Vote] Could only add " + n + " different gamemode maps (requested 3)", new Object[0]);
        } else {
            BFLog.log("[Map Vote] Successfully added " + n + " different gamemode maps", new Object[0]);
        }
    }

    public boolean method_3716(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @NotNull ServerLevel serverLevel) {
        this.field_3579 = false;
        UUID uUID = null;
        int n = 0;
        for (MapVoteEntry mapVoteEntry : this.entries) {
            int n2 = mapVoteEntry.getPlayerVotes().size();
            UUID uUID2 = mapVoteEntry.getGameUUID();
            if (n2 <= n) continue;
            n = n2;
            uUID = uUID2;
        }
        if (uUID == null) {
            uUID = this.field_3581.getUUID();
        }
        BFUtils.discardMatchEntities(serverLevel, this.field_3581, abstractGamePlayerManager);
        return this.field_3581.moveLobby(bFAbstractManager, serverLevel, uUID);
    }

    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        this.field_3579 = IPacket.readBoolean((ByteBuf)byteBuf);
        if (!this.field_3579) {
            return;
        }
        int n = byteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            if (i < this.entries.size()) {
                this.entries.get(i).read(byteBuf);
                continue;
            }
            MapVoteEntry mapVoteEntry = new MapVoteEntry();
            mapVoteEntry.read(byteBuf);
            this.entries.add(mapVoteEntry);
        }
    }

    public void write(@NotNull ByteBuf byteBuf) throws IOException {
        IPacket.writeBoolean((ByteBuf)byteBuf, (boolean)this.field_3579);
        if (!this.field_3579) {
            return;
        }
        byteBuf.writeInt(this.entries.size());
        for (MapVoteEntry mapVoteEntry : this.entries) {
            mapVoteEntry.write(byteBuf);
        }
    }

    public void reset() {
        this.field_3579 = false;
        this.field_3582.clear();
        this.entries.clear();
    }

    @NotNull
    public List<MapVoteEntry> getEntries() {
        return this.entries;
    }

    public boolean isEntry(@NotNull String string) {
        for (MapVoteEntry mapVoteEntry : this.entries) {
            if (!mapVoteEntry.getName().equals(string)) continue;
            return true;
        }
        return false;
    }
}

