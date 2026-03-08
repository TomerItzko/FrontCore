/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.cloud.common;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.player.PlayerDataContext;
import com.boehmod.bflib.cloud.common.player.status.PlayerStatus;
import com.boehmod.bflib.cloud.connection.Connection;
import com.boehmod.bflib.cloud.packet.PacketRegistry;
import com.boehmod.bflib.cloud.packet.common.PacketFeatureFlags;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedInventory;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedItemDefault;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedItemShowcase;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedPlayerData;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedPlayerDataSet;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedPlayerStatusSet;
import com.boehmod.bflib.cloud.packet.common.server.PacketServerNotification;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.impl.GameAsset;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.Pair;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class BFCommonCloudPacketHandlers {
    private static final Component CLOUD_PREFIX = Component.literal((String)String.valueOf('\ue012')).append(" ");

    public static void register() {
        BFLog.log("[Cloud] Registering common cloud packet handlers...", new Object[0]);
        PacketRegistry.registerPacketHandler(PacketServerNotification.class, BFCommonCloudPacketHandlers::serverNotification, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedPlayerData.class, BFCommonCloudPacketHandlers::requestedPlayerData, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedPlayerDataSet.class, BFCommonCloudPacketHandlers::requestedPlayerDataSet, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedInventory.class, BFCommonCloudPacketHandlers::requestedInventory, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedItemDefault.class, BFCommonCloudPacketHandlers::requestedItemDefault, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedItemShowcase.class, BFCommonCloudPacketHandlers::requestedItemShowcase, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketRequestedPlayerStatusSet.class, BFCommonCloudPacketHandlers::requestedPlayerStatusSet, BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketFeatureFlags.class, BFCommonCloudPacketHandlers::featureFlags, BFConnection.class);
        BFLog.log("[Cloud] Finished registering common cloud packet handlers.", new Object[0]);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void requestedPlayerData(PacketRequestedPlayerData packetRequestedPlayerData, Connection<?> connection) throws IOException {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        ByteBuf byteBuf = Unpooled.wrappedBuffer((byte[])packetRequestedPlayerData.data());
        try {
            PlayerDataContext playerDataContext = packetRequestedPlayerData.context();
            PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(packetRequestedPlayerData.uuid());
            playerCloudData.read(playerDataContext, byteBuf);
        }
        catch (Throwable throwable) {
            byteBuf.release();
            throw throwable;
        }
        byteBuf.release();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void requestedPlayerDataSet(PacketRequestedPlayerDataSet packetRequestedPlayerDataSet, Connection<?> connection) throws IOException {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        for (Map.Entry entry : packetRequestedPlayerDataSet.dataSet().entrySet()) {
            UUID uUID = (UUID)entry.getKey();
            Pair pair = (Pair)entry.getValue();
            PlayerDataContext playerDataContext = (PlayerDataContext)pair.left();
            byte[] byArray = (byte[])pair.right();
            ByteBuf byteBuf = Unpooled.wrappedBuffer((byte[])byArray);
            try {
                PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(uUID);
                playerCloudData.read(playerDataContext, byteBuf);
                ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).getRequester().removeUuid(uUID);
            }
            catch (Throwable throwable) {
                byteBuf.release();
                throw throwable;
            }
            byteBuf.release();
        }
    }

    public static void requestedPlayerStatusSet(PacketRequestedPlayerStatusSet packetRequestedPlayerStatusSet, Connection<?> connection) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        for (Map.Entry entry : packetRequestedPlayerStatusSet.statusSet().entrySet()) {
            PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile((UUID)entry.getKey());
            playerCloudData.setStatus((PlayerStatus)entry.getValue());
        }
    }

    public static void requestedInventory(PacketRequestedInventory packetRequestedInventory, Connection<?> connection) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(packetRequestedInventory.uuid());
        ((PlayerCloudInventory)playerCloudData.getInventory()).onReceiveSection(packetRequestedInventory.stacks(), packetRequestedInventory.section());
    }

    public static void requestedItemDefault(PacketRequestedItemDefault packetRequestedItemDefault, Connection<?> connection) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        CloudRegistry cloudRegistry = bFAbstractManager.getCloudRegistry();
        Object obj = bFAbstractManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(packetRequestedItemDefault.uuid());
        ((PlayerCloudInventory)playerCloudData.getInventory()).populateEquippedItems(cloudRegistry, (Set)packetRequestedItemDefault.itemStacks());
    }

    public static void requestedItemShowcase(PacketRequestedItemShowcase packetRequestedItemShowcase, Connection<?> connection) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        if (bFAbstractManager == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(packetRequestedItemShowcase.uuid());
        ((PlayerCloudInventory)playerCloudData.getInventory()).populateShowcasedItems((Set)packetRequestedItemShowcase.itemStacks());
    }

    public static void serverNotification(PacketServerNotification packetServerNotification, Connection<?> connection) {
        Optional optional = packetServerNotification.match();
        MutableComponent mutableComponent = Component.Serializer.fromJson((String)packetServerNotification.message(), (HolderLookup.Provider)RegistryAccess.EMPTY);
        if (mutableComponent == null) {
            return;
        }
        UUID uUID = optional.orElse(null);
        if (uUID == null) {
            BFLog.log("[Cloud Notification] %s", mutableComponent.getString());
        } else {
            BFLog.log("[Cloud Notification] For player/match '%s': %s", ((UUID)optional.get()).toString(), mutableComponent.getString());
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AssetRegistry<GameAsset> assetRegistry = bFAbstractManager.getAssetStore().getRegistry(GameAsset.class);
        for (GameAsset gameAsset : assetRegistry.getEntries().values()) {
            AbstractGame<?, ?, ?> abstractGame = gameAsset.getGame();
            if (abstractGame == null) continue;
            Set<UUID> set = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs();
            if (uUID != null && !abstractGame.getUUID().equals(uUID) && !set.contains(uUID)) continue;
            BFUtils.sendFancyMessage(set, CLOUD_PREFIX, (Component)mutableComponent);
        }
    }

    public static void featureFlags(PacketFeatureFlags packetFeatureFlags, Connection<?> connection) {
        Map map = packetFeatureFlags.featureFlags();
        BFLog.log("Received %d feature flags from the cloud!", map.size());
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).getRequester().getFeatureFlagManager().setFeatureFlags(map);
    }
}

