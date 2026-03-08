/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFQueueMusicPacket(@NotNull GameMusic queuedMusicBuilder) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFQueueMusicPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_queue_music"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFQueueMusicPacket> CODEC = CustomPacketPayload.codec(BFQueueMusicPacket::method_4405, BFQueueMusicPacket::new);

    public BFQueueMusicPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(GameMusic.method_1539((FriendlyByteBuf)registryFriendlyByteBuf));
    }

    public void method_4405(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this.queuedMusicBuilder.method_1542((FriendlyByteBuf)registryFriendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4404(BFQueueMusicPacket bFQueueMusicPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.queueMusic(bFQueueMusicPacket, iPayloadContext);
    }
}

