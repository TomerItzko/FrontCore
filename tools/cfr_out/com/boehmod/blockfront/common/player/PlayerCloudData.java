/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.AbstractClanData
 *  com.boehmod.bflib.cloud.common.MatchData
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.common.mm.SearchGame
 *  com.boehmod.bflib.cloud.common.player.AbstractCloudInventory
 *  com.boehmod.bflib.cloud.common.player.AbstractPlayerCloudData
 *  com.boehmod.bflib.cloud.common.player.PlayerDataContext
 *  com.boehmod.bflib.cloud.common.player.PlayerDataType
 *  com.boehmod.bflib.cloud.common.player.PlayerGroup
 *  com.boehmod.bflib.cloud.common.player.PunishmentType
 *  com.boehmod.bflib.cloud.common.player.status.OnlineStatus
 *  com.boehmod.bflib.cloud.common.player.status.PartyStatus
 *  com.boehmod.bflib.cloud.common.player.status.PlayerStatus
 *  com.mojang.authlib.GameProfile
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.player;

import com.boehmod.bflib.cloud.common.AbstractClanData;
import com.boehmod.bflib.cloud.common.MatchData;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.mm.SearchGame;
import com.boehmod.bflib.cloud.common.player.AbstractCloudInventory;
import com.boehmod.bflib.cloud.common.player.AbstractPlayerCloudData;
import com.boehmod.bflib.cloud.common.player.PlayerDataContext;
import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.bflib.cloud.common.player.PlayerGroup;
import com.boehmod.bflib.cloud.common.player.PunishmentType;
import com.boehmod.bflib.cloud.common.player.status.OnlineStatus;
import com.boehmod.bflib.cloud.common.player.status.PartyStatus;
import com.boehmod.bflib.cloud.common.player.status.PlayerStatus;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.cloud.server.ServerConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.PlayerGroupUtils;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCloudData
extends AbstractPlayerCloudData<PlayerCloudInventory> {
    private static final Component field_6324 = Component.translatable((String)"bf.status.match");
    private static final Component field_6325 = Component.translatable((String)"bf.status.party.host");
    private static final Component field_6326 = Component.translatable((String)"bf.status.party");
    @NotNull
    private Component field_2124 = Component.empty();
    private int field_2121 = 0;
    public int punishmentCheckTimer = 0;
    @NotNull
    private final EnumMap<PunishmentType, Integer> field_2117 = new EnumMap(PunishmentType.class);
    @NotNull
    private final EnumMap<PunishmentType, Integer> field_2118 = new EnumMap(PunishmentType.class);
    private int maxFriends = 0;
    @NotNull
    private PlayerDataType playerDataType = PlayerDataType.PLAYER;
    @Nullable
    private PlayerGroup playerGroup = null;
    private boolean field_6679 = false;
    private boolean field_6973 = false;
    private boolean field_6974 = false;
    private boolean field_2119 = false;

    public PlayerCloudData(@NotNull UUID uUID) {
        super(uUID);
    }

    public void setPlayerDataType(@NotNull PlayerDataType playerDataType) {
        this.playerDataType = playerDataType;
    }

    @NotNull
    public PlayerDataType getPlayerDataType() {
        return this.playerDataType;
    }

    public boolean method_1720() {
        return this.field_2119;
    }

    private void method_1714(@NotNull ByteBuf byteBuf) {
        this.field_2117.clear();
        for (PunishmentType punishmentType : PunishmentType.values()) {
            this.field_2117.put(punishmentType, byteBuf.readInt());
            this.field_2118.put(punishmentType, byteBuf.readInt());
        }
    }

    public void method_1716(@NotNull Player player) {
        this.setUsername(player.getScoreboardName());
    }

    public void method_1713(@NotNull BFServerManager bFServerManager) {
        UUID uUID = this.getUUID();
        if (this.field_2121++ < 120) {
            return;
        }
        this.field_2121 = 0;
        CloudRequestManager cloudRequestManager = ((ServerConnectionManager)bFServerManager.getConnectionManager()).getRequester();
        cloudRequestManager.push(RequestType.PLAYER_DATA, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
    }

    @NotNull
    public MutableComponent method_1715() {
        GameTeam gameTeam;
        UUID uUID = this.getUUID();
        String string = this.getUsername();
        boolean bl = false;
        MutableComponent mutableComponent = this.playerGroup != null ? PlayerGroupUtils.getComponent(this.playerGroup).append(" ") : Component.empty();
        MutableComponent mutableComponent2 = Component.literal((String)string);
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame != null && (gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID)) != null) {
            bl = true;
            mutableComponent2.withStyle(gameTeam.getStyleText());
        }
        if (!bl) {
            mutableComponent2.withStyle(ChatFormatting.GRAY);
        }
        return mutableComponent.append((Component)mutableComponent2);
    }

    public boolean hasPermission(@NotNull String id) {
        if (id.isEmpty()) {
            return true;
        }
        return this.playerGroup != null && (this.playerGroup.hasPermission(id) || this.playerGroup.hasPermission("*"));
    }

    @NotNull
    public Component method_1718() {
        return this.field_2124;
    }

    public boolean method_5636() {
        return this.field_6679;
    }

    public boolean method_5909() {
        return this.field_6973;
    }

    public boolean method_5910() {
        return this.field_6974;
    }

    public int getMaxFriends() {
        return this.maxFriends;
    }

    @NotNull
    public GameProfile getMcProfile() {
        return new GameProfile(this.getUUID(), this.getUsername());
    }

    @NotNull
    public ObjectList<Component> method_1719() {
        MatchData matchData;
        PlayerStatus playerStatus = this.getStatus();
        ObjectArrayList objectArrayList = new ObjectArrayList();
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.profile.base.status", (Object[])new Object[]{this.method_1718()});
        objectArrayList.add((Object)mutableComponent);
        if (playerStatus.getOnlineStatus().isOnline() && (matchData = playerStatus.getMatchData()) != null) {
            objectArrayList.add((Object)Component.empty());
            SearchGame searchGame = matchData.getGame();
            BFGameType bFGameType = BFGameType.getByName(searchGame.getId());
            if (bFGameType != null) {
                MutableComponent mutableComponent2 = bFGameType.getDisplayName().copy().withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.status.game.mode", (Object[])new Object[]{mutableComponent2});
                objectArrayList.add((Object)mutableComponent3);
                MutableComponent mutableComponent4 = Component.literal((String)matchData.getMapName()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent5 = Component.translatable((String)"bf.status.game.map", (Object[])new Object[]{mutableComponent4});
                objectArrayList.add((Object)mutableComponent5);
                MutableComponent mutableComponent6 = Component.literal((String)String.valueOf(matchData.getPlayerCount())).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent7 = Component.literal((String)String.valueOf(matchData.getMaxPlayerCount())).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent8 = Component.translatable((String)"bf.status.game.players", (Object[])new Object[]{mutableComponent6, mutableComponent7});
                objectArrayList.add((Object)mutableComponent8);
            }
        }
        return objectArrayList;
    }

    public void read(@NotNull PlayerDataContext playerDataContext, @NotNull ByteBuf byteBuf) throws IOException {
        super.read(playerDataContext, byteBuf);
        this.maxFriends = byteBuf.readInt();
        this.method_1714(byteBuf);
        this.field_6679 = byteBuf.readBoolean();
        this.field_6973 = byteBuf.readBoolean();
        this.field_6974 = byteBuf.readBoolean();
        this.field_2119 = true;
    }

    public void setStatus(@NotNull PlayerStatus playerStatus) {
        super.setStatus(playerStatus);
        OnlineStatus onlineStatus = playerStatus.getOnlineStatus();
        int n = onlineStatus.getColor();
        MutableComponent mutableComponent = Component.translatable((String)onlineStatus.getLabel());
        if (onlineStatus.isOnline()) {
            if (playerStatus.getServerOn() != null) {
                mutableComponent = field_6324;
            } else {
                PartyStatus partyStatus = playerStatus.getPartyStatus();
                if (partyStatus != PartyStatus.NONE) {
                    mutableComponent = partyStatus == PartyStatus.HOST ? field_6325 : field_6326;
                }
            }
        }
        this.field_2124 = mutableComponent.copy().withColor(n);
    }

    @NotNull
    protected PlayerCloudInventory createInventory() {
        return new PlayerCloudInventory(this);
    }

    @NotNull
    public Optional<PlayerGroup> getGroup() {
        return Optional.ofNullable(this.playerGroup);
    }

    public void setGroup(@Nullable PlayerGroup playerGroup) {
        this.playerGroup = playerGroup;
    }

    public int getPunishmentCount(@NotNull PunishmentType punishmentType) {
        return this.field_2117.getOrDefault(punishmentType, 0);
    }

    public boolean hasActivePunishment(@NotNull PunishmentType punishmentType) {
        return this.field_2118.getOrDefault(punishmentType, 0) > 0;
    }

    public AbstractClanData getClanData() {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return null;
        }
        UUID uUID = this.getClanId();
        return uUID != null ? ((PlayerDataHandler)bFAbstractManager.getPlayerDataHandler()).getClanData(uUID) : null;
    }

    @NotNull
    protected /* synthetic */ AbstractCloudInventory createInventory() {
        return this.createInventory();
    }
}

