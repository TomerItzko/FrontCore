/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.profanity.AbstractProfanityManager
 *  com.boehmod.bflib.cloud.common.profanity.Profanity
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.PacketProfanity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.player;

import com.boehmod.bflib.cloud.common.profanity.AbstractProfanityManager;
import com.boehmod.bflib.cloud.common.profanity.Profanity;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.PacketProfanity;
import com.boehmod.blockfront.cloud.server.ServerConnectionManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.BFLog;
import java.nio.file.Path;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ServerProfanityManager
extends AbstractProfanityManager {
    private static final String LIST_PATH = "blockfront/profanities.json";
    @NotNull
    private final BFServerManager manager;

    public ServerProfanityManager(@NotNull BFServerManager manager) {
        super(Path.of(LIST_PATH, new String[0]));
        this.manager = manager;
    }

    protected void handleProfanityImmediately(@NotNull UUID playerUUID, @NotNull Profanity profanity, @NotNull String content) {
        ServerPlayerDataHandler serverPlayerDataHandler = (ServerPlayerDataHandler)this.manager.getPlayerDataHandler();
        int n = profanity.getSeverity();
        BFServerPlayerData bFServerPlayerData = (BFServerPlayerData)serverPlayerDataHandler.getPlayerData(playerUUID);
        BFLog.log("Profanity detected in message from '%s' - Severity '%d' - Id '%s' ('%s')", playerUUID, n, profanity.getId(), content);
        ((ServerConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketProfanity(playerUUID, content, bFServerPlayerData.getPreviousMessages(), n));
    }

    protected void log(@NotNull String fmt, Object ... args) {
        BFLog.log(fmt, args);
    }

    protected void logError(@NotNull String fmt, Object ... args) {
        BFLog.logError(fmt, args);
    }
}

