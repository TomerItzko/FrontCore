/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.player;

import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.net.packet.BFFrozenPacket;
import com.boehmod.blockfront.common.net.packet.BFPlayerDataPacket;
import com.boehmod.blockfront.common.player.a;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class BFAbstractPlayerData<M extends BFAbstractManager<?, ?, ?>, L extends Level, P extends Player, B extends a<?>> {
    public static final int field_1170 = 80;
    public static final int field_1171 = 80;
    @NotNull
    private static final DateTimeFormatter field_1154 = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final int field_1172 = 5;
    @NotNull
    protected final UUID uuid;
    @NotNull
    private final Object2FloatMap<UUID> field_1153 = new Object2FloatOpenHashMap();
    @NotNull
    private final ObjectList<String> previousMessages = new ObjectArrayList();
    public int field_1173 = 0;
    public int playTimer = 0;
    public int calloutCooldown = 0;
    public float field_1165;
    public float field_1166 = 0.0f;
    public float field_1167;
    public float field_1168 = 0.0f;
    protected boolean field_1158 = false;
    private long field_1157 = 0L;
    protected boolean field_1160 = false;
    @Nullable
    private FDSPose pose;
    private boolean outOfGame = false;
    private int respawnTimer = 0;
    @Nullable
    private String lastMessage = null;
    private long lastChatTime = 0L;
    private boolean field_1162 = true;
    private int field_1177 = 0;
    private boolean isMuted = false;
    @NotNull
    protected final B field_1152;
    @NotNull
    private PlayerDataType field_1151 = PlayerDataType.PLAYER;

    public BFAbstractPlayerData(@NotNull UUID uuid) {
        this.uuid = uuid;
        this.field_1152 = this.method_836();
    }

    public void method_837(@NotNull PlayerDataType playerDataType) {
        this.field_1151 = playerDataType;
    }

    @NotNull
    public PlayerDataType method_835() {
        return this.field_1151;
    }

    @NotNull
    public B method_843() {
        return this.field_1152;
    }

    @NotNull
    public abstract B method_836();

    public void update(@NotNull M manager, @NotNull L level, @NotNull P player, @Nullable AbstractGame<?, ?, ?> game) {
        this.method_839((BFAbstractManager<?, ?, ?>)manager, (Level)level, false);
        if (this.field_1173 > 0) {
            --this.field_1173;
        }
        if (this.outOfGame) {
            player.setSprinting(false);
            player.setPos(((Player)player).xOld, ((Player)player).yOld, ((Player)player).zOld);
        }
        if (player.isSprinting() && player.isCrouching()) {
            player.setSprinting(false);
        }
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            this.method_841((Level)level, serverPlayer);
        }
    }

    public void method_841(@NotNull Level level, @NotNull ServerPlayer serverPlayer) {
        if (this.pose != null && (serverPlayer.getX() != this.pose.position.x || serverPlayer.getZ() != this.pose.position.z)) {
            serverPlayer.teleportTo(this.pose.position.x, serverPlayer.getY(), this.pose.position.z);
            serverPlayer.setDeltaMovement(0.0, 0.0, 0.0);
        }
        if (this.field_1160 && serverPlayer.getHealth() > 0.0f) {
            if (this.pose == null) {
                this.setPose(new FDSPose((Player)serverPlayer));
                PacketUtils.sendToPlayer(new BFFrozenPacket(this.pose), serverPlayer);
            }
        } else if (this.pose != null) {
            this.setPose(null);
            PacketUtils.sendToPlayer(new BFFrozenPacket(this.pose), serverPlayer);
        }
        ((a)this.field_1152).method_822(level, serverPlayer);
    }

    public void method_839(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Level level, boolean bl) {
        if (level.isClientSide()) {
            return;
        }
        ++this.field_1177;
        if (bl || this.field_1162 || this.field_1177 >= 40) {
            this.field_1162 = false;
            this.field_1177 = 0;
            AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(this.uuid);
            if (abstractGame != null) {
                PacketUtils.sendToGamePlayers(new BFPlayerDataPacket(this), abstractGame);
            } else {
                for (Player player : level.players()) {
                    if (!(player instanceof ServerPlayer)) continue;
                    ServerPlayer serverPlayer = (ServerPlayer)player;
                    PacketUtils.sendToPlayer(new BFPlayerDataPacket(this), serverPlayer);
                    if (!serverPlayer.getUUID().equals(this.uuid)) continue;
                    serverPlayer.refreshDimensions();
                }
            }
        }
    }

    public void writeBuf(@NotNull FriendlyByteBuf buf) {
        ((a)this.field_1152).method_823(buf);
        buf.writeBoolean(this.field_1160);
        buf.writeBoolean(this.outOfGame);
        buf.writeInt(this.respawnTimer);
        buf.writeBoolean(this.field_1158);
        buf.writeBoolean(this.isMuted);
    }

    public void readBuf(@NotNull FriendlyByteBuf buf) {
        ((a)this.field_1152).method_826(buf);
        this.field_1160 = buf.readBoolean();
        this.outOfGame = buf.readBoolean();
        this.respawnTimer = buf.readInt();
        this.field_1158 = buf.readBoolean();
        this.isMuted = buf.readBoolean();
    }

    public void method_846() {
        this.field_1162 = true;
    }

    public long method_852() {
        return this.field_1157;
    }

    public void method_844(long l) {
        this.field_1157 = l;
    }

    public boolean isOutOfGame() {
        return this.outOfGame;
    }

    public void setOutOfGame(boolean outOfGame) {
        if (this.outOfGame != outOfGame) {
            this.outOfGame = outOfGame;
            this.method_846();
        }
    }

    @Nullable
    public FDSPose getPose() {
        return this.pose;
    }

    public void setPose(@Nullable FDSPose pose) {
        this.pose = pose;
    }

    public boolean method_849() {
        return this.field_1160;
    }

    public void method_832(boolean bl) {
        if (this.field_1160 != bl) {
            this.field_1160 = bl;
            this.method_846();
        }
    }

    public int getRespawnTimer() {
        return this.respawnTimer;
    }

    public void setRespawnTimer(int respawnTimer) {
        if (this.respawnTimer != respawnTimer) {
            this.respawnTimer = respawnTimer;
            this.method_846();
        }
    }

    public void method_828(int n) {
        if (this.respawnTimer <= 0) {
            return;
        }
        this.respawnTimer -= n;
        if (this.respawnTimer % 5 == 0) {
            this.method_846();
        }
    }

    @Nullable
    public String getLastMessage() {
        return this.lastMessage;
    }

    public void addMessage(@NotNull String message) {
        if (this.lastMessage == null || !this.lastMessage.equals(message)) {
            this.lastMessage = message;
            this.previousMessages.add((Object)(LocalTime.now().format(field_1154) + ": \"" + message + "\""));
            if (this.previousMessages.size() > 5) {
                this.previousMessages.removeFirst();
            }
        }
    }

    @NotNull
    public ObjectList<String> getPreviousMessages() {
        return ObjectLists.unmodifiable(this.previousMessages);
    }

    public long getLastChatTime() {
        return this.lastChatTime;
    }

    public void setLastChatTime(long time) {
        this.lastChatTime = time;
    }

    public boolean method_842() {
        return this.field_1158;
    }

    public void method_833(boolean bl) {
        if (this.field_1158 != bl) {
            this.field_1158 = bl;
            this.method_846();
        }
    }

    public boolean isMuted() {
        return this.isMuted;
    }

    public void setMuted(boolean isMuted) {
        this.isMuted = isMuted;
    }

    public void method_840(@NotNull UUID uUID, float f) {
        if (!this.field_1153.containsKey((Object)uUID)) {
            this.field_1153.put((Object)uUID, f);
            return;
        }
        float f2 = this.field_1153.getFloat((Object)uUID);
        this.field_1153.put((Object)uUID, f2 + f);
    }

    public void reset() {
        this.field_1153.clear();
    }

    @NotNull
    public Map<UUID, Float> method_857() {
        return Collections.unmodifiableMap(this.field_1153);
    }

    public UUID getUUID() {
        return this.uuid;
    }
}

