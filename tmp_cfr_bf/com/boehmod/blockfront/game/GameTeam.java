/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.lang.runtime.SwitchBootstraps;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GameTeam {
    @NotNull
    private final String name;
    @NotNull
    private final Style styleText;
    @NotNull
    private final Style styleIcon;
    private final int color;
    private final int iconColor;
    @NotNull
    private final @NotNull List<@NotNull FDSPose> playerSpawns = new ObjectArrayList();
    @NotNull
    private final ObjectOpenHashSet<UUID> players = new ObjectOpenHashSet();
    private final int maxPlayers;
    @NotNull
    private final FDSTagCompound teamObjects = new FDSTagCompound("teamObjects");
    @NotNull
    private final AbstractGame<?, ?, ?> game;

    public GameTeam(@NotNull AbstractGame<?, ?, ?> game, @NotNull String name, @NotNull Style styleText, @NotNull Style styleIcon, int maxPlayers) {
        this.game = game;
        this.name = name;
        this.styleText = styleText;
        this.styleIcon = styleIcon;
        this.maxPlayers = maxPlayers;
        TextColor textColor = styleText.getColor();
        this.color = textColor != null ? textColor.getValue() : 0xFFFFFF;
        TextColor textColor2 = styleIcon.getColor();
        this.iconColor = textColor2 != null ? textColor2.getValue() : 0xFFFFFF;
    }

    public void writeFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound("team" + this.name);
        fDSTagCompound.setString("teamName", this.name);
        fDSTagCompound.setInteger("teamSpawnsSize", this.playerSpawns.size());
        int n = this.playerSpawns.size();
        for (int i = 0; i < n; ++i) {
            this.playerSpawns.get(i).writeNamedFDS("teamSpawns" + i, fDSTagCompound);
        }
        root.setTagCompound("team" + this.name, fDSTagCompound);
    }

    public void readFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = root.getTagCompound("team" + this.name);
        if (fDSTagCompound == null) {
            return;
        }
        this.playerSpawns.clear();
        int n = fDSTagCompound.getInteger("teamSpawnsSize");
        for (int i = 0; i < n; ++i) {
            FDSPose fDSPose = FDSPose.readNamedFDS("teamSpawns" + i, fDSTagCompound);
            if (fDSPose == null) continue;
            this.playerSpawns.add(fDSPose.copy());
        }
        this.players.clear();
    }

    @NotNull
    public DivisionData getDivisionData(@NotNull AbstractGame<?, ?, ?> game) {
        return this.isAllies() ? game.getAlliesDivision() : game.getAxisDivision();
    }

    public void clearTeamObjects() {
        this.teamObjects.clear();
    }

    public void teleportPlayersToSpawns(@NotNull PlayerDataHandler<?> dataHandler, @NotNull AbstractGame<?, ?, ?> game) {
        this.players.forEach(uuid -> BFUtils.teleportPlayerAndSync(dataHandler, uuid, this.randomSpawn(game)));
    }

    public FDSPose randomSpawn(@NotNull AbstractGame<?, ?, ?> game) {
        if (!this.playerSpawns.isEmpty()) {
            return this.playerSpawns.get(ThreadLocalRandom.current().nextInt(this.playerSpawns.size()));
        }
        return ((AbstractGamePlayerManager)game.getPlayerManager()).getLobbySpawn();
    }

    public void setObject(@NotNull BFStat stat, @NotNull Object value) {
        Object object = value;
        Objects.requireNonNull(object);
        Object object2 = object;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{Integer.class, String.class, Boolean.class}, (Object)object2, n)) {
            case 0: {
                Integer n2 = (Integer)object2;
                this.teamObjects.setInteger(stat.getKey(), n2.intValue());
                break;
            }
            case 1: {
                String string = (String)object2;
                this.teamObjects.setString(stat.getKey(), string);
                break;
            }
            case 2: {
                Boolean bl = (Boolean)object2;
                this.teamObjects.setBoolean(stat.getKey(), bl.booleanValue());
                break;
            }
        }
    }

    public void decreaseObjectInt(@NotNull BFStat stat, int amount) {
        int n = this.getObjectInt(stat);
        this.setObject(stat, n - amount);
    }

    public String getObjectString(@NotNull String key) {
        return this.teamObjects.getString(key);
    }

    public Boolean getObjectBoolean(@NotNull String key) {
        return this.teamObjects.getBoolean(key);
    }

    public int getObjectInt(@NotNull BFStat stat) {
        return this.teamObjects.getInteger(stat.getKey());
    }

    public int getObjectInt(@NotNull BFStat stat, int defaultValue) {
        return this.teamObjects.getInteger(stat.getKey(), defaultValue);
    }

    public boolean addPlayer(@NotNull ServerPlayer player) {
        return this.addPlayer(player.getUUID());
    }

    public boolean addPlayer(@NotNull UUID uuid) {
        if (!this.isAcceptingPlayers()) {
            this.players.add((Object)uuid);
            return true;
        }
        return false;
    }

    public void removePlayer(@NotNull UUID uuid) {
        this.players.remove((Object)uuid);
    }

    public void clearPlayers() {
        this.players.clear();
    }

    @NotNull
    public Set<UUID> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public List<FDSPose> getPlayerSpawns() {
        return Collections.unmodifiableList(this.playerSpawns);
    }

    public void addPlayerSpawn(@NotNull FDSPose pose) {
        this.playerSpawns.add(pose);
    }

    public void clearPlayerSpawns() {
        this.playerSpawns.clear();
    }

    @NotNull
    public Style getStyleText() {
        return this.styleText;
    }

    @NotNull
    public Style getStyleIcon() {
        return this.styleIcon;
    }

    public int getColor() {
        return this.color;
    }

    public int getIconColor() {
        return this.iconColor;
    }

    public boolean hasPlayer(@NotNull UUID uuid) {
        return this.players.contains((Object)uuid);
    }

    public boolean isAllies() {
        return this.name.equals("Allies");
    }

    public boolean isAxis() {
        return this.name.equals("Axis");
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Nullable
    public UUID randomPlayer() {
        return this.players.isEmpty() ? null : RandomUtils.randomFromSet(this.players);
    }

    public int numPlayers() {
        return this.players.size();
    }

    public boolean isAcceptingPlayers() {
        return this.game.hasPlayerLimit() && this.numPlayers() >= this.maxPlayers;
    }

    public void addPlayers(@NotNull Set<UUID> uuids) {
        this.players.addAll(uuids);
    }

    public void onUpdate() {
        int n = this.getObjectInt(BFStats.RADIO_DELAY, 0);
        if (n > 0) {
            this.setObject(BFStats.RADIO_DELAY, n - 1);
        }
    }

    public void writeBuf(@NotNull ByteBuf buf) throws IOException {
        buf.writeInt(this.playerSpawns.size());
        for (FDSPose fDSPose : this.playerSpawns) {
            fDSPose.write(buf);
        }
        buf.writeInt(this.players.size());
        for (UUID uUID : this.players) {
            IPacket.writeUUID((ByteBuf)buf, (UUID)uUID);
        }
        this.teamObjects.writeData(buf);
    }

    public void readBuf(@NotNull ByteBuf buf) throws IOException {
        int n;
        this.playerSpawns.clear();
        int n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            FDSPose fDSPose = new FDSPose();
            fDSPose.read(buf);
            this.playerSpawns.add(fDSPose);
        }
        this.players.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            this.players.add((Object)IPacket.readUUID((ByteBuf)buf));
        }
        this.teamObjects.readData(buf);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object other) {
        if (!(other instanceof GameTeam)) return false;
        GameTeam gameTeam = (GameTeam)other;
        if (!gameTeam.name.equalsIgnoreCase(this.name)) return false;
        return true;
    }
}

