/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFSoundPacket(@NotNull Holder<SoundEvent> sound, @NotNull SoundSource soundSource, float volume, float pitch) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFSoundPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_sound"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFSoundPacket> CODEC = CustomPacketPayload.codec(BFSoundPacket::method_4437, BFSoundPacket::new);

    public BFSoundPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((Holder<SoundEvent>)((Holder)SoundEvent.STREAM_CODEC.decode((Object)registryFriendlyByteBuf)), (SoundSource)registryFriendlyByteBuf.readEnum(SoundSource.class), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat());
    }

    public void method_4437(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        SoundEvent.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, this.sound);
        registryFriendlyByteBuf.writeEnum((Enum)this.soundSource);
        registryFriendlyByteBuf.writeFloat(this.volume);
        registryFriendlyByteBuf.writeFloat(this.pitch);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4435(BFSoundPacket bFSoundPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.sound(bFSoundPacket, iPayloadContext);
    }
}

