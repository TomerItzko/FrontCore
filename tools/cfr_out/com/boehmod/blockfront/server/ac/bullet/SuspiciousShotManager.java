/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.mm.PacketMMPlayerSuspicious
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.ac.bullet;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMPlayerSuspicious;
import com.boehmod.blockfront.cloud.BFFeatureFlags;
import com.boehmod.blockfront.cloud.server.ServerConnectionManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.util.BFLog;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class SuspiciousShotManager {
    @NotNull
    private static final Component KICK_REASON = Component.translatable((String)"bf.message.gamemode.suspicious.kick").withStyle(ChatFormatting.RED);
    private static final int RESET_TICKS = 200;
    private static final int MAX_SUSPICIOUS_SHOTS = 20;
    private boolean field_6902 = false;
    private static final int field_6889 = 20;
    private int suspiciousShots = 0;
    private int timer = 0;

    public void addSuspiciousShot() {
        ++this.suspiciousShots;
    }

    public boolean method_5851() {
        return this.field_6902;
    }

    public void method_5850(boolean bl) {
        this.field_6902 = bl;
    }

    public void update(@NotNull BFServerManager manager, @NotNull ServerPlayer player) {
        ++this.timer;
        if (this.timer >= 200) {
            this.timer = 0;
            this.suspiciousShots = 0;
        }
        if (this.suspiciousShots >= 20) {
            BFLog.log("Player " + player.getName().getString() + " has been logged as suspicious for firing " + this.suspiciousShots + " suspicious shots in the last 10 seconds.", new Object[0]);
            if (BFFeatureFlags.serverShotValidationReport) {
                ((ServerConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketMMPlayerSuspicious(player.getUUID(), "Player " + player.getName().getString() + " has been logged as suspicious for firing " + this.suspiciousShots + " suspicious shots in the last 10 seconds."));
            }
            if (BFFeatureFlags.serverShotValidationKick) {
                player.connection.disconnect(KICK_REASON);
            }
        }
    }
}

