/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.util.BFRes;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFCalloutWaypointPacket(@NotNull MatchCallout calloutType, @NotNull UUID playerUUID) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFCalloutWaypointPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_callout_waypoint"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFCalloutWaypointPacket> CODEC = CustomPacketPayload.codec(BFCalloutWaypointPacket::method_4035, BFCalloutWaypointPacket::new);

    public BFCalloutWaypointPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((MatchCallout)friendlyByteBuf.readEnum(MatchCallout.class), friendlyByteBuf.readUUID());
    }

    public void method_4035(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum((Enum)this.calloutType);
        friendlyByteBuf.writeUUID(this.playerUUID);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4034(BFCalloutWaypointPacket bFCalloutWaypointPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.calloutWaypoint(bFCalloutWaypointPacket, iPayloadContext);
    }
}

