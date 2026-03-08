/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.map.MapEnvironment;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class MapVoteEntry {
    @NotNull
    private final Set<UUID> playerVotes = new ObjectOpenHashSet();
    private String mapName;
    private String field_6746;
    private String gameType;
    private UUID gameUUID;
    @NotNull
    private final List<BFCountry> countries = new ObjectArrayList();

    public MapVoteEntry() {
    }

    public MapVoteEntry(@NotNull AbstractGame<?, ?, ?> game) {
        MapAsset mapAsset = game.getMap();
        MapEnvironment mapEnvironment = game.getMapEnvironment();
        this.gameUUID = game.getUUID();
        this.mapName = mapAsset.getName();
        this.field_6746 = mapEnvironment.getName();
        this.gameType = game.getType();
        for (GameTeam gameTeam : ((AbstractGamePlayerManager)game.getPlayerManager()).getTeams()) {
            this.countries.add(gameTeam.getDivisionData(game).getCountry());
        }
    }

    public void addPlayerVote(@NotNull UUID uuid) {
        this.playerVotes.add(uuid);
    }

    public void read(@NotNull ByteBuf buf) throws IOException {
        int n;
        this.mapName = IPacket.readString((ByteBuf)buf);
        this.field_6746 = IPacket.readString((ByteBuf)buf);
        this.gameType = IPacket.readString((ByteBuf)buf);
        this.countries.clear();
        int n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            this.countries.add(BFCountry.values()[buf.readInt()]);
        }
        this.playerVotes.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            this.playerVotes.add(IPacket.readUUID((ByteBuf)buf));
        }
    }

    public void write(@NotNull ByteBuf root) throws IOException {
        IPacket.writeString((ByteBuf)root, (String)this.mapName);
        IPacket.writeString((ByteBuf)root, (String)this.field_6746);
        IPacket.writeString((ByteBuf)root, (String)this.gameType);
        root.writeInt(this.countries.size());
        for (BFCountry object : this.countries) {
            root.writeInt(object.ordinal());
        }
        root.writeInt(this.playerVotes.size());
        for (UUID uUID : this.playerVotes) {
            IPacket.writeUUID((ByteBuf)root, (UUID)uUID);
        }
    }

    @NotNull
    public UUID getGameUUID() {
        return this.gameUUID;
    }

    @NotNull
    public Set<UUID> getPlayerVotes() {
        return this.playerVotes;
    }

    public String getName() {
        return this.mapName;
    }

    public String method_5714() {
        return this.field_6746;
    }

    @NotNull
    public String getGameType() {
        return this.gameType;
    }

    @NotNull
    public List<BFCountry> getCountries() {
        return Collections.unmodifiableList(this.countries);
    }
}

