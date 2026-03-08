/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.server.level.ServerPlayer
 *  net.neoforged.neoforge.network.PacketDistributor
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public final class PacketUtils {
    public static void sendToPlayer(@NotNull CustomPacketPayload payload, @NotNull ServerPlayer player) {
        PacketUtils.method_4064();
        PacketDistributor.sendToPlayer((ServerPlayer)player, (CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void sendToAllPlayers(@NotNull CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers((CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void sendToPlayers(@NotNull CustomPacketPayload payload, @NotNull List<ServerPlayer> players) {
        players.forEach(player -> PacketUtils.sendToPlayer(payload, player));
    }

    public static void sendToGamePlayers(@NotNull CustomPacketPayload payload, @NotNull AbstractGame<?, ?, ?> game) {
        for (UUID uUID : ((AbstractGamePlayerManager)game.getPlayerManager()).getPlayerUUIDs()) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            PacketUtils.sendToPlayer(payload, serverPlayer);
        }
    }

    public static void sendToServer(@NotNull CustomPacketPayload payload) {
        PacketUtils.method_4064();
        PacketDistributor.sendToServer((CustomPacketPayload)payload, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    private static void method_4064() {
    }
}

