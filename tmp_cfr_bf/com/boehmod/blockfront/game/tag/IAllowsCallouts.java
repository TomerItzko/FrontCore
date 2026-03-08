/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.net.packet.BFCalloutWaypointPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface IAllowsCallouts<G extends AbstractGame<?, ?, ?>> {
    public void method_3396(@NotNull ServerPlayer var1, @NotNull UUID var2, @NotNull MatchCallout var3);

    default public void method_3395(@NotNull G g, @NotNull UUID uUID, @NotNull MatchCallout matchCallout) {
        Object p = ((AbstractGame)g).getPlayerManager();
        if (matchCallout.getWaypoint() != null) {
            GameTeam gameTeam = ((AbstractGamePlayerManager)p).getPlayerTeam(uUID);
            if (gameTeam == null) {
                return;
            }
            for (UUID uUID2 : gameTeam.getPlayers()) {
                ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID2);
                if (serverPlayer == null) continue;
                PacketUtils.sendToPlayer(new BFCalloutWaypointPacket(matchCallout, uUID), serverPlayer);
            }
        }
    }
}

