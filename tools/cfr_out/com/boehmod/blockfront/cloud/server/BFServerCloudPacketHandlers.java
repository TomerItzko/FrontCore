/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.PacketRegistry
 *  com.boehmod.bflib.cloud.packet.common.mm.PacketMMSchedulePlayer
 *  com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedClanData
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.cloud.server;

import com.boehmod.bflib.cloud.packet.PacketRegistry;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMSchedulePlayer;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedClanData;
import com.boehmod.blockfront.cloud.connection.BFConnection;
import com.boehmod.blockfront.common.match.MMScheduleCache;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.BFLog;
import org.jetbrains.annotations.NotNull;

public class BFServerCloudPacketHandlers {
    public static void register(@NotNull BFServerManager manager) {
        BFLog.log("[Cloud] Registering server-specific cloud packet handlers...", new Object[0]);
        PacketRegistry.registerPacketHandler(PacketRequestedClanData.class, (packet, connection) -> BFServerCloudPacketHandlers.requestedClanData(manager, packet, connection), BFConnection.class);
        PacketRegistry.registerPacketHandler(PacketMMSchedulePlayer.class, (packet, connection) -> BFServerCloudPacketHandlers.matchMakingSchedulePlayer(manager, packet, connection), BFConnection.class);
        BFLog.log("[Cloud] Finished registering server-specific cloud packet handlers.", new Object[0]);
    }

    public static void requestedClanData(@NotNull BFServerManager manager, @NotNull PacketRequestedClanData packet, @NotNull BFConnection connection) {
        ((ServerPlayerDataHandler)manager.getPlayerDataHandler()).putClanData(packet.uuid(), packet.clanData());
    }

    public static void matchMakingSchedulePlayer(@NotNull BFServerManager manager, @NotNull PacketMMSchedulePlayer packet, @NotNull BFConnection connection) {
        MMScheduleCache mMScheduleCache = manager.getMatchMakingCache();
        mMScheduleCache.put(packet.playerUUID(), packet.match());
        BFLog.log("Received MM schedule for player '%s' to join match '%s'", packet.playerUUID().toString(), packet.match().toString());
    }
}

