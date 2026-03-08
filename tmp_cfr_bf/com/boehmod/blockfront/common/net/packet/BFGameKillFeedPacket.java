/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGameKillFeedPacket(@NotNull KillFeedEntry entry) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGameKillFeedPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_kill_feed"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFGameKillFeedPacket> CODEC = CustomPacketPayload.codec(BFGameKillFeedPacket::method_4327, BFGameKillFeedPacket::new);

    public BFGameKillFeedPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(KillFeedEntry.method_3212(registryFriendlyByteBuf));
    }

    public void method_4327(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.entry.method_3215(registryFriendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4326(BFGameKillFeedPacket bFGameKillFeedPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.gameKillFeed(bFGameKillFeedPacket, iPayloadContext);
    }
}

