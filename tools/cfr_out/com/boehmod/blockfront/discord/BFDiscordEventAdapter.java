/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.friend.PacketFriendJoinGame
 *  de.jcm.discordgamesdk.DiscordEventAdapter
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.discord;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.friend.PacketFriendJoinGame;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.util.BFLog;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class BFDiscordEventAdapter
extends DiscordEventAdapter {
    @NotNull
    private final BFClientManager manager;

    public BFDiscordEventAdapter(@NotNull BFClientManager manager) {
        this.manager = manager;
    }

    public void onActivityJoin(String secret) {
        BFLog.log("Received activity join attempt request (Secret: " + secret + ")", new Object[0]);
        try {
            UUID uUID = UUID.fromString(secret);
            BFLog.log("Attempting to join match " + String.valueOf(uUID), new Object[0]);
            ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketFriendJoinGame(uUID));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            BFLog.logThrowable("Failed to join match from secret.", illegalArgumentException, new Object[0]);
        }
    }
}

